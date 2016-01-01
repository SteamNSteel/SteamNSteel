/*
 * $Id: OgexParser.java 3827 2014-11-23 08:13:36Z pspeed $
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

import java.io.*;
import java.util.*;

import mod.steamnsteel.client.model.opengex.oddl.BaseStructure;
import mod.steamnsteel.client.model.opengex.oddl.DataStructure;
import mod.steamnsteel.client.model.opengex.oddl.OddlParser;
import mod.steamnsteel.client.model.opengex.oddl.PrimitiveStructure;
import mod.steamnsteel.utility.log.Logger;

/**
 *
 *
 *  @author    Paul Speed
 */
public class OgexParser {
 
    static Logger log = Logger.INSTANCE;
 
    private static Set<String> BASE_NODE_CHILDREN = new HashSet<String>(Arrays.asList(
        new String[] {
            StructTypes.NAME,
            StructTypes.TRANSFORM,
            StructTypes.ROTATION,
            StructTypes.SCALE,
            StructTypes.TRANSLATION,
            StructTypes.GEOM_NODE,
            StructTypes.NODE,
            StructTypes.BONE_NODE,
            StructTypes.ANIMATION
        })); 
 
    public OgexParser() {
    }   
 
    public static void main( String... args ) throws IOException {
        OgexParser parser = new OgexParser();
        
        for( String s : args ) {
            FileReader in = new FileReader(s);
            long start = System.nanoTime();
            OgexScene scene = parser.parseScene(in);
            long end = System.nanoTime();
            System.out.println( "Time:" + ((end - start)/1000000000.0) + " s  Read:" );
            scene.dumpTree("");    
        }
    }
    
    public OgexScene parseScene( Reader in ) throws IOException {
        OgexScene result = new OgexScene();        
        OddlParser oddl = new OddlParser(in);
        List<BaseStructure> list = oddl.parse();
        
        // Keep track of the objects we've already created
        Map<BaseStructure, Object> index = new HashMap<BaseStructure, Object>();
        
        for( BaseStructure child : list ) {
            // All of the roots should be data structures
            if( !(child instanceof DataStructure) ) {
                throw new IOException("Unknown OGEX structure found in root:" + child + ", from:" + child.location());
            }
            try {
                apply((DataStructure)child, result, index);
            } catch( Exception e ) {
                throw new IOException("Error applying:" + child + ", from:" + child.location(), e);
            }   
        }
        return result;           
    }
    
    protected void apply( DataStructure ds, OgexScene result, Map<BaseStructure, Object> index ) {
 
        String type = ds.getType();   
        if( StructTypes.METRIC.equals(type) ) {
            applyMetric(ds, result);
        } else if( StructTypes.GEOM_NODE.equals(type) ) {
            result.add(toGeometryNode(ds, index));
        } else if( StructTypes.LIGHT_NODE.equals(type) ) {
            result.add(toLightNode(ds, index));
        } else if( StructTypes.NODE.equals(type) ) {
            result.add(toNode(ds, index));
        } else if( StructTypes.BONE_NODE.equals(type) ) {
            result.add(toBoneNode(ds, index));           
        } else if( StructTypes.CAMERA_NODE.equals(type) ) {
            result.add(toCameraNode(ds, index));
        } else if( StructTypes.GEOM_OBJECT.equals(type) ) {
            result.addGeometry(toGeometry(ds, index));
        } else if( StructTypes.MATERIAL.equals(type) ) {
            result.addMaterial(toMaterial(ds, index));
        } else if( StructTypes.LIGHT_OBJECT.equals(type) ) {
            result.addLight(toLight(ds, index));
        } else if( StructTypes.CAMERA_OBJECT.equals(type) ) {
            result.addCamera(toCamera(ds, index));
        } else {
            log.warning("Unhandled structure type:" + type + ", from:" + ds.location());
        }
    }    
 
