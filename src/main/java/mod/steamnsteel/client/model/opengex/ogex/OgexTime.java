/*
 * $Id: OgexTime.java 3827 2014-11-23 08:13:36Z pspeed $
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

import java.util.Arrays;

/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexTime {
    
    private Curve curve;
    private OgexKey[] keys;
    
    public OgexTime() {
    }
 
    protected void validateKeys() {
        if( keys == null ) {
            return;
        }
        // TODO
    }
    
    public void setCurve( Curve curve ) {
        if( curve != Curve.Linear && curve != Curve.Bezier ) {
            throw new IllegalArgumentException("Invalud Time curve:" + curve);
        }
        this.curve = curve;
        validateKeys();
    }
    
    public Curve getCurve() {
        return curve == null ? Curve.Linear : curve;
    }
    
    public void setKeys( OgexKey... keys ) {
        this.keys = keys;
        validateKeys();
    }
    
    public OgexKey[] getKeys() {
        return keys;        
    }
 
    @Override   
    public String toString() {
        return getClass().getSimpleName() + "[curve=" + getCurve() 
                        + ", keys=" + (keys != null ? Arrays.asList(keys) : null) 
                        + "]";
    } 
}
