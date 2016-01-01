/*
 * $Id: OgexRotation.java 3827 2014-11-23 08:13:36Z pspeed $
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
public abstract class OgexRotation implements OgexTransform {
 
    public enum Kind { 
        X("x"), Y("y"), Z("z"), Axis("axis"), Quaternion("Quaternion");
                       
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
    
    protected OgexRotation( Kind kind ) {
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
    
    public static OgexRotation create( Kind kind, Object data ) {
        switch( kind ) {
            case X:
            case Y:
            case Z:
                return new ComponentRotation(kind, (Float)data);
            case Axis:
                return new AxisRotation(kind, (float[])data);
            case Quaternion:
                return new QuaternionRotation(kind, (float[])data);
        }
        return null;
    }
    
    public static class ComponentRotation extends OgexRotation {
        
        private float angle;
        
        public ComponentRotation( Kind kind, float angle ) {
            super(kind);
            this.angle = angle;
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
                    throw new IllegalArgumentException("Incompatible ComponentRotation kind:" + kind);
            }
        } 
 
        public void setAngle( float angle ) {
            this.angle = angle;
        }
        
        public float getAngle() {
            return angle;
        }
        
        public float[] toMatrix() {
            
            float cos = (float)Math.cos(angle);
            float sin = (float)Math.sin(angle);
            
            switch( getKind() ) {
                case X:
                    // Easier (visually) to write in row major form and transpose it
                    return //OgexMatrixTransform.transpose(
                            new float[] {
                            1, 0, 0, 0, 
                            0, cos, -sin, 0, 
                            0, sin, cos, 0, 
                            0, 0, 0, 1 
                        };//);
                case Y:
                    // Easier (visually) to write in row major form and transpose it
                    return //OgexMatrixTransform.transpose(
                            new float[] {
                            cos, 0, sin, 0, 
                            0, 1, 0, 0, 
                            -sin, 0, cos, 0, 
                            0, 0, 0, 1 
                        };//);
                case Z:
                    // Easier (visually) to write in row major form and transpose it
                    return //OgexMatrixTransform.transpose(
                            new float[] {
                            cos, -sin, 0, 0, 
                            sin, cos, 0, 0, 
                            0, 0, 1, 0, 
                            0, 0, 0, 1 
                        };//);
                default:
                    throw new IllegalArgumentException("Incompatible ComponentRotation kind:" + getKind());
            }                
        }
 
        @Override       
        public String toString() {
            return getClass().getSimpleName() + "[kind=" + getKind() + ", angle=" + angle + "]";
        }
    }
    
    public static class AxisRotation extends OgexRotation {
 
        private float angle;
        private float[] axis;        
        
        public AxisRotation( Kind kind, float[] data ) {
            super(kind);
            this.angle = data[0];
            this.axis = new float[] {data[1], data[2], data[3]};
        }
 
        @Override 
        public void setKind( Kind kind ) {
            if( kind != Kind.Axis ) {
                throw new IllegalArgumentException("Incompatible Axis kind:" + kind);
            }
        } 
 
        public void setAngle( float angle ) {
            this.angle = angle;
        }
        
        public float getAngle() {
            return angle;
        }
 
        public void setAxis( float[] axis ) {
            if( axis.length != 3 ) {
                throw new IllegalArgumentException("Axis array must be length 3, found:" + axis.length);
            }
            this.axis = axis;
        }
        
        public float[] getAxis() {
            return axis;
        }
        
        public float[] toMatrix() {
            
            float cos = (float)Math.cos(angle);
            float sin = (float)Math.sin(angle);
 
            float Ax = axis[0];
            float Ay = axis[1];
            float Az = axis[2];
            
            // See if the axis needs normalization
            float lengthSq = Ax * Ax + Ay * Ay + Az * Az;
            if( lengthSq != 1 ) {
                float length = (float)Math.sqrt(lengthSq);
                float norm = 1 / length;
                Ax *= norm;
                Ay *= norm;
                Az *= norm;
            }
                    
            // Easier (visually) to write in row major form and transpose it
            return //OgexMatrixTransform.transpose(
                    new float[] {
                cos + Ax * Ax * (1 - cos), Ax * Ay * (1 - cos) - Az * sin, Ax * Az * (1 - cos) + Ay * sin, 0, 
                Ax * Ay * (1 - cos) + Az * sin, cos + Ay * Ay * (1 - cos), Ay * Az * (1 - cos) - Az * sin, 0, 
                Ax * Az * (1 - cos) - Ay * sin, Ay * Az * (1 - cos) + Ax * sin, cos + Az * Az * (1 - cos), 0, 
                0, 0, 0, 1 
            };//);
        }
 
        @Override       
        public String toString() {
            return getClass().getSimpleName() + "[kind=" + getKind() + ", angle=" + angle
                        + ", axis=[" + axis[0] + ", " + axis[1] + ", " + axis[2] + "]" 
                        + "]";
        }
    }
    
    public static class QuaternionRotation extends OgexRotation {
 
        private float[] quat;        
        
        public QuaternionRotation( Kind kind, float[] data ) {
            super(kind);
            this.quat = data;
        }
 
        @Override 
        public void setKind( Kind kind ) {
            if( kind != Kind.Quaternion ) {
                throw new IllegalArgumentException("Incompatible Quaternion kind:" + kind);
            }
        } 
 
        public void setQuat( float[] quat ) {
            if( quat.length != 4 ) {
                throw new IllegalArgumentException("Quaternion array must be length 4, found:" + quat.length);
            }
            this.quat = quat;
        }
        
        public float[] getQuat() {
            return quat;
        }
        
        public float[] toMatrix() {
            
            float x = quat[0];
            float y = quat[1];
            float z = quat[2];
            float w = quat[3];
            
            float lengthSq = x * x + y * y + z * z + w * w;
            if( lengthSq != 1 ) {
                float length = (float)Math.sqrt(lengthSq);
                float norm = 1 / length;
                x *= norm;                
                y *= norm;                
                z *= norm;                
                w *= norm;                
            }
        
            return //OgexMatrixTransform.transpose(
                    new float[] {
                1 - (2 * y * y) - (2 * z * z), (2 * x * y) * (2 * w * z), (2 * x * z) + (2 * w * y), 0,
                (2 * x * y) + (2 * w * z), 1 - (2 * x * x) - (2 * z * z), (2 * y * z) - (2 * w * x), 0,
                (2 * x * z) - (2 * w * y), (2 * y * z) + (2 * w * x), 1 - (2 * x * x) - (2 * y * y), 0,
                0, 0, 0, 1 
            };//);
        }
 
        @Override       
        public String toString() {
            return getClass().getSimpleName() + "[kind=" + getKind() 
                        + ", quat=[" + quat[0] + ", " + quat[1] + ", " + quat[2] + ", " + quat[3] + "]" 
                        + "]";
        }
    }
}
