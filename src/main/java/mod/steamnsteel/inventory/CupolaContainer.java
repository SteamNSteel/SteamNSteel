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
import com.google.common.base.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

        addSlotToContainer(new Slot(te, CupolaTE.INPUT_FUEL, 42, 53));

        addSlotToContainer(new Slot(te, CupolaTE.INPUT_LEFT, 25, 17));
        addSlotToContainer(new Slot(te, CupolaTE.INPUT_RIGHT, 59, 17));

        addSlotToContainer(new CupolaSlot(te, CupolaTE.OUTPUT, 116, 35));

        addPlayerInventory(inventoryPlayer, 8, 84);
    }

    @Override
    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendProgressBarUpdate(this, 0, te.getDeviceCookTime());
        listener.sendProgressBarUpdate(this, 1, te.getFuelBurnTime());
        listener.sendProgressBarUpdate(this, 2, te.getItemCookTime());
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

            if (lastCookTime != te.getDeviceCookTime())
            {
                icrafting.sendProgressBarUpdate(this, 0, te.getDeviceCookTime());
            }

            if (lastBurnTime != te.getFuelBurnTime())
            {
                icrafting.sendProgressBarUpdate(this, 1, te.getFuelBurnTime());
            }

            if (lastItemCookTime != te.getItemCookTime())
            {
                icrafting.sendProgressBarUpdate(this, 2, te.getItemCookTime());
            }
        }

        lastCookTime = te.getDeviceCookTime();
        lastBurnTime = te.getFuelBurnTime();
        lastItemCookTime = te.getItemCookTime();
    }

    @SuppressWarnings({"ReturnOfNull", "MethodWithMoreThanThreeNegations", "OverlyComplexMethod"})
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        final Slot slot = (Slot) inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack())
            return null;

        final ItemStack slotItemStack = slot.getStack();
        final ItemStack itemStack = slotItemStack.copy();

        if (slotIndex == CupolaTE.OUTPUT)
        {
            if (mergeItemStack(slotItemStack, CupolaTE.INVENTORY_SIZE, inventorySlots.size(), true))
                slot.onSlotChange(slotItemStack, itemStack);
            else
                return null;
        } else if (slotIndex > CupolaTE.INPUT_FUEL)
        {
            final Optional<ItemStack> result1 = CraftingManager.alloyManager.get().getCupolaResult(slotItemStack,
                    te.getStackInSlot(CupolaTE.INPUT_RIGHT)).getItemStack();
            final Optional<ItemStack> result2 = CraftingManager.alloyManager.get().getCupolaResult(slotItemStack,
                    te.getStackInSlot(CupolaTE.INPUT_LEFT)).getItemStack();
            if (result1.isPresent() || result2.isPresent())
            {
                if (!mergeItemStack(slotItemStack, CupolaTE.INPUT_LEFT, CupolaTE.INPUT_FUEL, false))
                    return null;
            } else if (TileEntityFurnace.isItemFuel(slotItemStack))
            {
                if (!mergeItemStack(slotItemStack, CupolaTE.INPUT_FUEL, CupolaTE.OUTPUT, false))
                    return null;
            } else if (didTransferStackInStandardSlot(slotIndex, slotItemStack, CupolaTE.INVENTORY_SIZE)) return null;
        } else if (!mergeItemStack(slotItemStack, CupolaTE.INVENTORY_SIZE, inventorySlots.size(), false))
        {
            return null;
        }

        if (slotItemStack.stackSize == 0)
            slot.putStack(null);
        else
            slot.onSlotChanged();

        if (slotItemStack.stackSize == itemStack.stackSize)
            return null;

        slot.onPickupFromSlot(player, slotItemStack);

        return itemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int valueType, int updatedValue)
    {
        if (valueType == 0)
        {
            te.setDeviceCookTime(updatedValue);
        }

        if (valueType == 1)
        {
            te.setFuelBurnTime(updatedValue);
        }

        if (valueType == 2)
        {
            te.setItemCookTime(updatedValue);
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("te", te)
                .add("lastCookTime", lastCookTime)
                .add("lastBurnTime", lastBurnTime)
                .add("lastItemCookTime", lastItemCookTime)
                .toString();
    }
}
