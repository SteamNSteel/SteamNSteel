/*
 * $Id: Reference.java 3828 2014-11-23 08:21:03Z pspeed $
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

package mod.steamnsteel.client.model.opengex.oddl;

import java.util.regex.Pattern;

/**
 *  A placeholder for a reference to another structure. 
 *  It is a subclass of BaseStructure because it acts as a structure
 *  stand-in until resolution.
 *
 *  @author    Paul Speed
 */
public class Reference extends BaseStructure { 

    private static final Pattern TOKEN_SPLIT = Pattern.compile("%"); 

    private boolean global = false;
    private String ref;
    private String[] tokens;

    public Reference( String ref, int[] location ) {
        super(null, location);
        this.global = ref.charAt(0) == '$';
        this.ref = ref;
        this.tokens = TOKEN_SPLIT.split(ref.substring(1));
    }

    public boolean isGlobal() {
        return global;
    }

    public String getReference() {
        return ref;
    }

    public String[] getReferenceTokens() {
        return tokens; 
    }
 
    @Override
    public String getType() {
        return "Reference";
    }
    
    @Override
    public <T> T getValue() {
        throw new UnsupportedOperationException("References contain no default value.");
    }
 
    @Override
    public Object getData() {
        throw new UnsupportedOperationException("References contain no data.");
    }
 
    @Override   
    public String toString() {
        return getClass().getSimpleName() + "[" + getReference() + "]";
    }    
}
