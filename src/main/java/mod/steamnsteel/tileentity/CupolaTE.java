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
import com.google.common.base.Optional;
import mod.steamnsteel.library.Reference;
import mod.steamnsteel.library.Reference.BlockNames;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
import net.minecraft.tileentity.TileEntityFurnace;

@SuppressWarnings("ClassWithTooManyMethods")
public class CupolaTE extends SteamNSteelTE implements ISidedInventory, ITickable
{
    private Optional<AxisAlignedBB> renderBounds = Optional.absent();

    public static final int INPUT_LEFT = 0;
    public static final int INPUT_RIGHT = 1;
    public static final int INPUT_FUEL = 2;
    public static final int OUTPUT = 3;
    public static final int INVENTORY_SIZE = 4;

    private static final int[] slotsTop = {INPUT_LEFT, INPUT_RIGHT};
    private static final int[] slotsSides = {INPUT_FUEL};
    private static final int[] slotsBottom = {OUTPUT, INPUT_FUEL};

    private static final int COOK_TIME_PER_ITEM = 400;  // 200 is standard furnace rate, so cupola process 4 ops per coal

    private static final String IS_ACTIVE = "isActive";
    private static final String IS_SLAVE = "isSlave";
    private static final String DEVICE_COOK_TIME = "deviceCookTime";
    private static final String FUEL_BURN_TIME = "fuelBurnTime";
    private static final String ITEM_COOK_TIME = "itemCookTime";

    private final Inventory inventory = new Inventory(INVENTORY_SIZE);
    private Optional<Inventory> masterInventory = Optional.absent(); // the inventory of the block below, if this block is a slave
    private int deviceCookTime;
    private int fuelBurnTime;
    private int itemCookTime;
    private boolean isActive = false;
    private boolean isSlave = false;

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

    public boolean isSlave()
    {
        return isSlave;
    }

