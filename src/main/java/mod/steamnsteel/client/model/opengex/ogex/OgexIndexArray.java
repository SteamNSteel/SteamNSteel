/*
 * $Id: OgexIndexArray.java 3827 2014-11-23 08:13:36Z pspeed $
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

import mod.steamnsteel.client.model.opengex.oddl.PrimitiveType;

/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexIndexArray {
    
    private long material;
    private long restart;
    private VertexWinding front;
    private int elementSize;
    private int elementCount;
    private PrimitiveType elementType;
    private Object array;
    
    public OgexIndexArray() {
    }
    
    public void setMaterial( long material ) {
        this.material = material;
    }
    
    public long getMaterial() {
        return material;
    }
    
    public void setRestart( long restart ) {
        this.restart = restart;
    }
    
    public long getRestart() {
        return restart;
    }
    
    public void setFront( VertexWinding front ) {
        this.front = front;
    }
    
    public VertexWinding getFront() {
        return front;
    }
 
    public void setArray( Object array, PrimitiveType elementType, int elementSize ) {
        this.elementCount = Array.getLength(array);
        this.elementSize = elementSize;
        this.array = array;
        this.elementType = elementType; 
    }
 
    public PrimitiveType getElementType() {
        return elementType;
    }
    
    public int getElementSize() {
        return elementSize;
    }
    
    public int getSize() {
        return elementCount;
    }
 
    public Object getArray() {
        return array;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[material=" + material + ", restart=" + restart 
                        + ", arrayType=" + elementType.getId() + "[" + elementCount + "]" 
                        + (elementSize <= 1 ? "" : "[" + elementSize + "]" + "]"); 
    }    
}
