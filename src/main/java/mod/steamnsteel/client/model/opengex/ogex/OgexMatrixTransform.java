/*
 * $Id: OgexMatrixTransform.java 3827 2014-11-23 08:13:36Z pspeed $
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

import javax.vecmath.Vector3f;

/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexMatrixTransform implements OgexTransform {
    private float[] matrix;
    private boolean object;
    
    public OgexMatrixTransform() {
    }
 
    public void setObjectOnly(boolean b) {
        this.object = b;
    }
    
    public boolean isObjectOnly() {
        return object;
    }
    
    public void setMatrix( float[] matrix ) {
        this.matrix = transpose(matrix);
    }
    
    public float[] getMatrix() {
        return matrix;
    }
 
    public float[] toMatrix() {
        return matrix.clone();
    }
 
    /**
     *  Converts from row major to column major or reverse
     *  in place.  The passed array is returned again.
     */   
    public static float[] transpose( float[] array ) {
        for( int i = 0; i < 4; i++ ) {
            for( int j = 0; j < i; j++ ) {
                // Swap row, col for col, row
                float swap = array[i * 4 + j];
                array[i * 4 + j] = array[j * 4 + i];
                array[j * 4 + i] = swap;
            }
        }
        return array;
    }

    public static float[] identity() {
        return new float[] {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
    }

    //Stolen to help translate from ChickenBone's Matrix class.

    static final int m00 = 0;
    static final int m01 = 1;
    static final int m02 = 2;
    static final int m03 = 3;
    static final int m10 = 4;
    static final int m11 = 5;
    static final int m12 = 6;
    static final int m13 = 7;
    static final int m20 = 8;
    static final int m21 = 9;
    static final int m22 = 10;
    static final int m23 = 11;
    static final int m30 = 12;
    static final int m31 = 13;
    static final int m32 = 14;
    static final int m33 = 15;

    public static float[] multiply(float[] left, float[] right) {
        float n00 = left[m00] * right[m00] + left[m01] * right[m10] + left[m02] * right[m20] + left[m03] * right[m30];
        float n01 = left[m00] * right[m01] + left[m01] * right[m11] + left[m02] * right[m21] + left[m03] * right[m31];
        float n02 = left[m00] * right[m02] + left[m01] * right[m12] + left[m02] * right[m22] + left[m03] * right[m32];
        float n03 = left[m00] * right[m03] + left[m01] * right[m13] + left[m02] * right[m23] + left[m03] * right[m33];
        float n10 = left[m10] * right[m00] + left[m11] * right[m10] + left[m12] * right[m20] + left[m13] * right[m30];
        float n11 = left[m10] * right[m01] + left[m11] * right[m11] + left[m12] * right[m21] + left[m13] * right[m31];
        float n12 = left[m10] * right[m02] + left[m11] * right[m12] + left[m12] * right[m22] + left[m13] * right[m32];
        float n13 = left[m10] * right[m03] + left[m11] * right[m13] + left[m12] * right[m23] + left[m13] * right[m33];
        float n20 = left[m20] * right[m00] + left[m21] * right[m10] + left[m22] * right[m20] + left[m23] * right[m30];
        float n21 = left[m20] * right[m01] + left[m21] * right[m11] + left[m22] * right[m21] + left[m23] * right[m31];
        float n22 = left[m20] * right[m02] + left[m21] * right[m12] + left[m22] * right[m22] + left[m23] * right[m32];
        float n23 = left[m20] * right[m03] + left[m21] * right[m13] + left[m22] * right[m23] + left[m23] * right[m33];
        float n30 = left[m30] * right[m00] + left[m31] * right[m10] + left[m32] * right[m20] + left[m33] * right[m30];
        float n31 = left[m30] * right[m01] + left[m31] * right[m11] + left[m32] * right[m21] + left[m33] * right[m31];
        float n32 = left[m30] * right[m02] + left[m31] * right[m12] + left[m32] * right[m22] + left[m33] * right[m32];
        float n33 = left[m30] * right[m03] + left[m31] * right[m13] + left[m32] * right[m23] + left[m33] * right[m33];

        return new float[] {
                n00, n01, n02, n03,
                n10, n11, n12, n13,
                n20, n21, n22, n23,
                n30, n31, n32, n33
        };
    }

    /*public static void main( String... args ) {
        float[] test = new float[] {
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
            };
        int index = 0;
        for( int i = 0; i < 4; i++ ) {
            for( int j = 0; j < 4; j++ ) {
                System.out.print("[" + test[i * 4 + j] + "]");
            }
            System.out.println();
        }
 
        transpose(test);
                     
        System.out.println("transposed...");
        index = 0;
        for( int i = 0; i < 4; i++ ) {
            for( int j = 0; j < 4; j++ ) {
                System.out.print("[" + test[i * 4 + j] + "]");
            }
            System.out.println();
        }             
    }*/
 
    @Override   
    public String toString() {
        if( matrix == null ) {
            return getClass().getSimpleName() + "[]";
        }
        StringBuilder sb = new StringBuilder();
        for( float f : matrix ) {
            if( sb.length() > 0 ) {
                sb.append(", ");
            }
            sb.append(f);
        }
        return getClass().getSimpleName() + "[" + sb + "]";
    }

    public static void applyTo(float[] transform, Vector3f vec) {
        float x = transform[m00] * vec.x + transform[m01] * vec.y + transform[m02] * vec.z;
        float y = transform[m10] * vec.x + transform[m11] * vec.y + transform[m12] * vec.z;
        float z = transform[m20] * vec.x + transform[m21] * vec.y + transform[m22] * vec.z;

        vec.x = x + transform[m03];
        vec.y = y + transform[m13];
        vec.z = z + transform[m23];
    }
}
