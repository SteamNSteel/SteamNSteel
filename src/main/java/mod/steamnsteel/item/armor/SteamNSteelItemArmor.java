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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.library.Material;
import mod.steamnsteel.proxy.Proxies;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import static mod.steamnsteel.item.SteamNSteelItem.getFormattedName;
import static mod.steamnsteel.item.SteamNSteelItem.getUnwrappedUnlocalizedName;

@SuppressWarnings("StringConcatenationMissingWhitespace")
public abstract class SteamNSteelItemArmor extends ItemArmor
{
    private static final String TEXTURE_LOCATION = "textures/armor/";
    private final String undecoratedName;
    private final String materialName;
    final static int renderTypeIsNotUsedAnymore = -1;

    SteamNSteelItemArmor(Material material, int armourType, String name)
    {

        super(material.getArmorMaterial(), renderTypeIsNotUsedAnymore, armourType);
        setCreativeTab(TheMod.CREATIVE_TAB);
        undecoratedName = name + getFormattedName(material);
        setUnlocalizedName(undecoratedName);
        materialName = material.name().toLowerCase();
    }

    private static String getRendererName(Material material)
    {
        return TheMod.MOD_ID + ':' + material.toString() + "Armor";
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return TheMod.MOD_ID + ':' + TEXTURE_LOCATION + materialName + "_layer_" + getArmorLayer() + ".png";
    }

    protected abstract int getArmorLayer();

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
                .add("materialName", materialName)
                .toString();
    }
}
