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

package mod.steamnsteel.client.renderer.item;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.client.library.Textures;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class CupolaItemRenderer implements IItemRenderer
{
    private final CupolaModel model;

    public CupolaItemRenderer()
    {
        model = new CupolaModel();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;

        boolean doRender = true;
        //noinspection EnumSwitchStatementWhichMissesCases
        switch (type)
        {
            case ENTITY:
                x = -0.5f;
                z = 0.5f;
                break;
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                z = 1.0f;
                break;
            case INVENTORY:
                y = -0.1f;
                z = 1.0f;
                break;
            default:
                doRender = false;
        }

        if (doRender)
            renderCupola(x, y, z);
    }

    private void renderCupola(float x, float y, float z)
    {
        GL11.glPushMatrix();
        GL11.glScalef(1F, 1F, 1F);
        GL11.glTranslatef(x, y, z);
        GL11.glRotatef(-90F, 1F, 0, 0);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(Textures.Model.CUPOLA);

        model.render();

        GL11.glPopMatrix();
    }
}
