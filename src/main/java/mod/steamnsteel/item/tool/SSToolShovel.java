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

package mod.steamnsteel.item.tool;

import com.google.common.base.Objects;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.library.Material;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class SSToolShovel extends ItemSpade
{
    public SSToolShovel(Material material)
    {
        super(material.getToolMaterial());
        setCreativeTab(TheMod.CREATIVE_TAB);
    }

    @Override
    public String getUnlocalizedName()
    {
        return "item." + getRegistryName();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return getUnlocalizedName();
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .toString();
    }
}
