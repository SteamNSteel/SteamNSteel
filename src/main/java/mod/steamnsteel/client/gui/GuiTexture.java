package mod.steamnsteel.client.gui;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

/**
 * Created by codew on 6/01/2016.
 */
public class GuiTexture
{
    private final ResourceLocation textureLocation;
    private final int width;
    private final int height;
    private final Rectangle bounds;

    public GuiTexture(ResourceLocation textureLocation, int width, int height)
    {
        this.textureLocation = textureLocation;
        bounds = new Rectangle(0, 0, width, height);
        this.width = width;
        this.height = height;
    }

    public ResourceLocation getTextureLocation()
    {
        return textureLocation;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}

class GuiSubTexture extends GuiTexture {

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
