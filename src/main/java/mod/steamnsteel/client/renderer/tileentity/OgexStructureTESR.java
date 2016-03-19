/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */
package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.structure.FanLargeStructure;
import mod.steamnsteel.client.model.opengex.OpenGEXAnimationFrameProperty;
import mod.steamnsteel.client.model.opengex.OpenGEXState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.opengl.GL11;

public class OgexStructureTESR extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBlockState blockState = te.getWorld().getBlockState(te.getPos());
        BlockPos blockpos = te.getPos();
        final OpenGEXState openGEXState = new OpenGEXState(null, getWorld().getTotalWorldTime() / 20.0f);
        IBakedModel model = blockRenderer.getModelForState(blockState.withProperty(FanLargeStructure.RENDER_DYNAMIC, true));
        blockState = ((IExtendedBlockState)blockState).withProperty(OpenGEXAnimationFrameProperty.instance, openGEXState);



        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldRenderer = tessellator.getBuffer();
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

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        worldRenderer.setTranslation(x - blockpos.getX(), y - blockpos.getY(), z - blockpos.getZ());
        worldRenderer.color(255, 255, 255, 255);

        //TODO: Do I checkSides here?
        blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, blockState, te.getPos(), worldRenderer, true);

        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
    }
}
