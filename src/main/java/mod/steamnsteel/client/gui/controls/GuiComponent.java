package mod.steamnsteel.client.gui.controls;

import mod.steamnsteel.client.gui.GuiRenderer;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GuiComponent
{
    private final Rectangle componentBounds = new Rectangle();
    protected final GuiRenderer guiRenderer;
    private GuiComponent parent = null;
    private final List<GuiComponent> children = new ArrayList<>(10);

    public GuiComponent(GuiRenderer guiRenderer, Rectangle componentBounds)
    {
        this.guiRenderer = guiRenderer;
        this.componentBounds.setBounds(componentBounds);
    }
    public GuiComponent(GuiRenderer guiRenderer, int width, int height) {
        this(guiRenderer, new Rectangle(0, 0, width, height));
    }


    public void setLocation(int x, int y) {
        componentBounds.setLocation(x, y);
    }

    public void drawComponent() {
        for (final GuiComponent child : children)
        {
            child.drawComponent();
        }
    }

    public Rectangle getBounds() {
        return componentBounds;
    }

    public void addChild(GuiComponent child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(GuiComponent child) {
        children.remove(child);
        child.setParent(null);
    }

    public GuiComponent getParent() {
        return parent;
    }

    public void setParent(GuiComponent parent)
    {
        this.parent = parent;
    }

    public boolean mouseClicked(ReadablePoint point, final int mouseButton)
    {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(GuiComponent child, ReadablePoint localPoint) {
                return child.mouseClicked(localPoint, mouseButton);
            }

            @Override
            public boolean checkCurrent(Point point) {
                return onMouseClickInternal(point, mouseButton);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseReleased(ReadablePoint point, final int mouseButton)
    {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(GuiComponent child, ReadablePoint localPoint) {
                return child.mouseReleased(localPoint, mouseButton);
            }

            @Override
            public boolean checkCurrent(Point point) {
                return onMouseReleasedInternal(point, mouseButton);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    private boolean checkMouseBoundsAndPropagate(ReadablePoint point, IMouseCallback callback) {
        final Rectangle realControlBounds = new Rectangle(componentBounds);
        realControlBounds.translate(GuiRenderer.getControlLocation(this));

        Point localPoint = new Point();
        if (realControlBounds.contains(point)) {
            localPoint.setLocation(point);
            localPoint.untranslate(realControlBounds);

            boolean handled = false;
            for (final GuiComponent child : children)
            {
                if (callback.checkChild(child, localPoint)) {
                    handled = true;
                    break;
                }
            }

            if (!handled) {
                handled = callback.checkCurrent(localPoint);
            }
            return handled;
        }
        return false;
    }

    private interface IMouseCallback {
        boolean checkChild(GuiComponent child, ReadablePoint localPoint);

        boolean checkCurrent(Point point);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Internal event handling
    /////////////////////////////////////////////////////////////////////////////
    private boolean onMouseClickInternal(ReadablePoint point, int mouseButton)
    {
        //TODO: Handle dragging
        return onMouseClick(point, mouseButton);
    }

    private boolean onMouseReleasedInternal(ReadablePoint point, int mouseButton) {
        //TODO: Handle Dragging
        return onMouseRelease(point, mouseButton);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Events for subclasses
    /////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseClick(ReadablePoint point, int mouseButton)
    {
        return false;
    }

    protected boolean onMouseRelease(ReadablePoint point, int mouseButton) {
        return false;
    }
}
