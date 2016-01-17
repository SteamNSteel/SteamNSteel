package mod.steamnsteel.client.gui;

import com.google.common.collect.Lists;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.client.gui.controls.ProjectTableRecipeControl;
import mod.steamnsteel.client.gui.controls.ScrollPaneControl;
import mod.steamnsteel.client.gui.controls.ScrollbarControl;
import mod.steamnsteel.client.gui.controls.TexturedPaneControl;
import mod.steamnsteel.client.gui.events.IRecipeCraftingEventListener;
import mod.steamnsteel.client.gui.model.ProjectTableRecipe;
import mod.steamnsteel.inventory.ProjectTableContainer;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import mod.steamnsteel.networking.ProjectTableCraftPacket;
import mod.steamnsteel.proxy.Proxies;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import org.lwjgl.util.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectTableGui extends EasyGui
{
    private static final GuiTexture TEXTURE = new GuiTexture(getResourceLocation("SSCraftingTableGUI"), 273, 273);
    private final InventoryPlayer playerInventory;
    private GuiTextField searchField = null;
    private List<ProjectTableRecipe> recipeList = null;
    private ArrayList<ProjectTableRecipe> filteredList = null;
    private ScrollPaneControl recipeListGuiComponent = null;
    private ScrollbarControl scrollbarGuiComponent = null;

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

        //Temporary Item List:
        recipeList = Lists.newArrayList(
                new ProjectTableRecipe(new ItemStack(ModBlock.blockSteel, 1), new ItemStack(ModItem.ingotSteel, 15)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 10), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64))
        );
        filteredList = Lists.newArrayList(recipeList);

        searchField = new GuiTextField(0, fontRendererObj, guiLeft + 9, guiTop + 9, 151, fontRendererObj.FONT_HEIGHT);
        searchField.setMaxStringLength(60);
        searchField.setEnableBackgroundDrawing(false);
        searchField.setVisible(true);
        searchField.setTextColor(16777215);
        searchField.setFocused(true);

        createComponents();

        processPlayerInventory();

        setRecipeRenderText();
    }

    protected void createComponents()
    {
        final GuiRenderer guiRenderer = new GuiRenderer(mc, mc.getTextureManager(), fontRendererObj, itemRender);

        final GuiSubTexture guiBackground = new GuiSubTexture(TEXTURE, new Rectangle(0, 0, 176, 227));
        final GuiTexture inactiveHandle = new GuiSubTexture(TEXTURE, new Rectangle(176, 0, 12, 15));
        final GuiTexture activeHandle = new GuiSubTexture(TEXTURE, new Rectangle(176 + 12, 0, 12, 15));
        final GuiTexture craftableSubtexture = new GuiSubTexture(TEXTURE, new Rectangle(0, 227, 142, 23));
        final GuiTexture uncraftableSubtexture = new GuiSubTexture(TEXTURE, new Rectangle(0, 227 + 23, 142, 23));

        setRootControl(new TexturedPaneControl(guiRenderer, 176, 227, guiBackground));
        scrollbarGuiComponent = new ScrollbarControl(guiRenderer, activeHandle, inactiveHandle);
        scrollbarGuiComponent.setLocation(156, 24);
        scrollbarGuiComponent.setSize(20, 115);

        final ProjectTableRecipeControl templateRecipeControl = new ProjectTableRecipeControl(guiRenderer, craftableSubtexture, uncraftableSubtexture);
        recipeListGuiComponent = new ScrollPaneControl<ProjectTableRecipe, ProjectTableRecipeControl>(guiRenderer, 141, 23*5)
                .setScrollbar(scrollbarGuiComponent)
                .setItemRendererTemplate(templateRecipeControl)
                .setVisibleItemCount(5)
                .setItems(filteredList);
        recipeListGuiComponent.setLocation(8, 24);

        addChild(recipeListGuiComponent);
        addChild(scrollbarGuiComponent);

        templateRecipeControl.addOnRecipeCraftingEventListener(new RecipeCraftingEventListener());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ)
    {

    }

    protected void setRecipeRenderText()
    {
        for (final ProjectTableRecipe projectTableRecipe : recipeList)
        {
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
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        searchField.drawTextBox();
    }

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
        for (final ProjectTableRecipe projectTableRecipe : recipeList)
        {
            if (projectTableRecipe.getDisplayName().toLowerCase().contains(text)) {
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
                if (existingItemStack.getIsItemStackEqual(itemStack))
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
        boolean canCraft = true;
        for (final ItemStack recipeInput : recipe.getInput())
        {
            boolean itemMatched = false;
            for (final ItemStack playerItem : usableItems) {
                if (recipeInput.isItemEqual(playerItem)) {
                    itemMatched = true;
                    if (recipeInput.stackSize > playerItem.stackSize) {
                        canCraft = false;
                    }
                }
            }
            if (!itemMatched) {
                canCraft = false;
            }
        }

        if (canCraft) {
            Proxies.network.getNetwork().sendToServer(new ProjectTableCraftPacket(recipe));

            processPlayerInventory();
        }
    }


    private class RecipeCraftingEventListener implements IRecipeCraftingEventListener
    {
        @Override
        public void onRecipeCrafting(ProjectTableRecipe recipe)
        {
            craftRecipe(recipe);
        }
    }
}
