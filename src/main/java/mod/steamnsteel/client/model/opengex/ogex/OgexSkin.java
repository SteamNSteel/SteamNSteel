/*
 * $Id: OgexSkin.java 3827 2014-11-23 08:13:36Z pspeed $
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
public class OgexSkin {

    private OgexMatrixTransform transform;
    private OgexSkeleton skeleton;
    private IntArray boneCount;
    private IntArray boneIndex;
    private float[] boneWeight;

    public OgexSkin() {
    }
    
    public void setTransform( OgexMatrixTransform transform ) {
        this.transform = transform;
    }
    
    public OgexMatrixTransform getTransform() {
        return transform;
    }

    public void setSkeleton( OgexSkeleton skeleton ) {
        this.skeleton = skeleton;
    }
    
    public OgexSkeleton getSkeleton() {
        return skeleton;
    }
 
    public void setBoneCount( IntArray array ) {
        this.boneCount = array;
    }

    public IntArray getBoneCount() {
        return boneCount;
    }

    public void setBoneIndex( IntArray array ) {
        this.boneIndex = array;
    }
    
    public IntArray getBoneIndex() {
        return boneIndex;
    }
    
    public void setBoneWeight( float[] array ) {
        this.boneWeight = array;
    }
    
    public float[] getBoneWeight() {
        return boneWeight;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[transform=" + transform
                + ", skeleton=" + skeleton 
                + ", boneCount=" + boneCount 
                + ", boneIndex=" + boneIndex
                + ", boneWeight=float[" + (boneWeight != null ? boneWeight.length : 0) + "]" 
                + "]";
    } 
}
