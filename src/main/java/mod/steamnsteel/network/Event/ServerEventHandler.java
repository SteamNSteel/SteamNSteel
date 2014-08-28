package mod.steamnsteel.network.Event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.client.gui.ButtonVoxBox;
import mod.steamnsteel.client.gui.GUIVoxBox;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ServerEventHandler  {

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
    public void EntityJoinWorldEvent(EntityJoinWorldEvent event){
        Entity entity = event.entity;
        if(entity instanceof EntityPlayerMP){
            //MinecraftMod.networkWrapper.sendTo(new SSVoxBoxMessage(), (EntityPlayerMP) entity);
        }
        if(entity instanceof EntityPlayer && PropertyVoxBox.get((EntityPlayer) entity) == null){
            PropertyVoxBox.register((EntityPlayer) entity);
        }
    }

    @SubscribeEvent
    public void OnScreenPostInit(GuiScreenEvent.InitGuiEvent.Post event){
        if(event.gui instanceof GuiInventory){
            int height = 176;
            int width = 166;


            int posX = event.gui.mc.displayWidth;
            int posY = event.gui.mc.displayHeight;

            event.buttonList.add(new ButtonVoxBox(1000, height, width, 24, 24));
        }
    }

    @SubscribeEvent
    public void PreActionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event){
        if(event.gui instanceof GuiInventory){
            if(event.button.id == 1000 && event.button.enabled && event.button.visible){
                event.gui.mc.displayGuiScreen(new GUIVoxBox(event.gui.mc.thePlayer));
                event.setCanceled(true);
            }
        }
    }
}
