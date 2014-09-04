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

@SuppressWarnings("ClassWithTooManyMethods")
public class CupolaTE extends TileEntity implements ISidedInventory
{
    public static final int INPUT_LEFT_INVENTORY_INDEX = 0;
    public static final int INPUT_RIGHT_INVENTORY_INDEX = 1;
    private static final int[] slotsTop = {INPUT_LEFT_INVENTORY_INDEX, INPUT_RIGHT_INVENTORY_INDEX};
    public static final int FUEL_INVENTORY_INDEX = 2;
    private static final int[] slotsSides = {FUEL_INVENTORY_INDEX};
    public static final int OUTPUT_INVENTORY_INDEX = 3;
    private static final int[] slotsBottom = {OUTPUT_INVENTORY_INDEX, FUEL_INVENTORY_INDEX};
    public static final int INVENTORY_SIZE = 4;
    private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
    private static final String IS_ACTIVE = "isActive";
    private static final String INVENTORY = "inventory";
    private static final String SLOT = "Slot";
    private static final String DEVICE_COOK_TIME = "deviceCookTime";
    private static final String FUEL_BURN_TIME = "fuelBurnTime";
    private static final String ITEM_COOK_TIME = "itemCookTime";
    private int deviceCookTime;
    private int fuelBurnTime;
    private int itemCookTime;
    private boolean isActive = false;

    private static String containerName(String name)
    {
        return "container." + TheMod.MOD_ID + ':' + name;
    }

    public int getDeviceCookTime()
    {
        return deviceCookTime;
    }

    public void setDeviceCookTime(int deviceCookTime)
    {
        this.deviceCookTime = deviceCookTime;
    }

    public int getFuelBurnTime()
    {
        return fuelBurnTime;
    }

    public void setFuelBurnTime(int fuelBurnTime)
    {
        this.fuelBurnTime = fuelBurnTime;
    }

    public int getItemCookTime()
    {
        return itemCookTime;
    }

    public void setItemCookTime(int itemCookTime)
    {
        this.itemCookTime = itemCookTime;
    }

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

