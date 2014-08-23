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
import mod.steamnsteel.client.library.Textures;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.tileentity.CupolaTE;
import mod.steamnsteel.utility.Orientation;
import mod.steamnsteel.utility.Vector;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class CupbolaTESR extends TileEntitySpecialRenderer
{
    private static final Vector SCALE = new Vector(1.0f, 1.0f, 1.0f);
    private static final Vector OFFSET = new Vector(0.5f, 0.0f, 0.5f);

    private final CupolaModel model = new CupolaModel();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        if (tileEntity instanceof CupolaTE)
        {
            final CupolaTE te = (CupolaTE) tileEntity;

            final int metadata = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
            final boolean isSlave = CupolaBlock.isSlave(metadata);

            if (!isSlave)
            {
                GL11.glPushMatrix();

                GL11.glScalef(SCALE.getX(), SCALE.getY(), SCALE.getZ());
                //noinspection NumericCastThatLosesPrecision
                GL11.glTranslatef((float) x + OFFSET.getX(), (float) y + OFFSET.getY(), (float) z + OFFSET.getZ());

                final float rotationAngle;
                final Orientation orientation = Orientation.getdecodedOrientation(metadata);
                switch (orientation)
                {
                    case SOUTH:
                        rotationAngle = 0.0f;
                        break;
                    case WEST:
                        rotationAngle = 270.0f;
                        break;
                    case NORTH:
                        rotationAngle = 180.0f;
                        break;
                    default:
                        rotationAngle = 90.0f;
                        break;
                }
                GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);

                bindTexture(Textures.Model.CUPOLA);

                // Render
                model.render();

                GL11.glPopMatrix();
            }
        }
    }
}
