package mod.steamnsteel.client.gui;

import com.google.common.collect.Lists;
import mod.steamnsteel.client.gui.controls.ProjectTableRecipeControl;
import mod.steamnsteel.client.gui.events.IRecipeCraftingEventListener;
import mod.steamnsteel.client.gui.model.ProjectTableRecipeInstance;
import mod.steamnsteel.crafting.projecttable.ProjectTableManager;
import mod.steamnsteel.crafting.projecttable.ProjectTableRecipe;
import mod.steamnsteel.inventory.ProjectTableContainer;
import mod.steamnsteel.mcgui.client.gui.GuiRenderer;
import mod.steamnsteel.mcgui.client.gui.GuiSubTexture;
import mod.steamnsteel.mcgui.client.gui.GuiTexture;
import mod.steamnsteel.mcgui.client.gui.controls.ScrollPaneControl;
import mod.steamnsteel.mcgui.client.gui.controls.ScrollbarControl;
import mod.steamnsteel.mcgui.client.gui.controls.TexturedPaneControl;
import mod.steamnsteel.mcgui.client.gui.events.IItemMadeVisibleEventListener;
import mod.steamnsteel.networking.ProjectTableCraftPacket;
import mod.steamnsteel.proxy.Proxies;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.Rectangle;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ProjectTableGui extends SteamNSteelGui
{
    private final GuiTexture guiTexture = new GuiTexture(getResourceLocation("SSCraftingTableGUI"), 273, 273);
    private final InventoryPlayer playerInventory;
    private GuiTextField searchField = null;
    private Collection<ProjectTableRecipeInstance> recipeList = null;
    private List<ProjectTableRecipeInstance> filteredList = null;
    private ScrollPaneControl recipeListGuiComponent = null;
    private ScrollbarControl scrollbarGuiComponent = null;
    private GuiRenderer guiRenderer;

    public ProjectTableGui(InventoryPlayer playerInventory) {
        super(new ProjectTableContainer(playerInventory));
        this.playerInventory = playerInventory;
    }

    @Override
    protected String getInventoryName() {
        return "Project Table";
    }

    @Override
    public void initGui()
    {
        xSize = 176;
        ySize = 227;
        super.initGui();

        recipeList = Lists.newArrayList();

        //Temporary Item List:
        for (final ProjectTableRecipe projectTableRecipe : ProjectTableManager.INSTANCE.getRecipes())
        {
            recipeList.add(new ProjectTableRecipeInstance(projectTableRecipe));
        }
        filteredList = Lists.newArrayList(recipeList);

        searchField = new GuiTextField(0, fontRendererObj, guiLeft + 9, guiTop + 9, 151, fontRendererObj.FONT_HEIGHT);
        searchField.setMaxStringLength(60);
        searchField.setEnableBackgroundDrawing(false);
        searchField.setVisible(true);
        searchField.setTextColor(16777215);
        searchField.setFocused(true);

        createComponents();

        setRecipeRenderText();
    }

    protected void createComponents()
    {
        guiRenderer = new GuiRenderer(mc, mc.getTextureManager(), fontRendererObj, itemRender);

        final GuiSubTexture guiBackground = new GuiSubTexture(guiTexture, new Rectangle(0, 0, 176, 227));
        final GuiTexture inactiveHandle = new GuiSubTexture(guiTexture, new Rectangle(176, 0, 12, 15));
        final GuiTexture activeHandle = new GuiSubTexture(guiTexture, new Rectangle(176 + 12, 0, 12, 15));
        final GuiTexture craftableSubtexture = new GuiSubTexture(guiTexture, new Rectangle(0, 227, 142, 23));
        final GuiTexture uncraftableSubtexture = new GuiSubTexture(guiTexture, new Rectangle(0, 227 + 23, 142, 23));

        setRootControl(new TexturedPaneControl(guiRenderer, 176, 227, guiBackground));
        scrollbarGuiComponent = new ScrollbarControl(guiRenderer, activeHandle, inactiveHandle);
        scrollbarGuiComponent.setLocation(156, 24);
        scrollbarGuiComponent.setSize(20, 115);

        final ProjectTableRecipeControl templateRecipeControl = new ProjectTableRecipeControl(guiRenderer, craftableSubtexture, uncraftableSubtexture);
        recipeListGuiComponent = new ScrollPaneControl<ProjectTableRecipeInstance, ProjectTableRecipeControl>(guiRenderer, 141, 23*5)
                .setScrollbar(scrollbarGuiComponent)
                .setItemRendererTemplate(templateRecipeControl)
                .setVisibleItemCount(5)
                .setItems(filteredList);
        recipeListGuiComponent.setLocation(8, 24);

        addChild(recipeListGuiComponent);
        addChild(scrollbarGuiComponent);

        templateRecipeControl.addOnRecipeCraftingEventListener(new RecipeCraftingEventListener());
        recipeListGuiComponent.addOnFireItemMadeEventListener(new RecipeMadeVisibleEventListener());
        playerInventory.inventoryChanged = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ)
    {

    }

    protected void setRecipeRenderText()
    {
        for (final ProjectTableRecipeInstance recipeInstance : recipeList)
        {
            final ProjectTableRecipe projectTableRecipe = recipeInstance.getRecipe();
            if (projectTableRecipe.getRenderText() == null) {
                String proposedName = projectTableRecipe.getDisplayName();

                if (fontRendererObj.getStringWidth(proposedName) > 64) {
                    while (fontRendererObj.getStringWidth(proposedName + "...") > 64) {
                        proposedName = proposedName.substring(0, proposedName.length() - 2);
                    }
                    proposedName += "...";
                }

                projectTableRecipe.setRenderText(proposedName);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (playerInventory.inventoryChanged) {
            for (final ProjectTableRecipeInstance recipeInstance : filteredList)
            {
                final boolean canCraft = ProjectTableManager.INSTANCE.canCraftRecipe(recipeInstance.getRecipe(), playerInventory);
                recipeInstance.setCanCraft(canCraft);
            }

        }
        playerInventory.inventoryChanged = false;

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        searchField.drawTextBox();
        guiRenderer.notifyTextureChanged();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!checkHotbarKeys(keyCode))
        {
            if (searchField.textboxKeyTyped(typedChar, keyCode))
            {
                updateSearch();
            }
            else
            {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }

    private void updateSearch()
    {
        String text = searchField.getText();
        filteredList.clear();
        if (text == null || text.isEmpty()) {
            filteredList.addAll(recipeList);
            return;
        }
        text = text.toLowerCase();
        for (final ProjectTableRecipeInstance projectTableRecipe : recipeList)
        {
            if (projectTableRecipe.getRecipe().getDisplayName().toLowerCase().contains(text)) {
                filteredList.add(projectTableRecipe);
            }
        }
    }

    List<ItemStack> usableItems;

    private void processPlayerInventory() {
        List<ItemStack> usableItems = Lists.newArrayList();
        for (final ItemStack itemStack : inventorySlots.getInventory())
        {
            if (itemStack == null || itemStack.getItem() == null)
            {
                continue;
            }

            boolean itemMatched = false;
            for (final ItemStack existingItemStack : usableItems) {
                if (ItemStack.areItemStacksEqual(existingItemStack, itemStack))
                {
                    itemMatched = true;
                    existingItemStack.stackSize += itemStack.stackSize;
                }
            }

            if (!itemMatched) {
                final ItemStack copy = itemStack.copy();
                usableItems.add(copy);
            }
        }
        this.usableItems = usableItems;
    }

    private void craftRecipe(ProjectTableRecipe recipe) {
        playerInventory.inventoryChanged = false;
        Proxies.network.getNetwork().sendToServer(new ProjectTableCraftPacket(recipe));
    }

    private class RecipeCraftingEventListener implements IRecipeCraftingEventListener
    {
        @Override
        public void onRecipeCrafting(ProjectTableRecipe recipe)
        {
            craftRecipe(recipe);
        }
    }

    private class RecipeMadeVisibleEventListener implements IItemMadeVisibleEventListener<ProjectTableRecipeInstance, ProjectTableRecipeControl>
    {

        @Override
        public void onItemMadeVisible(ScrollPaneControl scrollPaneControl, ProjectTableRecipeControl projectTableRecipeControl, ProjectTableRecipeInstance projectTableRecipe)
        {

            final boolean canCraft = ProjectTableManager.INSTANCE.canCraftRecipe(projectTableRecipe.getRecipe(), playerInventory);
            projectTableRecipe.setCanCraft(canCraft);
        }
    }
}
