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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import java.util.Random;

public class PlotoniumChest extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "chestPlotonium";

    @SuppressWarnings("UnsecureRandomNumberGeneration")
    private final Random rng = new Random();

    public PlotoniumChest() { setBlockName(NAME); }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        final PlotoniumChestTE te = (PlotoniumChestTE) world.getTileEntity(x, y, z);

        if (te != null)
        {
            for (int slotindex = 0; slotindex < te.getSizeInventory(); ++slotindex)
            {
                dropSlotContents(world, x, y, z, te, slotindex);
            }

            world.func_147453_f(x, y, z, block);
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
    {
        final TileEntity te = world.getTileEntity(x, y, z);

        if (!world.isRemote && te != null && te instanceof PlotoniumChestTE)
        {
            player.displayGUIChest((IInventory) te);
            return true;
        }

        return false;
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    private void dropSlotContents(World world, int x, int y, int z, PlotoniumChestTE te, int slotIndex)
    {
        final ItemStack itemstack = te.getStackInSlot(slotIndex);

        if (itemstack != null)
        {
            final float xOffset = rng.nextFloat() * 0.8F + 0.1F;
            final float yOffset = rng.nextFloat() * 0.8F + 0.1F;
            final float zOffset = rng.nextFloat() * 0.8F + 0.1F;

            while (itemstack.stackSize > 0)
            {
                int j1 = rng.nextInt(21) + 10;

                if (j1 > itemstack.stackSize)
                {
                    j1 = itemstack.stackSize;
                }

                itemstack.stackSize -= j1;
                final EntityItem entityitem = new EntityItem(world, x + xOffset, y + yOffset, z + zOffset, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                final float motionMax = 0.05F;
                //noinspection NumericCastThatLosesPrecision
                entityitem.motionX = (float) rng.nextGaussian() * motionMax;
                //noinspection NumericCastThatLosesPrecision
                entityitem.motionY = (float) rng.nextGaussian() * motionMax + 0.2F;
                //noinspection NumericCastThatLosesPrecision
                entityitem.motionZ = (float) rng.nextGaussian() * motionMax;

                if (itemstack.hasTagCompound())
                {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                }

                world.spawnEntityInWorld(entityitem);
            }
        }
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
