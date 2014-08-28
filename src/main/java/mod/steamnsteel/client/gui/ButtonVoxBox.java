package mod.steamnsteel.client.gui;

import mod.steamnsteel.utility.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/**
 * Created by Probook on 8/15/2014.
 */
public class ButtonVoxBox extends GuiButton {
    public int id;

    public ButtonVoxBox(int id, int x, int y, int width, int height){
        super(id, x, y, width, height, "Test");
        this.id = id;
        this.visible = true;
        this.enabled = true;
    }

    @Override
    public void drawButton(Minecraft minecraft, int x, int y) {
        super.drawButton(minecraft, x, y);
    }
}
