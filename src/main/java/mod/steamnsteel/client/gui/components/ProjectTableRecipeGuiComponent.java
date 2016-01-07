package mod.steamnsteel.client.gui.components;

import com.google.common.collect.ImmutableList;
import mod.steamnsteel.client.gui.GuiRenderer;
import mod.steamnsteel.client.gui.GuiTexture;
import mod.steamnsteel.client.gui.model.ProjectTableRecipe;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public class ProjectTableRecipeGuiComponent extends GuiComponent implements IGuiTemplate<ProjectTableRecipeGuiComponent>, IModelView<ProjectTableRecipe>
{
    private final GuiTexture craftableTexture;
    private final GuiTexture uncraftableTexture;
    private ProjectTableRecipe recipe = null;

    public ProjectTableRecipeGuiComponent(GuiRenderer guiRenderer, GuiTexture craftableTexture, GuiTexture uncraftableTexture)
    {
        super(guiRenderer, new Rectangle(0, 0, craftableTexture.getBounds().getWidth(), craftableTexture.getBounds().getHeight()));
        this.craftableTexture = craftableTexture;
        this.uncraftableTexture = uncraftableTexture;
    }

    @Override
    public void drawComponent() {
        if (recipe == null) { return; }

        guiRenderer.drawComponentTexture(this, craftableTexture);

        GlStateManager.enableRescaleNormal();
        final ImmutableList<ItemStack> output = recipe.getOutput();
        final ItemStack outputItemStack = output.get(0);
        if (output.size() == 1 && outputItemStack.getItem() != null)
        {
            RenderHelper.enableGUIStandardItemLighting();
            guiRenderer.renderItem(this, outputItemStack, 2, 3);
            RenderHelper.disableStandardItemLighting();

            guiRenderer.drawStringWithShadow(this, recipe.getDisplayName(), 2 + 20, 8, 16777215);
        }

        final int inputItemCount = recipe.getInput().size();

        for (int j = 0; j < inputItemCount; ++j) {
            final ItemStack inputItemStack = recipe.getInput().get(j);

            final String requiredItemCount = String.format("%d", inputItemStack.stackSize);
            final int textWidth = guiRenderer.getStringWidth(requiredItemCount);

            final int border = 1;
            final int padding = 2;
            final int itemSize = 16;

            guiRenderer.renderItem(this, inputItemStack, getBounds().getWidth() - border - (itemSize + padding) * (j + border), padding + border);

            GlStateManager.depthFunc(GL11.GL_ALWAYS);
            guiRenderer.drawStringWithShadow(this, requiredItemCount, getBounds().getWidth() - border - (itemSize + padding) * j - textWidth - border , 12, 16777215);
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
        return new ProjectTableRecipeGuiComponent(guiRenderer, craftableTexture, uncraftableTexture);
    }

    @Override
    public void setModel(ProjectTableRecipe recipe)
    {
        this.recipe = recipe;
    }
}
