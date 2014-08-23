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

package mod.steamnsteel.item.block;

import mod.steamnsteel.block.SteamNSteelDirectionalBlock;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.library.Blocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CupolaItem extends ItemBlock
{
    private static final int UPDATE_CLIENT = 3;

    public CupolaItem(Block block)
    {
        super(block);
    }

    private static boolean isAir(World world, int x, int y, int z) {return world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z);}

    private static boolean canPlayerEdit(ItemStack itemStack, EntityPlayer player, int x, int y, int z, int side) {return player.canPlayerEdit(x, y, z, side, itemStack) && player.canPlayerEdit(x, y + 1, z, side, itemStack);}

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitVectorX, float hitVectorY, float hitVectorZ)
    {
        if (world.isRemote) return true;
        if (side != 1) return false;
        if (y > world.getActualHeight() - 3) return false;

        final int baseY = y + 1;

        if (!canPlayerEdit(itemStack, player, x, baseY, z, side)) return false;
        if (!isAir(world, x, baseY, z)) return false;

        final Block block = Blocks.CUPOLA;
        final int metadata = SteamNSteelDirectionalBlock.getMetadataFromDirection(player.rotationYaw);

        world.setBlock(x, baseY, z, block, metadata, UPDATE_CLIENT);
        //noinspection ObjectEquality
        if (world.getBlock(x, baseY, z) == block)
        {
            world.setBlock(x, baseY + 1, z, block, CupolaBlock.codeSlaveMetadata(metadata), UPDATE_CLIENT);
        }

        itemStack.stackSize--;
        return true;
    }
}
