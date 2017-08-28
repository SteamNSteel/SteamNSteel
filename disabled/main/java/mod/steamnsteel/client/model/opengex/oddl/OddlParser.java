/*
 * $Id: OddlParser.java 3828 2014-11-23 08:21:03Z pspeed $
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
import java.lang.reflect.Array;
import java.util.*;

/**
 *  Parser for the Open Data Description Language http://openddl.org/
 *
 *  @author    Paul Speed
 */
public class OddlParser {

    private boolean debug = false;

    private static final Map<String, DataType> DATA_TYPES = new HashMap<String, DataType>();
    static {
        DATA_TYPES.put("bool", new BooleanType()); 
        DATA_TYPES.put("int8", new IntType(PrimitiveType.Int8, 0xff, true)); 
        DATA_TYPES.put("int16", new IntType(PrimitiveType.Int16, 0xffff, true));  
        DATA_TYPES.put("int32", new IntType(PrimitiveType.Int32, 0xffffffff, true));  
        DATA_TYPES.put("int64", new IntType(PrimitiveType.Int64));  
        DATA_TYPES.put("unsigned_int8", new IntType(PrimitiveType.Uint8, 0xff));  
        DATA_TYPES.put("unsigned_int16", new IntType(PrimitiveType.Uint16, 0xffff));  
        DATA_TYPES.put("unsigned_int32", new IntType(PrimitiveType.Uint32, 0xffffffff));  
        DATA_TYPES.put("unsigned_int64", new IntType(PrimitiveType.Uint64)); // not really supported  
        DATA_TYPES.put("float", new FloatType());  
        DATA_TYPES.put("double", new DoubleType());  
        DATA_TYPES.put("string", new StringType());  
        DATA_TYPES.put("ref", new RefType());  
        DATA_TYPES.put("type", new TypeType());          
    }         

    private OddlLexer in;
    private Map<String, BaseStructure> globals = new HashMap<String, BaseStructure>();
    private DataStructure root;

    public OddlParser( Reader in ) throws IOException {
        this.in = new OddlLexer(in);
        this.root = new DataStructure("$", null, null);
    }
            
    /**
     *  
     */
    public List<BaseStructure> parse() throws IOException {    
        root.setChildren(parseStructContents(root, -1));
        resolveReferences(root);
        return root.getChildren();
    }

    protected static DataType getDataType( String s ) {
        return DATA_TYPES.get(s);
    }

    protected BaseStructure resolveReference( DataStructure parent, Reference ref ) throws IOException {
        if( ref.isGlobal() ) {
            return root.findChild(ref.getReferenceTokens());
        }

        // Check up the tree
        for( DataStructure searchRoot = parent; searchRoot.getParent() != null; searchRoot = searchRoot.getParent() ) {
            BaseStructure replace = searchRoot.findChild(ref.getReferenceTokens());
            if( replace != null ) {
                return replace;
            }
        }
        return null;
    }

    protected void resolveReferences( DataStructure parent, PrimitiveStructure ps ) throws IOException {    
        if( ps.getPrimitiveType() != PrimitiveType.Ref ) {
            return;
        }
        if( ps.getElementSize() != 0 ) {
            BaseStructure[][] refs = (BaseStructure[][])ps.getData();
            for( BaseStructure[] sub : refs ) {
                resolveReferences(parent, sub);
            }
        } else {
            resolveReferences(parent, (BaseStructure[])ps.getData());
        }
    } 

    protected void resolveReferences( DataStructure parent, BaseStructure[] refs ) throws IOException {
        for( int i = 0; i < refs.length; i++ ) {
            if( refs[i] == null ) {
                continue;
            }
            Reference ref = (Reference)refs[i];
            BaseStructure replace = resolveReference(parent, ref);           
            if( replace == null ) {
                in.error("Unresolved reference " + ref.getReference(), ref.getLocation());
            }
            refs[i] = replace; 
        }
    } 

