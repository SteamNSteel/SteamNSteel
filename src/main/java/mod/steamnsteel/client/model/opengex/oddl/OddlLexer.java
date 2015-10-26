/*
 * $Id: OddlLexer.java 3828 2014-11-23 08:21:03Z pspeed $
 * 
 * Copyright (c) 2014, Simsilica, LLC
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the 
 *    distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its 
 *    contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package mod.steamnsteel.client.model.opengex.oddl;

import java.io.*;


/**
 *  Provides the character reader and low-level literal reading
 *  support for parsing the ODDL files.
 *
 *  @author    Paul Speed
 */
public class OddlLexer {

    private boolean debug = false;

    private PushbackReader reader;
    private int column = 1;
    private int line = 1;
    private int[] lastPos = new int[2];

    public OddlLexer( Reader in ) {
        // There is at least one case where we pushback 
        // 2 characters deep when interpretting numbers
        // starting with 0 
        this.reader = new PushbackReader(in, 2);
    }

    public void error( String msg ) throws IOException {
        throw new IOException(msg);   
    }

    public void error( String msg, int[] pos ) throws IOException {
        throw new IOException(msg + ", at:" + pos[0] + ":" + pos[1]);      
    }
    
    protected boolean isWhitespace( int c ) {
        // For ODDL the spec says anything <= 32 is whitespace
        return c <= 32;
    }
    
    protected boolean isIdStart( int c ) {
        if( c == '_' ) {
            return true;
        }
        if( c >= 'a' && c <= 'z' ) {
            return true;
        } 
        if( c >= 'A' && c <= 'Z' ) {
            return true;
        }
        return false; 
    }
    
    protected boolean isId( int c ) {
        if( isIdStart(c) ) {
            return true;
        }
        if( c >= '0' && c <= '9' ) {
            return true;
        }
        return false;
    }

    protected int parseComment() throws IOException {
        // Either we are in a single-line comment or 
        // a multiline comment... we can tell by the next character
        int c = read();
        if( c == '/' ) {
            // Continue to the EOL
            while( (c = read()) >= 0 ) {
                // The line will always have at least a \n and we'd
                // have already skipped the \r
                if( c == '\n' ) {
                    break;
                }
            }
        } else if( c == '*' ) {
            // Continue until the first */
            while( (c = read()) >= 0 ) {
                if( c == '*' ) {
                    c = read();
                    if( c == '/' ) {
                        break;
                    }
                }
            }
        } else {
            error("Malformed comment beginning '/'" + (char)c, position(-1));
        }
        return c;
    }

    public int nextNonWhitespace() throws IOException {
        int c;
        while( (c = read()) >= 0 ) {
            // Check for comments here, too
            if( c == '/' ) {
                // Could be the start of a comment... or an error
                // For this format, '/' should never appear in places
                // where a comment can start so it is safe to assume
                // that it is trying to be a comment.
                if( parseComment() < 0 ) {
                    // Reached EOF
                    return -1;
                }
                continue;
            }
        
            // For whitespace, just skip it
            if( !isWhitespace(c) ) {
                return c;
            }
        }
        return -1;
    }

    /**
     *  Parses and returns an identifier literal, including marking
     *  the current position after the white space is consumed.
     *
     *  @param tokenType provided for better idefintification in error messages.
     */
    public String parseIdentifier( String tokenType ) throws IOException {
    
        // identifier ::= ('[A-Z]' | '[a-z]' | '_') ('[0-9]' | '[A-Z]' | '[a-z]' | '_')+ 
 
        // Check the first character   
        int c = nextNonWhitespace(); 
        if( c < 0 ) {
            error("Expected " + tokenType + ", found end of file");
        }       
        if( !isIdStart(c) ) {
            error("Illegal " + tokenType + " start '" + (char)c + "'", position(-1));
        }
        position(-1); 
 
        StringBuilder sb = new StringBuilder();
        sb.append((char)c);
        
        while( (c = read()) >= 0 ) {
            if( !isId(c) ) {
                pushBack(c);
                break;
            }
            sb.append((char)c);
        }
        return sb.toString();
    }

    protected int parseHexDigit() throws IOException  {
        int c = read();
        if( c < 0 ) {
            error("Expected hexidecimal digit, found end of file");
        }
        int result = Character.digit(c, 16);
        if( result < 0 ) {
            error("Expected hexidecimal digit, found:" + (char)c, position(-1));
        }
        return result;
    }

    protected int parseHexSequence() throws IOException  {
        int d1 = parseHexDigit();
        int d2 = parseHexDigit();
        return d1 * 16 + d2;           
    }
    
    protected int parseUnicodeSequence( int length ) throws IOException  {
        int result = 0;
        for( int i = 0; i < length; i++ ) {
            int d = parseHexDigit();
            result = result * 16 + d;      
        }
        return result;
    }

