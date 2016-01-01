/*
 * $Id: OgexAnimation.java 3827 2014-11-23 08:13:36Z pspeed $
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

import com.google.common.collect.Lists;

import java.util.*;

/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexAnimation implements Iterable<OgexTrack> {
    
    private int clip;    
    private float begin;
    private float end;

    private final ArrayList<OgexTrack> tracks = Lists.newArrayList();
    
    public OgexAnimation() {
    }
    
    public void setClip( int i ) {
        this.clip = i;
    }
    
    public int getClip() {
        return clip;
    }
    
    public void setBegin( float f ) {
        this.begin = f;
    }
 
    public float getBegin() {
        return begin;
    }   
       
    public void setEnd( float f ) {
        this.end = f;
    }
    
    public float getEnd() {
        return end;
    }

    public Iterator<OgexTrack> iterator() {
        return tracks.iterator();
    }

    public void add(OgexTrack node) {
        tracks.add(node);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[clip=" + clip 
                + ", begin=" + getBegin() 
                + ", end=" + getEnd()
                + ", tracks=" + super.toString()
                + "]";
    }
}
