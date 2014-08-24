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

import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.tileentity.CupolaTE;
import mod.steamnsteel.utility.Orientation;
import mod.steamnsteel.utility.Vector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CupbolaTESR extends SteamNSteelTESR
{
    private static final Vector SCALE = new Vector(1.0f, 1.0f, 1.0f);
    private static final Vector OFFSET = new Vector(0.5f, 0.0f, 0.5f);

    public static final ResourceLocation TEXTURE = getResourceLocation(CupolaBlock.NAME);

    private final CupolaModel model = new CupolaModel();

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        if (tileEntity instanceof CupolaTE)
        {
            final CupolaTE te = (CupolaTE) tileEntity;

            final int metadata = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
            final boolean isMaster = !CupolaBlock.isSlave(metadata);

            if (isMaster)
            {
                // Open Render buffer
                GL11.glPushMatrix();

                // Inherent adjustments to model
                GL11.glScalef(SCALE.getX(), SCALE.getY(), SCALE.getZ());
                GL11.glTranslatef((float) x + OFFSET.getX(), (float) y + OFFSET.getY(), (float) z + OFFSET.getZ());

                // Orient the model to match the placement
                final float rotationAngle;
                final Orientation orientation = Orientation.getdecodedOrientation(metadata);
                switch (orientation)
                {
                    case SOUTH:
                        rotationAngle = 180.0f;
                        break;
                    case WEST:
                        rotationAngle = 90.0f;
                        break;
                    case NORTH:
                        rotationAngle = 0.0f;
                        break;
                    default:
                        rotationAngle = 270.0f;
                        break;
                }
                GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);

                // Bind the texture
                bindTexture(TEXTURE);

                // Render
                model.render();

                // Close Render Buffer
                GL11.glPopMatrix();
            }
        }
    }
}
