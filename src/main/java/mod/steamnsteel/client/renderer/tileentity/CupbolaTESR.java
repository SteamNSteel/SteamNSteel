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

import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.client.library.Textures;
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class CupbolaTESR extends TileEntitySpecialRenderer
{
    private final CupolaModel model = new CupolaModel();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        if (tileEntity instanceof CupolaTE)
        {
            final CupolaTE te = (CupolaTE) tileEntity;

            GL11.glPushMatrix();

            // Scale, Translate, Rotate
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float) x + 0.5F, (float) y + 0.0F, (float) z + 1.2F);
            GL11.glRotatef(45F, 0F, 1F, 0F);
            GL11.glRotatef(-90F, 1F, 0F, 0F);

            bindTexture(Textures.Model.CUPOLA);

            // Render
            model.render();

            GL11.glPopMatrix();
        }
    }
}
