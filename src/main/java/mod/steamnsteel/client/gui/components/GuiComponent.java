package mod.steamnsteel.client.gui.components;

import mod.steamnsteel.client.gui.GuiTexture;
import net.minecraft.client.gui.Gui;
import org.lwjgl.util.Rectangle;

/**
 * Created by codew on 6/01/2016.
 */
public abstract class GuiComponent extends Gui
{
    private final Rectangle componentBounds = new Rectangle();

    public GuiComponent(Rectangle componentBounds)
    {
        this.componentBounds.setBounds(componentBounds);
    }

    public void setLocation(int x, int y) {
        componentBounds.setLocation(x, y);
    }

    public abstract void drawComponent();

    public Rectangle getBounds() {
        return componentBounds;
    }

    protected void drawComponentTexture(GuiTexture texture, Rectangle componentSubtexture)
    {

        drawModalRectWithCustomSizedTexture(
                componentBounds.getX(), componentBounds.getY(),
                componentSubtexture.getX(), componentSubtexture.getY(),
                componentSubtexture.getWidth(), componentSubtexture.getHeight(),
                texture.getWidth(), texture.getHeight());
    }

    protected void drawComponentTextureWithOffset(GuiTexture texture, Rectangle componentSubtexture, int offsetX, int offsetY)
    {
        drawModalRectWithCustomSizedTexture(
                componentBounds.getX() + offsetX, componentBounds.getY() + offsetY,
                componentSubtexture.getX(), componentSubtexture.getY(),
                componentSubtexture.getWidth(), componentSubtexture.getHeight(),
                texture.getWidth(), texture.getHeight());
    }
}
