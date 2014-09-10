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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.client.renderer.tileentity.CupbolaTESR;
import mod.steamnsteel.utility.Vector;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class CupolaItemRenderer implements IItemRenderer
{
    private static final Optional<Vector<Float>> ENTITY_OFFSET = Optional.of(new Vector<Float>(0.0f, -1.0f, 0.0f));
    private static final Optional<Vector<Float>> EQUIPPED_OFFSET = Optional.of(new Vector<Float>(1.0f, 0.0f, 1.5f));
    private static final Optional<Vector<Float>> FIRST_PERSON_OFFSET = Optional.of(new Vector<Float>(-0.0f, 0.0f, 0.0f));
    private static final Optional<Vector<Float>> INVENTORY_OFFSET = Optional.of(new Vector<Float>(-0.0f, -1.0f, 0.0f));

    private static final Vector<Float> SCALE = new Vector<Float>(0.666667f, 0.666667f, 0.666667f);

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
        Optional<Vector<Float>> vector = Optional.absent();

        switch (type)
        {
            case ENTITY:
                vector = ENTITY_OFFSET;
                break;
            case EQUIPPED:
                vector = EQUIPPED_OFFSET;
                break;
            case EQUIPPED_FIRST_PERSON:
                vector = FIRST_PERSON_OFFSET;
                break;
            case INVENTORY:
                vector = INVENTORY_OFFSET;
                break;
            default:
        }

        if (vector.isPresent())
            renderCupola(vector.get());
    }

    private void renderCupola(Vector<Float> vector)
    {
        GL11.glPushMatrix();
        GL11.glScalef(SCALE.getX(), SCALE.getY(), SCALE.getZ());
        GL11.glTranslatef(vector.getX(), vector.getY(), vector.getZ());

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(CupbolaTESR.TEXTURE);

        model.render();

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
