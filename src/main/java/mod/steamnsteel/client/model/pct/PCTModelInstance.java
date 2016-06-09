package mod.steamnsteel.client.model.pct;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mod.steamnsteel.library.ModProperties;
import mod.steamnsteel.library.Reference;
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Map;

public class PCTModelInstance implements IPerspectiveAwareModel
{
    private final IModelState state;
    private final VertexFormat format;
    private final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
    private final ProceduralConnectedTexture proceduralConnectedTexture;
    static final Map<Integer, IBakedModel> cache = Maps.newConcurrentMap();
    private IBakedModel bakedModel = null;
    private IModel baseModel = null;

    public PCTModelInstance(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ProceduralConnectedTexture pct)
    {
        this(state, format, bakedTextureGetter, pct, getBaseModel());
        //CalculateSides(null, null);
    }

    private static IModel getBaseModel()
    {
        try
        {
            return ModelLoaderRegistry.getModel(new ResourceLocation(Reference.MOD_ID, "block/cube_mirrored_bottom"));
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private PCTModelInstance(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ProceduralConnectedTexture pct, IModel baseModel) {
        this.state = state;
        this.format = format;
        this.bakedTextureGetter = bakedTextureGetter;
        proceduralConnectedTexture = pct;

        this.baseModel = baseModel;
        bakedModel = baseModel.bake(state, format, bakedTextureGetter);
    }

    public PCTModelInstance(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ProceduralConnectedTexture pct, BlockPos blockPos, IBlockAccess blockAccess, IModel baseModel)
    {
        this(state, format, bakedTextureGetter, pct, baseModel);
    }

    private IBakedModel getModelForSide(BlockPos blockPos, IBlockAccess blockAccess, EnumFacing side) {
        final Builder<String, String> textures = ImmutableMap.builder();

        final String key = side.toString().toLowerCase();
        final String texture = getTextureForSide(blockPos, blockAccess, side);
        int identifier = 31;
        identifier ^= side.hashCode() * 31;
        identifier ^= texture.hashCode() * 31;

        textures.put(key, texture);

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
        return bakedModel;
    }

    private String getTextureForSide(BlockPos blockPos, IBlockAccess blockAccess, EnumFacing side)
    {
        TextureAtlasSprite sprite = proceduralConnectedTexture.getDefaultTextureForSide(side);
        if (blockAccess != null)
        {
            final BlockPos immutableBlockPos = blockPos.toImmutable();
            final IBlockState blockState = blockAccess.getBlockState(immutableBlockPos);

            if (blockState.shouldSideBeRendered(blockAccess, immutableBlockPos, side))
            {
                try
                {
                    sprite = proceduralConnectedTexture.getSpriteForSide(blockAccess, immutableBlockPos, side);
                } catch (final Exception e)
                {
                    Logger.info("blockPos: %s, side: %s", blockPos, side);
                    sprite = proceduralConnectedTexture.getSpriteForSide(blockAccess, immutableBlockPos, side);
                }
            }
        }
        String texture = "missingno";
        if (sprite != null)
        {
            texture = sprite.getIconName();
        } else
        {
            System.out.print("woof");
        }
        return texture;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        if (side == null) return Lists.newArrayList();

        if (state instanceof IExtendedBlockState) {
            final IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
            if (extendedBlockState.getUnlistedProperties().containsKey(ModProperties.PROPERTY_BLOCK_POS) &&
                    extendedBlockState.getUnlistedProperties().containsKey(ModProperties.PROPERTY_BLOCK_ACCESS)) {

                final BlockPos blockPos = extendedBlockState.getValue(ModProperties.PROPERTY_BLOCK_POS);
                final IBlockAccess blockAccess = extendedBlockState.getValue(ModProperties.PROPERTY_BLOCK_ACCESS);

                return getModelForSide(blockPos, blockAccess, side).getQuads(state, side, rand);
            }
        }

        return bakedModel.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
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
    public TextureAtlasSprite getParticleTexture()
    {
        return proceduralConnectedTexture.getParticleTexture();
    }

    @Override
    @Deprecated
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return bakedModel.getItemCameraTransforms();
    }
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
    {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, state, cameraTransformType);
    }

    public ProceduralConnectedTexture getProceduralConnectedTexture() {
        return proceduralConnectedTexture;
    }

    public ItemOverrideList getOverrides()
    {
        // TODO handle items
        return ItemOverrideList.NONE;
    }
}
