package mod.steamnsteel.client.gui.controls;

import mod.steamnsteel.client.gui.GuiRenderer;
import mod.steamnsteel.utility.log.Logger;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Control
{
    private final Rectangle componentBounds = new Rectangle();
    protected final GuiRenderer guiRenderer;
    private Control parent = null;
    private final List<Control> children = new ArrayList<>(10);

    public Control(GuiRenderer guiRenderer, Rectangle componentBounds)
    {
        this.guiRenderer = guiRenderer;
        this.componentBounds.setBounds(componentBounds);
    }
    public Control(GuiRenderer guiRenderer, int width, int height) {
        this(guiRenderer, new Rectangle(0, 0, width, height));
    }


    public void setLocation(int x, int y) {
        componentBounds.setLocation(x, y);
    }

    public void draw() {
        for (final Control child : children)
        {
            child.draw();
        }
    }

    public Rectangle getBounds() {
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
            public boolean checkCurrent(final Point point) {
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
            public boolean checkCurrent(Point point) {
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
            public boolean checkCurrent(Point point) {
                return onMouseMovedInternal(point);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseDragged(final ReadablePoint point, final int buttons) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(Control child, ReadablePoint localPoint) {
                return child.mouseDragged(localPoint, buttons);
            }

            @Override
            public boolean checkCurrent(Point point) {
                return onMouseDraggedInternal(point, buttons);
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
            public boolean checkCurrent(Point point) {
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
            public boolean checkCurrent(Point point) {
                return onMouseDragEndedInternal(point, buttons);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    private boolean checkMouseBoundsAndPropagate(final ReadablePoint point, final IMouseCallback callback) {
        final Rectangle realControlBounds = new Rectangle(componentBounds);
        realControlBounds.translate(GuiRenderer.getControlLocation(this));

        Point localPoint = new Point();
        if (realControlBounds.contains(point)) {
            Logger.info("event triggered in %s @ %s", this.getClass().getSimpleName(), this.getBounds());
            localPoint.setLocation(point);
            localPoint.untranslate(realControlBounds);

            boolean handled = false;
            for (final Control child : children)
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
        boolean checkChild(Control child, ReadablePoint localPoint);

        boolean checkCurrent(Point point);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Internal event handling
    /////////////////////////////////////////////////////////////////////////////
    private boolean onMouseClickInternal(ReadablePoint point, int mouseButton)
    {
        //Fire Event
        return onMouseClick(point, mouseButton);
    }

    private boolean onMouseReleasedInternal(ReadablePoint point, int mouseButton) {
        return onMouseRelease(point, mouseButton);
    }

    private boolean onMouseMovedInternal(ReadablePoint point) {
        return onMouseMoved(point);
    }

    private boolean onMouseDraggedInternal(ReadablePoint point, int mouseButton) {
        return onMouseDragged(point, mouseButton);
    }

    private boolean onMouseDragStartedInternal(ReadablePoint point, int mouseButton) {
        return onMouseDragStarted(point, mouseButton);
    }

    private boolean onMouseDragEndedInternal(ReadablePoint point, int mouseButton) {
        return onMouseDragEnded(point, mouseButton);
    }

    protected void captureMouse() {
        MouseCapture.register(this);
    }

    protected void releaseMouse() {
        MouseCapture.unregister(this);
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
    protected boolean onMouseDragged(ReadablePoint point, int mouseButton) {
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



}