    protected void resolveProperties( DataStructure child ) throws IOException {

        // Resolve the properties
        for( Map.Entry<String, Object> e : child.getProperties().entrySet() ) {
            Object value = e.getValue();
            if( !(value instanceof Reference) ) {
                continue;
            }
            Reference ref = (Reference)value;
            BaseStructure replace = resolveReference(child, ref);           
            if( replace == null ) {
                in.error("Unresolved reference " + ref.getReference(), ref.getLocation());
            }
            e.setValue(replace);
        }        
    }

    protected void resolveReferences( DataStructure parent ) throws IOException {
    
        for( ListIterator<BaseStructure> it = parent.getChildren().listIterator(); it.hasNext(); ) {
            BaseStructure child = it.next();
            if( child instanceof PrimitiveStructure ) {
                resolveReferences(parent, (PrimitiveStructure)child);
            } else if( child instanceof DataStructure ) {
                resolveProperties((DataStructure)child); 
                resolveReferences((DataStructure)child);
            }
        } 
    }

    protected Object parsePropertyValue( DataStructure parent ) throws IOException {
        
        // Might be:
        //   bool-literal
        //   integer-literal
        //   float-literal
        //   string-literal
        //   reference
        //   data-type
        //
        // Many we can detect.  Some are ambiguous.
    
        int c = in.nextNonWhitespace();
        if( c < 0 ) {
            in.error("Expected literal, found end of file");
        }

        if( c == '"' ) {
            in.pushBack(c);
            return in.parseString();
        } else if( Character.isDigit(c) || c == '+' || c == '-' ) {
            in.pushBack(c);
            return in.parseUnknownNumber();            
        } else if( c == '$' || c == '%' ) {
            // We _know_ this is a ref at least... and won't be readable with 
            // the standard parseIdentifier().
            in.pushBack(c);
            String ref = in.parseReference();
            if( ref == null ) {
                return null;
            }
            return new Reference(ref, in.lastPosition());
        } else {
            in.pushBack(c);
            String val = in.parseIdentifier("value");
            if( "true".equals(val) ) {
                return Boolean.TRUE; 
            } else if( "false".equals(val) ) {
                return Boolean.FALSE; 
            } if( "null".equals(val) ) {
                return null;
            }
 
            // By process of elimination, it must be a type reference
            // or an error
            DataType type = getDataType(val);
            if( type == null ) {
                in.error("Invalid property value:" + val, in.lastPosition());
                return null; // never called
            }
            
            return type.getType(); 
        } 
    }     

    protected Map<String, Object> parseProperties( DataStructure parent ) throws IOException {
 
        // Properties format:
        //
        // properties ::= '(' (key '=' value)* ')'
        //
        // the first ( has already been read by now
    
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        
        in.position(-1).clone();
        
        int c;
        while( (c = in.nextNonWhitespace()) != ')' ) {
            if( c < 0 ) {
                in.error("Unexpected end of file in property values");
            }
 
            if( result.isEmpty() ) {
                // First pass, so push back           
                in.pushBack(c);
            } else if( c != ',' ) {
                in.error("Missing ',' in property values", in.position(-1));
            }
            String key = in.parseIdentifier("property");
            c = in.nextNonWhitespace();
            if( c != '=' ) {
                in.error("Expected '=', found:" + (char)c, in.position(-1));
            }
            
            Object value = parsePropertyValue(parent);
            
            result.put(key, value);   
        }
        
        return result;
    }

