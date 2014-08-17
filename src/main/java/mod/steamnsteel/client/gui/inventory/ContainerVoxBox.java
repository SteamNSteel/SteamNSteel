package mod.steamnsteel.client.gui.inventory;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Probook on 8/15/2014.
 */
public class ContainerVoxBox extends Container{
    public VoxBoxInventory innerInventory = new VoxBoxInventory();

    public ContainerVoxBox(EntityPlayer player, InventoryPlayer inventoryPlayer){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 9; j++){
                //Add inventory slots
                if(i == 3){
                    //add hotbar slots
                }
            }
        }

        this.addSlotToContainer(new Slot(innerInventory, 0, 85, 20));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        ItemStack itemStack = innerInventory.getStackInSlot(0);
        if(itemStack != null) {
            World world = player.getEntityWorld();
            world.spawnEntityInWorld(new EntityItem(world, world.rand.nextFloat() - world.rand.nextFloat(), world.rand.nextFloat(), world.rand.nextFloat() - world.rand.nextFloat(), itemStack));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        Slot slot = (Slot) getSlot(slotID);
        ItemStack itemStack = null;

        if(slot != null && slot.getHasStack()){
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
        }

        return itemStack;
    }

    private class VoxBoxInventory implements IInventory{
        private ItemStack[] inventory = new ItemStack[1];
        private final String NAME = "voxboxgui";

        @Override
        public int getSizeInventory() {
            return inventory.length;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return inventory[slot];
        }

        @Override
        public ItemStack decrStackSize(int slot, int amount) {
            ItemStack stack = getStackInSlot(slot);
            stack.stackSize -= amount;
            return stack;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int slot) {
            return getStackInSlot(slot);
        }

        @Override
        public void setInventorySlotContents(int slot, ItemStack itemstack) {
            this.inventory[slot] = itemstack;

            if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
            {
                itemstack.stackSize = this.getInventoryStackLimit();
            }

            this.markDirty();
        }

        @Override
        public String getInventoryName() {
            return NAME;
        }

        @Override
        public boolean hasCustomInventoryName() {
            return NAME.length() > 0;
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }

        @Override
        public void markDirty() {
            for (int i = 0; i < this.getSizeInventory(); ++i)
            {
                if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize == 0)
                    this.setInventorySlotContents(i, null);
            }
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
            return true;
        }

        @Override
        public void openInventory() {

        }

        @Override
        public void closeInventory() {

        }

        @Override
        public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
            return true;
        }
    }
}
