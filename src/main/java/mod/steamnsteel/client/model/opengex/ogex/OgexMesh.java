/*
 * $Id: OgexMesh.java 3827 2014-11-23 08:13:36Z pspeed $
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

import java.util.*;

/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexMesh {
 
    private long lod;
    private MeshType type;
    private List<OgexVertexArray> arrays = new ArrayList<OgexVertexArray>();
    private List<OgexIndexArray> indexes = new ArrayList<OgexIndexArray>();
    private OgexSkin skin;

    public OgexMesh() {
    }
    
    public void setLod( long lod ) {
        this.lod = lod;
    }
    
    public long getLod() {
        return lod;
    }
 
    public void setType( MeshType type ) {
        this.type = type;
    }
    
    public MeshType getType() {
        return type;
    }
 
    public void setSkin( OgexSkin skin ) {
        this.skin = skin;
    }
    
    public OgexSkin getSkin() {
        return skin;
    }
 
    public void addIndexArray( OgexIndexArray array ) {
        indexes.add(array);
    }
    
    public List<OgexIndexArray> getIndexArrays() {
        return indexes;
    }
    
    public void addVertexArray( OgexVertexArray array ) {
        arrays.add(array);
    }
    
    public List<OgexVertexArray> getVertexArrays() {
        return arrays;
    }
    
    public OgexVertexArray getVertexArray( long morph, String name ) {
        
        OgexVertexArray candidate = null;
        long maxMorph = -1;
        
        for( OgexVertexArray array : arrays ) {
            if( !name.equals(array.getName()) ) {
                continue;
            }
            long m = array.getMorph(); 
               
            if( m == morph ) {
                // Direct match, so return it
                return array;
            } 
        
            // Else see if it is a better match than we've seen
            if( m < morph && m > maxMorph ) {
                // The book is a little confusing here but I think this
                // is what they mean...  until I get to implementing real
                // morph targets and have some examples at least
                candidate = array;
                maxMorph = m;   
            }            
        }
        return candidate; // which may be null
    }
 
    @Override   
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ", lod=" + lod 
                            + ", vertexArrayCount=" + (arrays != null ? arrays.size() : 0) 
                            + ", indexArrayCount=" + (indexes != null ? indexes.size() : 0)
                            + ", skin=" + skin 
                            + "]";
    }
}
