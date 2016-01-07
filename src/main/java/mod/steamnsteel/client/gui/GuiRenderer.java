package mod.steamnsteel.client.gui;

import mod.steamnsteel.client.gui.components.GuiComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;
import org.w3c.dom.css.Rect;
import javax.annotation.Nullable;
import java.util.Stack;

public class GuiRenderer
{
    private final Minecraft client;
    private final TextureManager textureManager;
    private final FontRenderer fontRenderer;
    private final RenderItem itemRenderer;

    @Nullable
    private GuiTexture currentTexture = null;

    public GuiRenderer(Minecraft client, TextureManager textureManager, FontRenderer fontRenderer, RenderItem itemRenderer)
    {
        this.client = client;
        this.textureManager = textureManager;

        this.fontRenderer = fontRenderer;
        this.itemRenderer = itemRenderer;
    }

    public TextureManager getTextureManager()
    {
        return textureManager;
    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    public RenderItem getItemRenderer()
    {
        return itemRenderer;
    }

    /////////////////////////////////////////////////////////////////////////////
    // Texture Management
    /////////////////////////////////////////////////////////////////////////////
    private void verifyTexture(GuiTexture texture)
    {
        if (currentTexture == null || !texture.equals(currentTexture)) {
            currentTexture = texture;
            textureManager.bindTexture(texture.getTextureLocation());
        }
    }

    public void notifyTextureChanged() {
        currentTexture = null;
    }

    /////////////////////////////////////////////////////////////////////////////
    // Image rendering
    /////////////////////////////////////////////////////////////////////////////
    public void drawModelRectWithCustomSizedTexture(GuiComponent control, Rectangle componentSubtexture, GuiTexture texture)
    {
        drawModelRectWithCustomSizedTexture(control, componentSubtexture, texture, 0, 0);
    }

    public void drawModelRectWithCustomSizedTexture(GuiComponent control, Rectangle componentSubtexture, GuiTexture texture, int offsetX, int offsetY)
    {
        final ReadablePoint controlLocation = getControlLocation(control);
        verifyTexture(texture);
        Gui.drawModalRectWithCustomSizedTexture(
                controlLocation.getX() + offsetX, controlLocation.getY() + offsetY,
                componentSubtexture.getX(), componentSubtexture.getY(),
                componentSubtexture.getWidth(), componentSubtexture.getHeight(),
                texture.getWidth(), texture.getHeight());
    }

    public void drawComponentTexture(GuiComponent control, GuiTexture texture, Rectangle componentSubtexture)
    {
        verifyTexture(texture);
        drawModelRectWithCustomSizedTexture(control, componentSubtexture, texture, 0, 0);
    }

    public void drawComponentTexture(GuiComponent control, GuiTexture texture)
    {
        verifyTexture(texture);
        drawModelRectWithCustomSizedTexture(control, texture.getBounds(), texture, 0, 0);
    }

    public void drawComponentTextureWithOffset(GuiComponent control, GuiTexture texture, Rectangle componentSubtexture, int offsetX, int offsetY)
    {
        drawModelRectWithCustomSizedTexture(control, componentSubtexture, texture, offsetX, offsetY);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Item Rendering
    /////////////////////////////////////////////////////////////////////////////
    public void renderItem(GuiComponent control, ItemStack itemStack, int x, int y)
    {
        final ReadablePoint controlLocation = getControlLocation(control);
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.renderItemIntoGUI(itemStack, controlLocation.getX() + x, controlLocation.getY() + y);
        RenderHelper.disableStandardItemLighting();
        notifyTextureChanged();
    }

    /////////////////////////////////////////////////////////////////////////////
    // Text Rendering
    /////////////////////////////////////////////////////////////////////////////
    public void drawStringWithShadow(GuiComponent control, String text, int x, int y, int colour)
    {
        final ReadablePoint controlLocation = getControlLocation(control);
        fontRenderer.drawStringWithShadow(text, controlLocation.getX() + x, controlLocation.getY() + y, colour);
    }

    public int getStringWidth(String text)
    {
        return fontRenderer.getStringWidth(text);
    }


    /////////////////////////////////////////////////////////////////////////////
    // Viewport Management
    /////////////////////////////////////////////////////////////////////////////
    public Stack<Rectangle> viewportStack = new Stack<>();

    public void startViewport(GuiComponent control, Rectangle bounds) {
        final ReadablePoint controlLocation = getControlLocation(control);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        final ScaledResolution res = new ScaledResolution(client);
        final double scaleW = client.displayWidth / res.getScaledWidth_double();
        final double scaleH = client.displayHeight / res.getScaledHeight_double();

        //noinspection NumericCastThatLosesPrecision
        final int x = (int) ((controlLocation.getX() + bounds.getX()) * scaleW);
        final int y = client.displayHeight - ((int) (controlLocation.getY() * scaleH) + (int) (bounds.getHeight() * scaleH));
        final int width = (int) (bounds.getWidth() * scaleW);
        final int height = (int) (bounds.getHeight() * scaleH);
        GL11.glScissor(x, y, width, height);

    }

    public void endViewport() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Utilities
    /////////////////////////////////////////////////////////////////////////////
    private static ReadablePoint getControlLocation(GuiComponent control) {
        GuiComponent parent = control;
        int offsetX = 0;
        int offsetY = 0;
        while (parent != null) {
            final Rectangle bounds = parent.getBounds();
            offsetX += bounds.getX();
            offsetY += bounds.getY();
            parent = parent.getParent();
        }
        return new Point(offsetX, offsetY);
    }
}