    public boolean isActive()
    {
        return isActive;
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

        final NBTTagList tagList = nbt.getTagList(INVENTORY, 10);
        inventory = new ItemStack[INVENTORY_SIZE];
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            final NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            final byte slotIndex = tagCompound.getByte(SLOT);
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

        final NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < INVENTORY_SIZE; ++currentIndex)
        {
            if (inventory[currentIndex] != null)
            {
                //noinspection ObjectAllocationInLoop
                final NBTTagCompound tagCompound = new NBTTagCompound();
                //noinspection NumericCastThatLosesPrecision
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

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        final ForgeDirection direction = ForgeDirection.values()[side];
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

    @SuppressWarnings({"AssignmentToNull", "ReturnOfNull"})
    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount)
    {
        if (inventory[slotIndex] != null)
        {
            final ItemStack itemStack;

            if (inventory[slotIndex].stackSize <= decrementAmount)
            {
                itemStack = inventory[slotIndex];
                inventory[slotIndex] = null;
                return itemStack;
            } else
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
        final ItemStack itemStack = getStackInSlot(slotIndex);
        //noinspection VariableNotUsedInsideIf
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
        return slotIndex != OUTPUT_INVENTORY_INDEX && (slotIndex != FUEL_INVENTORY_INDEX || TileEntityFurnace.isItemFuel(itemStack));

    }

    @Override
    public void updateEntity()
    {
        final boolean isBurning = deviceCookTime > 0;

        updateDeviceCookTime();

        boolean sendUpdate = false;
        if (!worldObj.isRemote)
        {
            if (deviceCookTime == 0 && canAlloy())
                sendUpdate = didConsumeNextFuel();

            if (deviceCookTime > 0 && canAlloy())
            {
                if (didCookItem())
                    sendUpdate = true;
            } else
                itemCookTime = 0;

            if (isBurning != deviceCookTime > 0)
                sendUpdate = true;
        }

        if (sendUpdate)
            sendUpdate();
    }

    private void sendUpdate()
    {
        markDirty();
        isActive = deviceCookTime > 0;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, isActive ? 1 : 0);
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
    }

    private boolean didCookItem()
    {
        itemCookTime++;

        boolean sendUpdate = false;
        if (itemCookTime == 200)
        {
            itemCookTime = 0;
            alloy();
            sendUpdate = true;
        }
        return sendUpdate;
    }

    private boolean didConsumeNextFuel()
    {
        fuelBurnTime = TileEntityFurnace.getItemBurnTime(inventory[FUEL_INVENTORY_INDEX]);
        deviceCookTime = fuelBurnTime;

        boolean sendUpdate = false;
        if (deviceCookTime > 0)
        {
            sendUpdate = true;

            if (inventory[FUEL_INVENTORY_INDEX] != null)
            {
                inventory[FUEL_INVENTORY_INDEX].stackSize--;

                if (inventory[FUEL_INVENTORY_INDEX].stackSize == 0)
                {
                    inventory[FUEL_INVENTORY_INDEX] = inventory[FUEL_INVENTORY_INDEX].getItem().getContainerItem(inventory[FUEL_INVENTORY_INDEX]);
                }
            }
        }
        return sendUpdate;
    }

    private void updateDeviceCookTime()
    {
        if (deviceCookTime > 0)
        {
            deviceCookTime--;
        }
    }

    @Override
    public boolean receiveClientEvent(int eventId, int eventData)
    {
        if (eventId == 1)
        {
            isActive = eventData != 0;
            worldObj.func_147451_t(xCoord, yCoord, zCoord);
            return true;
        }
        return super.receiveClientEvent(eventId, eventData);
    }

    private boolean canAlloy()
    {
        if (inventory[INPUT_LEFT_INVENTORY_INDEX] == null || inventory[INPUT_RIGHT_INVENTORY_INDEX] == null)
            return false;

        final IAlloyResult output = AlloyManager.INSTANCE.getCupolaResult(inventory[INPUT_LEFT_INVENTORY_INDEX], inventory[INPUT_RIGHT_INVENTORY_INDEX]);
        if (!output.getItemStack().isPresent() || output.getConsumedA() > inventory[INPUT_LEFT_INVENTORY_INDEX].stackSize || output.getConsumedB() > inventory[INPUT_RIGHT_INVENTORY_INDEX].stackSize)
        {
            return false;
        }

        if (inventory[OUTPUT_INVENTORY_INDEX] == null)
        {
            return true;

        }

        if (!inventory[OUTPUT_INVENTORY_INDEX].isItemEqual(output.getItemStack().get()))
        {
            return false;
        }

        final int result = inventory[OUTPUT_INVENTORY_INDEX].stackSize + output.getItemStack().get().stackSize;
        return result <= getInventoryStackLimit() && result <= output.getItemStack().get().getMaxStackSize();
    }

    @SuppressWarnings("AssignmentToNull")
    private void alloy()
    {
        if (canAlloy())
        {
            final IAlloyResult output = AlloyManager.INSTANCE.getCupolaResult(inventory[INPUT_LEFT_INVENTORY_INDEX], inventory[INPUT_RIGHT_INVENTORY_INDEX]);
            if (output.getItemStack().isPresent())
            {
                addItemStackToOutput(output.getItemStack().get().copy());

                inventory[INPUT_RIGHT_INVENTORY_INDEX].stackSize -= output.getConsumedA();
                inventory[INPUT_LEFT_INVENTORY_INDEX].stackSize -= output.getConsumedB();

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
        final int maxStackSize = Math.min(getInventoryStackLimit(), itemStack.getMaxStackSize());

        if (inventory[OUTPUT_INVENTORY_INDEX] == null)
        {
            inventory[OUTPUT_INVENTORY_INDEX] = itemStack;
            return;
        }
        if (inventory[OUTPUT_INVENTORY_INDEX].isItemEqual(itemStack)
                && inventory[OUTPUT_INVENTORY_INDEX].stackSize < maxStackSize)
        {
            final int addedSize = Math.min(itemStack.stackSize, maxStackSize - inventory[OUTPUT_INVENTORY_INDEX].stackSize);
            itemStack.stackSize -= addedSize;
            inventory[OUTPUT_INVENTORY_INDEX].stackSize += addedSize;
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("inventory", inventory)
                .add("deviceCookTime", deviceCookTime)
                .add("fuelBurnTime", fuelBurnTime)
                .add("itemCookTime", itemCookTime)
                .add("isActive", isActive)
                .toString();
    }
}
