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

package mod.steamnsteel.item.armor;

import com.google.common.base.Objects;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.TheMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import static mod.steamnsteel.item.SteamNSteelItem.getUnwrappedUnlocalizedName;

public abstract class SteamNSteelItemArmor extends ItemArmor
{
    private final String undecoratedName;

    SteamNSteelItemArmor(ArmorMaterial material, int armorType, int renderIndex, String name)
    {
        super(material, armorType, renderIndex);
        setCreativeTab(TheMod.CREATIVE_TAB);
        setUnlocalizedName(name);
        undecoratedName = name;
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        final String unlocalizedName = getUnlocalizedName();
        itemIcon = iconRegister.registerIcon(unlocalizedName.substring(unlocalizedName.indexOf('.') + 1));
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("undecoratedName", undecoratedName)
                .toString();
    }
}
