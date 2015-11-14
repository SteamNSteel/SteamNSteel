package mod.steamnsteel.client.model.pct;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;
import java.util.Collection;

/**
 * Created by codew on 9/11/2015.
 */
public class PCTModel implements IModel
{
    private final ProceduralConnectedTexture proceduralConnectedTexture;

    public PCTModel(ProceduralConnectedTexture proceduralConnectedTexture)
    {
        this.proceduralConnectedTexture = proceduralConnectedTexture;
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.of(new ResourceLocation("block/cube"));
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        return Lists.newArrayList();
    }

    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        return new PCTModelInstance(state, format, bakedTextureGetter, proceduralConnectedTexture);
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }
}