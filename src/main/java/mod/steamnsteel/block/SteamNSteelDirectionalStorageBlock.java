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

import net.minecraft.block.material.Material;

public class SteamNSteelDirectionalStorageBlock extends SteamNSteelDirectionalBlock
{
    public static final String STORAGE_BRASS_BLOCK = "storageBrass";
    public static final String STORAGE_BRONZE_BLOCK = "storageBronze";
    public static final String STORAGE_COPPER_BLOCK = "storageCopper";
    public static final String STORAGE_PLOTONIUM_BLOCK = "storagePlotonium";
    public static final String STORAGE_STEEL_BLOCK = "storageSteel";
    public static final String STORAGE_TIN_BLOCK = "storageTin";
    public static final String STORAGE_ZINC_BLOCK = "storageZinc";

    public SteamNSteelDirectionalStorageBlock(String name)
    {
        super(Material.iron);
        setUnlocalizedName(name);
        setHardness(5.0f);
        setResistance(10.0f);
        setStepSound(soundTypeMetal);
    }
}
