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
import mod.steamnsteel.inventory.Inventory;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.Reference;
import mod.steamnsteel.library.Reference.BlockNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentTranslation;

public class RemnantRuinChestTE extends SteamNSteelTE implements IInventory, ITickable
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
    public boolean func_191420_l()
    {
        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            if (!inventory.getStack(i).func_190926_b()) {
                return false;
            }
        }
        return true;
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
    public ItemStack removeStackFromSlot(int index) {
        return inventory.getStackOnClosing(index);
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        inventory.setSlot(slotIndex, itemStack);
    }

    @Override
    public String getName()
    {
        return Reference.containerName(BlockNames.REMNANT_RUIN_CHEST);
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {

        return new TextComponentTranslation(this.getName(), new Object[0]);
        //return new TextComponentString(getName());
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
    public void openInventory(EntityPlayer player)
    {
        ++numUsingPlayers;
        world.addBlockEvent(getPos(), ModBlock.remnantRuinChest, 1, numUsingPlayers);
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        --numUsingPlayers;
        world.addBlockEvent(getPos(), ModBlock.remnantRuinChest, 1, numUsingPlayers);
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return true;
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
        for (int i = 0; i < inventory.getSize(); ++i) {
            inventory.clearSlot(i);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        inventory.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        inventory.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void update()
    {
        ++ticksSinceSync;
        final BlockPos pos = getPos();
        if (ticksSinceSync % 20 * 4 == 0)
        {
            world.addBlockEvent(pos, ModBlock.remnantRuinChest, 1, numUsingPlayers);
        }

        prevLidAngle = lidAngle;

        if (numUsingPlayers != 0 && lidAngle == 0.0F)
        {
            world.playSound(null, pos.getX(), pos.getY() + 0.5D, pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

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
                world.playSound(null, pos.getX(), pos.getY() + 0.5D, pos.getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
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