    protected PrimitiveStructure parseArray( DataStructure parent, DataType dataType ) throws IOException {
        if( debug ) 
            System.out.println("\nparseArray(" + dataType + ")");
 
        PrimitiveStructure result = new PrimitiveStructure(dataType.getType(), parent, in.lastPosition().clone());
 
        // Array format:
        //
        // array ::= 
        //  Name? '{' DataList '}'
        //   |
        //  ArraySize Name? '{' DataArrayList '}'
 
        int c = in.nextNonWhitespace();
        int size = 0;
        
        if( c == '[' ) {
            // Array size
            size = (int)in.parseLong(0xffffffff, true);
            c = in.nextNonWhitespace();
            if( c != ']' ) {
                in.error("Expected ']' but found '" + (char)c + "'", in.position(-1));
            } else {
                c = in.nextNonWhitespace();
            }
            result.setElementSize(size);            
        }
        
        if( c == '%' || c == '$' ) {
            // Array name
            String name = in.parseIdentifier("name");
            result.setName((char)c + name);
                        
            if( c == '$' ) {
                if( !root.addIndex(name, result) ) {
                //if( globals.put(name, result) != null ) {
                    in.error("Global name '" + name + "' is already used", in.lastPosition());
                }
            } else {
                // We add the indexes as we encounter them so that
                // we can error right away on duplicate
                if( !parent.addIndex(name, result) ) {
                    in.error("Local name '" + name + "' is already used", in.lastPosition());
                } 
            }  
                        
            c = in.nextNonWhitespace(); 
        }
 
        if( c != '{' ) {
            in.error("Unexpected character '" + (char)c + "' at array start", in.position(-1));
        }
 
        Object array = dataType.parseArray(in, size);
        
        result.setData(array);
 
        if( debug ) {
            System.out.print( "\nArray[" );       
            int length = Array.getLength(array);
            for( int i = 0; i < length; i++ ) {
                if( i > 0 ) {
                    System.out.print(',');
                }
                System.out.print(Array.get(array, i));
            }
            System.out.println("]");
        }
        
        return result;
    }

    protected BaseStructure parseStruct( DataStructure parent ) throws IOException {

        // General struct format:
        // 
        // struct ::= 
        //  Identifier Name? Properties? StructContents
        //   |
        //  DataType Name? DataList
        //   |
        //  DataType ArraySize Name? DataArrayList
        //
        
        // Identifier or DataType can be parsed the same way                     
        String id = in.parseIdentifier("identifier");
        DataType type = getDataType(id);            
        if( type != null ) {
            return parseArray(parent, type);
        }

        if( debug )
            System.out.println("\nidentifier:" + id + "  from:" + in.lastPosition()[0] + ":" + in.lastPosition()[1]);
 
        DataStructure result = new DataStructure(id, parent, in.lastPosition());
 
        int c = in.nextNonWhitespace();
        if( c < 0 ) {
            in.error("End of file reached in structure header");
        }
        if( c == '$' || c == '%' ) {
            // Structure name
            String name = in.parseIdentifier("name");
            result.setName((char)c + name);
            
            if( c == '$' ) {
                if( !root.addIndex(name, result) ) {
                //if( globals.put(name, result) != null ) {
                    in.error("Global name '" + name + "' is already used", in.lastPosition());
                }
            } else {
                // We add the indexes as we encounter them so that
                // we can error right away on duplicate
                if( !parent.addIndex(name, result) ) {
                    in.error("Local name '" + name + "' is already used", in.lastPosition());
                } 
            }  
                        
            c = in.nextNonWhitespace(); 
        }
        if( c == '(' ) {
            // Properties
            Map<String, Object> props = parseProperties(result);            
            result.setProperties(props);
            c = in.nextNonWhitespace(); 
        }
        if( c != '{' ) {
            in.error("No structure contents found", in.position());
        }
        List<BaseStructure> children = parseStructContents(result, '}');
        if( !children.isEmpty() ) {
            result.setChildren(children);
        } 
        
        return result;
    }

    protected List<BaseStructure> parseStructContents( DataStructure parent, int end ) throws IOException {
    
        if( debug ) 
            System.out.println("\nparseStructContents()");
        // General struct contents:
        // contents ::= 
        //  '{'
        //      (struct)*
        //  '}'
        // 
        // by now the '{' would have already been read so we keep
        // processing until we see the 'end'

 
        List<BaseStructure> children = new ArrayList<BaseStructure>();
                   
        int c;        
        while( (c = in.nextNonWhitespace()) != end ) {
            // If we reach the end of the file early then
            // it's an error
            if( c == -1 ) {
                in.error("Missing '" + (char)end + "' at EOF");            
            }
 
            // Get ready to read the identifier that we already
            // started reading the first character           
            in.pushBack(c);
     
            BaseStructure o = parseStruct(parent);
            children.add(o);
        }            
                  
        return children;
    }
 
