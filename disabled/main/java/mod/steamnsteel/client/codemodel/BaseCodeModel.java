package mod.steamnsteel.client.codemodel;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public abstract class BaseCodeModel
{
    protected static final DefaultStateMapper sdm = new DefaultStateMapper();
    protected static final Function<ResourceLocation, TextureAtlasSprite> textureGetter =
            location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

    public abstract void loadModel(ModelBakeEvent event);

    protected static IModel loadModel(ResourceLocation rl)
    {
        try
        {
            return ModelLoaderRegistry.getModel(rl);
        } catch (Exception e)
        {
            return ModelLoaderRegistry.getMissingModel();
        }
    }

    protected static class DefaultStateMapper extends StateMapperBase
    {
        public ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            return new ModelResourceLocation(state.getBlock().getRegistryName(), getPropertyString(state.getProperties()));
            //return new ModelResourceLocation(Block.blockRegistry.getNameForObject(state.getBlock()), getPropertyString(state.getProperties()));
        }
    }
}
