package mod.steamnsteel.client.model.opengex;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.client.model.opengex.ogex.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings("deprecation") //Seriously, screw deprecated methods on interfaces. I *HATE* having to do this.
public class OpenGEXModelInstance implements IPerspectiveAwareModel
{
    private final OpenGEXModel model;
    private final IModelState state;
    private final VertexFormat format;
    private final ImmutableMap<String, TextureAtlasSprite> textures;
    private final float[][] nodeMatrices;
    private final boolean gui3d;
    private final boolean smoothLighting;
    private ImmutableList<BakedQuad> quads;

    public OpenGEXModelInstance(OpenGEXModel openGEXModel, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textureSpriteMap, float[][] nodeMatrices, boolean gui3d, boolean smoothLighting)
    {
        this.model = openGEXModel;
        this.state = state;
        this.format = format;
        this.textures = textureSpriteMap;
        this.nodeMatrices = nodeMatrices;
        this.gui3d = gui3d;
        this.smoothLighting = smoothLighting;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand)
    {
        if (quads == null)
        {
            float animationTime = 0;
            if (state instanceof OpenGEXState)
            {
                animationTime = ((OpenGEXState) state).getTime();
            }

            final Optional<TRSRTransformation> globalTransform = state.apply(Optional.<IModelPart>absent());

            float[][] nodeMatrices = this.nodeMatrices;
            if (nodeMatrices == null)
            {
                nodeMatrices = Animation.calculateTransforms(model.getScene(), animationTime, model.getAllNodes(), model.getNodeParents());
            }

            final OpenGEXNode node = model.getNode();
            final Builder<BakedQuad> builder = ImmutableList.builder();
            for (final OgexNode ogexNode : node)
            {
                //FIXME: Why the hell am I creating a new model as opposed to using my current one?
                builder.addAll(new OpenGEXModelInstance(new OpenGEXModel(model.getLocation(), ogexNode, model.getScene(), model.getTextureMap(), model.getEnabledNodes(), gui3d, smoothLighting), state, format, textures, nodeMatrices, gui3d, smoothLighting).getQuads(blockState, side, rand));
            }

            final List<String> enabledNodes = model.getEnabledNodes();

            if (node instanceof OgexGeometryNode)
            {
                final OgexGeometryNode geometryNode = (OgexGeometryNode) node;
                if (enabledNodes == null || enabledNodes.contains(geometryNode.getName()))
                {
                    final OgexMesh mesh = geometryNode.getGeometry().getMesh();
                    final MeshType type = mesh.getType();
                    if (type != MeshType.Quads && type != MeshType.Triangles)
                    {
                        throw new OpenGEXException("Attempting to generate a model for an unsupported OpenGL Mesh Type: " + type);
                    }

                    final List<OgexTexture> textures = new ArrayList<>();
                    for (final OgexMaterial ogexMaterial : geometryNode.getMaterials())
                    {
                        textures.add(ogexMaterial.getTexture("diffuse"));
                    }

                    if (textures.isEmpty())
                    {
                        textures.add(OpenGEXModel.white);
                    }

                    final List<OgexVertexArray> vertexArrays = mesh.getVertexArrays();

                    Vector4f vertex = new Vector4f();
                    Vector3f normal = new Vector3f();

                    final Matrix4f nodeTransformation = new Matrix4f(nodeMatrices[geometryNode.getIndex()]);

                    for (OgexIndexArray indexArray : mesh.getIndexArrays())
                    {
                        for (long[] polyGroup : (long[][]) indexArray.getArray())
                        {
                            UnpackedBakedQuad.Builder quadBuilder = new UnpackedBakedQuad.Builder(format);

                            for (int i = 0; i < polyGroup.length; i++)
                            {
                                final long longVertexIndex = polyGroup[i];
                                final int vertexIndex = (int) longVertexIndex;

                                float[] positionArray = null;
                                float[] normalArray = null;
                                float[] texcoordArray = null;
                                float[] colorArray = null;

                                for (final OgexVertexArray array : vertexArrays)
                                {
                                    if ("position".equals(array.getName()))
                                    {
                                        positionArray = array.getArray2()[vertexIndex];
                                    } else if ("normal".equals(array.getName()))
                                    {
                                        normalArray = array.getArray2()[vertexIndex];
                                    } else if ("color".equals(array.getName()))
                                    {
                                        colorArray = array.getArray2()[vertexIndex];
                                    } else if ("texcoord".equals(array.getName()))
                                    {
                                        texcoordArray = array.getArray2()[vertexIndex];
                                    }
                                }

                                //FIXME: don't assume arrays are populated, update for appropriate format.
                                vertex.x = positionArray[0];
                                vertex.y = positionArray[1];
                                vertex.z = positionArray[2];
                                vertex.w = 1;

                                normal.x = normalArray[0];
                                normal.y = normalArray[1];
                                normal.z = normalArray[2];

                                nodeTransformation.transform(vertex);
                                // pos
                                if (globalTransform.isPresent()) {
                                    globalTransform.get().getMatrix().transform(vertex);
                                }
                                vertex.x /= vertex.w;
                                vertex.y /= vertex.w;
                                vertex.z /= vertex.w;

                                // normal
                                Matrix3f tm = new Matrix3f();
                                nodeTransformation.getRotationScale(tm);
                                tm.invert();
                                tm.transpose();
                                //FIXME: apply global transformation to normal calculations
                                tm.transform(normal);
                                normal.normalize();

                                //FIXME: generate face normal
                                quadBuilder.setQuadOrientation(EnumFacing.getFacingFromVector(normal.x, normal.y, normal.z));

                                TextureAtlasSprite sprite;
                                if (this.textures.isEmpty()){
                                    sprite = this.textures.get("missingno");
                                }
                                else if (textures.get(0) == OpenGEXModel.white) sprite = ModelLoader.White.INSTANCE;
                                else sprite = this.textures.get(textures.get(0).getTexture());

                                //FIXME: calculate the face normal.
                                Vector3f faceNormal = normal;
                                putVertexData(quadBuilder, vertex, faceNormal, normal, texcoordArray, colorArray, sprite);
                                if (i == 2 && type == MeshType.Triangles)
                                {
                                    putVertexData(quadBuilder, vertex, faceNormal, normal, texcoordArray, colorArray, sprite);
                                }
                            }
                            builder.add(quadBuilder.build());
                        }
                    }
                }
            }

            quads = builder.build();
        }
        return quads;
    }

