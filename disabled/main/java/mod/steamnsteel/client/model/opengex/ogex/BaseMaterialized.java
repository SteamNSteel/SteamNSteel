/*
 * $Id: BaseMaterialized.java 3826 2014-11-23 08:01:12Z pspeed $
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
 *  Base class for any object that takes params, textures,
 *  and colors.
 *
 *  @author    Paul Speed
 */
public abstract class BaseMaterialized {

    private Map<String, OgexColor> colors = new HashMap<String, OgexColor>();
    private Map<String, Float> params = new HashMap<String, Float>();
    private Map<String, OgexTexture> textures = new HashMap<String, OgexTexture>();
 
    protected BaseMaterialized() {
    }
    
    public void setColor( String name, OgexColor color ) {
        if( color == null ) {
            colors.remove(name);
        } else {
            colors.put(name, color);
        }
    }
    
    public OgexColor getColor( String name, OgexColor defaultValue ) {
        OgexColor result = getColor(name);
        return result == null ? defaultValue : result;
    }
    
    public OgexColor getColor( String name ) {
        return colors.get(name);
    }

    public void setTexture( String name, OgexTexture texture ) {
        if( texture == null ) {
            textures.remove(name);
        } else {
            textures.put(name, texture);
        }
    }
    
    public OgexTexture getTexture( String name, OgexTexture defaultValue ) {
        OgexTexture result = getTexture(name);
        return result == null ? defaultValue : result;
    }
    
    public OgexTexture getTexture( String name ) {
        return textures.get(name);
    }
 
    public void setParam( String name, Float value ) {
        if( value == null ) {
            params.remove(name);
        } else {
            params.put(name, value);
        }       
    }
    
    public float getParam( String name, float defaultValue ) {
        Float result = getParam(name);
        return result == null ? defaultValue : result; 
    }
 
    public Float getParam( String name ) {
        return params.get(name);
    }
    
    protected void appendFieldStrings( StringBuilder sb ) {
        if( sb.length() > 0 ) {
            sb.append(", ");
        }
        sb.append("params=" + params);
        sb.append(", colors=" + colors);
        sb.append(", textures=" + textures);         
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        appendFieldStrings(sb);
        
        return getClass().getSimpleName() + "[" + sb + "]";
    }
}