    /**
     *  For command line ODDL validation and testing.
     */   
    public static void main( String... args ) throws Exception {
    
        for( String s : args ) {
            OddlParser parser = new OddlParser(new BufferedReader(new FileReader(s), 16384));
            Object o = parser.parse();
            System.out.println("\n" + s + " parsed as:");
            System.out.println(o);       
        }
    }

    protected static abstract class DataType {
 
        private PrimitiveType type;
        
        protected DataType( PrimitiveType type ) {
            this.type = type;
        }       
 
        public PrimitiveType getType() {
            return type;
        }
    
        protected abstract Object parseValue( OddlLexer in ) throws IOException;
 
        protected abstract Object toArray( List list ) throws IOException;
    
        public Object parseArray( OddlLexer in, int size ) throws IOException {
            if( size == 0 ) {
                return parseArray(in);
            }
            List<Object> results = new ArrayList<Object>();
            int c;
            while( (c = in.nextNonWhitespace()) != '}' ) {
                if( !results.isEmpty() ) {
                    // Move past the comma as necessary
                    if( c == ',' ) {
                        c = in.nextNonWhitespace();
                    } else if( c < 0 ) {
                        in.error("Unexpected end of file in array");  
                    } else {
                        in.error("Missing ',' in array values", in.position(-1));
                    }
                } else {
                    // The first character is part of the child... which should
                    // be the open bracket, so we don't have to push it back
                }
                
                if( c == '{' ) {
                    // Start of next child
                    // Parse the child array
                    int[] pos = in.position(-1).clone();
                    Object o = parseArray(in);
                    int len = Array.getLength(o); 
                    if( size != len ) {
                        in.error("Expected " + size + " elements but found:" + len, pos);  
                    }                
                    results.add(o);
                } else if( c < 0 ) {
                    in.error("Unexpected end of file in array");
                } else {                               
                    in.error("Unexpected character '" + (char)c + "' in array", in.position(-1));
                }
            }
            
            // Now create an array of arrays
            Object array = Array.newInstance(getType().getJavaType(), results.size(), size);
            for( int i = 0; i < results.size(); i++ ) {
                Array.set(array, i, results.get(i));
            }
            return array; 
        }

        public Object parseArray( OddlLexer in ) throws IOException {
            
            List<Object> results = new ArrayList<Object>();
            int c;
            while( (c = in.nextNonWhitespace()) != '}' ) {
                if( !results.isEmpty() ) {
                    // Move past the comma
                    if( c != ',' ) {
                        in.error("Missing ',' in array values", in.position(-1));
                    } else if( c < 0 ) {
                        in.error("Unexpected end of file in array values");  
                    }
                } else {
                    // The first character is part of the value
                    in.pushBack(c);
                }
                Object o = parseValue(in);
                results.add(o);
            }
            return toArray(results);
        } 
        
    }
 
    protected static class TypeType extends DataType {
 
        public TypeType() {
            super(PrimitiveType.Type);
        }
     
        @Override
        protected Object toArray( List list ) throws IOException {
            PrimitiveType[] array = new PrimitiveType[list.size()];
            for( int i = 0; i < array.length; i++ ) {
                array[i] = (PrimitiveType)list.get(i);
            }
            return array;
        }
    
        @Override
        public Object parseValue( OddlLexer in ) throws IOException {
            String val = in.parseIdentifier("type");
            DataType type = getDataType(val);
            if( type == null ) {
                in.error("Invalid type literal:" + val, in.lastPosition());
                return null; // never used
            } 
            return type.getType(); 
        } 
    }
    
    protected static class StringType extends DataType {
    
        public StringType() {
            super(PrimitiveType.String);
        }
     
        @Override
        protected Object toArray( List list ) throws IOException {
            String[] array = new String[list.size()];
            for( int i = 0; i < array.length; i++ ) {
                array[i] = (String)list.get(i);
            }
            return array;
        }
    
        @Override
        public Object parseValue( OddlLexer in ) throws IOException {
            String result = in.parseString();
            
            // check the next character for another string start
            int c;
            while( (c = in.nextNonWhitespace()) == '"' ) {
                in.pushBack(c);
                result += in.parseString();
            }
            in.pushBack(c);
            return result;
        } 
    }

    protected static class RefType extends DataType {
    
