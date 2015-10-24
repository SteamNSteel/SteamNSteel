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

package mod.steamnsteel.library;

import com.google.common.base.Objects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

import static net.minecraftforge.common.util.EnumHelper.addArmorMaterial;
import static net.minecraftforge.common.util.EnumHelper.addToolMaterial;

public enum Material
{
    BRONZE(addArmorMaterial("bronze", Reference.ArmourTextures.BRONZE, 11, new int[]{2, 6, 4, 2}, 9),
            addToolMaterial("bronze", 2, 250, 5.0F, 1.5F, 14)),
    STEEL(addArmorMaterial("steel", Reference.ArmourTextures.STEEL, 24, new int[]{3, 7, 6, 3}, 10),
            addToolMaterial("steel", 3, 905, 7.0F, 2.5F, 10));


    private final ItemArmor.ArmorMaterial armorMaterial;
    private final Item.ToolMaterial toolMaterial;

    Material(ItemArmor.ArmorMaterial armorMaterial, Item.ToolMaterial toolMaterial)
    {
        this.armorMaterial = armorMaterial;
        this.toolMaterial = toolMaterial;
    }

    public ItemArmor.ArmorMaterial getArmorMaterial()
    {
        return armorMaterial;
    }

    public Item.ToolMaterial getToolMaterial()
    {
        return toolMaterial;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("armorMaterial", armorMaterial)
                .add("toolMaterial", toolMaterial)
                .toString();
    }
}
