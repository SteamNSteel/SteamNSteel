package mod.steamnsteel.client.model;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.client.model.opengex.ogex.OgexAnimation;
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
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenGEXFlexibleBakedModel implements IFlexibleBakedModel, ISmartBlockModel, ISmartItemModel, IPerspectiveAwareModel {
    private final OpenGEXModel model;
    private final IModelState state;
    private final VertexFormat format;
    private final ImmutableMap<String, TextureAtlasSprite> textureSpriteMap;

    public OpenGEXFlexibleBakedModel(OpenGEXModel openGEXModel, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textureSpriteMap) {
        this.model = openGEXModel;
        this.state = state;
        this.format = format;
        this.textureSpriteMap = textureSpriteMap;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side)
    {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        throw new NotImplementedException("");
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
        return textureSpriteMap.values().asList().get(0);
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
            cache.put(frame, new OpenGEXFlexibleBakedModel(model, new OpenGEXState(animation, frame, state), format, textureSpriteMap));
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
