package mod.steamnsteel.client.model;

import com.google.common.base.Preconditions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public abstract class SNSMultiModel
{
    static final DefaultStateMapper stateMapper = new DefaultStateMapper();
    static final Function<ResourceLocation, TextureAtlasSprite> textureGetter =
            location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

    public abstract void loadModel(ModelBakeEvent event);

    static IModel loadModel(ResourceLocation rl)
    {
        try
        {
            return ModelLoaderRegistry.getModel(rl);
        } catch (final Exception e)
        {
            return ModelLoaderRegistry.getMissingModel();
        }
    }

    //This is necessary because the getModelResourceLocation method is protected in the Minecraft version.
    @SuppressWarnings("ProtectedInnerClass")
    protected static class DefaultStateMapper extends StateMapperBase
    {
        @Override
        public ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            final ResourceLocation registryName = state.getBlock().getRegistryName();
            Preconditions.checkNotNull(registryName);
            return new ModelResourceLocation(registryName, getPropertyString(state.getProperties()));
        }
    }
}
