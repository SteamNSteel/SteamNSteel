/*
 * $Id: OgexLightObject.java 3826 2014-11-23 08:01:12Z pspeed $
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
public class OgexLightObject extends BaseMaterialized {

    public enum Type { Infinite("infinite"), Point("point"), Spot("spot");
                       
        private String ogexName;
        
        private Type( String ogexName ) {
            this.ogexName = ogexName;
        }
        
        public String getOgexName() {
            return ogexName;
        }
        
        public static Type lookup( String ogexName ) {
            for( Type type : values() ) {
                if( ogexName.equals(type.ogexName) ) {
                    return type;
                }
            }
            return null;
        }
                
    }
 
    private static String KEY_LIGHT = "light";
    private static String KEY_PROJECTION = "projection";
    private static String KEY_INTENSITY = "intensity";
 
    private Type type;   
    private boolean shadow;
    private List<OgexAtten> attenFunctions = new ArrayList<OgexAtten>();
    
    public OgexLightObject() {
    }
 
    public void setType( Type t ) {
        this.type = t;
    }
    
    public Type getType() {
        return type;
    }
    
    public void setShadow( boolean b ) {
        this.shadow = b;
    }
    
    public boolean getShadow() {
        return shadow;
    }

    public void setLightColor( OgexColor color ) {
        setColor(KEY_LIGHT, color);
    }
    
    public OgexColor getLightColor() {
        return getColor(KEY_LIGHT, OgexColor.WHITE);
    }

    public void setProjectionTexture( OgexTexture t ) {
        setTexture(KEY_PROJECTION, t);
    }
    
    public OgexTexture getProjectionTexture() {
        return getTexture(KEY_PROJECTION, null);
    }    
    
    public void setIntensity( float f ) {
        setParam(KEY_INTENSITY, f);
    }
    
    public float getIntensity() {
        return getParam(KEY_INTENSITY, 1f);
    }

    public void addAttenuationFunction( OgexAtten atten ) {
        attenFunctions.add(atten);
    }
    
    public List<OgexAtten> getAttenuationFunctions() {
        return attenFunctions;
    }

    @Override
    protected void appendFieldStrings( StringBuilder sb ) {
        if( sb.length() > 0 ) {
            sb.append(", ");
        }
        sb.append("type=" + type);
        sb.append(", shadow=" + shadow);
        if( !attenFunctions.isEmpty() ) {
            sb.append(", attenfuncs=" + attenFunctions);
        } 
        super.appendFieldStrings(sb);
    }
}
