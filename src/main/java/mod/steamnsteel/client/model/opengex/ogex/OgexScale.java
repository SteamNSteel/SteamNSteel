/*
 * $Id: OgexScale.java 3827 2014-11-23 08:13:36Z pspeed $
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
public abstract class OgexScale implements OgexTransform {
 
    public enum Kind { 
        X("x"), Y("y"), Z("z"), Xyz("xyz");
                       
        private String ogexName;
        
        private Kind( String ogexName ) {
            this.ogexName = ogexName;
        }
        
        public String getOgexName() {
            return ogexName;
        }
        
        public static Kind lookup( String ogexName ) {
            for( Kind kind : values() ) {
                if( ogexName.equals(kind.ogexName) ) {
                    return kind;
                }
            }
            return null;
        }
                
    }
 
    private Kind kind;
    private boolean object;
    
    protected OgexScale( Kind kind ) {
        this.kind = kind;
    }
    
    public void setObjectOnly(boolean b) {
        this.object = b;
    }
    
    public boolean isObjectOnly() {
        return object;
    }
    
    protected void setKind( Kind kind ) {
        this.kind = kind;
    }
    
    public Kind getKind() {
        return kind;
    }
    
    public static OgexScale create( Kind kind, Object data ) {
        switch( kind ) {
            case X:
            case Y:
            case Z:
                return new ComponentScale(kind, (Float)data);
            case Xyz:
                return new XyzScale(kind, (float[])data);
        }
        return null;
    }
    
    public static class ComponentScale extends OgexScale {
        
        private float scale;
        
        public ComponentScale( Kind kind, float scale ) {
            super(kind);
            this.scale = scale;
        }
 
        @Override 
        public void setKind( Kind kind ) {
            switch( kind ) {
                case X:
                case Y:
                case Z:
                    super.setKind(kind);
                    break;
                default:
                    throw new IllegalArgumentException("Incompatible ComponentScale kind:" + kind);
            }
        } 
 
        public void setScale( float scale ) {
            this.scale = scale;
        }
        
        public float getScale() {
            return scale;
        }
        
        public float[] toMatrix() {
            float x = 1;
            float y = 1;
            float z = 1;
            switch( getKind() ) {
                case X: x = scale; break;
                case Y: y = scale; break;
                case Z: z = scale; break;
            }
            // Easier (visually) to write in row major form and transpose it
            return //OgexMatrixTransform.transpose(
                    new float[] {
                    x, 0, 0, 0, 
                    0, y, 0, 0, 
                    0, 0, z, 0, 
                    0, 0, 0, 1 
                };//);
        }
 
        @Override       
        public String toString() {
            return getClass().getSimpleName() + "[kind=" + getKind() + ", scale=" + scale + "]";
        }
    }
    
    public static class XyzScale extends OgexScale {
 
        private float[] scale;        
        
        public XyzScale( Kind kind, float[] data ) {
            super(kind);
            this.scale = data;
        }

        @Override 
        public void setKind( Kind kind ) {
            if( kind != Kind.Xyz ) {
                throw new IllegalArgumentException("Incompatible Xyz kind:" + kind);
            }
        } 
 
        public void setScale( float[] scale ) {
            if( scale.length != 3 ) {
                throw new IllegalArgumentException("Scale array must be length 3, found:" + scale.length);
            }
            this.scale = scale;
        }
        
        public float[] getScale() {
            return scale;
        }
        
        public float[] toMatrix() {
            float x = scale[0];
            float y = scale[1];
            float z = scale[2];
            // Easier (visually) to write in row major form and transpose it
            return //OgexMatrixTransform.transpose(
                    new float[] {
                    x, 0, 0, 0, 
                    0, y, 0, 0, 
                    0, 0, z, 0, 
                    0, 0, 0, 1 
                };//);
        }
 
        @Override       
        public String toString() {
            return getClass().getSimpleName() + "[kind=" + getKind() 
                        + ", scale=[" + scale[0] + ", " + scale[1] + ", " + scale[2] + "]" 
                        + "]";
        }
    }
    
}
