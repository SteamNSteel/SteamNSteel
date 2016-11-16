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

import com.google.common.base.Objects;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public abstract class SteamNSteelMachineBlock extends SteamNSteelDirectionalBlock
{
    private static final Material MATERIAL = Material.PISTON;
    private static final SoundType SOUND = SoundType.STONE;
    private static final float HARDNESS = 0.5f;

    @SuppressWarnings("UnsecureRandomNumberGeneration")
    private final Random rng = new Random();

    protected SteamNSteelMachineBlock()
    {
        super(MATERIAL);
        setSoundType(SOUND);
        setHardness(HARDNESS);
    }

    //TODO: This may have a sane default implementation. but we should check and make sure the material is appropriate
    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        final TileEntity te = worldIn.getTileEntity(pos);
        return te != null && te.receiveClientEvent(id, param);
    }

    @Override
    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        // total immobility and stop pistons
        return EnumPushReaction.BLOCK;
    }

    protected void dropInventory(World world, BlockPos pos, IInventory inventory)
    {
        for (int slotIndex = 0; slotIndex < inventory.getSizeInventory(); slotIndex++)
        {
            dropSlotContents(world, pos, inventory, slotIndex);
        }
    }

    void dropSlotContents(World world, BlockPos pos, IInventory inventory, int slotIndex)
    {
        final ItemStack itemstack = inventory.getStackInSlot(slotIndex);

        if (itemstack != null)
        {
            final float xOffset = rng.nextFloat() * 0.8F + 0.1F;
            final float yOffset = rng.nextFloat() * 0.8F + 0.1F;
            final float zOffset = rng.nextFloat() * 0.8F + 0.1F;

            while (itemstack.func_190916_E() > 0)
            {
                int j1 = rng.nextInt(21) + 10;

                if (j1 > itemstack.func_190916_E())
                {
                    j1 = itemstack.func_190916_E();
                }

                itemstack.func_190918_g(j1);
                //noinspection ObjectAllocationInLoop
                final EntityItem entityitem = new EntityItem(world,
                        pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset,
                        new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
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
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("rng", rng)
                .toString();
    }
}
