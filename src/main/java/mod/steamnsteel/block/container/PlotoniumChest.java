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

import com.google.common.base.Objects;
import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.tileentity.PlotoniumChestTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PlotoniumChest extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "chestPlotonium";

    public PlotoniumChest() { setBlockName(NAME); }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        final PlotoniumChestTE te = (PlotoniumChestTE) world.getTileEntity(x, y, z);

        if (te != null)
        {
            for (int slotIndex = 0; slotIndex < te.getSizeInventory(); ++slotIndex)
            {
                dropSlotContents(world, x, y, z, te, slotIndex);
            }

            world.func_147453_f(x, y, z, block);
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
    {
        final TileEntity te = world.getTileEntity(x, y, z);

        if (!player.isSneaking())
            if (!world.isRemote && te != null && te instanceof PlotoniumChestTE)
                player.displayGUIChest((IInventory) te);

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new PlotoniumChestTE();
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("rng", rng)
                .toString();
    }
}
