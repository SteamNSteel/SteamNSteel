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

import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class CupolaContainer extends SteamNSteelContainer
{
    private CupolaTE te;
    private int lastCookTime;               // How much longer the Cupola will burn
    private int lastBurnTime;               // The fuel value for the currently burning fuel
    private int lastItemCookTime;           // How long the current item has been "cooking"

    public CupolaContainer(InventoryPlayer inventoryPlayer, CupolaTE te)
    {
        this.te = te;

//        addSlotToContainer(new Slot(te, CupolaTE.FUEL_INVENTORY_INDEX, 56, 62));
//
//        addSlotToContainer(new Slot(te, CupolaTE.INPUT_LEFT_INVENTORY_INDEX, 56, 17));
//        addSlotToContainer(new Slot(te, CupolaTE.INPUT_RIGHT_INVENTORY_INDEX, 56, 17));
//
//        addSlotToContainer(new CupolaSlot(te, CupolaTE.OUTPUTINVENTORY_INDEX, 116, 35));

        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex)
        {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex)
            {
                addSlotToContainer(new Slot(inventoryPlayer, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 94 + inventoryRowIndex * 18));
            }
        }

        for (int actionBarSlotIndex = 0; actionBarSlotIndex < 9; ++actionBarSlotIndex)
        {
            addSlotToContainer(new Slot(inventoryPlayer, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 152));
        }
    }

    // @Override
    // public void addCraftingToCrafters(ICrafting iCrafting)
    // {
    //     super.addCraftingToCrafters(iCrafting);
    //     iCrafting.sendProgressBarUpdate(this, 0, this.tileEntityCalcinator.deviceCookTime);
    //     iCrafting.sendProgressBarUpdate(this, 1, this.tileEntityCalcinator.fuelBurnTime);
    //     iCrafting.sendProgressBarUpdate(this, 2, this.tileEntityCalcinator.itemCookTime);
    // }

//    @Override
//    public void detectAndSendChanges()
//    {
//        super.detectAndSendChanges();
//
//        for (Object crafter : this.crafters)
//        {
//            ICrafting icrafting = (ICrafting) crafter;
//
//            if (this.lastCookTime != this.tileEntityCalcinator.deviceCookTime)
//            {
//                icrafting.sendProgressBarUpdate(this, 0, this.tileEntityCalcinator.deviceCookTime);
//            }
//
//            if (this.lastBurnTime != this.tileEntityCalcinator.fuelBurnTime)
//            {
//                icrafting.sendProgressBarUpdate(this, 1, this.tileEntityCalcinator.fuelBurnTime);
//            }
//
//            if (this.lastItemCookTime != this.tileEntityCalcinator.itemCookTime)
//            {
//                icrafting.sendProgressBarUpdate(this, 2, this.tileEntityCalcinator.itemCookTime);
//            }
//        }
//
//        this.lastCookTime = this.tileEntityCalcinator.deviceCookTime;
//        this.lastBurnTime = this.tileEntityCalcinator.fuelBurnTime;
//        this.lastItemCookTime = this.tileEntityCalcinator.itemCookTime;
//    }

//    @Override
//    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex)
//    {
//        ItemStack itemStack = null;
//        Slot slot = (Slot) inventorySlots.get(slotIndex);
//
//        if (slot != null && slot.getHasStack())
//        {
//
//            ItemStack slotItemStack = slot.getStack();
//            itemStack = slotItemStack.copy();
//
//            /**
//             * If we are shift-clicking an item out of the Aludel's container,
//             * attempt to put it in the first available slot in the player's
//             * inventory
//             */
//            if (slotIndex < TileEntityCalcinator.INVENTORY_SIZE)
//            {
//                if (!this.mergeItemStack(slotItemStack, TileEntityCalcinator.INVENTORY_SIZE, inventorySlots.size(), false))
//                {
//                    return null;
//                }
//            }
//            else
//            {
//                /**
//                 * If the stack being shift-clicked into the Aludel's container
//                 * is a fuel, first try to put it in the fuel slot. If it cannot
//                 * be merged into the fuel slot, try to put it in the input
//                 * slot.
//                 */
//                if (TileEntityFurnace.isItemFuel(slotItemStack))
//                {
//                    if (!this.mergeItemStack(slotItemStack, TileEntityCalcinator.FUEL_INVENTORY_INDEX, TileEntityCalcinator.OUTPUT_LEFT_INVENTORY_INDEX, false))
//                    {
//                        return null;
//                    }
//                }
//
//                /**
//                 * Finally, attempt to put stack into the input slot
//                 */
//                else if (!this.mergeItemStack(slotItemStack, TileEntityCalcinator.INPUT_INVENTORY_INDEX, TileEntityCalcinator.OUTPUT_LEFT_INVENTORY_INDEX, false))
//                {
//                    return null;
//                }
//            }
//
//            if (slotItemStack.stackSize == 0)
//            {
//                slot.putStack(null);
//            }
//            else
//            {
//                slot.onSlotChanged();
//            }
//        }
//
//        return itemStack;
//    }

//    @SideOnly(Side.CLIENT)
//    public void updateProgressBar(int valueType, int updatedValue)
//    {
//        if (valueType == 0)
//        {
//            this.tileEntityCalcinator.deviceCookTime = updatedValue;
//        }
//
//        if (valueType == 1)
//        {
//            this.tileEntityCalcinator.fuelBurnTime = updatedValue;
//        }
//
//        if (valueType == 2)
//        {
//            this.tileEntityCalcinator.itemCookTime = updatedValue;
//        }
//    }
}
