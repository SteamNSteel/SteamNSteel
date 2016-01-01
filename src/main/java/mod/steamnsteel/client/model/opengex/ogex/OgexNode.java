/*
 * $Id: OgexNode.java 3826 2014-11-23 08:01:12Z pspeed $
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

import com.google.common.collect.Maps;
import mod.steamnsteel.client.model.opengex.OpenGEXNode;

import java.util.*;

/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexNode implements OpenGEXNode {
    private String name;
    private final List<OgexTransform> transforms = new ArrayList<OgexTransform>();
    private final Map<Integer, OgexAnimation> animations = Maps.newHashMap();
    private final ArrayList<OgexNode> childNodes = new ArrayList<OgexNode>();
    private int index;

    public OgexNode() {
    }
    
    public void setName( String name ) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
 
    public void addTransform( OgexTransform o ) {
        transforms.add(o);
    }
 
    public List<OgexTransform> getTransforms() {
        return transforms;
    }
 
    public void addAnimation( OgexAnimation anim ) {
        this.animations.put(anim.getClip(), anim);
    }
    
    public OgexAnimation getAnimation( int clip ) {
        return animations.get(clip);
    }
 
    public Collection<OgexAnimation> getAnimations() {
        return animations.values();
    }
 
    public Set<Integer> getAnimationClips() {
        return animations.keySet();
    }
    
    public void dumpTree( String indent ) {
        System.out.println(indent + toString());
        if( animations != null ) {
            for( Map.Entry<Integer, OgexAnimation> e : animations.entrySet() ) {
                System.out.println(indent + "    animation[" + e.getKey() + "]=" + e.getValue());
            }
        }
        for( OgexNode n : this ) {
            n.dumpTree(indent + "    ");
        }
    }
    
    protected void appendFieldStrings( StringBuilder sb ) {
        if( sb.length() > 0 ) {
            sb.append(", ");
        }
        sb.append("name=" + getName() + ", transforms=" + transforms + ", childCount=" + childNodes.size());
        sb.append(", animationCount=" + (animations != null ? animations.size() : 0));        
    }
 
    @Override   
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        appendFieldStrings(sb);
        return getClass().getSimpleName() + "[" + sb + "]";
    }

    public Iterator<OgexNode> iterator() {
        return childNodes.iterator();
    }

    public void add(OgexNode node) {
        childNodes.add(node);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