        public RefType() {
            super(PrimitiveType.Ref);
        }
     
        @Override
        protected Object toArray( List list ) throws IOException {
            BaseStructure[] array = new BaseStructure[list.size()];
            for( int i = 0; i < array.length; i++ ) {
                array[i] = (BaseStructure)list.get(i);
            }
            return array;
        }
    
        @Override
        public Object parseValue( OddlLexer in ) throws IOException {
 
            String ref = in.parseReference();
            if( ref == null ) {
                return null;
            }
            return new Reference(ref, in.lastPosition());
        } 
    }
    
    protected static class BooleanType extends DataType {
 
        public BooleanType() {
            super(PrimitiveType.Bool);
        }     
   
        @Override
        protected Object toArray( List list ) {
            boolean[] array = new boolean[list.size()];
            for( int i = 0; i < array.length; i++ ) {
                array[i] = (Boolean)list.get(i);
            }
            return array;
        }
    
        @Override
        public Object parseValue( OddlLexer in ) throws IOException {
            String literal = in.parseIdentifier("bool");
            if( "true".equals(literal) ) {
                return Boolean.TRUE;
            }
            if( "false".equals(literal) ) {
                return Boolean.FALSE;
            }
            in.error("Invalid bool:" + literal, in.position());
            return null;
        } 
    }
 
    protected static class FloatType extends DoubleType {
    
        public FloatType() {
            super(PrimitiveType.Float);
        }
    
        @Override
        protected Object fromBits( long value, int sign ) {
            return Float.intBitsToFloat((int)value) * sign;
        }
        
        @Override
        protected Object toArray( List list ) {
            float[] result = new float[list.size()];
            for( int i = 0; i < result.length; i++ ) {
                Number num = (Number)list.get(i);
                result[i] = num.floatValue();
            }
            return result;
        }
        
        @Override
        public Object parseValue( OddlLexer in ) throws IOException {
            return in.parseFloat();
        }            
    }

    protected static class DoubleType extends DataType {
    
        public DoubleType() {
            super(PrimitiveType.Double);
        }

        protected DoubleType( PrimitiveType type ) {
            super(type);
        }
    
        protected Object fromBits( long value, int sign ) {
            return Double.longBitsToDouble(value) * sign;
        }
        
        @Override
        protected Object toArray( List list ) {
            double[] result = new double[list.size()];
            for( int i = 0; i < result.length; i++ ) {
                Number num = (Number)list.get(i);
                result[i] = num.doubleValue();
            }
            return result;
        }
        
        @Override
        public Object parseValue( OddlLexer in ) throws IOException { 
            return in.parseDouble();
        } 
    }

    protected static class IntType extends DataType {
 
        private long bitMask;
        private boolean signed;
        
        public IntType( PrimitiveType type ) {
            this(type, 0xffffffffffffffffL, true);
        }
        
        public IntType( PrimitiveType type, long bitMask ) {
            this(type, bitMask, false);
        }
        
        public IntType( PrimitiveType type, long bitMask, boolean signed ) {
            super(type);
            this.bitMask = bitMask;
            this.signed = signed;
        }
    
        protected Object toType( Number num ) {
            Class jType = getType().getJavaType();
            if( jType == Long.TYPE || jType == Double.TYPE ) {
                // Should already be the right type
                return num;
            }
            if( jType == Byte.TYPE ) {
                return num.byteValue();
            }
            if( jType == Short.TYPE ) {
                return num.shortValue(); 
            }
            if( jType == Integer.TYPE ) {
                return num.intValue();
            }
            if( jType == Float.TYPE ) {
                return num.floatValue();
            }
            throw new UnsupportedOperationException("Unhandled type:" + getType());
        }
    
        @Override
        protected Object toArray( List list ) {
            Object array = Array.newInstance(getType().getJavaType(), list.size());
            for( int i = 0; i < list.size(); i++ ) {
                Object val = toType((Number)list.get(i));
                Array.set(array, i, val);
            }
            return array; 
        }
        
        @Override
        public Object parseValue( OddlLexer in ) throws IOException {            
            return in.parseLong(bitMask, signed);
        } 
    }    
}


