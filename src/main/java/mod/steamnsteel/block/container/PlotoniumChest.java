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

package mod.steamnsteel.block.container;

import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.tileentity.PlotoniumChestTE;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PlotoniumChest extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "chestPlotonium";

    public PlotoniumChest()
    {
        super();
        setBlockName(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new PlotoniumChestTE();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
    {
        TileEntity te = world.getTileEntity(x, y, z);

        if (!world.isRemote && te != null && te instanceof PlotoniumChestTE)
        {
            player.displayGUIChest((PlotoniumChestTE) te);
            return true;
        }

        return false;
    }
}
