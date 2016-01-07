package mod.steamnsteel.client.gui.components;

import mod.steamnsteel.client.gui.GuiRenderer;
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

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        final Rectangle realControlBounds = new Rectangle(componentBounds);
        realControlBounds.translate(GuiRenderer.getControlLocation(this));

        if (realControlBounds.contains(mouseX, mouseY)) {
            final int localX = mouseX - realControlBounds.getX();
            final int localY = mouseY - realControlBounds.getY();

            boolean handled = false;
            for (final GuiComponent child : children)
            {
                if (child.mouseClicked(localX, localY, mouseButton)) {
                    handled = true;
                    break;
                }
            }

            if (!handled) {
                handled = onMouseClickInternal(localX, localY, mouseButton);
            }
            return handled;
        }
        return false;
    }

    private boolean onMouseClickInternal(int localX, int localY, int mouseButton)
    {
        //TODO: Handle dragging

        return onMouseClick(localX, localY, mouseButton);
    }

    protected boolean onMouseClick(int localX, int localY, int mouseButton)
    {
        return false;
    }
}
