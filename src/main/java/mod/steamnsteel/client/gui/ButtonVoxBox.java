package mod.steamnsteel.client.gui;

import mod.steamnsteel.utility.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/**
 * Created by Probook on 8/15/2014.
 */
public class ButtonVoxBox extends GuiButton {
    public int id;

    public ButtonVoxBox(int id){
        super(id, 100, 100, 24, 32, "Test");
        this.id = id;
        Logger.info("%s", "ButtonVoxBox made");
        this.visible = true;
        this.enabled = true;
        this.zLevel = 100.0F;
    }

    @Override
    public void drawButton(Minecraft minecraft, int x, int y) {
        super.drawButton(minecraft, x, y);
        Logger.info("%s", "Drawing at " +this.xPosition + " " + this.yPosition + "  with zLevel: " + this.zLevel + "    " + x + " " + y);
    }
}