    public void setSlave()
    {
        isSlave = true;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new SPacketUpdateTileEntity(getPos(), 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        isSlave = nbt.getBoolean(IS_SLAVE);
        if (isSlave) return;

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

        nbt.setBoolean(IS_SLAVE, isSlave);
        if (isSlave) return;

        nbt.setBoolean(IS_ACTIVE, isActive);

        inventory.writeToNBT(nbt);

        nbt.setInteger(DEVICE_COOK_TIME, deviceCookTime);
        nbt.setInteger(FUEL_BURN_TIME, fuelBurnTime);
        nbt.setInteger(ITEM_COOK_TIME, itemCookTime);
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        switch (side)
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
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return index == OUTPUT;
    }

    @Override
    public int getSizeInventory()
    {
        return getTargetInventory().getSize();
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return getTargetInventory().getStack(slotIndex);
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrAmount)
    {
        return getTargetInventory().decrStackSize(slotIndex, decrAmount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slotIndex)
    {
        return getTargetInventory().getStackOnClosing(slotIndex);
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        getTargetInventory().setSlot(slotIndex, itemStack);
    }

    @Override
    public String getName() {
        return Reference.containerName(BlockNames.CUPOLA);
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    public int getInventoryStackLimit()
    {
        return getTargetInventory().getStackSizeMax();
    }

    /**
     * @return In the case of slave blocks, return the inventory of the block below
     */
    private Inventory getTargetInventory()
    {
        if (!isSlave) return inventory;

        // Lazy initialization of masterInventory
        if (!masterInventory.isPresent())
        {
            final CupolaTE te = (CupolaTE) worldObj.getTileEntity(getPos().down());
            masterInventory = Optional.of(te.inventory);
        }
        return masterInventory.get();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        // noop
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        // noop
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return slotIndex != OUTPUT && (slotIndex != INPUT_FUEL || TileEntityFurnace.isItemFuel(itemStack));

    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = inventory.getSize() - 1; i >= 0; --i) {
            inventory.clearSlot(i);
        }
    }

    @Override
    public void update()
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
        worldObj.addBlockEvent(getPos(), getBlockType(), 1, isActive ? 1 : 0);
        worldObj.notifyNeighborsOfStateChange(getPos(), getBlockType());
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
        final ItemStack fuelStack = inventory.getStack(INPUT_FUEL);
        fuelBurnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
        deviceCookTime = fuelBurnTime;

        boolean sendUpdate = false;
        if (deviceCookTime > 0)
        {
            sendUpdate = true;
            fuelStack.stackSize--;

            if (fuelStack.stackSize == 0)
            {
                inventory.setSlot(INPUT_FUEL, fuelStack.getItem().getContainerItem(fuelStack));
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
            worldObj.checkLight(getPos());
            return true;
        }
        return super.receiveClientEvent(eventId, eventData);
    }

    private boolean canAlloy()
    {
        if (inventory.isEmpty(INPUT_LEFT) || inventory.isEmpty(INPUT_RIGHT))
            return false;

        final ItemStack leftStack = inventory.getStack(INPUT_LEFT);
        final ItemStack rightStack = inventory.getStack(INPUT_RIGHT);

        final IAlloyResult output = AlloyManager.INSTANCE.getCupolaResult(leftStack, rightStack);
        if (!output.getItemStack().isPresent() ||
                output.getConsumedA() > leftStack.stackSize || output.getConsumedB() > rightStack.stackSize)
        {
            return false;
        }

        if (inventory.isEmpty(OUTPUT))
            return true;

        final ItemStack outputStack = inventory.getStack(OUTPUT);
        if (!outputStack.isItemEqual(output.getItemStack().get()))
            return false;

        final int result = outputStack.stackSize + output.getItemStack().get().stackSize;
        return result <= getInventoryStackLimit() && result <= output.getItemStack().get().getMaxStackSize();
    }

    private void alloy()
    {
        if (canAlloy())
        {
            final ItemStack leftStack = inventory.getStack(INPUT_LEFT);
            final ItemStack rightStack = inventory.getStack(INPUT_RIGHT);

            final IAlloyResult output = AlloyManager.INSTANCE.getCupolaResult(leftStack, rightStack);
            if (output.getItemStack().isPresent())
            {
                addItemStackToOutput(output.getItemStack().get().copy());

                leftStack.stackSize -= output.getConsumedA();
                rightStack.stackSize -= output.getConsumedB();

                if (rightStack.stackSize <= 0)
                {
                    inventory.clearSlot(INPUT_RIGHT);
                }

                if (leftStack.stackSize <= 0)
                {
                    inventory.clearSlot(INPUT_LEFT);
                }
            }
        }
    }

    private void addItemStackToOutput(ItemStack itemStack)
    {
        final int maxStackSize = Math.min(getInventoryStackLimit(), itemStack.getMaxStackSize());

        if (inventory.isEmpty(OUTPUT))
        {
            inventory.setSlot(OUTPUT, itemStack);
            return;
        }
        final ItemStack outputStack = inventory.getStack(OUTPUT);
        if (outputStack.isItemEqual(itemStack)
                && outputStack.stackSize < maxStackSize)
        {
            final int addedSize = Math.min(itemStack.stackSize, maxStackSize - outputStack.stackSize);
            itemStack.stackSize -= addedSize;
            outputStack.stackSize += addedSize;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        if (!isSlave){
            if (!renderBounds.isPresent())
            {
                final BlockPos pos = getPos();
                renderBounds = Optional.of(new AxisAlignedBB(
                        pos.getX() - 1,
                        pos.getY(),
                        pos.getZ() - 1,
                        pos.getX() + 1,
                        pos.getY() + 2,
                        pos.getZ() + 1));
            }

            return renderBounds.get();
        }

        return super.getRenderBoundingBox();
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("inventory", inventory)
                .add("masterInventory", masterInventory)
                .add("deviceCookTime", deviceCookTime)
                .add("fuelBurnTime", fuelBurnTime)
                .add("itemCookTime", itemCookTime)
                .add("isActive", isActive)
                .add("isSlave", isSlave)
                .add("renderBounds", renderBounds)
                .toString();
    }
}