    protected void configureNode( OgexNode node, DataStructure ds, Map<BaseStructure, Object> index, boolean strict ) {
    
        for( BaseStructure child : ds ) {
            String type = child.getType();
            if( StructTypes.NAME.equals(type) ) {
                String name = child.getValue();
                node.setName(name);
            } else if( StructTypes.TRANSFORM.equals(type) ) {
                node.addTransform(toTransform((DataStructure)child, index));
            } else if( StructTypes.ROTATION.equals(type) ) {
                node.addTransform(toRotation((DataStructure)child, index));
            } else if( StructTypes.SCALE.equals(type) ) {
                node.addTransform(toScale((DataStructure)child, index));
            } else if( StructTypes.TRANSLATION.equals(type) ) {
                node.addTransform(toTranslation((DataStructure)child, index));
            } else if( StructTypes.GEOM_NODE.equals(type) ) {
                node.add(toGeometryNode((DataStructure)child, index));           
            } else if( StructTypes.LIGHT_NODE.equals(type) ) {
                node.add(toLightNode(ds, index));
            } else if( StructTypes.CAMERA_NODE.equals(type) ) {
                node.add(toCameraNode(ds, index));
            } else if( StructTypes.NODE.equals(type) ) {
                node.add(toNode((DataStructure)child, index));           
            } else if( StructTypes.BONE_NODE.equals(type) ) {
                node.add(toBoneNode((DataStructure)child, index));
            } else if( StructTypes.ANIMATION.equals(type) ) {
                node.addAnimation(toAnimation((DataStructure)child, index));           
            } else if( strict ) {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
    }
 
    protected OgexNode toNode( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexNode result = new OgexNode();
        configureNode(result, ds, index, true);
        return result;
    }
    
    protected OgexGeometryNode toGeometryNode( DataStructure ds, Map<BaseStructure, Object> index ) {
               
        OgexGeometryNode geom = new OgexGeometryNode();
        configureNode(geom, ds, index, false);

        if( "false".equals(ds.getProperty("visible")) ) {
            geom.setVisible(false);
        }
        if( "false".equals(ds.getProperty("shadow")) ) {
            geom.setShadow(false);
        }
        if( "false".equals(ds.getProperty("motionBlur")) ) {
            geom.setMotionBlur(false);
        }
    
        for( BaseStructure child : ds ) {
            String type = child.getType();
            if( BASE_NODE_CHILDREN.contains(type) ) {
                continue;
            } else if( StructTypes.MATERIAL_REF.equals(type) ) {
                        
                BaseStructure[] array = (BaseStructure[])child.getData();
                // A geom node can contain more than one material
                for( BaseStructure ref : array ) {
                    geom.addMaterial(toMaterial((DataStructure)ref, index));
                }
            } else if( StructTypes.OBJECT_REF.equals(type) ) {
                geom.setGeometry(toGeometry((DataStructure)child.getValue(), index));
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
 
        return geom;        
    }

    protected OgexLightNode toLightNode( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexLightNode result = new OgexLightNode();
        configureNode(result, ds, index, false);
        
        result.setShadow((Boolean)ds.getProperty("shadow"));
 
        OgexLightObject light = null;       
        for( BaseStructure child : ds ) {
            String type = child.getType();
            if( BASE_NODE_CHILDREN.contains(type) ) {
                continue;
            } else if( StructTypes.OBJECT_REF.equals(type) ) {
                if( light != null ) {
                    throw new RuntimeException("Too many ObjectRef structures found in LightNode, from:" + child.location());
                }
                BaseStructure[] array = (BaseStructure[])child.getData();
                if( array.length != 1 ) {
                    throw new RuntimeException("Too many references (" + array.length + ") found in LightNode ObjectRef, from:" + child.location());
                }
                light = toLight((DataStructure)array[0], index);
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        result.setLight(light);
        
        return result;                
    } 
    
    protected OgexBoneNode toBoneNode( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexBoneNode result = (OgexBoneNode)index.get(ds);
        if( result != null ) {
            return result;
        }
        result = new OgexBoneNode();
        index.put(ds, result);
        configureNode(result, ds, index, true);
        return result;
    }

    protected OgexCameraNode toCameraNode( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexCameraNode result = (OgexCameraNode)index.get(ds);
        if( result != null ) {
            return result;
        }
        result = new OgexCameraNode();
        index.put(ds, result);
        configureNode(result, ds, index, false);
        
        OgexCameraObject camera = null;       
        for( BaseStructure child : ds ) {
            String type = child.getType();
            if( BASE_NODE_CHILDREN.contains(type) ) {
                continue;
            } else if( StructTypes.OBJECT_REF.equals(type) ) {
                if( camera != null ) {
                    throw new RuntimeException("Too many ObjectRef structures found in CameraNode, from:" + child.location());
                }
                BaseStructure[] array = (BaseStructure[])child.getData();
                if( array.length != 1 ) {
                    throw new RuntimeException("Too many references (" + array.length + ") found in CameraNode ObjectRef, from:" + child.location());
                }
                camera = toCamera((DataStructure)array[0], index);
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        result.setCamera(camera);
                
        return result;
    }

    protected OgexTrack toTrack( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexTrack result = new OgexTrack();
 
        BaseStructure target = ds.getProperty("target");
        Object resolved = index.get(target);
        if( resolved == null ) {
            // Need to figure out what it is and create it...
            // but not right now FIXME
            throw new UnsupportedOperationException("Forward track targets not yet supported.");
        }
        result.setTarget(resolved);
        
        OgexTime time = null;
        OgexValue value = null;
        
        for( BaseStructure child : ds ) {
            if( StructTypes.TIME.equals(child.getType()) ) {
                if( time != null ) {
                    throw new RuntimeException("Only one Time structure is allowed in Track, from:" + ds.location());
                }
                time = toTime((DataStructure)child);
            } else if( StructTypes.VALUE.equals(child.getType()) ) {                
                if( value != null ) {
                    throw new RuntimeException("Only one Value structure is allowed in Track, from:" + ds.location());
                }
                value = toValue((DataStructure)child);
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        if( time == null ) {
            throw new RuntimeException("No Time structure found in Track, from:" + ds.location());
        }
        if( value == null ) {
            throw new RuntimeException("No Value structure found in Track, from:" + ds.location());
        }
        result.setTime(time);
        result.setValue(value);
                       
        return result;
    } 

    protected OgexTime toTime( DataStructure ds ) {
        OgexTime result = new OgexTime();
        
        String s = ds.getProperty("curve", "linear");
        Curve curve = Curve.lookup(s);
        result.setCurve(curve);
        
        OgexKey[] keys;
        switch( curve ) {
            case Linear:
                keys = new OgexKey[1];
                break;
            case Bezier:
                keys = new OgexKey[3];
                break;
            default:
                throw new RuntimeException("Unhandled curve type:" + curve + ", from:" + ds.location());
        }

        int index = 0;
        for( BaseStructure child : ds ) {
            if( StructTypes.KEY.equals(child.getType()) ) {
                if( index >= keys.length ) {
                    throw new RuntimeException("Too many Key structures found in Time, from:" + ds.location());
                }
                keys[index++] = toKey((DataStructure)child);    
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        if( index < keys.length ) {
            throw new RuntimeException("Missing Key structure for Time structure with curve:" + curve + ", from:" + ds.location());
        }
        result.setKeys(keys);
        
        return result;
    }
 
    protected OgexKey toKey( DataStructure ds ) {
        OgexKey key = new OgexKey();
        
        String s = ds.getProperty("kind", "value");
        OgexKey.Kind kind = OgexKey.Kind.lookup(s);
        key.setKind(kind);
        
        key.setData(ds.getData());
        
        return key;
    }
     
    protected OgexValue toValue( DataStructure ds ) {
        OgexValue result = new OgexValue();
        
        String s = ds.getProperty("curve", "linear");
        Curve curve = Curve.lookup(s);
        result.setCurve(curve);
        
        OgexKey[] keys;
        switch( curve ) {
            case Linear:
                keys = new OgexKey[1];
                break;
            case Bezier:
                keys = new OgexKey[3];
                break;
            case Tcb:
                keys = new OgexKey[4];
                break;
            default:
                throw new RuntimeException("Unhandled curve type:" + curve + ", from:" + ds.location());
        }
        
        int index = 0;
        for( BaseStructure child : ds ) {
            if( StructTypes.KEY.equals(child.getType()) ) {
                if( index >= keys.length ) {
                    throw new RuntimeException("Too many Key structures found in Value, from:" + ds.location());
                }
                keys[index++] = toKey((DataStructure)child);    
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        if( index < keys.length ) {
            throw new RuntimeException("Missing Key structure for Value structure with curve:" + curve + ", from:" + ds.location());
        }
        result.setKeys(keys);        
        
        return result;
    }

    protected OgexAnimation toAnimation( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexAnimation result = new OgexAnimation();
 
        result.setClip(((Number)ds.getProperty("clip", 0)).intValue());
        result.setBegin(((Number)ds.getProperty("begin", 0f)).floatValue());
        result.setEnd(((Number)ds.getProperty("end", 0f)).floatValue());
 
        for( BaseStructure child : ds ) {
            if( StructTypes.TRACK.equals(child.getType()) ) {
                result.add(toTrack((DataStructure)child, index));
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        
        return result;
    }
        
    protected void applyMetric( DataStructure ds, OgexScene result ) {
        String key = ds.getProperty("key");
        Object value = ds.getValue();
        
        if( OgexMetrics.KEY_DISTANCE.equals(key) ) {
            result.getMetrics().setDistance((Float)value);
        } else if( OgexMetrics.KEY_ANGLE.equals(key) ) {
            result.getMetrics().setAngle((Float)value);
        } else if( OgexMetrics.KEY_TIME.equals(key) ) {
            result.getMetrics().setTime((Float)value);
        } else if( OgexMetrics.KEY_UP.equals(key) ) {
            Axis axis = Axis.valueOf(String.valueOf(value).toUpperCase());        
            result.getMetrics().setUp(axis);
        } else {
            result.getMetrics().put(key, value);    
        }
    }
 
    protected OgexGeometryObject toGeometry( DataStructure ds, Map<BaseStructure, Object> index ) {         
 
        OgexGeometryObject result = (OgexGeometryObject)index.get(ds);
        if( result != null ) {
            return result;
        }
        result = new OgexGeometryObject();
        index.put(ds, result);
        
        if( "false".equals(ds.getProperty("visible")) ) {
            result.setVisible(false);
        }
        if( "false".equals(ds.getProperty("shadow")) ) {
            result.setShadow(false);
        }
        if( "false".equals(ds.getProperty("motionBlur")) ) {
            result.setMotionBlur(false);
        }
 
        for( BaseStructure child : ds ) {
            if( StructTypes.MESH.equals(child.getType()) ) {
                result.addMesh(toMesh((DataStructure)child, index));
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
 
        return result;       
    }

    protected OgexCameraObject toCamera( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexCameraObject result = (OgexCameraObject)index.get(ds);
        if( result != null ) {
            return result;
        }
        result = new OgexCameraObject();
        index.put(ds, result);
        
        for( BaseStructure child : ds ) {        
            String type = child.getType();
            if( StructTypes.COLOR.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib"); 
                result.setColor(name, new OgexColor((float[])child.getValue())); 
            } else if( StructTypes.TEXTURE.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib"); 
                result.setTexture(name, toTexture((DataStructure)child, index));
            } else if( StructTypes.PARAM.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib"); 
                result.setParam(name, (Float)child.getValue());
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }            
 
        return result;       
    } 

    protected OgexLightObject toLight( DataStructure ds, Map<BaseStructure, Object> index ) {
    
        OgexLightObject result = (OgexLightObject)index.get(ds);
        if( result != null ) {
            return result;
        }
        result = new OgexLightObject();
        index.put(ds, result);
        
        String s = ds.getProperty("type");
        if( s == null ) {
            throw new RuntimeException("No type defined for LightObject, from:" + ds.location());
        }
        OgexLightObject.Type lightType = OgexLightObject.Type.lookup(s);
        
        result.setType(lightType);
        
        OgexColor color = null;
        OgexTexture texture = null;
        Float intensity = null;
        
        for( BaseStructure child : ds ) {        
            String type = child.getType();
            if( StructTypes.COLOR.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib");
                if( "light".equals(name) ) {
                    if( color != null ) {
                        throw new RuntimeException("Too many 'light' Color structures found in LightObject, from:" + child.location());
                    }
                    color = new OgexColor((float[])child.getValue());
                } else {
                    result.setColor(name, new OgexColor((float[])child.getValue())); 
                }
            } else if( StructTypes.TEXTURE.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib");
                if( "projection".equals(name) ) {
                    if( texture != null ) {
                        throw new RuntimeException("Too many 'projection' Texture structures found in LightObject, from:" + child.location());
                    }
                    texture = toTexture((DataStructure)child, index);
                } else {
                    result.setTexture(name, toTexture((DataStructure)child, index));
                }
            } else if( StructTypes.PARAM.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib");
                if( "intensity".equals(name) ) {
                    if( intensity != null ) {
                        throw new RuntimeException("Too many 'intensity' Param structures found in LightObject, from:" + child.location());
                    }
                    intensity = (Float)child.getValue();
                } else {
                    result.setParam(name, (Float)child.getValue());
                }
            } else if( StructTypes.ATTEN.equals(type) ) {
                result.addAttenuationFunction(toAtten((DataStructure)child, index));
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        
        if( color != null ) {
            result.setLightColor(color);
        }
        if( texture != null ) {
            result.setProjectionTexture(texture);
        }
        if( intensity != null ) {
            result.setIntensity(intensity);
        }
        
        return result;
    }                

    protected OgexAtten toAtten( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexAtten result = new OgexAtten();
        
        String s = ds.getProperty("kind", "distance");
        result.setKind(OgexAtten.Kind.lookup(s));
        
        s = ds.getProperty("curve", "linear");        
        result.setCurve(Curve.lookup(s));
        
        for( BaseStructure child : ds ) {
            if( StructTypes.PARAM.equals(child.getType()) ) {
                String name = ((DataStructure)child).getProperty("attrib");
                result.setParam(name, (Float)child.getValue());
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }        
        }
 
        return result;       
    } 

    protected OgexSkin toSkin( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexSkin result = new OgexSkin();
 
        for( BaseStructure child : ds ) {
            if( StructTypes.TRANSFORM.equals(child.getType()) ) {
                result.setTransform(toTransform((DataStructure)child, index));
            } else if( StructTypes.SKELETON.equals(child.getType()) ) {
                result.setSkeleton(toSkeleton((DataStructure)child, index));
            } else if( StructTypes.BONE_COUNT_ARRAY.equals(child.getType()) ) {
                result.setBoneCount(new IntArray(child.getData()));
            } else if( StructTypes.BONE_INDEX_ARRAY.equals(child.getType()) ) {
                result.setBoneIndex(new IntArray(child.getData()));
            } else if( StructTypes.BONE_WEIGHT_ARRAY.equals(child.getType()) ) {                
                result.setBoneWeight((float[])child.getData());
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        
        return result;
    }  
 
    protected OgexSkeleton toSkeleton( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexSkeleton result = new OgexSkeleton();

        for( BaseStructure child : ds ) {
            if( StructTypes.TRANSFORM.equals(child.getType()) ) {
                // Not a normal transform
                float[][] array = (float[][])child.getData();
                result.setTransforms(array);
            } else if( StructTypes.BONE_REF_ARRAY.equals(child.getType()) ) {
                BaseStructure[] array = (BaseStructure[])child.getData();
                //BoneNode[] boneNodes = new OgexBoneNode[array.length]; // FIXME when OGEX is fixed
                OgexNode[] boneNodes = new OgexNode[array.length];
                for( int i = 0; i < array.length; i++ ) {
                    if( !StructTypes.BONE_NODE.equals(array[i].getType()) ) {
                        // Put back in when OGEX is fixed
                        //throw new RuntimeException("BoneNodeRef array at:" + child.location()
                        //                            + " points to non-OgexBoneNode at index:" + i
                        //                            + ", node structure:" + array[i]);
                        boneNodes[i] = toNode((DataStructure)array[i], index);
                    } else {
                        boneNodes[i] = toBoneNode((DataStructure)array[i], index);
                    }
                }
                result.setBoneNodes(boneNodes);                
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
                
        return result;
    }
 
    protected OgexMesh toMesh( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexMesh result = new OgexMesh();
 
        result.setType(toType((String)ds.getProperty("primitive")));
        long lod = ((Number)ds.getProperty("lod", 0L)).longValue();
        result.setLod(lod); 
 
        for( BaseStructure child : ds ) {
            if( StructTypes.VERTEX_ARRAY.equals(child.getType()) ) {
                result.addVertexArray(toVertexArray((DataStructure)child));
            } else if( StructTypes.INDEX_ARRAY.equals(child.getType()) ) {
                result.addIndexArray(toIndexArray((DataStructure)child));
            } else if( StructTypes.SKIN.equals(child.getType()) ) {
                result.setSkin(toSkin((DataStructure)child, index));
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            } 
        }
         
        return result;
    }
 
    protected OgexIndexArray toIndexArray( DataStructure ds ) {
        OgexIndexArray result = new OgexIndexArray();
        
        result.setMaterial(((Number)ds.getProperty("material", 0L)).longValue());
        result.setRestart(((Number)ds.getProperty("restart", 0L)).longValue());
        
        String front = ds.getProperty("front", "ccw");
        VertexWinding winding = "ccw".equals(front) ? VertexWinding.CounterClockwise : VertexWinding.Clockwise; 
        if( "ccw".equals(front) ) {
            winding = VertexWinding.CounterClockwise; 
        } else if( "cw".equals(front) ) {
            winding = VertexWinding.Clockwise;
        } else {
            throw new RuntimeException("Invalude value for front:" + front);
        }
        result.setFront(winding);
        
        PrimitiveStructure struct = ds.getPrimitiveChild(true);
        result.setArray(struct.getData(), struct.getPrimitiveType(), struct.getElementSize());               
 
        return result;        
    }
    
    protected OgexVertexArray toVertexArray( DataStructure ds ) {
        OgexVertexArray result = new OgexVertexArray();
        
        String name = ds.getProperty("attrib");
        if( name == null ) {
            throw new RuntimeException("VertexArray has no 'attrib' property");
        }
        result.setName(name);
        result.setMorph(ds.getProperty("morph", 0));
        
        PrimitiveStructure struct = ds.getPrimitiveChild(true);
        if( struct.getElementSize() > 1 ) {
            result.setArray((float[][])struct.getData(), struct.getElementSize());
        } else {
            result.setArray((float[])struct.getData());
        }
        return result; 
    }
 
    protected MeshType toType( String s ) {
        if( s == null ) {
            return MeshType.Triangles;
        }
        MeshType result = MeshType.lookup(s);
        if( result == null ) {
            throw new RuntimeException("Unknown mesh type:" + s);
        }
        return result;
    }
 
    protected OgexMatrixTransform toTransform( DataStructure ds, Map<BaseStructure, Object> index ) {
        
        OgexMatrixTransform result = (OgexMatrixTransform)index.get(ds);
        if( result != null ) {
            return result;
        }
        result = new OgexMatrixTransform();        
        index.put(ds, result);

        Object object = ds.getProperty("object");
        if( object instanceof Boolean && (Boolean)object) {
            result.setObjectOnly(true);
        }
        
        float[][] array =  (float[][])ds.getData();
        if( array.length != 1 ) {
            throw new RuntimeException("Unexpected outer array length:" + array.length + ", from:" + ds.location());
        }
        result.setMatrix(array[0]);
        
        return result;
    }

    protected OgexRotation toRotation( DataStructure ds, Map<BaseStructure, Object> index ) {
        
        OgexRotation result = (OgexRotation)index.get(ds);
        if( result != null ) {
            return result;
        }
        
        // Gotta grab the values up front because we'll use the factory
        if( ds.getChildren().isEmpty() ) {
            throw new RuntimeException("Missing data in Rotation, from:" + ds.location());
        }
        if( ds.getChildren().size() > 1 ) {
            throw new RuntimeException("Too many children in Rotation structure, from:" + ds.location());
        }
        
        String s = ds.getProperty("kind", "axis");
        OgexRotation.Kind kind = OgexRotation.Kind.lookup(s);
        
        Object value = ds.getValue();
        
        result = OgexRotation.create(kind, value);
        
        if( "true".equals(ds.getProperty("object")) ) {
            result.setObjectOnly(true);
        } 
        
        index.put(ds, result);
        
        return result;
    }

    protected OgexScale toScale( DataStructure ds, Map<BaseStructure, Object> index ) {
        
        OgexScale result = (OgexScale)index.get(ds);
        if( result != null ) {
            return result;
        }
        
        // Gotta grab the values up front because we'll use the factory
        if( ds.getChildren().isEmpty() ) {
            throw new RuntimeException("Missing data in Scale, from:" + ds.location());
        }
        if( ds.getChildren().size() > 1 ) {
            throw new RuntimeException("Too many children in Scale structure, from:" + ds.location());
        }
        
        String s = ds.getProperty("kind", "xyz");
        OgexScale.Kind kind = OgexScale.Kind.lookup(s);
        
        Object value = ds.getValue();
                
        result = OgexScale.create(kind, value);
        
        if( "true".equals(ds.getProperty("object")) ) {
            result.setObjectOnly(true);
        } 
        
        index.put(ds, result);
        
        return result;
    }

    protected OgexTranslation toTranslation( DataStructure ds, Map<BaseStructure, Object> index ) {
        
        OgexTranslation result = (OgexTranslation)index.get(ds);
        if( result != null ) {
            return result;
        }
        
        // Gotta grab the values up front because we'll use the factory
        if( ds.getChildren().isEmpty() ) {
            throw new RuntimeException("Missing data in Translation, from:" + ds.location());
        }
        if( ds.getChildren().size() > 1 ) {
            throw new RuntimeException("Too many children in Translation structure, from:" + ds.location());
        }
        
        String s = ds.getProperty("kind", "xyz");
        OgexTranslation.Kind kind = OgexTranslation.Kind.lookup(s);
        
        Object value = ds.getValue();
        
        result = OgexTranslation.create(kind, value);
        
        if( "true".equals(ds.getProperty("object")) ) {
            result.setObjectOnly(true);
        } 
        
        index.put(ds, result);
        
        return result;
    }
    
    protected OgexTexture toTexture( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexTexture result = new OgexTexture();
        result.setTexCoord(((Number)ds.getProperty("texCoord", 0L)).longValue());
        
        for( BaseStructure child : ds ) {
            if( child instanceof PrimitiveStructure ) {
                String texture = child.getValue();
                result.setTexture(texture);
            } else if( StructTypes.TRANSFORM.equals(child.getType()) ) {
                result.addTransform(toTransform((DataStructure)child, index));
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        
        return result;
    }
    
    protected OgexMaterial toMaterial( DataStructure ds, Map<BaseStructure, Object> index ) {
        OgexMaterial result = (OgexMaterial)index.get(ds);
        if( result != null ) {
            return result;
        }
        result = new OgexMaterial();
        index.put(ds, result);
 
        if( "true".equals(ds.getProperty("two_sided")) ) {
            result.setTwoSided(true);
        }
 
        for( BaseStructure child : ds ) {
            String type = child.getType();
            if( StructTypes.NAME.equals(type) ) {
                result.setName((String)child.getValue());
            } else if( StructTypes.COLOR.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib");
                result.setColor(name, new OgexColor((float[])child.getValue()));
            } else if( StructTypes.TEXTURE.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib");
                result.setTexture(name, toTexture((DataStructure)child, index));
            } else if( StructTypes.PARAM.equals(type) ) {
                String name = ((DataStructure)child).getProperty("attrib");
                result.setParam(name, (Float)child.getValue());
            } else {
                log.warning("Unhandled structure type:" + child.getType() + ", from:" + child.location());
            }
        }
        
        return result;
    }    
}
