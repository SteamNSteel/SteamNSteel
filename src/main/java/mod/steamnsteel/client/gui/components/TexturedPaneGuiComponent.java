package mod.steamnsteel.client.gui.components;

import mod.steamnsteel.client.gui.GuiRenderer;
import mod.steamnsteel.client.gui.GuiTexture;
import org.lwjgl.util.Rectangle;

/**
 * Created by codew on 7/01/2016.
 */
public class TexturedPaneGuiComponent extends GuiComponent
{
    private final GuiTexture texture;

    public TexturedPaneGuiComponent(GuiRenderer guiRenderer, Rectangle componentBounds, GuiTexture texture)
    {
        super(guiRenderer, componentBounds);
        this.texture = texture;
    }

    public TexturedPaneGuiComponent(GuiRenderer guiRenderer, int width, int height, GuiTexture texture)
    {
        super(guiRenderer, width, height);
        this.texture = texture;
    }

    @Override
    public void drawComponent()
    {
        guiRenderer.drawComponentTexture(this, texture);
        super.drawComponent();
    }
}
