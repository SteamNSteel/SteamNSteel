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

package mod.steamnsteel.block.machine;

import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CupolaBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "cupola";

    public CupolaBlock()
    {
        setBlockName(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new CupolaTE();
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof CupolaTE)
            if (((CupolaTE) te).isActive())
                return 15;

        return super.getLightValue(world, x, y, z);
    }
}
