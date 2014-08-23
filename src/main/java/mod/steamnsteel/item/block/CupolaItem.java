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
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

    private static boolean canPlayerEdit(ItemStack itemStack, EntityPlayer player, int x, int y, int z, int side) {return player.canPlayerEdit(x, y, z, side, itemStack) && player.canPlayerEdit(x, y + 1, z, side, itemStack);}

    private static boolean canPlaceOnSide(EntityPlayer player, World world, Block block, int x, int y, int z, int side, ItemStack itemStack) {return world.canPlaceEntityOnSide(block, x, y, z, false, side, player, itemStack) && world.canPlaceEntityOnSide(block, x, y + 1, z, false, 1, player, itemStack);}

    private static boolean canPlayerPlaceInWorld(EntityPlayer player, World world, int x, int y, int z, int side, ItemStack itemStack) {return itemStack.stackSize == 0 || !canPlayerEdit(itemStack, player, x, y, z, side) || y > world.getActualHeight() - 3;}

    @SuppressWarnings({"OverlyComplexBooleanExpression", "MethodWithMoreThanThreeNegations", "ObjectEquality"})
    private static boolean doAdjustPlacementCoords(World world, int x, int y, int z, Block testBlock)
    {
        return testBlock != Blocks.vine && testBlock != Blocks.tallgrass && testBlock != Blocks.deadbush &&
                !testBlock.isReplaceable(world, x, y, z);
    }

    @SuppressWarnings("ObjectEquality")
    private static boolean doForceSideOne(World world, int x, int y, int z, Block testBlock) {return testBlock == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1;}

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod", "ObjectEquality", "ImplicitNumericConversion"})
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitVectorX, float hitVectorY, float hitVectorZ)
    {
        if (world.isRemote) return true;

        final Block testBlock = world.getBlock(x, y, z);

        int actualSide = side;
        int actualX = x;
        int actualY = y;
        int actualZ = z;

        if (doForceSideOne(world, x, y, z, testBlock))
        {
            actualSide = 1;
        } else if (doAdjustPlacementCoords(world, x, y, z, testBlock))
        {
            if (actualSide == 0)
            {
                actualY--;
            }

            if (actualSide == 1)
            {
                actualY++;
            }

            if (actualSide == 2)
            {
                actualZ--;
            }

            if (actualSide == 3)
            {
                actualZ++;
            }

            if (actualSide == 4)
            {
                actualX--;
            }

            if (actualSide == 5)
            {
                actualX++;
            }
        }

        if (canPlayerPlaceInWorld(player, world, actualX, actualY, actualZ, actualSide, itemStack)) return false;

        if (canPlaceOnSide(player, world, testBlock, actualX, actualY, actualZ, actualSide, itemStack))
        {
            final Block block = mod.steamnsteel.library.Blocks.CUPOLA;
            final int metadata = SteamNSteelDirectionalBlock.getMetadataFromDirection(player.rotationYaw);

            world.setBlock(actualX, actualY, actualZ, block, metadata, UPDATE_CLIENT);
            if (world.getBlock(actualX, actualY, actualZ) == block)
            {
                if (world.setBlock(actualX, actualY + 1, actualZ, block, CupolaBlock.codeSlaveMetadata(metadata), UPDATE_CLIENT))
                {
                    world.playSoundEffect(actualX + 0.5f, actualY + 0.5f, actualZ + 0.5f, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getPitch() + 0.8f);
                    itemStack.stackSize--;
                }
            }

            return true;
        }

        return false;
    }
}
