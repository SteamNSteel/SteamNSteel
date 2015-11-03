package mod.steamnsteel.client.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.client.model.opengex.OpenGEXNode;
import mod.steamnsteel.client.model.opengex.ogex.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.b3d.B3DModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.*;

public class OpenGEXFlexibleBakedModel implements IFlexibleBakedModel, ISmartBlockModel, ISmartItemModel, IPerspectiveAwareModel {
    private final OpenGEXModel model;
    private final IModelState state;
    private final VertexFormat format;
    private final ImmutableMap<String, TextureAtlasSprite> textures;
    private ImmutableList<BakedQuad> quads;

    public OpenGEXFlexibleBakedModel(OpenGEXModel openGEXModel, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textureSpriteMap) {
        this.model = openGEXModel;
        this.state = state;
        this.format = format;
        this.textures = textureSpriteMap;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side)
    {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        if (quads == null)
        {

            final OpenGEXNode node = model.getNode();
            Builder<BakedQuad> builder = ImmutableList.builder();
            for (final OgexNode ogexNode : node)
            {
                builder.addAll(new OpenGEXFlexibleBakedModel(new OpenGEXModel(model.getLocation(), ogexNode, model.getTextureMap()), state, format, textures).getGeneralQuads());
            }

            if (node instanceof OgexGeometryNode) {
                final OgexGeometryNode geometryNode = (OgexGeometryNode) node;
                final OgexMesh mesh = geometryNode.getGeometry().getMesh();
                final MeshType type = mesh.getType();
                if (type != MeshType.Quads && type != MeshType.Triangles) {
                    throw new OpenGEXException("Attempting to generate a model for an unsupported OpenGL Mesh Type: " + type);
                }

                List<OgexTexture> textures = new ArrayList<OgexTexture>();
                for (final OgexMaterial ogexMaterial : geometryNode.getMaterials())
                {
                    textures.add(ogexMaterial.getTexture("diffuse"));
                }

                if (textures.isEmpty()) {

                    textures.add(OpenGEXModel.white);
                }

                final List<OgexVertexArray> vertexArrays = mesh.getVertexArrays();

                Vector3f vertex = new Vector3f();
                Vector3f normal = new Vector3f();

                for (OgexIndexArray indexArray : mesh.getIndexArrays()) {
                    for (long[] polyGroup : (long[][]) indexArray.getArray()) {
                        UnpackedBakedQuad.Builder quadBuilder = new UnpackedBakedQuad.Builder(format);
                        quadBuilder.setQuadColored();

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

                            normal.x = normalArray[0];
                            normal.y = normalArray[1];
                            normal.z = normalArray[2];

                            //FIXME: do this.
                            /*faces = mesh.getKind().bake(new Function<B3DModel.Node<?>, Matrix4f>()
                            {
                                // gets transformation in global space
                                public Matrix4f apply(B3DModel.Node<?> node)
                                {
                                    return state.apply(PartWrapper.create(node)).getMatrix();
                                }
                            });*/
                            //this was the colladatest equivalent:
                            //OgexMatrixTransform.applyTo(calculatedTransform, vertex);

                            //FIXME: generate face normal
                            quadBuilder.setQuadOrientation(EnumFacing.getFacingFromVector(normal.x, normal.y, normal.z));

                            TextureAtlasSprite sprite;
                            if (this.textures == null || this.textures.isEmpty()) sprite = this.textures.get("missingno");
                            else if (textures.get(0) == OpenGEXModel.white) sprite = ModelLoader.White.instance;
                            else sprite = this.textures.get(textures.get(0).getTexture());

                            //FIXME: calculate the face normal.
                            Vector3f faceNormal = normal;
                            putVertexData(quadBuilder, vertex, faceNormal, normal, texcoordArray, colorArray, sprite);
                            if (i == 2 && type == MeshType.Triangles) {
                                putVertexData(quadBuilder, vertex, faceNormal, normal, texcoordArray, colorArray, sprite);
                            }
                        }
                        quads = builder.build();
                    }
                }
            }

            quads = builder.build();
        }
        return quads;
    }

    private final void putVertexData(UnpackedBakedQuad.Builder builder, Vector3f vertex, Vector3f faceNormal, Vector3f vertexNormal, float[] textureCoordinates, float[] color, TextureAtlasSprite sprite)
    {
        System.out.println(vertex);

        // TODO handle everything not handled (texture transformations, bones, transformations, normals, e.t.c)
        for(int e = 0; e < format.getElementCount(); e++)
        {
            switch(format.getElement(e).getUsage())
            {
                case POSITION:
                    builder.put(e, vertex.x, vertex.y, vertex.z, 1);
                    break;
                case COLOR:
                    //FIXME: Color not currently supported - maybe too many variants?
                    float d = LightUtil.diffuseLight(faceNormal.x, faceNormal.y, faceNormal.z);
                    if(color != null)
                    {
                        //If color.length != 4, then input was RGB, use Alpha of 1.0
                        final float v = color.length == 4 ? color[3] : 1.0f;
                        builder.put(e, d * color[0], d * color[1], d * color[2], v);
                    }
                    else
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
                                sprite.getInterpolatedV(textureCoordinates[index * 2 + 1] * 16),
                                0,
                                1
                        );
                    }
                    else
                    {
                        builder.put(e, 0, 0, 0, 1);
                    }
                    break;
                case NORMAL:
                    //w changed to 0, Fry assures me it's a bug in B3d.
                    if(vertexNormal != null)
                    {
                        builder.put(e, vertexNormal.x, vertexNormal.y, vertexNormal.z, 0);
                    }
                    else
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
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getTexture() {
        //Speak to fry once he figures out how he wants to define the particle texture and locate that in the map.
        return textures.values().asList().get(0);
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }

    @Override
    public Pair<IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        if(state instanceof IPerspectiveState)
        {
            return Pair.of((IBakedModel)this, TRSRTransformation.blockCornerToCenter(((IPerspectiveState)state).forPerspective(cameraTransformType).apply(model)).getMatrix());
        }
        return Pair.of((IBakedModel)this, null);
    }

    @Override
    public OpenGEXFlexibleBakedModel handleBlockState(IBlockState state)
    {
        if(state instanceof IExtendedBlockState)
        {
            IExtendedBlockState exState = (IExtendedBlockState)state;
            if(exState.getUnlistedNames().contains(B3DLoader.B3DFrameProperty.instance))
            {
                B3DLoader.B3DState s = exState.getValue(B3DLoader.B3DFrameProperty.instance);
                if(s != null)
                {
                    throw new NotImplementedException("Need to be able to determine the animation being played.");
                    //return getCachedModel(s.getFrame(), null);
                }
            }
        }
        return this;
    }

    private final Map<Integer, OpenGEXFlexibleBakedModel> cache = new HashMap<Integer, OpenGEXFlexibleBakedModel>();

    public OpenGEXFlexibleBakedModel getCachedModel(int frame, OgexAnimation animation)
    {
        if(!cache.containsKey(frame))
        {
            cache.put(frame, new OpenGEXFlexibleBakedModel(model, new OpenGEXState(animation, frame, state), format, textures));
        }
        return cache.get(frame);
    }

    @Override
    public OpenGEXFlexibleBakedModel handleItemState(ItemStack stack)
    {
        // TODO: Copy whatever the hell Fry ends up doing here :P
        return this;
    }
}
