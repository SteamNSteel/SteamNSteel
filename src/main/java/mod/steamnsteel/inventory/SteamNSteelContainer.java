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

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

abstract class SteamNSteelContainer extends Container
{
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLUMNS = 9;

    private static boolean isSlotInRange(int slotIndex, int slotMin, int slotMax, boolean ascending)
    {
        return ascending ? slotIndex >= slotMin : slotIndex < slotMax;
    }

    private static boolean equalsIgnoreStackSize(ItemStack itemStack1, ItemStack itemStack2)
    {
        if (itemStack1 != null && itemStack2 != null)
        {
            if (Item.getIdFromItem(itemStack1.getItem()) - Item.getIdFromItem(itemStack2.getItem()) == 0)
            {
                //noinspection ObjectEquality
                if (itemStack1.getItem() == itemStack2.getItem())
                {
                    if (itemStack1.getItemDamage() == itemStack2.getItemDamage() &&
                            areItemStackTagsEqual(itemStack1, itemStack2)) return true;
                }
            }
        }

        return false;
    }

    private static boolean areItemStackTagsEqual(ItemStack itemStack1, ItemStack itemStack2)
    {
        if (itemStack1.hasTagCompound() && itemStack2.hasTagCompound())
        {
            if (ItemStack.areItemStackTagsEqual(itemStack1, itemStack2))
            {
                return true;
            }
        } else
        {
            return true;
        }
        return false;
    }

    private static ItemStack cloneItemStack(ItemStack itemStack, int stackSize)
    {
        final ItemStack clonedItemStack = itemStack.copy();
        clonedItemStack.func_190920_e(stackSize);
        return clonedItemStack;
    }

    @SuppressWarnings({"MethodWithMultipleLoops", "OverlyLongMethod", "OverlyComplexMethod"})
    @Override
    protected boolean mergeItemStack(ItemStack itemStack, int slotMin, int slotMax, boolean ascending)
    {
        boolean slotFound = false;

        if (itemStack.isStackable())
        {
            int currentSlotIndex = ascending ? slotMax - 1 : slotMin;
            while (itemStack.func_190916_E() > 0 && isSlotInRange(currentSlotIndex, slotMin, slotMax, ascending))
            {
                final Slot slot = (Slot) inventorySlots.get(currentSlotIndex);
                final ItemStack stackInSlot = slot.getStack();

                if (slot.isItemValid(itemStack) && equalsIgnoreStackSize(itemStack, stackInSlot))
                {
                    final int combinedStackSize = stackInSlot.func_190916_E() + itemStack.func_190916_E();
                    final int slotStackSizeLimit = Math.min(stackInSlot.getMaxStackSize(), slot.getSlotStackLimit());

                    if (combinedStackSize <= slotStackSizeLimit)
                    {
                        itemStack.func_190920_e(0);
                        stackInSlot.func_190920_e(combinedStackSize);
                        slot.onSlotChanged();
                        slotFound = true;
                    } else if (stackInSlot.func_190916_E() < slotStackSizeLimit)
                    {
                        itemStack.func_190918_g(slotStackSizeLimit - stackInSlot.func_190916_E());
                        stackInSlot.func_190920_e(slotStackSizeLimit);
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                }

                currentSlotIndex += ascending ? -1 : 1;
            }
        }

        if (itemStack.func_190916_E() > 0)
        {
            int currentSlotIndex = ascending ? slotMax - 1 : slotMin;

            while (isSlotInRange(currentSlotIndex, slotMin, slotMax, ascending))
            {
                final Slot slot = (Slot) inventorySlots.get(currentSlotIndex);
                final ItemStack stackInSlot = slot.getStack();

                if (slot.isItemValid(itemStack) && stackInSlot == null)
                {
                    slot.putStack(cloneItemStack(itemStack, Math.min(itemStack.func_190916_E(), slot.getSlotStackLimit())));
                    slot.onSlotChanged();

                    if (slot.getStack() != null)
                    {
                        itemStack.func_190918_g(slot.getStack().func_190916_E());
                        return true;
                    }
                }

                currentSlotIndex += ascending ? -1 : 1;
            }
        }

        return slotFound;
    }

    void addPlayerInventory(InventoryPlayer playerInventory, int xOffset, int yOffset)
    {
        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex)
        {
            addInventoryRowSlots(playerInventory, xOffset, yOffset, inventoryRowIndex);
        }

        addActionBarSlots(playerInventory, xOffset, yOffset);
    }

    private void addInventoryRowSlots(InventoryPlayer playerInventory, int xOffset, int yOffset, int rowIndex)
    {
        for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex)
        {
            //noinspection ObjectAllocationInLoop
            addSlotToContainer(new Slot(playerInventory, inventoryColumnIndex + rowIndex * 9 + 9, xOffset + inventoryColumnIndex * 18, yOffset + rowIndex * 18));
        }
    }

    private void addActionBarSlots(InventoryPlayer playerInventory, int xOffset, int yOffset)
    {
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < 9; ++actionBarSlotIndex)
        {
            //noinspection ObjectAllocationInLoop
            addSlotToContainer(new Slot(playerInventory, actionBarSlotIndex, xOffset + actionBarSlotIndex * 18, yOffset + 58));
        }
    }

    boolean didTransferStackInStandardSlot(int slotIndex, ItemStack slotItemStack, int indexFirstStdSlot)
    {
        if (slotIndex >= indexFirstStdSlot && slotIndex < inventorySlots.size() - 9)
        {
            if (!mergeItemStack(slotItemStack, inventorySlots.size() - 9, inventorySlots.size(), false))
            {
                return true;
            }
        } else if (slotIndex >= inventorySlots.size() - 9 && slotIndex < inventorySlots.size())
        {
            if (!mergeItemStack(slotItemStack, indexFirstStdSlot, inventorySlots.size() - 9, false))
            {
                return true;
            }
        }
        return false;
    }
}
