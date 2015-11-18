package mod.steamnsteel.client.model.pct;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.sun.org.apache.bcel.internal.generic.IFLE;
import mod.steamnsteel.library.ModProperties;
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.property.IExtendedBlockState;
import java.io.IOException;
import java.net.CookieManager;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

/**
 * Created by codew on 9/11/2015.
 */
public class PCTModelInstance implements IFlexibleBakedModel, ISmartBlockModel
{
    private final IModelState state;
    private final VertexFormat format;
    private final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
    private final ProceduralConnectedTexture proceduralConnectedTexture;
    private final Map<Integer, IFlexibleBakedModel> cache = Maps.newHashMap();
    private IFlexibleBakedModel bakedModel;
    private IModel baseModel;

    public PCTModelInstance(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ProceduralConnectedTexture proceduralConnectedTexture)
    {
        this.state = state;
        this.format = format;
        this.bakedTextureGetter = bakedTextureGetter;
        this.proceduralConnectedTexture = proceduralConnectedTexture;

        try
        {
            baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("block/cube"));
            bakedModel = baseModel.bake(state, format, bakedTextureGetter);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public PCTModelInstance(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ProceduralConnectedTexture proceduralConnectedTexture, BlockPos blockPos, IBlockAccess blockAccess, IModel baseModel)
    {
        this(state, format, bakedTextureGetter, proceduralConnectedTexture);
        ImmutableMap.Builder<String, String> textures = ImmutableMap.builder();

        int identifier = 31;

        for (final EnumFacing side : EnumFacing.VALUES)
        {
            final Block block = blockAccess.getBlockState(blockPos).getBlock();
            String texture = "missingno";
            final String key = side.toString().toLowerCase();
            if (block.shouldSideBeRendered(blockAccess, blockPos.offset(side), side))
            {

                TextureAtlasSprite sprite = null;
                try
                {
                    sprite = proceduralConnectedTexture.getSpriteForSide(blockAccess, blockPos, side);
                } catch (Exception e) {
                    Logger.info("blockPos: %s, side: %s", blockPos, side);
                    sprite = proceduralConnectedTexture.getSpriteForSide(blockAccess, blockPos, side);
                }
                if (sprite != null)
                {
                    texture = sprite.getIconName();
                } else{
                    System.out.print("woof");

                }
            }


            identifier ^= texture.hashCode() * 31;

            textures.put(key, texture);
        }

        bakedModel = cache.get(identifier);
        if (bakedModel == null) {

            IModel newModel = baseModel;

            if (baseModel instanceof IRetexturableModel) {
                final ImmutableMap<String, String> build = textures.build();
                newModel = ((IRetexturableModel) baseModel).retexture(build);
            }

            bakedModel = newModel.bake(state, format, bakedTextureGetter);
            cache.put(identifier, bakedModel);
        }
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side)
    {
        return bakedModel.getFaceQuads(side);
    }

    @Override
    public List<BakedQuad> getGeneralQuads()
    {
        return bakedModel.getGeneralQuads();
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return bakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return bakedModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return bakedModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getTexture()
    {
        return bakedModel.getTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return bakedModel.getItemCameraTransforms();
    }

    @Override
    public VertexFormat getFormat()
    {
        return bakedModel.getFormat();
    }

    @Override
    public IBakedModel handleBlockState(IBlockState blockState)
    {
        if (blockState instanceof IExtendedBlockState) {
            final IExtendedBlockState extendedBlockState = (IExtendedBlockState) blockState;
            if (extendedBlockState.getUnlistedProperties().containsKey(ModProperties.PROPERTY_BLOCK_POS) &&
                    extendedBlockState.getUnlistedProperties().containsKey(ModProperties.PROPERTY_BLOCK_ACCESS)) {

                final BlockPos blockPos = extendedBlockState.getValue(ModProperties.PROPERTY_BLOCK_POS);
                final IBlockAccess blockAccess = extendedBlockState.getValue(ModProperties.PROPERTY_BLOCK_ACCESS);

                return new PCTModelInstance(state, format, bakedTextureGetter, proceduralConnectedTexture, blockPos, blockAccess, baseModel);
            }
        }

        return this;
    }

    public ProceduralConnectedTexture getProceduralConnectedTexture() {
        return proceduralConnectedTexture;
    }
}
