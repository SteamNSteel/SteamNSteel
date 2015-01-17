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

package mod.steamnsteel.block.resource.structure;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class SteelFloorBlock extends SteamNSteelBlock
{
    public static final String NAME = "blockSteelFloor";
    public static IIcon[] floorIcons;
    public SteelFloorBlock()
    {
        super(Material.rock);
        setBlockName(NAME);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        //return super.getIcon(world, x, y, z, side);
        if (side == ForgeDirection.UP.ordinal()) {
            int xPos = x % 3;
            if (xPos < 0) xPos += 3;
            int zPos = z % 9;
            if (zPos < 0) zPos += 9;

            final int index = zPos * 3 + xPos;
            return floorIcons[index];
        } else {
            return null;
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        floorIcons = new IIcon[3*9];
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 3; ++x) {
                String filename = String.format("%s:steelfloor/steelfloor_%dx%d", TheMod.MOD_ID, x + 1, y + 1);
                floorIcons[y * 3 + x] = iconRegister.registerIcon(filename);
            }
        }
    }
}
