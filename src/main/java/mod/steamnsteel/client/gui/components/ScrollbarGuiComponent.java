package mod.steamnsteel.client.gui.components;

import mod.steamnsteel.client.gui.GuiTexture;
import mod.steamnsteel.utility.SteamNSteelException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.util.Rectangle;

/**
 * Created by codew on 7/01/2016.
 */
public class ScrollbarGuiComponent extends GuiComponent
{
    private final TextureManager textureManager;
    private final GuiTexture texture;

    private static final Rectangle inactiveHandle = new Rectangle(176, 0, 12, 15);
    private static final Rectangle activeHandle = new Rectangle(inactiveHandle.getX() + inactiveHandle.getWidth(), 0, inactiveHandle.getWidth(), inactiveHandle.getHeight());

    private int minimumValue = 0;
    private int maximumValue = 100;
    private int currentValue = 0;

    public ScrollbarGuiComponent(TextureManager textureManager, GuiTexture texture)
    {
        super(new Rectangle(0, 0, inactiveHandle.getWidth(), inactiveHandle.getHeight()));
        this.textureManager = textureManager;
        this.texture = texture;
    }

    @Override
    public void drawComponent()
    {
        textureManager.bindTexture(texture.getTextureLocation());
        drawComponentTextureWithOffset(texture, inactiveHandle, 0, 0);
    }

    public int getMinimumValue()
    {
        return minimumValue;
    }

    public void setMinimumValue(int minimumValue)
    {
        if (minimumValue < maximumValue) {
            throw new SteamNSteelException("Attempt to set a scrollbar's maximum to less than it's minimum");
        }

        this.minimumValue = minimumValue;
        if (currentValue < minimumValue) {
            currentValue = minimumValue;
        }
    }

    public int getMaximumValue()
    {
        return maximumValue;
    }

    public void setMaximumValue(int maximumValue)
    {
        if (maximumValue < minimumValue) {
            throw new SteamNSteelException("Attempt to set a scrollbar's maximum to less than it's minimum");
        }

        this.maximumValue = maximumValue;
        if (currentValue > maximumValue) {
            currentValue = maximumValue;
        }
    }

    public int getCurrentValue()
    {
        return currentValue;
    }

    public void setCurrentValue(int currentValue)
    {
        if (currentValue < minimumValue){
            currentValue = minimumValue;
        }
        if (currentValue > maximumValue) {
            currentValue = maximumValue;
        }

        this.currentValue = currentValue;

    }
}
