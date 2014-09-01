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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.api.crafting.IAlloyResult;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.crafting.alloy.AlloyManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

public class CupolaTE extends TileEntity implements ISidedInventory
{
    public static final int INPUT_LEFT_INVENTORY_INDEX = 0;
    public static final int INPUT_RIGHT_INVENTORY_INDEX = 1;
    public static final int FUEL_INVENTORY_INDEX = 2;
    public static final int OUTPUT_INVENTORY_INDEX = 3;

    public static final int INVENTORY_SIZE = 4;

    private static final int[] slotsTop = new int[] {INPUT_LEFT_INVENTORY_INDEX, INPUT_RIGHT_INVENTORY_INDEX};
    private static final int[] slotsBottom = new int[] {OUTPUT_INVENTORY_INDEX, FUEL_INVENTORY_INDEX};
    private static final int[] slotsSides = new int[] {FUEL_INVENTORY_INDEX};

    private static final String IS_ACTIVE = "isActive";
    private static final String INVENTORY = "inventory";
    private static final String SLOT = "Slot";
    private static final String DEVICE_COOK_TIME = "deviceCookTime";
    private static final String FUEL_BURN_TIME = "fuelBurnTime";
    private static final String ITEM_COOK_TIME = "itemCookTime";

    private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
    public int deviceCookTime;
    public int fuelBurnTime;
    public int itemCookTime;
    private boolean isActive = false;

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scale)
    {
        return itemCookTime * scale / 200;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scale)
    {
        if (fuelBurnTime > 0)
        {
            return deviceCookTime * scale / fuelBurnTime;
        }

        return 0;
    }

    private static String containerName(String name)
    {
        return "container." + TheMod.MOD_ID + ':' + name;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        isActive = nbt.getBoolean(IS_ACTIVE);

        NBTTagList tagList = nbt.getTagList(INVENTORY, 10);
        inventory = new ItemStack[INVENTORY_SIZE];
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slotIndex = tagCompound.getByte(SLOT);
            if (slotIndex >= 0 && slotIndex < inventory.length)
            {
                inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

        deviceCookTime = nbt.getInteger(DEVICE_COOK_TIME);
        fuelBurnTime = nbt.getInteger(FUEL_BURN_TIME);
        itemCookTime = nbt.getInteger(ITEM_COOK_TIME);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setBoolean(IS_ACTIVE, isActive);

        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < INVENTORY_SIZE; ++currentIndex)
        {
            if (inventory[currentIndex] != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte(SLOT, (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        nbt.setTag(INVENTORY, tagList);
        nbt.setInteger(DEVICE_COOK_TIME, deviceCookTime);
        nbt.setInteger(FUEL_BURN_TIME, fuelBurnTime);
        nbt.setInteger(ITEM_COOK_TIME, itemCookTime);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        ForgeDirection direction = ForgeDirection.values()[side];
        switch (direction)
        {
            case DOWN:
                return slotsBottom;
            case UP:
                return slotsTop;
            default:
                return slotsSides;
        }
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side)
    {
        return isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side)
    {
        return slotIndex == OUTPUT_INVENTORY_INDEX;
    }

    @Override
    public int getSizeInventory()
    {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return inventory[slotIndex];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount)
    {
        if (inventory[slotIndex] != null)
        {
            ItemStack itemStack;

            if (inventory[slotIndex].stackSize <= decrementAmount)
            {
                itemStack = inventory[slotIndex];
                inventory[slotIndex] = null;
                return itemStack;
            }
            else
            {
                itemStack = inventory[slotIndex].splitStack(decrementAmount);

                if (inventory[slotIndex].stackSize == 0)
                {
                    inventory[slotIndex] = null;
                }

                return itemStack;
            }
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null)
        {
            setInventorySlotContents(slotIndex, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        inventory[slotIndex] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
        {
            itemStack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName()
    {
        return containerName(CupolaBlock.NAME);
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
        // noop
    }

    @Override
    public void closeInventory()
    {
        // noop
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return slotIndex == OUTPUT_INVENTORY_INDEX ? false : (slotIndex == FUEL_INVENTORY_INDEX) ? TileEntityFurnace.isItemFuel(itemStack) : true;

    }

    @Override
    public void updateEntity()
    {
        boolean isBurning = deviceCookTime > 0;
        boolean sendUpdate = false;

        if (deviceCookTime > 0)
        {
            deviceCookTime--;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.deviceCookTime == 0 && this.canAlloy())
            {
                this.fuelBurnTime = this.deviceCookTime = TileEntityFurnace.getItemBurnTime(this.inventory[FUEL_INVENTORY_INDEX]);

                if (this.deviceCookTime > 0)
                {
                    sendUpdate = true;

                    if (this.inventory[FUEL_INVENTORY_INDEX] != null)
                    {
                        --this.inventory[FUEL_INVENTORY_INDEX].stackSize;

                        if (this.inventory[FUEL_INVENTORY_INDEX].stackSize == 0)
                        {
                            this.inventory[FUEL_INVENTORY_INDEX] = this.inventory[FUEL_INVENTORY_INDEX].getItem().getContainerItem(inventory[FUEL_INVENTORY_INDEX]);
                        }
                    }
                }
            }

            if (this.deviceCookTime > 0 && this.canAlloy())
            {
                this.itemCookTime++;

                if (this.itemCookTime == 200)
                {
                    this.itemCookTime = 0;
                    this.alloy();
                    sendUpdate = true;
                }
            }
            else
            {
                this.itemCookTime = 0;
            }

            if (isBurning != this.deviceCookTime > 0)
            {
                sendUpdate = true;
            }
        }

        if (sendUpdate)
        {
            this.markDirty();
            isActive = this.deviceCookTime > 0;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, isActive?1:0);
            this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        }
    }

    @Override
    public boolean receiveClientEvent(int eventId, int eventData)
    {
        if (eventId == 1)
        {
            isActive = eventData != 0;
            worldObj.func_147451_t(this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
        return super.receiveClientEvent(eventId, eventData);
    }

    private boolean canAlloy()
    {
        if (inventory[INPUT_LEFT_INVENTORY_INDEX] == null || inventory[INPUT_RIGHT_INVENTORY_INDEX] == null)
            return false;

        IAlloyResult output = AlloyManager.INSTANCE.getCupolaResult(this.inventory[INPUT_LEFT_INVENTORY_INDEX], inventory[INPUT_RIGHT_INVENTORY_INDEX]);
        if (!output.getItemStack().isPresent() || output.getConsumedFirst() > inventory[INPUT_LEFT_INVENTORY_INDEX].stackSize || output.getConsumedSecond() > inventory[INPUT_RIGHT_INVENTORY_INDEX].stackSize)
        {
            return false;
        }

        if (this.inventory[OUTPUT_INVENTORY_INDEX] == null)
        {
            return true;

        }

        if (!this.inventory[OUTPUT_INVENTORY_INDEX].isItemEqual(output.getItemStack().get()))
        {
            return false;
        }

        int result = this.inventory[OUTPUT_INVENTORY_INDEX].stackSize + output.getItemStack().get().stackSize;
        return result <= getInventoryStackLimit() && result <= output.getItemStack().get().getMaxStackSize();
    }

    public void alloy()
    {
        if (canAlloy())
        {
            IAlloyResult output = AlloyManager.INSTANCE.getCupolaResult(this.inventory[INPUT_LEFT_INVENTORY_INDEX], inventory[INPUT_RIGHT_INVENTORY_INDEX]);
            if (output.getItemStack().isPresent())
            {
                addItemStackToOutput(output.getItemStack().get().copy());

                inventory[INPUT_RIGHT_INVENTORY_INDEX].stackSize -= output.getConsumedFirst();
                inventory[INPUT_LEFT_INVENTORY_INDEX].stackSize -= output.getConsumedSecond();

                if (inventory[INPUT_RIGHT_INVENTORY_INDEX].stackSize <= 0)
                {
                    inventory[INPUT_RIGHT_INVENTORY_INDEX] = null;
                }

                if (inventory[INPUT_LEFT_INVENTORY_INDEX].stackSize <= 0)
                {
                    inventory[INPUT_LEFT_INVENTORY_INDEX] = null;
                }
            }
        }
    }

    private void addItemStackToOutput(ItemStack itemStack)
    {
        int maxStackSize = Math.min(getInventoryStackLimit(), itemStack.getMaxStackSize());

        if (this.inventory[OUTPUT_INVENTORY_INDEX] == null)
        {
            this.inventory[OUTPUT_INVENTORY_INDEX] = itemStack;
            return;
        }
        if (this.inventory[OUTPUT_INVENTORY_INDEX].isItemEqual(itemStack)
                && this.inventory[OUTPUT_INVENTORY_INDEX].stackSize < maxStackSize)
        {
            int addedSize = Math.min(itemStack.stackSize, maxStackSize - this.inventory[OUTPUT_INVENTORY_INDEX].stackSize);
            itemStack.stackSize -= addedSize;
            this.inventory[OUTPUT_INVENTORY_INDEX].stackSize += addedSize;
        }
    }
}
