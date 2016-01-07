package mod.steamnsteel.client.gui;

import com.google.common.collect.Lists;
import mod.steamnsteel.client.gui.components.ProjectTableRecipeGuiComponent;
import mod.steamnsteel.client.gui.components.ScrollPaneGuiComponent;
import mod.steamnsteel.client.gui.components.ScrollbarGuiComponent;
import mod.steamnsteel.client.gui.components.TexturedPaneGuiComponent;
import mod.steamnsteel.client.gui.model.ProjectTableRecipe;
import mod.steamnsteel.inventory.ProjectTableContainer;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectTableGui extends SteamNSteelGui {
    private static final GuiTexture TEXTURE = new GuiTexture(getResourceLocation("SSCraftingTableGUI"), 273, 273);
    private GuiTextField searchField = null;
    private List<ProjectTableRecipe> recipeList = null;
    private ScrollPaneGuiComponent recipeListGuiComponent = null;
    private ArrayList<ProjectTableRecipe> filteredList = null;
    private ScrollbarGuiComponent scrollbarGuiComponent = null;
    private TexturedPaneGuiComponent rootElement;

    public ProjectTableGui(InventoryPlayer playerInventory) {
        super(new ProjectTableContainer(playerInventory));
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
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
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

        CreateComponents();

        setRecipeRenderText();
    }

    protected void CreateComponents()
    {
        final GuiRenderer guiRenderer = new GuiRenderer(mc, mc.getTextureManager(), fontRendererObj, itemRender);

        final GuiSubTexture guiBackground = new GuiSubTexture(TEXTURE, new Rectangle(0, 0, 176, 227));
        final GuiTexture inactiveHandle = new GuiSubTexture(TEXTURE, new Rectangle(176, 0, 12, 15));
        final GuiTexture activeHandle = new GuiSubTexture(TEXTURE, new Rectangle(176 + 12, 0, 12, 15));
        final GuiTexture craftableSubtexture = new GuiSubTexture(TEXTURE, new Rectangle(0, 227, 142, 23));
        final GuiTexture uncraftableSubtexture = new GuiSubTexture(TEXTURE, new Rectangle(0, 227 + 23, 142, 23));

        rootElement = new TexturedPaneGuiComponent(guiRenderer, 176, 227, guiBackground);
        scrollbarGuiComponent = new ScrollbarGuiComponent(guiRenderer, activeHandle, inactiveHandle);
        scrollbarGuiComponent.setLocation(156, 24);

        recipeListGuiComponent = new ScrollPaneGuiComponent<ProjectTableRecipe, ProjectTableRecipeGuiComponent>(guiRenderer, 176, 66)
                .setScrollbar(scrollbarGuiComponent)
                .setItemRendererTemplate(new ProjectTableRecipeGuiComponent(guiRenderer, craftableSubtexture, uncraftableSubtexture))
                .setVisibleItemCount(5)
                .setItems(filteredList);
        recipeListGuiComponent.setLocation(8, 24);

        rootElement.addChild(recipeListGuiComponent);
        rootElement.addChild(scrollbarGuiComponent);
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
        final int xStart = (width - xSize) / 2;
        final int yStart = (height - ySize) / 2;

        rootElement.setLocation(xStart, yStart);
        rootElement.drawComponent();

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

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        rootElement.mouseClicked(mouseX, mouseY, mouseButton);
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
}
