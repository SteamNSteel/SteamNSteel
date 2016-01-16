/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.client.gui;

import mod.steamnsteel.TheMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;

import java.io.IOException;

abstract class EasyGui extends GuiContainer
{
    private static final int TEXT_COLOR = 4210752;
    private static final String LOCATION = "textures/gui/";
    private static final String FILE_EXTENSION = ".png";
    private static final String INVENTORY = "container.inventory";
    private Control rootControl = null;

    EasyGui(Container container)
    {
        super(container);
    }

    static ResourceLocation getResourceLocation(String path)
    {
        return getResourceLocation(TheMod.MOD_ID.toLowerCase(), LOCATION + path + FILE_EXTENSION);
    }

    private static ResourceLocation getResourceLocation(String modID, String path)
    {
        return new ResourceLocation(modID, path);
    }

    protected abstract String getInventoryName();

    public final void setRootControl(Control rootControl) {
        this.rootControl = rootControl;
    }

    protected final void addChild(Control childControl) {
        this.rootControl.addChild(childControl);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        final int xStart = (width - xSize) / 2;
        final int yStart = (height - ySize) / 2;

        rootControl.setLocation(xStart, yStart);
        rootControl.draw();
    }


    /////////////////////////////////////////////////////////////////////////////
    // Control Event handling
    /////////////////////////////////////////////////////////////////////////////
    Point lastMouseLocation = new Point();
    Point currentMouseLocation = new Point();
    boolean isDragging;
    int dragButton;
    private Rectangle bounds = new Rectangle();
    private int eventButton;
    private int touchValue;
    private long lastMouseEvent;

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        if (rootControl == null) {
            return;
        }

        int eventX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int eventY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int eventButton = Mouse.getEventButton();

        bounds.setBounds(rootControl.getBounds());
        if (!bounds.contains(eventX, eventY)) {
            return;
        }
        eventX -= bounds.getX();
        eventY -= bounds.getY();

        currentMouseLocation.setLocation(eventX, eventY);
        if (Mouse.getEventButtonState())
        {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0)
            {
                return;
            }

            this.eventButton = eventButton;
            this.lastMouseEvent = Minecraft.getSystemTime();
            rootControl.mouseClicked(currentMouseLocation, eventButton);
        }
        else if (eventButton != -1)
        {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0)
            {
                return;
            }

            this.eventButton = -1;
            rootControl.mouseReleased(currentMouseLocation, eventButton);
            for (final Control control : MouseCapture.getCapturedControls())
            {
                control.mouseReleased(currentMouseLocation, eventButton);
            }
            if (isDragging && eventButton == dragButton) {
                //Logger.info("Mouse Drag Ended %s", currentMouseLocation);
                rootControl.mouseDragEnded(currentMouseLocation, eventButton);
                for (final Control control : MouseCapture.getCapturedControls())
                {
                    control.mouseDragEnded(currentMouseLocation, eventButton);
                }
                isDragging = false;
            }
        }

        if (!currentMouseLocation.equals(lastMouseLocation)) {

            //Logger.info("Mouse Moved %s", currentMouseLocation);
            rootControl.mouseMoved(currentMouseLocation);

            if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
                if (!isDragging) {
                    //Logger.info("Mouse Drag started %s", currentMouseLocation);
                    rootControl.mouseDragStarted(currentMouseLocation, this.eventButton);
                    isDragging = true;
                    dragButton = this.eventButton;
                } else {
                    //Logger.info("Mouse Dragged %s", currentMouseLocation);
                    Point delta = new Point(currentMouseLocation);
                    delta.untranslate(lastMouseLocation);

                    rootControl.mouseDragged(currentMouseLocation, delta, this.eventButton);
                    Point p = new Point();
                    for (final Control control : MouseCapture.getCapturedControls())
                    {
                        final ReadablePoint controlLocation = GuiRenderer.getControlLocation(control);
                        p.setLocation(currentMouseLocation);
                        p.untranslate(controlLocation);
                        p.translate(rootControl.getBounds());
                        control.mouseDragged(p, delta, this.eventButton);
                    }
                }
            }
            lastMouseLocation.setLocation(currentMouseLocation);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ)
    {
        final String name = StatCollector.translateToLocal(getInventoryName());
        fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 6, TEXT_COLOR);
        fontRendererObj.drawString(StatCollector.translateToLocal(INVENTORY), 8, ySize - 96 + 2, TEXT_COLOR);
    }
}
