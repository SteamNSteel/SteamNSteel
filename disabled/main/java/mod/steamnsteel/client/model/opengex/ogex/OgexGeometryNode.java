/*
 * $Id: OgexGeometryNode.java 3827 2014-11-23 08:13:36Z pspeed $
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
public class OgexGeometryNode extends OgexNode {
    
    private OgexGeometryObject geom;
    private List<OgexMaterial> materials = new ArrayList<OgexMaterial>();   
    private boolean visible = true;
    private boolean shadow = true;
    private boolean motionBlur = true;
    
    public OgexGeometryNode() {
    }
    
    public void setVisible( boolean b ) {
        this.visible = b;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setShadow( boolean b ) {
        this.shadow = b;
    }
    
    public boolean getShadow() {
        return shadow;
    }
    
    public void setMotionBlur( boolean b ) {
        this.motionBlur = b;
    }
    
    public boolean getMotionBlur() {
        return motionBlur;
    }
    
    public void setGeometry( OgexGeometryObject geom ) {
        this.geom = geom;
    }
 
    public OgexGeometryObject getGeometry() {
        return geom;
    }
    
    public void addMaterial( OgexMaterial mat ) {
        materials.add(mat);
    }

    @Override
    protected void appendFieldStrings( StringBuilder sb ) {
        super.appendFieldStrings(sb);
        if( sb.length() > 0 ) {
            sb.append(", ");
        }
        sb.append("materials=" + materials);
        sb.append(", visible=" + visible);
        sb.append(", shadow=" + shadow);
        sb.append(", motionBlur=" + motionBlur);
        sb.append(", geometry=" + geom);    
    }

    public Iterable<OgexMaterial> getMaterials() {
        return materials;
    }

    public boolean hasGeometry() {
        return geom != null;
    }
}
