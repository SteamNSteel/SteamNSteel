package mod.steamnsteel.client.gui.controls;

import mod.steamnsteel.client.gui.GuiRenderer;
import mod.steamnsteel.client.gui.GuiTexture;
import mod.steamnsteel.utility.SteamNSteelException;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.ReadableRectangle;

public class ScrollbarControl extends Control
{
    private final GuiTexture activeHandle;
    private final GuiTexture inactiveHandle;

    private int minimumValue = 0;
    private int maximumValue = 100;
    private int currentValue = 0;
    private int mouseOffset = 0;

    private GuiTexture currentTexture;

    public ScrollbarControl(GuiRenderer guiRenderer, GuiTexture activeHandle, GuiTexture inactiveHandle)
    {
        super(guiRenderer);
        this.activeHandle = activeHandle;
        this.inactiveHandle = inactiveHandle;
        this.currentTexture = inactiveHandle;
    }

    @Override
    public void draw()
    {
        guiRenderer.drawComponentTextureWithOffset(this, currentTexture, 0, 0);
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

    private int usableScrollHeight = 0;

    @Override
    protected void onResized(ReadableRectangle componentBounds) {
        usableScrollHeight = componentBounds.getHeight() - inactiveHandle.getBounds().getHeight();
    }

    @Override
    protected boolean onMouseClick(ReadablePoint point, int mouseButton) {
        if (mouseButton == 0) {
            currentTexture = activeHandle;
            mouseOffset = getHandleTop() - point.getY();
            captureMouse();
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMouseRelease(ReadablePoint point, int mouseButton) {
        if (mouseButton == 0) {
            currentTexture = inactiveHandle;
            releaseMouse();
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMouseDragged(ReadablePoint point, ReadablePoint delta, int mouseButton) {
        int newY = point.getY() - mouseOffset;
        if (newY < 0) newY = 0;
        if (newY > usableScrollHeight) newY = usableScrollHeight;

        currentValue = minimumValue + (newY * usableScrollHeight
        
        return true;
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

    private int getHandleTop() {
        int percentageLocation = (maximumValue - currentValue) / (maximumValue - minimumValue);
        return percentageLocation * usableScrollHeight + getBounds().getY();
    }
}
