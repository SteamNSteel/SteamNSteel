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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.library.Material;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import static mod.steamnsteel.item.SteamNSteelItem.getFormattedName;
import static mod.steamnsteel.item.SteamNSteelItem.getUnwrappedUnlocalizedName;

public class SSToolHoe extends ItemHoe
{
    private final String undecoratedName;

    public SSToolHoe(Material material)
    {
        super(material.getToolMaterial());
        //noinspection StringConcatenationMissingWhitespace
        undecoratedName = "hoe" + getFormattedName(material);
        setUnlocalizedName(undecoratedName);
        setCreativeTab(TheMod.CREATIVE_TAB);
    }

    public String getUndecoratedName()
    {
        return undecoratedName;
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("item.%s%s", TheMod.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
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
                .add("undecoratedName", undecoratedName)
                .toString();
    }
}
