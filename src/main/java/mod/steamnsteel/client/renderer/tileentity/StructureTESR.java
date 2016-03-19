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

import mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class StructureTESR extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity ute, double x, double y, double z, float partialTicks, int destroyStage)
    {
        final SteamNSteelStructureTE te = (SteamNSteelStructureTE) ute;
        final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final Tessellator tessellator = Tessellator.getInstance();
        final VertexBuffer worldRenderer = tessellator.getBuffer();

        final BlockPos pos = te.getPos();
        final IBlockState state = te.getWorld().getBlockState(pos);
        final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(state);

        bindTexture(TextureMap.locationBlocksTexture);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled()? 7425 : 7424);

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        worldRenderer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        worldRenderer.color(255, 255, 255, 255);

        blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, pos, worldRenderer, true);

        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
    }
}
