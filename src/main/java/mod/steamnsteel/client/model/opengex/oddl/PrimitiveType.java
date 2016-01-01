/*
 * $Id: PrimitiveType.java 3828 2014-11-23 08:21:03Z pspeed $
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

import java.lang.reflect.Array;


/**
 *  Represents one of the primitive data types supported by
 *  ODDL.
 *
 *  @author    Paul Speed
 */
public enum PrimitiveType {

    Bool("bool", Boolean.TYPE), 
    Int8("int8", Byte.TYPE),
    Int16("int16", Short.TYPE),
    Int32("int32", Integer.TYPE),
    Int64("int64", Long.TYPE),
    Uint8("unsigned_int8", Short.TYPE),
    Uint16("unsigned_int16", Integer.TYPE),
    Uint32("unsigned_int32", Long.TYPE),
    Uint64("unsigned_int64", Long.TYPE), // not fully supported
    Float("float", java.lang.Float.TYPE),
    Double("double", java.lang.Double.TYPE),
    String("string", java.lang.String.class),
    Ref("ref", BaseStructure.class),
    Type("type", PrimitiveType.class);
 
 
    private String id;
    private Class javaType;
       
    private PrimitiveType( String id, Class type ) {
        this.id = id;
        this.javaType = type;
    }
 
    public static PrimitiveType forId( String id ) {
        for( PrimitiveType type : values() ) {
            if( type.id.equals(id) ) {
                return type;
            }
        }
        return null;
    }
 
    public Class getJavaType() {
        return javaType;
    }
    
    public String getId() {
        return id;
    }
 
    public String arrayToString( Object array ) {
        StringBuilder sb = new StringBuilder();
        int length = Array.getLength(array);
        for( int i = 0; i < length; i++ ) {
            Object val = Array.get(array, i);
            if( val != null && val.getClass().isArray() ) {
                val = arrayToString(val);
            }
            if( i > 0 ) {
                sb.append(", ");
            }
            sb.append(val);
        }
        return "[" + sb + "]";
    }    
}
