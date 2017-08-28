/*
 * $Id: OgexColor.java 3827 2014-11-23 08:13:36Z pspeed $
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
public class OgexColor {

    public static final OgexColor WHITE = new OgexColor(1, 1, 1, 1); 

    private float[] color;
    
    public OgexColor() {
    }
    
    public OgexColor( float... f ) {
        setColor(f);
    }
 
    public final void setColor( float[] color ) {
        if( color.length < 4 ) {
            this.color = new float[] { color[0], color[1], color[2], 1 };
        } else {
            this.color = color;
        }
    }
    
    public float[] getColor() {
        return color;
    }
    
    public float getRed() {
        return color != null ? color[0] : 1;
    }

    public float getGreen() {
        return color != null ? color[1] : 1;
    }

    public float getBlue() {
        return color != null ? color[2] : 1;
    }

    public float getAlpha() {
        return color != null ? color[3] : 1;
    }
    
    @Override
    public String toString() {
        if( color == null ) {
            return getClass().getSimpleName() + "[]";
        }
        return getClass().getSimpleName() + "[" 
                + getRed() + ", " 
                + getGreen() + ", " 
                + getBlue() + ", " 
                + getAlpha() + "]";
    }
}
