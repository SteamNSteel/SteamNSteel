/*
 * $Id: OgexScene.java 3827 2014-11-23 08:13:36Z pspeed $
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

import mod.steamnsteel.client.model.opengex.OpenGEXNode;

import java.util.*;

/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexScene implements OpenGEXNode {
    private OgexMetrics metrics = new OgexMetrics();
    private List<OgexMaterial> materials = new ArrayList<OgexMaterial>();
    private List<OgexLightObject> lights = new ArrayList<OgexLightObject>();
    private List<OgexGeometryObject> geometry = new ArrayList<OgexGeometryObject>();
    private List<OgexCameraObject> cameras = new ArrayList<OgexCameraObject>();
    private List<OgexNode> childNodes = new ArrayList<OgexNode>();

    public OgexScene() {
    }
    
    public OgexMetrics getMetrics() {
        return metrics;
    }

    public List<OgexMaterial> getMaterials() { return materials; }
 
    public void addMaterial( OgexMaterial mat ) {
        materials.add(mat);
    }
    
    public void addGeometry( OgexGeometryObject geom ) {
        geometry.add(geom);
    }
 
    public void addLight( OgexLightObject light ) {
        lights.add(light);
    }
 
    public void addCamera( OgexCameraObject cam ) {
        cameras.add(cam);
    }
 
    public void dumpTree( String indent ) {
        System.out.println(indent + "Scene:");
        for( OgexNode n : this ) {
            n.dumpTree(indent + "    ");
        }
    }
 
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[metrics=" + metrics + ", nodes=" + super.toString() + "]";
    }

    public Iterator<OgexNode> iterator() {
        return childNodes.iterator();
    }

    public void add(OgexNode node) {
        childNodes.add(node);
    }
}
