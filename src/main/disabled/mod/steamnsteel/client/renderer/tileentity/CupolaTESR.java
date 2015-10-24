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


import com.google.common.base.Objects;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.tileentity.CupolaTE;
import mod.steamnsteel.utility.Orientation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class CupolaTESR extends SteamNSteelTESR
{
    public static final ResourceLocation TEXTURE = getResourceLocation(CupolaBlock.NAME);
    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static final ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(0.5f, 0.0f, 0.5f);
    private static final ResourceLocation TEXTURE_ACTIVE = getResourceLocation(CupolaBlock.NAME + "_active");

    private final CupolaModel model = new CupolaModel();

    private static float getAngleFromOrientation(Orientation orientation)
    {
        switch (orientation)
        {
            case SOUTH:
                return 180.0f;
            case WEST:
                return 90.0f;
            case NORTH:
                return 0.0f;
            default:
                return 270.0f;
        }
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float tick, int whatDoesThisDo)
    {
        if (tileEntity instanceof CupolaTE)
        {
            final CupolaTE te = (CupolaTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) posX, (float) posY, (float) posZ);

            renderCupola(te);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderCupola(CupolaTE te)
    {
        if (te.isSlave()) return;
        final Tessellator instance = Tessellator.getInstance();

        final BlockPos pos = te.getPos();
        final World world = te.getWorld();

        // Lighting
        final float brightness = ModBlock.cupola.getMixedBrightnessForBlock(world, pos);
        final int skyLight = world.getLightBrightnessForSkyBlocks(pos, 0);
        final int skyLightLSB = skyLight % 65536;
        final int skyLightMSB = skyLight / 65536;


        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.setColorOpaque_F(brightness, brightness, brightness);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, skyLightLSB, skyLightMSB);

        // Open Render buffer
        GL11.glPushMatrix();

        // Inherent adjustments to model
        GL11.glScalef(SCALE.left, SCALE.middle, SCALE.right);
        GL11.glTranslatef(OFFSET.left, OFFSET.middle, OFFSET.right);

        // Orient the model to match the placement
        final IBlockState metadata = world.getBlockState(pos);
        final Orientation orientation = Orientation.getdecodedOrientation(metadata);

        GL11.glRotatef(getAngleFromOrientation(orientation), 0.0F, 1.0F, 0.0F);

        // Bind the texture
        if (te.isActive())
            bindTexture(TEXTURE_ACTIVE);
        else
            bindTexture(TEXTURE);

        // Render
        model.render();

        // Close Render Buffer
        GL11.glPopMatrix();
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("model", model)
                .toString();
    }
}
