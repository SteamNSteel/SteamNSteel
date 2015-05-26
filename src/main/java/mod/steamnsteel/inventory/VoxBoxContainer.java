package mod.steamnsteel.inventory;

import com.google.common.base.Objects;
import mod.steamnsteel.inventory.slot.VoxBoxSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class VoxBoxContainer extends SteamNSteelContainer {
    private InventoryBasic inventoryBasic = new InventoryBasic("", false, 1);

    public VoxBoxContainer(InventoryPlayer playerInventory){
        addSlotToContainer(new VoxBoxSlot(inventoryBasic, 0, 79, 17));
        addPlayerInventory(playerInventory, 7, 167);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
        return null;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }

    @Override
    public boolean canDragIntoSlot(Slot slot) {
        return !(slot instanceof VoxBoxSlot);
    }

    //TODO clean up method
    @Override
    public ItemStack slotClick(int slot, int rightClick, int p_75144_3_, EntityPlayer player) { //Used for the ghost slot
        if (slot != -999 && slot == 0) //-999 is the exit opcode for slots and 0 is the only slot
            if (getSlot(slot).getHasStack()) {
                getSlot(slot).inventory.setInventorySlotContents(slot, null); //Clears the ghost item

                if(player.inventory.getItemStack() != null){
                    player.inventory.getItemStack().stackSize++;//Replace the item in the slot with the held item
                    return super.slotClick(slot, rightClick, p_75144_3_, player);
                }

                return player.inventory.getItemStack();
            } else {
                if(player.inventory.getItemStack() != null)
                    player.inventory.getItemStack().stackSize++;//Makes sure an items is not used up
                return super.slotClick(slot, rightClick, p_75144_3_, player);
            }

        return super.slotClick(slot, rightClick, p_75144_3_, player);
    }
}
