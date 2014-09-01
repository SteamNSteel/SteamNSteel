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

import com.google.common.base.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.api.crafting.CraftingManager;
import mod.steamnsteel.inventory.slot.CupolaSlot;
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class CupolaContainer extends SteamNSteelContainer
{
    private final CupolaTE te;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemCookTime;

    public CupolaContainer(InventoryPlayer inventoryPlayer, CupolaTE te)
    {
        this.te = te;

        addSlotToContainer(new Slot(te, CupolaTE.FUEL_INVENTORY_INDEX, 42, 53));

        addSlotToContainer(new Slot(te, CupolaTE.INPUT_LEFT_INVENTORY_INDEX, 25, 17));
        addSlotToContainer(new Slot(te, CupolaTE.INPUT_RIGHT_INVENTORY_INDEX, 59, 17));

        addSlotToContainer(new CupolaSlot(te, CupolaTE.OUTPUT_INVENTORY_INDEX, 116, 35));

        addPlayerInventory(inventoryPlayer, 8, 84);
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting)
    {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, te.deviceCookTime);
        iCrafting.sendProgressBarUpdate(this, 1, te.fuelBurnTime);
        iCrafting.sendProgressBarUpdate(this, 2, te.itemCookTime);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return te.isUseableByPlayer(player);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (final Object crafter : crafters)
        {
            final ICrafting icrafting = (ICrafting) crafter;

            if (lastCookTime != te.deviceCookTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, te.deviceCookTime);
            }

            if (lastBurnTime != te.fuelBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, te.fuelBurnTime);
            }

            if (lastItemCookTime != te.itemCookTime)
            {
                icrafting.sendProgressBarUpdate(this, 2, te.itemCookTime);
            }
        }

        lastCookTime = te.deviceCookTime;
        lastBurnTime = te.fuelBurnTime;
        lastItemCookTime = te.itemCookTime;
    }

    @SuppressWarnings("ReturnOfNull")
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        ItemStack itemStack = null;
        final Slot slot = (Slot)inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {

            final ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();

            if (slotIndex == CupolaTE.OUTPUT_INVENTORY_INDEX)
            {
                if (!mergeItemStack(slotItemStack, CupolaTE.INVENTORY_SIZE, inventorySlots.size(), true))
                {
                    return null;
                }

                slot.onSlotChange(slotItemStack, itemStack);
            }
            else if (slotIndex > CupolaTE.FUEL_INVENTORY_INDEX)
            {
                final Optional<ItemStack> result1 = CraftingManager.alloyManager.get().getCupolaResult(slotItemStack,
                        te.getStackInSlot(CupolaTE.INPUT_RIGHT_INVENTORY_INDEX)).getItemStack();
                final Optional<ItemStack> result2 = CraftingManager.alloyManager.get().getCupolaResult(slotItemStack,
                        te.getStackInSlot(CupolaTE.INPUT_LEFT_INVENTORY_INDEX)).getItemStack();
                if (result1.isPresent() || result2.isPresent())
                {
                    if (!mergeItemStack(slotItemStack, CupolaTE.INPUT_LEFT_INVENTORY_INDEX, CupolaTE.FUEL_INVENTORY_INDEX, false))
                    {
                        return null;
                    }
                } else if (TileEntityFurnace.isItemFuel(slotItemStack))
                {
                    if (!mergeItemStack(slotItemStack, CupolaTE.FUEL_INVENTORY_INDEX, CupolaTE.OUTPUT_INVENTORY_INDEX, false))
                    {
                        return null;
                    }
                } else if (slotIndex >= CupolaTE.INVENTORY_SIZE && slotIndex < inventorySlots.size() - 9)
                {
                    if (!mergeItemStack(slotItemStack, inventorySlots.size() - 9, inventorySlots.size(), false))
                    {
                        return null;
                    }
                } else if (slotIndex >= inventorySlots.size() - 9 && slotIndex < inventorySlots.size())
                {
                    if (!mergeItemStack(slotItemStack, CupolaTE.INVENTORY_SIZE, inventorySlots.size() - 9, false))
                    {
                        return null;
                    }
                }
            }
            else if (!mergeItemStack(slotItemStack, CupolaTE.INVENTORY_SIZE, inventorySlots.size(), false))
            {
                return null;
            }

            if (slotItemStack.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (slotItemStack.stackSize == itemStack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, slotItemStack);
        }

        return itemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int valueType, int updatedValue)
    {
        if (valueType == 0)
        {
            te.deviceCookTime = updatedValue;
        }

        if (valueType == 1)
        {
            te.fuelBurnTime = updatedValue;
        }

        if (valueType == 2)
        {
            te.itemCookTime = updatedValue;
        }
    }
}
