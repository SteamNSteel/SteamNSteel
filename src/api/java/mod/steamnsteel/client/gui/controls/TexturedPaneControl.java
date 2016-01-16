package mod.steamnsteel.client.gui.controls;

import mod.steamnsteel.client.gui.Control;
import mod.steamnsteel.client.gui.GuiRenderer;
import mod.steamnsteel.client.gui.GuiTexture;
import org.lwjgl.util.Rectangle;

/**
 * Created by codew on 7/01/2016.
 */
public class TexturedPaneControl extends Control
{
    private final GuiTexture texture;

    public TexturedPaneControl(GuiRenderer guiRenderer, Rectangle componentBounds, GuiTexture texture)
    {
        super(guiRenderer, componentBounds);
        this.texture = texture;
    }

    public TexturedPaneControl(GuiRenderer guiRenderer, int width, int height, GuiTexture texture)
    {
        super(guiRenderer, width, height);
        this.texture = texture;
    }

    @Override
    public void draw()
    {
        guiRenderer.drawComponentTexture(this, texture);
        super.draw();
    }
}
