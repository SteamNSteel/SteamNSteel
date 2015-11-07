package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.FanLargeBlock;
import mod.steamnsteel.client.model.opengex.OpenGEXAnimationFrameProperty;
import mod.steamnsteel.client.model.opengex.OpenGEXState;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * Created by codew on 5/11/2015.
 */
public class LargeFanTESR extends TileEntitySpecialRenderer
{



    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBlockState blockState = te.getWorld().getBlockState(te.getPos());
        BlockPos blockpos = te.getPos();
        final OpenGEXState openGEXState = new OpenGEXState(null, getWorld().getTotalWorldTime() / 5.0f);
        IBakedModel model = blockRenderer.getModelFromBlockState(blockState.withProperty(FanLargeBlock.RENDER_DYNAMIC, true), getWorld(), te.getPos());
        blockState = ((IExtendedBlockState)blockState).withProperty(OpenGEXAnimationFrameProperty.instance, openGEXState);



        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(7425);
        }
        else
        {
            GlStateManager.shadeModel(7424);
        }

        worldRenderer.startDrawingQuads();
        worldRenderer.setVertexFormat(DefaultVertexFormats.BLOCK);
        worldRenderer.setTranslation(x - blockpos.getX(), y - blockpos.getY(), z - blockpos.getZ());
        worldRenderer.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        if (model instanceof ISmartBlockModel) {
            model = ((ISmartBlockModel) model).handleBlockState(blockState);
        }

        blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, blockState, te.getPos(), worldRenderer);

        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
    }
}
