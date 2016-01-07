package mod.steamnsteel.client.gui;

import org.lwjgl.util.Rectangle;

public class GuiSubTexture extends GuiTexture {

    private final Rectangle subtextureBounds;

    public GuiSubTexture(GuiTexture baseTexture, Rectangle subtextureBounds)
    {
        super(baseTexture.getTextureLocation(), baseTexture.getWidth(), baseTexture.getHeight());
        this.subtextureBounds = subtextureBounds;
    }

    @Override
    public Rectangle getBounds()
    {
        return subtextureBounds;
    }
}
