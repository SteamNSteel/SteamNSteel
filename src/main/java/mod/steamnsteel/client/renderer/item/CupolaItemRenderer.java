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

import com.google.common.base.Optional;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.client.library.Textures;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.utility.Vector;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class CupolaItemRenderer implements IItemRenderer
{
    private static final Optional<Vector> ENTITY_LOC = Optional.of(new Vector(0.0f, 0.0f, 0.0f));
    private static final Optional<Vector> EQUIPPED_LOC = Optional.of(new Vector(1.0f, 0.0f, 1.5f));
    private static final Optional<Vector> FIRST_PERSON_LOC = Optional.of(new Vector(-0.0f, 0.0f, 0.0f));
    private static final Optional<Vector> INVENTORY_LOC = Optional.of(new Vector(-0.0f, -1.0f, 0.0f));

    private static final float SCALE = 0.66666f;

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
        Optional<Vector> vector = Optional.absent();

        switch (type)
        {
            case ENTITY:
                vector = ENTITY_LOC;
                break;
            case EQUIPPED:
                vector = EQUIPPED_LOC;
                break;
            case EQUIPPED_FIRST_PERSON:
                vector = FIRST_PERSON_LOC;
                break;
            case INVENTORY:
                vector = INVENTORY_LOC;
                break;
            default:
        }

        if (vector.isPresent())
            renderCupola(vector.get());
    }

    private void renderCupola(Vector vector)
    {
        GL11.glPushMatrix();
        GL11.glScalef(SCALE, SCALE, SCALE);
        GL11.glTranslatef(vector.getX(), vector.getY(), vector.getZ());

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(Textures.Model.CUPOLA);

        model.render();

        GL11.glPopMatrix();
    }
}
