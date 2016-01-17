package mod.steamnsteel.client.gui.controls;

import com.google.common.collect.ImmutableList;
import mod.steamnsteel.client.gui.*;
import mod.steamnsteel.client.gui.events.IRecipeCraftingEventListener;
import mod.steamnsteel.client.gui.model.ProjectTableRecipe;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ProjectTableRecipeControl extends ButtonControl implements IGuiTemplate<ProjectTableRecipeControl>, IModelView<ProjectTableRecipe>
{
    private final GuiTexture craftableTexture;
    private final GuiTexture uncraftableTexture;
    private ProjectTableRecipe recipe = null;

    public ProjectTableRecipeControl(GuiRenderer guiRenderer, GuiTexture craftableTexture, GuiTexture uncraftableTexture)
    {
        super(guiRenderer, new Rectangle(0, 0, craftableTexture.getBounds().getWidth(), craftableTexture.getBounds().getHeight()));
        this.craftableTexture = craftableTexture;
        this.uncraftableTexture = uncraftableTexture;
    }

    @Override
    public void draw() {
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

            if (outputItemStack.stackSize > 1)
            {
                final String craftedItemCount = String.format("%d", outputItemStack.stackSize);
                final int textWidth = guiRenderer.getStringWidth(craftedItemCount);

                GlStateManager.depthFunc(GL11.GL_ALWAYS);
                guiRenderer.drawStringWithShadow(this, craftedItemCount, 16 - textWidth + 2, 12, 16777215);
                GlStateManager.depthFunc(GL11.GL_LEQUAL);

            }
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
    public ProjectTableRecipeControl construct()
    {
        final ProjectTableRecipeControl concreteControl = new ProjectTableRecipeControl(guiRenderer, craftableTexture, uncraftableTexture);

        concreteControl.recipeCraftingEventListeners = recipeCraftingEventListeners;

        return concreteControl;
    }

    @Override
    public void setModel(ProjectTableRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    protected void onButtonPressed() {
        onRecipeCraftingInternal();
    }

    /////////////////////////////////////////////////////////////////////////////
    // On Recipe Crafting Event Handling
    /////////////////////////////////////////////////////////////////////////////

    private void onRecipeCraftingInternal() {
        onRecipeCrafting();

        fireRecipeCraftingEvent();
    }

    protected void onRecipeCrafting() {
    }

    private void fireRecipeCraftingEvent()
    {
        for (final IRecipeCraftingEventListener eventListener : recipeCraftingEventListeners)
        {
            try {
                eventListener.onRecipeCrafting(recipe);
            } catch (final RuntimeException e) {
                Logger.warning("Exception in an IRecipeCraftingEventListener %s", e);
            }
        }
    }

    private List<IRecipeCraftingEventListener> recipeCraftingEventListeners = new ArrayList<>(1);

    @SuppressWarnings("unused")
    public void addOnRecipeCraftingEventListener(IRecipeCraftingEventListener listener) {
        recipeCraftingEventListeners.add(listener);
    }
    @SuppressWarnings("unused")
    public void removeOnRecipeCraftingEventListener(IRecipeCraftingEventListener listener) {
        recipeCraftingEventListeners.remove(listener);
    }
}
