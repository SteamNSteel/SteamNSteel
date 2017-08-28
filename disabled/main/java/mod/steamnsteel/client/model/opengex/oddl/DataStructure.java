/*
 * $Id: DataStructure.java 3828 2014-11-23 08:21:03Z pspeed $
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

import java.util.*;

/**
 *  Represents custom app-specific data structures.
 *
 *  @author    Paul Speed
 */
public class DataStructure extends BaseStructure
                           implements Iterable<BaseStructure> {
 
    private String type;    
    private Map<String, Object> properties;
    private List<BaseStructure> children;
    private Map<String, BaseStructure> childIndex; 
    
    public DataStructure( String type, DataStructure parent, int[] location ) {
        super(parent, location);
        this.type = type;
    }
 
    @Override
    public String getType() {
        return type;
    }
 
    public BaseStructure findChild( String... tokens ) {
        BaseStructure parent = this;
        BaseStructure result = null;
        for( String s : tokens ) {
            if( !(parent instanceof DataStructure) ) {
                return null;
            }
            DataStructure ds = (DataStructure)parent;
            if( ds.childIndex == null ) {
                return null;
            }
            result = ds.childIndex.get(s);
            parent = result;            
        }
        return result;
    }
 
    protected boolean addIndex( String name, BaseStructure struct ) {
        if( childIndex == null ) {
            childIndex = new HashMap<String, BaseStructure>();            
        }
        return childIndex.put(name, struct) == null;
    }

    protected void setProperties( Map<String, Object> properties ) {
        this.properties = properties;
    }
    
    public Map<String, Object> getProperties() {
        if( properties == null ) {
            return Collections.emptyMap();
        }        
        return properties;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getProperty( String key ) {
        if( properties == null ) {
            return null;
        }
        return (T)properties.get(key);
    }
 
    public <T> T getProperty( String key, T defaultValue ) {
        T result = getProperty(key);
        return result == null ? defaultValue : result;
    }
 
    /**
     *  Convenience method for getting the first value of the first
     *  primitive child.
     */
    @Override
    public <T> T getValue() {
        for( BaseStructure struct : getChildren() ) {
            if( struct instanceof PrimitiveStructure ) {
                return ((PrimitiveStructure)struct).getValue();
            }
        }
        throw new RuntimeException(getType() + " has no primitive children.");
    }
 
    /**
     *  Convenience method for returning the array data of the
     *  first primitive child.
     */
    @Override
    public Object getData() {
        for( BaseStructure struct : getChildren() ) {
            if( struct instanceof PrimitiveStructure ) {
                return ((PrimitiveStructure)struct).getData();
            }
        }
        throw new RuntimeException(getType() + " has no primitive children.");
    }
    
    protected void setChildren( List<BaseStructure> children ) {
        this.children = children;
    }
    
    public List<BaseStructure> getChildren() {
        List<BaseStructure> result = children;
        if( children == null ) {
            result = Collections.emptyList(); 
        }
        return result;
    }

    /**
     *  Convenience method for returning the only primitive structure child
     *  if it has one.  If 'strict' is true then anything but one child
     *  will throw an exception.
     */ 
    public PrimitiveStructure getPrimitiveChild( boolean strict ) {
    
        if( strict && getChildren().isEmpty() ) {
            throw new RuntimeException(getType() + " float data not specified.");
        }
        if( strict && getChildren().size() > 1 ) {
            throw new RuntimeException(getType() + " has too many child structures.");
        }          
        for( BaseStructure struct : getChildren() ) {
            if( struct instanceof PrimitiveStructure ) {
                return (PrimitiveStructure)struct;
            }
        }
        throw new RuntimeException(getType() + " has no primitive children.");
    }
 
    @Override   
    public Iterator<BaseStructure> iterator() {
        return getChildren().iterator();
    }
    
    @Override   
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "[");
        sb.append("type=" + type);
        if( getName() != null ) {
            sb.append(", name=" + getName());
        }
        if( properties != null ) {
            sb.append(", properties=" + properties);
        }
        if( children != null ) {
            sb.append(", children.size=" + (children == null ? 0 : children.size()));
        }            
        sb.append("]");
        return sb.toString();
    }    
}
