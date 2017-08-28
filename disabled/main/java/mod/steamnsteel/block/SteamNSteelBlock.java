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

package mod.steamnsteel.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.steamnsteel.TheMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class SteamNSteelBlock extends Block
{
    protected SteamNSteelBlock(Material material, boolean addToCreativeTab)
    {
        super(material);
        if (addToCreativeTab) setCreativeTab(TheMod.CREATIVE_TAB);
    }

    protected SteamNSteelBlock(Material material) { this(material, true); }

    @Override
    public String getUnlocalizedName()
    {
        return "tile." + getRegistryName();
    }


}
