package mod.steamnsteel.client.model.pct;

import com.google.common.collect.Maps;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.IOException;
import java.util.Map;

/**
 * Created by codew on 8/11/2015.
 */
public enum PCTModelLoader implements ICustomModelLoader
{
    instance;

    private Map<String, ProceduralConnectedTexture> textures = Maps.newHashMap();
    private static final String modelIndicator  = "advancedConnectedTexture/";

    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        final String resourceDomain = modelLocation.getResourceDomain();
        final String resourcePath = modelLocation.getResourcePath();
        final boolean isValidPath = resourcePath.startsWith(modelIndicator) ||
                resourcePath.startsWith("models/block/advancedConnectedTexture/") ||
                resourcePath.startsWith("models/item/advancedConnectedTexture/");
        if (!resourceDomain.equals(TheMod.MOD_ID)) {
            return false;
        }
        return isValidPath;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException
    {
        String textureName = modelLocation.getResourcePath().substring(
         modelLocation.getResourcePath().indexOf(modelIndicator) + modelIndicator.length());

        return new PCTModel(textures.get(textureName));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) { }

    @SubscribeEvent
    public void onPreTextureStitch(TextureStitchEvent.Pre event) {
        for (final ProceduralConnectedTexture proceduralConnectedTexture : textures.values())
        {
            proceduralConnectedTexture.registerSprites(event.map);
        }
    }

    public void registerTexture(String textureName, ProceduralConnectedTexture texture) {
        textures.put(textureName, texture);
    }

    public ProceduralConnectedTexture getTexture(String textureName) {
        return textures.get(textureName);
    }

    public static String describeTextureAt(World worldIn, BlockPos pos, EnumFacing side) {
        final IBlockState state = worldIn.getBlockState(pos);
        final BlockRendererDispatcher rendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final IBakedModel model = rendererDispatcher.getModelFromBlockState(state, worldIn, pos);
        if (model instanceof PCTModelInstance) {
            final ProceduralConnectedTexture proceduralConnectedTexture = ((PCTModelInstance) model).getProceduralConnectedTexture();
            return proceduralConnectedTexture.describeTextureAt(worldIn, pos, side);
        }
        return "Not a Procedural Connected Texture";
    }
}
