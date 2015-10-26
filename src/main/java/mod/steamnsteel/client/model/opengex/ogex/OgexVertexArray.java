/*
 * $Id: OgexVertexArray.java 3827 2014-11-23 08:13:36Z pspeed $
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


/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexVertexArray {
    
    private String name;
    private Object array;
    private int elementCount;
    private int elementSize;
    private long morph;
    private int index;
    
    public OgexVertexArray() {
    }
    
    public void setName( String name ) {
        int split = name.indexOf("[");
        if( split > 0 ) {   
            int last = name.lastIndexOf("]");
            String part = name.substring(split + 1, last);
            setIndex(Integer.parseInt(part));
            name = name.substring(0, split);
        }
        this.name = name;
    }
    
    public String getName() {
        return name;
    }    

    public void setIndex( int index ) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }

    public void setMorph( long morph ) {
        this.morph = morph;
    }
    
    public long getMorph() {
        return morph;
    }

    public void setArray( float[] array ) {
        this.elementSize = 1;
        this.array = array;
        this.elementCount = array.length;   
    }    

    public void setArray( float[][] array, int elementSize ) {
        this.array = array;
        this.elementSize = elementSize;
        this.elementCount = array.length;   
    }
 
    public float[] getArray() {
        if( elementSize != 1 ) {
            throw new RuntimeException("Array is multidimensional.");
        }   
        return (float[])array; 
    }
    
    public float[][] getArray2() {
        if( elementSize == 1 ) {
            throw new RuntimeException("Array is single dimensional.");
        }
        return (float[][])array;
    }
    
    public int getElementSize() {
        return elementSize;
    }
 
    public int getSize() {
        return elementCount;
    }
 
    @Override
    public String toString() {
        return getClass().getSimpleName() 
                + "[name=" + name + (index > 0 ? "[" + index + "]" : "") 
                + ", vertexCount=" + elementCount + "]"; 
    }
}