    protected int parseEscapeSequence( boolean allowUnicode ) throws IOException  {
        int c = read();
        switch( c ) {
            case '"': return '"';
            case '\'': return '\'';
            case '?': return '?';
            case '\\': return '\\';
            case 'a': return 0x07;
            case 'b': return 0x08;
            case 'f': return 0x0c;
            case 'n': return 0x0a;
            case 'r': return 0x0d;
            case 't': return 0x09;
            case 'v': return 0x08;
            case 'x': 
                return parseHexSequence(); 
            case -1:
                error("Expected escape character, found end of file");
                break;
            case 'u':
                if( allowUnicode ) { 
                    return parseUnicodeSequence(4);
                }
            case 'U':
                if( allowUnicode ) { 
                    return parseUnicodeSequence(6);
                }
            default:
                error("Invalid escape character:" + (int)c, lastPosition());
                break;
        }
        return -1;
    }

    protected int parseCharacter( int c ) throws IOException {
        // A single character in a char-literal can be:
        // [U+0020 - U+0026] | [U+0028 - U+005B] | [U+005D - U+007E] | escape-char
        // 
        // The first character is passed to us because we already
        // read it. 
        if( c == '\\' ) {
            c = parseEscapeSequence(false);
        }
        if( c < 0 ) {
            error("Expected character, found end of file");
        }
            
        // Check range
        if( !(c >= 0x20 && c <= 0x26) 
            && !(c >= 0x28 && c <= 0x58) 
            && !(c >= 0x5d && c <= 0x7e) ) {
            error("Character digit not in allowed range", position(-1));          
        }

        return c;                    
    }

    protected long parseCharLiteral() throws IOException {
        
        // A char literal is defined as
        //
        // charLiteral ::= '\'' char+ '\''
        //
        // And by now we've already read the first '\''
        
        long result = 0;
        int c;
        while( (c = read()) != '\'' ) {
            int d = parseCharacter(c);           
            result = result * 256 + c;
        }
        return result;
    }

    /**
     *  Parses a quoted string and returns it.
     */
    public String parseString() throws IOException {
           
        int c = nextNonWhitespace();
        if( c < 0 ) {
            error("Expected '\"', found end of file");
        }
        position(-1); // mark the position       
        if( c != '"' ) {
            error("Expected '\"', found:" + (char)c, lastPosition());
        }
        
        StringBuilder sb = new StringBuilder();
        while( (c = read()) >= 0 ) {
            if( c == '"' ) {
                break; // end of string
            }
            if( c == '\n' ) {
                error("Newline detected in string", lastPosition());
            }
            if( c == '\\' ) {
                c = parseEscapeSequence(true);
            } 
            sb.append((char)c);
        }
        
        return sb.toString();        
    }

    protected long parseIntString( int radix, String radixName ) throws IOException {
        long value = 0;
        boolean first = true;
        int c;
        while( (c = read()) >= 0 ) {
            int d = Character.digit(c, radix);
            if( d < 0 ) {
                if( first ) {
                    error("Unexpected character '" + (char)c + "' in " + radixName + " literal", position(-1));
                }
                // Else let the caller sort it out
                pushBack(c);
                break;
            }
            value *= radix;
            value += d;
            first = false;
        }
        if( c == -1 ) {
            error("End of file reached in " + radixName + " literal");
        }
        return value;
    }

    protected double parseFloatString() throws IOException {
 
        // The simple part of a float literal, without exponent
        // float ::= [0-9]* '.' [0-9]*

        boolean first = true;
        double result = 0;
 
        int c;
        while( (c = read()) >= 0 ) {
            if( c == '.' ) {
                break;
            }
            int d = Character.digit(c, 10);
            if( d < 0 ) {
                if( first ) {
                    error("Unexpected character '" + (char)c + "' in floating point literal", position(-1));
                }
                break;
            }
            
            result = result * 10 + d;
            first = false;
        }
 
        // Check for a decimal part       
        if( c == '.' ) {
            double numerator = 0;
            double denominator = 1;
            
            while( (c = read()) >= 0 ) {
                int d = Character.digit(c, 10);
                if( d < 0 ) {
                    if( first ) {
                        error("Unexpected character '" + (char)c + "' in floating point literal", position(-1));
                    }
                    break;
                }

                numerator *= 10;
                numerator += d;
                denominator *= 10; 
                first = false;
            }
            result += numerator / denominator;   
        }
        if( c == -1 ) {
            error("Unexpected end of file in floating point literal");
        }

        if( c == 'e' || c == 'E' ) {
            // And the sign
            int expSign = 1;
            c = read();
            if( c == '+' || c == '-' ) {
                expSign = c == '-' ? -1 : 1;
            } else {
                pushBack(c);
            }
                
            // Note: ODDL reference code allows empty exponents which
            // I think is an error... especially as compared to the railroad
            // diagrams.  I will throw an error.
            long exp = parseIntString(10, "exponent");
            if( exp == 0 ) {
                error("Invalid exponent", lastPosition());
            }
              
            result = result * Math.pow(10, exp * expSign);    
        } else {
            pushBack(c);
        }
      
        return result;
    }

