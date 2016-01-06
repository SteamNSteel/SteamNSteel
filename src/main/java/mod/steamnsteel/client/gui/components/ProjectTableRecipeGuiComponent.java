package mod.steamnsteel.client.gui.components;

import com.google.common.collect.ImmutableList;
import mod.steamnsteel.client.gui.GuiTexture;
import mod.steamnsteel.client.gui.model.ProjectTableRecipe;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public class ProjectTableRecipeGuiComponent extends GuiComponent implements IGuiTemplate<ProjectTableRecipeGuiComponent>, IModelView<ProjectTableRecipe>
{
    private final FontRenderer fontRenderer;
    private final TextureManager textureManager;
    private final RenderItem itemRender;
    private final GuiTexture texture;

    private ProjectTableRecipe recipe = null;
    private static final Rectangle craftableSubtexture = new Rectangle(0, 227, 142, 23);
    private static final Rectangle uncraftableSubtexture = new Rectangle(0, 227 + 23, craftableSubtexture.getWidth(), craftableSubtexture.getHeight());
    private static final Rectangle componentBounds = new Rectangle(0, 0, craftableSubtexture.getWidth(), craftableSubtexture.getHeight());

    public ProjectTableRecipeGuiComponent(FontRenderer fontRenderer, TextureManager textureManager, RenderItem itemRender, GuiTexture texture)
    {
        super(componentBounds);
        this.fontRenderer = fontRenderer;
        this.textureManager = textureManager;
        this.itemRender = itemRender;
        this.texture = texture;
    }

    @Override
    public void drawComponent() {
        if (recipe == null) { return; }

        textureManager.bindTexture(texture.getTextureLocation());

        drawComponentTexture(texture, craftableSubtexture);

        final int x = getBounds().getX();
        final int y = getBounds().getY();

        GlStateManager.enableRescaleNormal();
        final ImmutableList<ItemStack> output = recipe.getOutput();
        final ItemStack outputItemStack = output.get(0);
        if (output.size() == 1 && outputItemStack.getItem() != null)
        {
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemIntoGUI(outputItemStack, x + 2, y + 3);
            RenderHelper.disableStandardItemLighting();

            fontRenderer.drawStringWithShadow(recipe.getDisplayName(), x + 2 + 20, (float)y + 8, 16777215);
        }

        final int inputItemCount = recipe.getInput().size();

        for (int j = 0; j < inputItemCount; ++j) {
            final ItemStack inputItemStack = recipe.getInput().get(j);

            final String requiredItemCount = String.format("%d", inputItemStack.stackSize);
            final int textWidth = fontRenderer.getStringWidth(requiredItemCount);

            final int border = 1;
            final int padding = 2;
            final int itemSize = 16;

            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemIntoGUI(inputItemStack, x + getBounds().getWidth() - border - (itemSize + padding) * (j + border), y + padding + border);
            RenderHelper.disableStandardItemLighting();

            GlStateManager.depthFunc(GL11.GL_ALWAYS);
            fontRenderer.drawStringWithShadow(requiredItemCount, x + getBounds().getWidth() - border - (itemSize + padding) * j - textWidth - border , y + 12, 16777215);
            GlStateManager.depthFunc(GL11.GL_LEQUAL);
        }

        GlStateManager.disableRescaleNormal();

    }

    public ProjectTableRecipe getRecipe()
    {
        return recipe;
    }

    public void setRecipe(ProjectTableRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public ProjectTableRecipeGuiComponent construct()
    {
        return new ProjectTableRecipeGuiComponent(fontRenderer, textureManager, itemRender, texture);
    }

    @Override
    public void setModel(ProjectTableRecipe recipe)
    {
        this.recipe = recipe;
    }
}
