package mod.steamnsteel.client.renderer.block;

import codechicken.lib.render.RenderUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.block.resource.structure.RemnantRuinPillarBlock;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.client.renderer.model.RemnantRuinPillarModel;
import mod.steamnsteel.library.ModBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RemnantRuinPillarRenderer implements ISimpleBlockRenderingHandler
{
    public static RemnantRuinPillarRenderer INSTANCE = new RemnantRuinPillarRenderer();
    public static ResourceLocation TEXTURE = getResourceLocation(RemnantRuinPillarBlock.NAME);
    private RemnantRuinPillarRenderer() {}

    private final RemnantRuinPillarModel model = new RemnantRuinPillarModel();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        final Tessellator tessellator = Tessellator.instance;
        tessellator.draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);

        int lightValue = block.getMixedBrightnessForBlock(world, x, y, z);
        tessellator.setBrightness(lightValue);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        model.render();

        GL11.glPopMatrix();
        tessellator.startDrawingQuads();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return ModBlock.remnantRuinPillar.getRenderType();
    }

    static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), getTexturePath(name));
    }

    private static final String TEXTURE_FILE_EXTENSION = ".png";
    public static final String TEXTURE_LOCATION = "textures/models/";
    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static String getTexturePath(String name)
    {
        return TEXTURE_LOCATION + name + TEXTURE_FILE_EXTENSION;
    }
}
