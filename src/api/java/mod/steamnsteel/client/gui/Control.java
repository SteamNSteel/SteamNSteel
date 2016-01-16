package mod.steamnsteel.client.gui;

import mod.steamnsteel.client.gui.controls.MouseCapture;
import org.lwjgl.util.*;

import java.util.ArrayList;
import java.util.List;

public class Control
{
    private final Rectangle componentBounds = new Rectangle();
    protected final GuiRenderer guiRenderer;
    private Control parent = null;
    private final List<Control> children = new ArrayList<>(10);

    public Control(GuiRenderer guiRenderer) {
        this(guiRenderer, new Rectangle());
    }

    public Control(GuiRenderer guiRenderer, Rectangle componentBounds)
    {
        this.guiRenderer = guiRenderer;
        this.componentBounds.setBounds(componentBounds);
        onResizeInternal();
    }
    public Control(GuiRenderer guiRenderer, int width, int height) {
        this(guiRenderer, new Rectangle(0, 0, width, height));
    }


    public void setLocation(int x, int y)
    {
        componentBounds.setLocation(x, y);
        onResizeInternal();
    }
    public void setLocation(ReadablePoint point) {
        componentBounds.setLocation(point);
        onResizeInternal();
    }
    public void setSize(int width, int height) {
        componentBounds.setSize(width, height);
        onResizeInternal();
    }
    public void setSize(ReadableDimension dimensions) {
        componentBounds.setSize(dimensions);
        onResizeInternal();
    }

    public void draw() {
        for (final Control child : children)
        {
            child.draw();
        }
    }

    public ReadableRectangle getBounds() {
        return componentBounds;
    }

    public void addChild(Control child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Control child) {
        children.remove(child);
        child.setParent(null);
    }

    public Control getParent() {
        return parent;
    }

    public void setParent(Control parent)
    {
        this.parent = parent;
    }

    public boolean mouseClicked(ReadablePoint point, final int mouseButton)
    {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(Control child, ReadablePoint localPoint) {
                return child.mouseClicked(localPoint, mouseButton);
            }

            @Override
            public boolean checkCurrent(final ReadablePoint point) {
                return onMouseClickInternal(point, mouseButton);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseReleased(final ReadablePoint point, final int mouseButton)
    {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(Control child, ReadablePoint localPoint) {
                return child.mouseReleased(localPoint, mouseButton);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseReleasedInternal(point, mouseButton);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseMoved(final ReadablePoint point) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(Control child, ReadablePoint localPoint) {
                return child.mouseMoved(localPoint);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseMovedInternal(point);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseDragged(final ReadablePoint point, final ReadablePoint delta, final int buttons) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(Control child, ReadablePoint localPoint) {
                return child.mouseDragged(localPoint, delta, buttons);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseDraggedInternal(point, delta, buttons);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseDragStarted(final ReadablePoint point, final int buttons) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(Control child, ReadablePoint localPoint) {
                return child.mouseDragStarted(localPoint, buttons);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseDragStartedInternal(point, buttons);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseDragEnded(final ReadablePoint point, final int buttons) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(Control child, ReadablePoint localPoint) {
                return child.mouseDragEnded(localPoint, buttons);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseDragEndedInternal(point, buttons);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    private boolean checkMouseBoundsAndPropagate(final ReadablePoint point, final IMouseCallback callback) {
        final Rectangle realControlBounds = new Rectangle();

        Point localPoint = new Point();
        //Logger.info("event triggered in %s @ %s - %s", this.getClass().getSimpleName(), this.getBounds(), point);

        boolean handled = false;
        for (final Control child : children)
        {
            realControlBounds.setSize(child.getBounds());

            localPoint.setLocation(point);
            localPoint.untranslate(child.getBounds());
            if (realControlBounds.contains(localPoint)) {
                if (callback.checkChild(child, localPoint)) {
                    handled = true;
                    break;
                }
            }
        }

        if (!handled) {
            handled = callback.checkCurrent(point);
        }
        return handled;
    }

    private interface IMouseCallback {
        boolean checkChild(Control child, ReadablePoint localPoint);

        boolean checkCurrent(ReadablePoint point);
    }

    protected void captureMouse() {
        MouseCapture.register(this);
    }

    protected void releaseMouse() {
        MouseCapture.unregister(this);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Internal event handling
    /////////////////////////////////////////////////////////////////////////////
    private boolean onMouseClickInternal(ReadablePoint point, int mouseButton)
    {
        return onMouseClick(point, mouseButton);
    }

    private boolean onMouseReleasedInternal(ReadablePoint point, int mouseButton) {
        return onMouseRelease(point, mouseButton);
    }

    private boolean onMouseMovedInternal(ReadablePoint point) {
        return onMouseMoved(point);
    }

    private boolean onMouseDraggedInternal(ReadablePoint point, ReadablePoint delta, int mouseButton) {
        return onMouseDragged(point, delta, mouseButton);
    }

    private boolean onMouseDragStartedInternal(ReadablePoint point, int mouseButton) {
        return onMouseDragStarted(point, mouseButton);
    }

    private boolean onMouseDragEndedInternal(ReadablePoint point, int mouseButton) {
        return onMouseDragEnded(point, mouseButton);
    }

    private void onResizeInternal() {
        onResized(componentBounds);
    }



    /////////////////////////////////////////////////////////////////////////////
    // Events for subclasses
    /////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseClick(ReadablePoint point, int mouseButton)
    {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseRelease(ReadablePoint point, int mouseButton) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseMoved(ReadablePoint point) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseDragged(ReadablePoint point, ReadablePoint delta, int mouseButton) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseDragStarted(ReadablePoint point, int mouseButton) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseDragEnded(ReadablePoint point, int mouseButton) {
        return false;
    }

    @SuppressWarnings("UnusedParameters")
    protected void onResized(ReadableRectangle componentBounds) {
    }

}
