/*
 * $Id: OgexTranslation.java 3827 2014-11-23 08:13:36Z pspeed $
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
public abstract class OgexTranslation implements OgexTransform {
 
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
    
    protected OgexTranslation( Kind kind ) {
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
    
    public static OgexTranslation create( Kind kind, Object data ) {
        switch( kind ) {
            case X:
            case Y:
            case Z:
                return new ComponentTranslation(kind, (Float)data);
            case Xyz:
                return new XyzTranslation(kind, (float[])data);
        }
        return null;
    }
    
    public static class ComponentTranslation extends OgexTranslation {
        
        private float translation;
        
        public ComponentTranslation( Kind kind, float translation ) {
            super(kind);
            this.translation = translation;
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
                    throw new IllegalArgumentException("Incompatible ComponentTranslation kind:" + kind);
            }
        } 
 
        public void setTranslation( float translation ) {
            this.translation = translation;
        }
        
        public float getTranslation() {
            return translation;
        }
        
        public float[] toMatrix() {
            float x = 0;
            float y = 0;
            float z = 0;
            switch( getKind() ) {
                case X: x = translation; break;
                case Y: y = translation; break;
                case Z: z = translation; break;
            }
            // Easier (visually) to write in row major form and transpose it
            return //OgexMatrixTransform.transpose(
                    new float[] {
                    1, 0, 0, x, 
                    0, 1, 0, y, 
                    0, 0, 1, z, 
                    0, 0, 0, 1 
                };//);
        }
 
        @Override       
        public String toString() {
            return getClass().getSimpleName() + "[kind=" + getKind() + ", translation=" + translation + "]";
        }
    }
    
    public static class XyzTranslation extends OgexTranslation {
 
        private float[] translation;        
        
        public XyzTranslation( Kind kind, float[] data ) {
            super(kind);
            this.translation = data;
        }

        @Override 
        public void setKind( Kind kind ) {
            if( kind != Kind.Xyz ) {
                throw new IllegalArgumentException("Incompatible Xyz kind:" + kind);
            }
        } 
 
        public void setTranslation( float[] translation ) {
            if( translation.length != 3 ) {
                throw new IllegalArgumentException("Translation array must be length 3, found:" + translation.length);
            }
            this.translation = translation;
        }
        
        public float[] getTranslation() {
            return translation;
        }
        
        public float[] toMatrix() {
            // Easier (visually) to write in row major form and transpose it
            return //OgexMatrixTransform.transpose(
                    new float[] {
                    1, 0, 0, translation[0], 
                    0, 1, 0, translation[1], 
                    0, 0, 1, translation[2], 
                    0, 0, 0, 1 
                };
            //);
        }
 
        @Override       
        public String toString() {
            return getClass().getSimpleName() + "[kind=" + getKind() 
                        + ", translation=[" + translation[0] + ", " + translation[1] + ", " + translation[2] + "]" 
                        + "]";
        }
    }
}
