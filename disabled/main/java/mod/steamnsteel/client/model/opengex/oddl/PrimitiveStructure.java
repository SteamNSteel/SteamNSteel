/*
 * $Id: PrimitiveStructure.java 3828 2014-11-23 08:21:03Z pspeed $
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
 *  Represents an array of primitives.
 *
 *  @author    Paul Speed
 */
public class PrimitiveStructure extends BaseStructure {
 
    private PrimitiveType type;   
    private int elementCount;
    private int elementSize;
    private Object data;
    
    public PrimitiveStructure( PrimitiveType type, DataStructure parent, int[] location ) {        
        super(parent, location);
        this.type = type;
    }
 
    public PrimitiveType getPrimitiveType() {
        return type;
    }
    
    public int getSize() {
        return elementCount; 
    }
    
    protected void setElementSize( int i ) {
        this.elementSize = i;
    }
    
    public int getElementSize() {
        return elementSize;
    }
    
    protected void setData( Object data ) {
        this.data = data;
        if( data == null ) {
            this.elementCount = 0;
        } else {
            this.elementCount = Array.getLength(data);
        }
    }
    
    @Override
    public Object getData() {
        return data;
    }
 
    /**
     *  Convenience method for primitive structures that only hold
     *  one array value.  Throws a runtime exception if the array has
     *  more than one value.  Returns null if the array is empty or null.
     */   
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        if( elementCount == 0 || data == null ) {
            return null;
        }
        if( elementCount > 1 ) {
            throw new RuntimeException("Array has more than one value");
        }
        // Else, let's extract that puppy
        T value = (T)Array.get(data, 0);
        return value;
    }
 
    @Override
    public String getType() {
        if( elementSize > 0 ) {
            return type.getId() + "[" + elementSize + "]"; 
        } else {
            return type.getId();
        }        
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "[");
        sb.append("type=" + type);
        if( getName() != null ) {
            sb.append(", name=" + getName());
        }
        if( data != null ) {
            sb.append(", data=" + type.arrayToString(data));
        }            
        sb.append("]");
        return sb.toString();        
    }
}
