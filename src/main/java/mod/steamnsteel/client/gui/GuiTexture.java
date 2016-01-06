package mod.steamnsteel.client.gui;

import net.minecraft.util.ResourceLocation;

/**
 * Created by codew on 6/01/2016.
 */
public class GuiTexture
{
    private final ResourceLocation textureLocation;
    private final int width;
    private final int height;

    public GuiTexture(ResourceLocation textureLocation, int width, int height)
    {
        this.textureLocation = textureLocation;
        this.width = width;
        this.height = height;
    }

    public ResourceLocation getTextureLocation()
    {
        return textureLocation;
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
