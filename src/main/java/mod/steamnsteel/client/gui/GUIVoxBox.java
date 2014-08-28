package mod.steamnsteel.client.gui;

import mod.steamnsteel.client.gui.inventory.ContainerVoxBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Probook on 8/15/2014.
 */
public class GUIVoxBox extends GuiContainer{
    private EntityPlayer player;

    public GUIVoxBox(EntityPlayer player){
        super(new ContainerVoxBox(player, player.inventory));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

    }
}
