package mod.steamnsteel.tileentity;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.container.PlotoniumChest;
import mod.steamnsteel.inventory.Inventory;
import mod.steamnsteel.library.ModBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class PlotoniumChestTE extends TileEntity implements IInventory {
    public static final int INVENTORY_SIZE = 27;
    private final Inventory inventory = new Inventory(INVENTORY_SIZE);

    public float lidAngle;
    public float prevLidAngle;

    public int numUsingPlayers;
    private int ticksSinceSync;

    public static String containerName(String name)
    {
        return "container." + TheMod.MOD_ID + ':' + name;
    }

    @Override
    public int getSizeInventory()
    {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        ItemStack item = inventory.getStack(slot);
        this.markDirty();
        return item;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrAmount)
    {
        ItemStack item = inventory.decrStackSize(slotIndex, decrAmount);
        this.markDirty();
        return item;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        return inventory.getStackOnClosing(slotIndex);
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        inventory.setSlot(slotIndex,itemStack);
        this.markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return containerName(PlotoniumChest.NAME);
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
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
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        inventory.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        inventory.readFromNBT(nbt);
    }

    @Override
    public void openInventory()
    {
        ++numUsingPlayers;
        worldObj.addBlockEvent(xCoord,yCoord,zCoord, ModBlock.chestPlotonium, 1, numUsingPlayers);
    }

    @Override
    public void closeInventory()
    {
        --numUsingPlayers;
        worldObj.addBlockEvent(xCoord,yCoord,zCoord, ModBlock.chestPlotonium, 1, numUsingPlayers);
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return true;
    }

    @Override
    public boolean receiveClientEvent(int eventID, int numUsingPlayers)
    {
        if (eventID == 1)
        {
            this.numUsingPlayers = numUsingPlayers;
            return true;
        }
        else
        {
            return super.receiveClientEvent(eventID, numUsingPlayers);
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (++ticksSinceSync % 20 * 4 == 0)
        {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, ModBlock.chestPlotonium, 1, numUsingPlayers);
        }

        prevLidAngle = lidAngle;
        float angleIncrement = 0.1F;

        if (numUsingPlayers != 0 && lidAngle == 0.0F)
            worldObj.playSoundEffect(xCoord, yCoord +0.5F, zCoord, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);

        if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F)
        {
            if (numUsingPlayers > 0)
                lidAngle += angleIncrement;

            if (numUsingPlayers == 0)
                lidAngle -= angleIncrement;

            if (lidAngle < 0)
                lidAngle = 0.0F;

            if (lidAngle > 1.0F)
                lidAngle = 1.0F;

            if (lidAngle < 0.5 && prevLidAngle > 0.5){
                worldObj.playSoundEffect(xCoord, yCoord +0.5F, zCoord, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

        }
    }

}
