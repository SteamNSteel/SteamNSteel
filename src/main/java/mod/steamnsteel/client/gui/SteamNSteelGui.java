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
import mod.steamnsteel.client.gui.controls.Control;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

import java.io.IOException;

abstract class SteamNSteelGui extends GuiContainer
{
    private static final int TEXT_COLOR = 4210752;
    private static final String LOCATION = "textures/gui/";
    private static final String FILE_EXTENSION = ".png";
    private static final String INVENTORY = "container.inventory";
    private Control rootControl = null;

    SteamNSteelGui(Container container)
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
    int lastButtonCheck;
    Point lastMouseLocation = new Point();
    Point currentMouseLocation = new Point();
    boolean isDragging;
    int dragButton;
    boolean[] buttonStates;
    private Rectangle bounds = new Rectangle();
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        if (rootControl == null) {
            return;
        }
        if (buttonStates == null) {
            buttonStates = new boolean[Mouse.getButtonCount()];
        }
        bounds.setBounds(rootControl.getBounds());
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        if (!bounds.contains(x, y)) {
            return;
        }

        x = x - bounds.getX();
        y = y - bounds.getY();
        currentMouseLocation.setLocation(x, y);
        int button = Mouse.getEventButton();

        if (button != -1) {
            boolean buttonState = Mouse.getEventButtonState();
            if (buttonStates[button] == true && buttonState != true) {
                Logger.info("Mouse Clicked %s", currentMouseLocation);
                rootControl.mouseClicked(currentMouseLocation, button);
            } else if (buttonStates[button] != true && buttonState == true) {
                Logger.info("Mouse Unclicked %s", currentMouseLocation);
                rootControl.mouseReleased(currentMouseLocation, button);
                if (isDragging) {
                    Logger.info("Mouse Drag Ended %s", currentMouseLocation);
                    rootControl.mouseDragEnded(currentMouseLocation, button);
                }
            }
            buttonStates[button] = buttonState;
        }

        if (!currentMouseLocation.equals(lastMouseLocation)){
            Logger.info("Mouse Moved %s", currentMouseLocation);
            rootControl.mouseMoved(currentMouseLocation);
            if (button > 0) {
                if (!isDragging) {
                    Logger.info("Mouse Drag started %s", currentMouseLocation);
                    rootControl.mouseDragStarted(currentMouseLocation, button);
                    isDragging = true;
                    dragButton = button;
                } else {
                    Logger.info("Mouse Dragged %s", currentMouseLocation);
                    rootControl.mouseDragged(currentMouseLocation, button);
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
