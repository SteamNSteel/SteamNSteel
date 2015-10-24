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

import mod.steamnsteel.block.SteamNSteelBlock;
import net.minecraft.block.material.Material;

public class SteamNSteelStorageBlock extends SteamNSteelBlock
{
    public static final String BRASS_BLOCK = "blockBrass";
    public static final String BRONZE_BLOCK = "blockBronze";
    public static final String COPPER_BLOCK = "blockCopper";
    public static final String PLOTONIUM_BLOCK = "blockPlotonium";
    public static final String STEEL_BLOCK = "blockSteel";
    public static final String TIN_BLOCK = "blockTin";
    public static final String ZINC_BLOCK = "blockZinc";

    public SteamNSteelStorageBlock(String name)
    {
        super(Material.iron);
        setUnlocalizedName(name);
        setHardness(5.0f);
        setResistance(10.0f);
        setStepSound(soundTypeMetal);
    }
}
