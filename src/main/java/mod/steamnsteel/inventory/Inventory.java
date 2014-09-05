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

package mod.steamnsteel.inventory;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import java.util.Map;

public class Inventory
{
    private static final String INVENTORY = "inventory";
    private static final String SLOT = "Slot";

    private final Map<Integer, ItemStack> inventory;
    private final int size;

    public Inventory(int size)
    {
        this.size = size;
        inventory = Maps.newHashMapWithExpectedSize(size);
    }

    public ItemStack decrStackSize(int slotIndex, int decrAmount)
    {
        if (inventory.containsKey(slotIndex))
        {
            final ItemStack slotStack = inventory.get(slotIndex);
            if (slotStack.stackSize <= decrAmount)
            {
                inventory.remove(slotIndex);
                return slotStack;
            } else
            {
                final ItemStack itemStack = slotStack.splitStack(decrAmount);

                if (slotStack.stackSize == 0)
                {
                    inventory.remove(slotIndex);
                }

                return itemStack;
            }
        }

        //noinspection ReturnOfNull
        return null;
    }

    public int getSize()
    {
        return size;
    }

    public ItemStack getStack(int slotIndex)
    {
        return inventory.get(slotIndex);
    }

    public ItemStack getStackOnClosing(int slotIndex)
    {
        if (hasStack(slotIndex))
        {
            setSlot(slotIndex, null);
        }
        return getStack(slotIndex);
    }

    @SuppressWarnings({"MethodMayBeStatic", "SameReturnValue"})
    public int getStackSizeMax() { return 64; }

    boolean hasStack(int slotIndex)
    {
        return inventory.containsKey(slotIndex);
    }

    public boolean isEmpty(int slotIndex) { return !hasStack(slotIndex); }

    public void readFromNBT(NBTTagCompound nbt)
    {
        final NBTTagList tagList = nbt.getTagList(INVENTORY, 10);
        inventory.clear();
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            final NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            final int slotIndex = tagCompound.getByte(SLOT);
            if (slotIndex >= 0 && slotIndex < size)
                inventory.put(slotIndex, ItemStack.loadItemStackFromNBT(tagCompound));
        }
    }

    public void clearSlot(int slotIndex)
    {
        setSlot(slotIndex, null);
    }

    public void setSlot(int slotIndex, ItemStack itemStack)
    {
        if (itemStack == null)
            inventory.remove(slotIndex);
        else
        {
            inventory.put(slotIndex, itemStack);
            if (itemStack.stackSize > getStackSizeMax())
                itemStack.stackSize = getStackSizeMax();
        }
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        final NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < size; ++currentIndex)
        {
            if (hasStack(currentIndex))
            {
                //noinspection ObjectAllocationInLoop
                final NBTTagCompound tagCompound = new NBTTagCompound();
                //noinspection NumericCastThatLosesPrecision
                tagCompound.setByte(SLOT, (byte) currentIndex);
                inventory.get(currentIndex).writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        nbt.setTag(INVENTORY, tagList);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("inventory", inventory)
                .add("size", size)
                .toString();
    }
}

