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
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import java.util.Random;

public abstract class SteamNSteelMachineBlock extends SteamNSteelDirectionalBlock
{
    private static final Material MATERIAL = Material.piston;
    private static final SoundType SOUND = Block.soundTypePiston;
    private static final float HARDNESS = 0.5f;

    @SuppressWarnings("UnsecureRandomNumberGeneration")
    private final Random rng = new Random();

    protected SteamNSteelMachineBlock()
    {
        super(MATERIAL);
        setStepSound(SOUND);
        setHardness(HARDNESS);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        // Disable normal block rendering.
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventParameter)
    {
        super.onBlockEventReceived(world, x, y, z, eventId, eventParameter);
        final TileEntity te = world.getTileEntity(x, y, z);
        return te != null && te.receiveClientEvent(eventId, eventParameter);
    }

    @Override
    public int getMobilityFlag()
    {
        // total immobility and stop pistons
        return 2;
    }

    @SuppressWarnings("NoopMethodInAbstractClass")
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        // no op
    }

    protected void dropInventory(World world, WorldBlockCoord coord, IInventory inventory)
    {
        for (int slotIndex = 0; slotIndex < inventory.getSizeInventory(); slotIndex++)
        {
            dropSlotContents(world, coord, inventory, slotIndex);
        }
    }

    void dropSlotContents(World world, WorldBlockCoord coord, IInventory inventory, int slotIndex)
    {
        final ItemStack itemstack = inventory.getStackInSlot(slotIndex);

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
                //noinspection ObjectAllocationInLoop
                final EntityItem entityitem = new EntityItem(world,
                        coord.getX() + xOffset, coord.getY() + yOffset, coord.getZ() + zOffset,
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
