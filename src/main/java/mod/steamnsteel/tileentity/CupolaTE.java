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
import mod.steamnsteel.inventory.Inventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
    public static final int FUEL_INVENTORY_INDEX = 2;
    public static final int OUTPUT_INVENTORY_INDEX = 3;
    public static final int INVENTORY_SIZE = 4;

    private static final int[] slotsTop = {INPUT_LEFT_INVENTORY_INDEX, INPUT_RIGHT_INVENTORY_INDEX};
    private static final int[] slotsSides = {FUEL_INVENTORY_INDEX};
    private static final int[] slotsBottom = {OUTPUT_INVENTORY_INDEX, FUEL_INVENTORY_INDEX};

    private static final int COOK_TIME_PER_ITEM = 400;  // 200 is standard furnace rate, so cupola process 4 ops per coal

    private static final String IS_ACTIVE = "isActive";
    private static final String DEVICE_COOK_TIME = "deviceCookTime";
    private static final String FUEL_BURN_TIME = "fuelBurnTime";
    private static final String ITEM_COOK_TIME = "itemCookTime";

    private final Inventory inventory = new Inventory(INVENTORY_SIZE);
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
        return itemCookTime * scale / COOK_TIME_PER_ITEM;
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

        inventory.readFromNBT(nbt);

        deviceCookTime = nbt.getInteger(DEVICE_COOK_TIME);
        fuelBurnTime = nbt.getInteger(FUEL_BURN_TIME);
        itemCookTime = nbt.getInteger(ITEM_COOK_TIME);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setBoolean(IS_ACTIVE, isActive);

        inventory.writeToNBT(nbt);

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
        return inventory.getSize();
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return inventory.getStack(slotIndex);
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
        return inventory.getStackSizeMax();
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
        if (itemCookTime == COOK_TIME_PER_ITEM)
        {
            itemCookTime = 0;
            alloy();
            sendUpdate = true;
        }
        return sendUpdate;
    }

    private boolean didConsumeNextFuel()
    {
        final ItemStack fuelStack = inventory.getStack(FUEL_INVENTORY_INDEX);
        fuelBurnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
        deviceCookTime = fuelBurnTime;

        boolean sendUpdate = false;
        if (deviceCookTime > 0)
        {
            sendUpdate = true;

            if (inventory.isEmpty(FUEL_INVENTORY_INDEX))
            {
                fuelStack.stackSize--;

                if (fuelStack.stackSize == 0)
                {
                    inventory.setSlot(FUEL_INVENTORY_INDEX, fuelStack.getItem().getContainerItem(fuelStack));
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
        if (inventory.isEmpty(INPUT_LEFT_INVENTORY_INDEX) || inventory.isEmpty(INPUT_RIGHT_INVENTORY_INDEX))
            return false;

        final ItemStack leftStack = inventory.getStack(INPUT_LEFT_INVENTORY_INDEX);
        final ItemStack rightStack = inventory.getStack(INPUT_RIGHT_INVENTORY_INDEX);

        final IAlloyResult output = AlloyManager.INSTANCE.getCupolaResult(leftStack, rightStack);
        if (!output.getItemStack().isPresent() ||
                output.getConsumedA() > leftStack.stackSize || output.getConsumedB() > rightStack.stackSize)
        {
            return false;
        }

        if (inventory.isEmpty(OUTPUT_INVENTORY_INDEX))
            return true;

        final ItemStack outputStack = inventory.getStack(OUTPUT_INVENTORY_INDEX);
        if (!outputStack.isItemEqual(output.getItemStack().get()))
            return false;

        final int result = outputStack.stackSize + output.getItemStack().get().stackSize;
        return result <= getInventoryStackLimit() && result <= output.getItemStack().get().getMaxStackSize();
    }

    private void alloy()
    {
        if (canAlloy())
        {
            final ItemStack leftStack = inventory.getStack(INPUT_LEFT_INVENTORY_INDEX);
            final ItemStack rightStack = inventory.getStack(INPUT_RIGHT_INVENTORY_INDEX);

            final IAlloyResult output = AlloyManager.INSTANCE.getCupolaResult(leftStack, rightStack);
            if (output.getItemStack().isPresent())
            {
                addItemStackToOutput(output.getItemStack().get().copy());

                leftStack.stackSize -= output.getConsumedA();
                rightStack.stackSize -= output.getConsumedB();

                if (rightStack.stackSize <= 0)
                {
                    inventory.clearSlot(INPUT_RIGHT_INVENTORY_INDEX);
                }

                if (leftStack.stackSize <= 0)
                {
                    inventory.clearSlot(INPUT_LEFT_INVENTORY_INDEX);
                }
            }
        }
    }

    private void addItemStackToOutput(ItemStack itemStack)
    {
        final int maxStackSize = Math.min(getInventoryStackLimit(), itemStack.getMaxStackSize());

        if (inventory.isEmpty(OUTPUT_INVENTORY_INDEX))
        {
            inventory.setSlot(OUTPUT_INVENTORY_INDEX, itemStack);
            return;
        }
        final ItemStack outputStack = inventory.getStack(OUTPUT_INVENTORY_INDEX);
        if (outputStack.isItemEqual(itemStack)
                && outputStack.stackSize < maxStackSize)
        {
            final int addedSize = Math.min(itemStack.stackSize, maxStackSize - outputStack.stackSize);
            itemStack.stackSize -= addedSize;
            outputStack.stackSize += addedSize;
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