    public double parseDouble() throws IOException {
 
        int sign = 1;
    
        int c = nextNonWhitespace();
        if( c == '+' ) {
            // Unclear if whitespace is allowed here or not
            c = read();
        } else if( c == '-' ) {
            sign = -1;
            // Unclear if whitespace is allowed here or not
            c = read();
        }
            
        if( c == '0' ) {
            int d = read();
            if( d == 'x' || d == 'X' ) {
                long val = (int)parseIntString(16, "hexidecimal");
                return Double.longBitsToDouble(val) * sign;
            } else if( d == 'b' || d == 'B' ) {
                return Double.longBitsToDouble(parseIntString(1, "binary")) * sign;
            } else {
                pushBack(d);               
            }     
        }
        
        // If we got here then we didn't use the original character
        // we read at the top of the method... or the one we read to
        // replace it
        pushBack(c);
 
        // Else read the number while we have digits
        double result = parseFloatString() * sign;
        return result;
    }

    public float parseFloat() throws IOException {
 
        int sign = 1;
    
        int c = nextNonWhitespace();
        if( c == '+' ) {
            // Unclear if whitespace is allowed here or not
            c = read();
        } else if( c == '-' ) {
            sign = -1;
            // Unclear if whitespace is allowed here or not
            c = read();
        }
            
        if( c == '0' ) {
            int d = read();
            if( d == 'x' || d == 'X' ) {
                int val = (int)parseIntString(16, "hexidecimal");
                return Float.intBitsToFloat(val) * sign;
            } else if( d == 'b' || d == 'B' ) {
                return Float.intBitsToFloat((int)parseIntString(1, "binary")) * sign;
            } else {
                pushBack(d);               
            }     
        }
        
        // If we got here then we didn't use the original character
        // we read at the top of the method... or the one we read to
        // replace it
        pushBack(c);
 
        // Else read the number while we have digits
        float result = (float)(parseFloatString() * sign);
        return result;
    }

    protected long signExtend( long value, long bitMask, boolean signed ) throws IOException {
        // See if we've exceeded the bits
        if( value != (value & bitMask) ) {
            error("Number overflow", lastPosition());
        }
        if( signed ) {
            // See if the sign bit is set
            long signBits = ~(bitMask >> 1);
            if( (value & signBits) != 0 ) {
                value = signBits | value;
            } 
        }
        return value;              
    }

    protected void checkBits( long value, long bitMask, boolean signed ) throws IOException {
        if( !signed ) {
            // Simple, see if we've exceeded the bits
            if( value != (value & bitMask) ) {
                error("Number overflow", lastPosition());
            }
        } else if( value > 0 ) {
            // See if the value pops the sign bit or greater      
            long signBits = ~(bitMask >> 1);
            if( (value & signBits) != 0 ) {
                error("Number overflow", lastPosition());
            }
        } else if( value < 0 ) {
            // We try to detect non-sign extension in the sign bits
            // area... which means all of those 1s should be on.            
            long signBits = ~(bitMask >> 1);
            if( (value & signBits) != signBits ) {
                error("Number overflow", lastPosition());
            }
        }        
    }

    public long parseLong( long bitMask, boolean signed ) throws IOException {
        int sign = 1;

        int c = nextNonWhitespace();
        position(-1);
             
        if( c == '+' ) {
            // Unclear if whitespace is allowed here or not
            c = read();
        } else if( c == '-' ) {
            sign = -1;
            // Unclear if whitespace is allowed here or not
            c = read();
        }
 
        Long result = null;           
        if( c == '0' ) {
            int d = read();
            if( d == 'x' || d == 'X' ) {
                result = parseIntString(16, "hexidecimal");
                result = signExtend(result, bitMask, signed) * sign;
            } else if( d == 'b' || d == 'B' ) {
                result = parseIntString(2, "binary");
                result = signExtend(result, bitMask, signed) * sign;
            } else {
                pushBack(d);
            }     
        } else if( c == '\'' ) {
            result = parseCharLiteral() * sign;
        }
        
        if( result == null ) {
            // If we got here then we didn't use the original 
            // char we read at the top of the method.
            pushBack(c);
 
            // Else read the number while we have digits
            result = parseIntString(10, "decimal") * sign;            
        }
        
        // Verify that it fits the requirements    
        checkBits(result, bitMask, signed);            
 
        return result;
    }