    private void putVertexData(UnpackedBakedQuad.Builder builder, Vector4f vertex, Vector3f faceNormal, Vector3f vertexNormal, float[] textureCoordinates, float[] color, TextureAtlasSprite sprite)
    {
        // TODO handle everything not handled (texture transformations, bones, transformations, normals, e.t.c)
        for (int e = 0; e < format.getElementCount(); e++)
        {
            switch (format.getElement(e).getUsage())
            {
                case POSITION:
                    builder.put(e, vertex.x, vertex.y, vertex.z, 1);
                    break;
                case COLOR:
                    //FIXME: Color not currently supported - maybe too many variants?
                    float d = LightUtil.diffuseLight(faceNormal.x, faceNormal.y, faceNormal.z);
                    if (color != null)
                    {
                        //If color.length != 4, then input was RGB, use Alpha of 1.0
                        final float v = color.length == 4 ? color[3] : 1.0f;
                        builder.put(e, d * color[0], d * color[1], d * color[2], v);
                    } else
                    {
                        builder.put(e, d, d, d, 1);
                    }
                    break;
                case UV:
                    final int index = format.getElement(e).getIndex();
                    if (index < (textureCoordinates.length / 2))
                    {
                        builder.put(e,
                                sprite.getInterpolatedU(textureCoordinates[index * 2] * 16),
                                sprite.getInterpolatedV((1 - textureCoordinates[index * 2 + 1]) * 16),
                                0,
                                1
                        );
                    } else
                    {
                        builder.put(e, 0, 0, 0, 1);
                    }
                    break;
                case NORMAL:
                    //w changed to 0, Fry assures me it's a bug in B3d.
                    if (vertexNormal != null)
                    {
                        builder.put(e, vertexNormal.x, vertexNormal.y, vertexNormal.z, 0);
                    } else
                    {
                        builder.put(e, faceNormal.x, faceNormal.y, faceNormal.z, 0);
                    }
                    break;
                default:
                    builder.put(e);
            }
        }
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return textures.values().asList().get(0);
    }

    @Override
    @Deprecated
    public net.minecraft.client.renderer.block.model.ItemCameraTransforms getItemCameraTransforms()
    {
        return net.minecraft.client.renderer.block.model.ItemCameraTransforms.DEFAULT;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
    {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, state, cameraTransformType);
    }

    /*@Override
    public OpenGEXModelInstance handleBlockState(IBlockState state)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState exState = (IExtendedBlockState) state;
            if (exState.getUnlistedNames().contains(OpenGEXAnimationFrameProperty.instance))
            {
                OpenGEXState s = exState.getValue(OpenGEXAnimationFrameProperty.instance);
                if (s != null)
                {
                    //FIXME: Need to find a multi-animation-track ogex file to determine how this is going to work
                    OgexAnimation animation = null;
                    return new OpenGEXModelInstance(model, new OpenGEXState(animation, s.getTime(), this.state), format, textures, nodeMatrices);
                }
            }
        }
        return this;
    }*/

    public ItemOverrideList getOverrides()
    {
        // TODO handle items
        return ItemOverrideList.NONE;
    }
}
