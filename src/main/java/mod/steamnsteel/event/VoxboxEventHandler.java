package mod.steamnsteel.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.client.gui.VoxBoxGui;
import mod.steamnsteel.network.packet.VoxBoxPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class VoxboxEventHandler {

    @SubscribeEvent
    public void onInventoryInitPost(GuiScreenEvent.InitGuiEvent.Post event){
        if(event.gui instanceof GuiInventory){
            //TODO fix up the button and maybe change it's look and feel
            event.buttonList.add(new GuiButton(VoxBoxGui.BUTTON_ID, 0, 0, 20, 20, I18n.format("gui.button.voxbox")));
        }
    }

    @SubscribeEvent
    public void onButtonClickPre(GuiScreenEvent.ActionPerformedEvent.Pre event){
        if(event.gui instanceof GuiInventory){
            if(event.button.id == VoxBoxGui.BUTTON_ID){
                TheMod.packetChannel.sendToServer(new VoxBoxPacket());
                event.setCanceled(true);
            }
        }
    }


}
