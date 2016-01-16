package mod.steamnsteel.client.gui.controls;

import mod.steamnsteel.client.gui.Control;
import mod.steamnsteel.client.gui.GuiRenderer;
import mod.steamnsteel.client.gui.events.IButtonPressedEventListener;
import mod.steamnsteel.client.gui.events.ICurrentValueChangedEventListener;
import mod.steamnsteel.utility.log.Logger;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ButtonControl extends Control
{
    public ButtonControl(GuiRenderer guiRenderer)
    {
        super(guiRenderer);
    }

    public ButtonControl(GuiRenderer guiRenderer, Rectangle componentBounds)
    {
        super(guiRenderer, componentBounds);
    }

    public ButtonControl(GuiRenderer guiRenderer, int width, int height)
    {
        super(guiRenderer, width, height);
    }

    @Override
    protected boolean onMouseRelease(ReadablePoint point, int mouseButton)
    {
        if (mouseButton == 0)
        {
            onButtonPressedInternal();
            return true;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////
    // Button Pressed Event Handling
    /////////////////////////////////////////////////////////////////////////////

    private void onButtonPressedInternal() {
        onButtonPressed();

        fireButtonPressedEvent();
    }

    protected void onButtonPressed() {
    }

    private void fireButtonPressedEvent()
    {
        for (final IButtonPressedEventListener currentValueChangedEventListener : buttonPressedEventListeners)
        {
            try {
                currentValueChangedEventListener.onButtonPressed(this);
            } catch (Exception e) {
                Logger.warning("Exception in an ICurrentValueChangedEventListener %s", e);
            }
        }
    }

    List<IButtonPressedEventListener> buttonPressedEventListeners = new ArrayList<>();

    @SuppressWarnings("unused")
    public void addOnButtonPressedEventListener(IButtonPressedEventListener listener) {
        buttonPressedEventListeners.add(listener);
    }
    @SuppressWarnings("unused")
    public void removeOnButtonPressedEventListener(IButtonPressedEventListener listener) {
        buttonPressedEventListeners.remove(listener);
    }
}
