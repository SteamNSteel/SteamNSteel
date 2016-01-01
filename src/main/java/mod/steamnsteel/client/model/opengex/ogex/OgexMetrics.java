/*
 * $Id: OgexMetrics.java 3827 2014-11-23 08:13:36Z pspeed $
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
public class OgexMetrics extends HashMap<String, Object> {
    
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_ANGLE = "angle";
    public static final String KEY_TIME = "time";
    public static final String KEY_UP = "up";
 
    public OgexMetrics() {
    }
 
    @SuppressWarnings("unchecked")
    public <T> T get( String key, T defaultValue ) {
        Object o = get(key);
        return o == null ? defaultValue : (T)o;
    } 
    
    public void setDistance( float f ) {
        put(KEY_DISTANCE, f); 
    }
    
    public float getDistance() {
        return get(KEY_DISTANCE, 1f);
    }
    
    public void setAngle( float f ) {
        put(KEY_ANGLE, f); 
    }
    
    public float getAngle() {
        return get(KEY_ANGLE, 1f);
    }
    
    public void setTime( float f ) {
        put(KEY_TIME, f); 
    }
    
    public float getTime() {
        return get(KEY_TIME, 1f);
    }
    
    public void setUp( Axis axis ) {
        put(KEY_UP, axis);
    }
    
    public Axis getUp() {
        return get(KEY_UP, Axis.Z);
    } 
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + entrySet();
    }
}
