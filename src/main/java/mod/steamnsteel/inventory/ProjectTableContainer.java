package mod.steamnsteel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by codew on 5/01/2016.
 */
public class ProjectTableContainer extends SteamNSteelContainer{
    public ProjectTableContainer(InventoryPlayer inventoryPlayer) {
        addPlayerInventory(inventoryPlayer, 8, 84);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
