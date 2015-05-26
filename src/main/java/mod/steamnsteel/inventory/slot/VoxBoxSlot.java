package mod.steamnsteel.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class VoxBoxSlot extends Slot{

    public VoxBoxSlot(IInventory inventory, int index, int x, int y){
        super(inventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        inventory.setInventorySlotContents(slotNumber, null);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean canTakeStack(EntityPlayer p_82869_1_) {
        return true;
    }

    @Override
    public void putStack(ItemStack stack) {
        if(stack != null && stack.getItem() != null)
            inventory.setInventorySlotContents(slotNumber, new ItemStack(stack.getItem(), 0));
    }


}
