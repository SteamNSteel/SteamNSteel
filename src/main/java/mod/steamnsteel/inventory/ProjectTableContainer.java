package mod.steamnsteel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Created by codew on 5/01/2016.
 */
public class ProjectTableContainer extends SteamNSteelContainer{

    /** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 1, 32);
    public IInventory craftResult = new InventoryCraftResult();
    private World worldObj;
    /** Position of the workbench */
    private BlockPos pos;

    public ProjectTableContainer(InventoryPlayer playerInventory) {

        addPlayerInventory(playerInventory, 8, 145);
        addSlotToContainer(new ProjectTableCraftingSlot(playerInventory.player, craftMatrix, craftResult, 0));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    class ProjectTableCraftingSlot extends SlotCrafting
    {
        private final EntityPlayer player;
        private final InventoryCrafting craftMatrix;

        public ProjectTableCraftingSlot(EntityPlayer player, InventoryCrafting craftingMaterials, IInventory craftingOutput, int slotIndex)
        {
            super(player, craftingMaterials, craftingOutput, slotIndex, 0, 0);
            this.player = player;
            craftMatrix = craftingMaterials;
        }

        public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
        {
            //FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, ProjectTableContainer.this.craftMatrix);
            onCrafting(stack);
        }
    }
}
