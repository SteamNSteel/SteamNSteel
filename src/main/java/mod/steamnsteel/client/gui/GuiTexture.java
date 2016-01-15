package mod.steamnsteel.client.gui;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.ReadableRectangle;
import org.lwjgl.util.Rectangle;

public class GuiTexture
{
    private final ResourceLocation textureLocation;
    private final int width;
    private final int height;
    private final ReadableRectangle bounds;

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

    public ReadableRectangle getBounds() {
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

