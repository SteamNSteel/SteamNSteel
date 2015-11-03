/*
 * $Id: OgexTexture.java 3827 2014-11-23 08:13:36Z pspeed $
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
public class OgexTexture {
    private long texCoord;
    private String texture;
    private List<OgexTransform> transforms;
    
    public OgexTexture() {

    }
    
    public void setTexture( String texture ) {
        this.texture = texture;
    }
    
    public String getTexture() {
        return texture;
    }
 
    public void setTexCoord( long l ) {
        this.texCoord = l;
    }
    
    public long getTexCoord() {
        return texCoord;
    }
    
    public void addTransform( OgexTransform o ) {
        if( transforms == null ) {
            transforms = new ArrayList<OgexTransform>();
        }
        transforms.add(o);
    }
    
    public List<OgexTransform> getTransforms() {
        if( transforms == null ) {
            return Collections.emptyList();
        }
        return transforms;
    }
 
    @Override   
    public String toString() {
        return getClass().getSimpleName() + "[texture=" + texture + ", texCoord=" + texCoord
                        + ", transforms=" + getTransforms() + "]"; 
    }       
}
