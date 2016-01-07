package mod.steamnsteel.client.gui.components;

import mod.steamnsteel.client.gui.GuiRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class GuiComponent extends Gui
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
}
