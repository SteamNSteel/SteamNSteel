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

package mod.steamnsteel.tileentity;

import com.google.common.base.Objects;
import mod.steamnsteel.block.container.RemnantRuinChestBlock;
import mod.steamnsteel.inventory.Inventory;
import mod.steamnsteel.library.ModBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RemnantRuinChestTE extends SteamNSteelTE implements IInventory
{
    private static final int INVENTORY_SIZE = 27;
    private final Inventory inventory = new Inventory(INVENTORY_SIZE);

    private float lidAngle;
    private float prevLidAngle;

    private int numUsingPlayers;
    private int ticksSinceSync;

    public float getLidAngle()
    {
        return lidAngle;
    }

    public float getPrevLidAngle()
    {
        return prevLidAngle;
    }

    @Override
    public int getSizeInventory()
    {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrAmount)
    {
        return inventory.decrStackSize(slotIndex, decrAmount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        return inventory.getStackOnClosing(slotIndex);
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        inventory.setSlot(slotIndex, itemStack);
    }

    @Override
    public String getInventoryName()
    {
        return containerName(RemnantRuinChestBlock.NAME);
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return inventory.getStackSizeMax();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
        ++numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, ModBlock.remnantRuinChest, 1, numUsingPlayers);
    }

    @Override
    public void closeInventory()
    {
        --numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, ModBlock.remnantRuinChest, 1, numUsingPlayers);
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        inventory.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        inventory.writeToNBT(nbt);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        ++ticksSinceSync;
        if (ticksSinceSync % 20 * 4 == 0)
        {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, ModBlock.remnantRuinChest, 1, numUsingPlayers);
        }

        prevLidAngle = lidAngle;

        if (numUsingPlayers != 0 && lidAngle == 0.0F)
            worldObj.playSoundEffect(xCoord, yCoord + 0.5F, zCoord, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);

        if (isChestOpen())
        {
            final float angleIncrement = 0.1F;
            if (numUsingPlayers > 0)
                lidAngle += angleIncrement;

            if (numUsingPlayers == 0)
                lidAngle -= angleIncrement;

            if (lidAngle < 0)
                lidAngle = 0.0F;

            if (lidAngle > 1.0F)
                lidAngle = 1.0F;

            if (lidAngle < 0.5 && prevLidAngle > 0.5)
            {
                worldObj.playSoundEffect(xCoord, yCoord + 0.5F, zCoord, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int eventID, int numUsingPlayers)
    {
        if (eventID == 1)
        {
            this.numUsingPlayers = numUsingPlayers;
            return true;
        } else
        {
            return super.receiveClientEvent(eventID, numUsingPlayers);
        }
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    private boolean isChestOpen()
    {
        return numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("inventory", inventory)
                .add("lidAngle", lidAngle)
                .add("prevLidAngle", prevLidAngle)
                .add("numUsingPlayers", numUsingPlayers)
                .add("ticksSinceSync", ticksSinceSync)
                .toString();
    }
}