    /**
     *  Attempts to detect the type of number and return a value that
     *  is appropriate.  The Number result will allow conversion to
     *  whatever type is required.
     */
    public Number parseUnknownNumber() throws IOException {
        int c = nextNonWhitespace();
        if( c < 0 ) {
            error("Expected literal, found end of file");
        }
        position(-1);
        Number result = null;
        int sign = 1;
        if( c == '+' || c == '-' ) {
            sign = c == '-' ? -1 : 1;
            c = read();
        }
 
        if( c == '0' ) {
            // Might be a hex or binary sequence
            int d = read();
            if( d == 'x' || d == 'X' ) {
                // Need to wrap this
                result = new BitSequence(parseIntString(16, "hexidecimal"), sign);
            } else if( d == 'b' || d == 'B' ) {
                // Need to wrap this
                result = new BitSequence(parseIntString(2, "binary"), sign);
            } else {
                pushBack(d);
            }     
        } else if( c == '\'' ) {
            // This is always an int so no wrapping is needed
            result = parseCharLiteral() * sign;
        }
        
        if( result == null ) {
            pushBack(c);
            
            // Just read it as a double, aleast for now.  If it's a _huge_ long
            // then it will overflow
            result = parseFloatString() * sign;
        }
        return result;       
    }

    public String parseReference() throws IOException { 
        int c = nextNonWhitespace();
        if( c < 0 ) {
            error("Expected '$' or '%' but found end of file");
        }
        
        if( c == 'n' ) {
            // Might be a null
            int[] pos = position(-1);
            String check = "ull";
            for( int i = 0; i < check.length(); i++ ) {
                int c2 = read();
                if( c2 < 0 ) {
                    error("Unexpected end of file in reference");
                }
                if( check.charAt(i) != c2 ) {
                    // We don't know that it was meant to be a null so
                    // we'll just say bad ref.
                    error("Expected '$' or '%' but found:" + (char)c, pos);
                } 
            }
            return null; 
        }            
        if( c != '$' && c != '%' ) {
            error("Expected '$' or '%' but found:" + (char)c, position(-1));
        }

        // Keep track of our starting position because parseIdentifier
        // will wipe it out and we want to restore it upon return.
        int[] pos = position(-1).clone();        
        
        StringBuilder result = new StringBuilder();
        
        do {
            String id = parseIdentifier("reference");
            result.append((char)c);
            result.append(id);
            c = read();           
            if( c < 0 ) {
                error("Unexpected end of file in reference");
            }
        } while( c == '%' );
 
        pushBack(c);
 
        lastPos[0] = pos[0];
        lastPos[1] = pos[1];
               
        return result.toString();
    }            
    
    /**
     *  Resets and returns the internal position marker.  Note: the content
     *  of this array change every time position() is called.  For
     *  long-term storage, the value must be cloned.
     *  Every non-single-character literal read will set this value. 
     */
    public int[] position() {
        lastPos[0] = line;
        lastPos[1] = column;
        return lastPos;
    }

    /**
     *  Resets and returns the internal position marker one column back.  Note: the content
     *  of this array change every time position() is called.  For
     *  long-term storage, the value must be cloned.
     *  Every non-single-character literal read will set this value. 
     */
    public int[] position( int delta ) {
        lastPos[0] = line;
        lastPos[1] = column + delta;
        return lastPos;
    }

    /**
     *  Returns the position marker without resetting it.  It will be
     *  set to whatever is was at the last position() call.
     */
    public int[] lastPosition() {
        return lastPos;
    }

    public void pushBack( int c ) throws IOException {
        if( debug ) {
            if( c == 0x0a ) {
                System.out.print("(<0x0a)");
            } else if( c == 0x0d ) {
                System.out.print("(<0x0d)");
            } else if( c == 0x09 ) {
                System.out.print("(<0x09)");
            } else {
                System.out.print("(<" + (char)c + ")");
            }            
        }
        reader.unread(c);
        column--;

        if( c == '\n' ) {
            // We partially deal with linefeed pushbacks
            // here with some caveats.  The biggest one is
            // that the column is temporarily incorrect because
            // it goes invalid.  When the character is read again
            // it will catch up, of course, but right after the
            // pushback the line is incorrect.
            line--;
        }            
    }

    public int read() throws IOException {
        int c = reader.read();
        if( c <= 0 ) {
            return c;
        }            

        if( c == '\n' ) {
            line++;
            column = 1;
        } else {
            column++;
        }

        if( debug )
            System.out.print((char)c);
        return c;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + line + ":" + column + "]";
    }    
}
