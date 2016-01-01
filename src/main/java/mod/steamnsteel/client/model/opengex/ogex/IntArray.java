/*
 * $Id: IntArray.java 3827 2014-11-23 08:13:36Z pspeed $
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

package mod.steamnsteel.client.model.opengex.ogex;

import java.lang.reflect.Array;
import java.util.*;

/**
 *
 *
 *  @author    Paul Speed
 */
public class IntArray implements Iterable<Number> {
 
    private Class type;
    private Object array;
    private int size;
    
    public IntArray( Object array ) {
        if( array == null ) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        this.array = array;
        this.type = array.getClass().getComponentType();
        this.size = Array.getLength(array);
    }
    
    public int length() {
        return size; 
    }
 
    public Class getType() {
        return type;
    }
 
    public byte[] asByteArray() {
        if( type == Byte.TYPE ) {
            return (byte[])array;
        }
        throw new RuntimeException("Type overflow, " + type + "[] cannot be converted to byte[]");
    }

    public short[] asShortArray() {
        if( type == Short.TYPE ) {
            return (short[])array;
        }
        if( type == Byte.TYPE ) {
            short[] result = new short[size];
            for( int i = 0; i < result.length; i++ ) {
                result[i] = ((Number)Array.get(array, i)).shortValue();
            }
            return result;
        } 
        throw new RuntimeException("Type overflow, " + type + "[] cannot be converted to short[]");
    }

    public int[] asIntArray() {
        if( type == Integer.TYPE ) {
            return (int[])array;
        }
        if( type == Byte.TYPE || type == Short.TYPE ) {
            int[] result = new int[size];
            for( int i = 0; i < result.length; i++ ) {
                result[i] = ((Number)Array.get(array, i)).intValue();
            }
            return result;
        } 
        throw new RuntimeException("Type overflow, " + type + "[] cannot be converted to int[]");
    }

    public long[] asLongArray() {
        if( type == Long.TYPE ) {
            return (long[])array;
        }
        if( type == Byte.TYPE || type == Short.TYPE || type == Integer.TYPE ) {
            long[] result = new long[size];
            for( int i = 0; i < result.length; i++ ) {
                result[i] = ((Number)Array.get(array, i)).longValue();
            }
            return result;
        } 
        throw new RuntimeException("Type overflow, " + type + "[] cannot be converted to long[]");
    }
    
    public Iterator<Number> iterator() {
        return new IntIterator();
    }   
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + type + ":" + size + "]";
    }
 
    private class IntIterator implements Iterator<Number> {
 
        private int next = 0;
 
        protected IntIterator() {
        }
 
        public boolean hasNext() {
            return next < size;
        }
        
        public Number next() {
            Number result = (Number)Array.get(array, next++);
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
        }
    }   
}
