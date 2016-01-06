package mod.steamnsteel.client.gui;

import com.google.common.collect.Lists;
import mod.steamnsteel.client.gui.components.ProjectTableRecipeGuiComponent;
import mod.steamnsteel.client.gui.components.ScrollPaneGuiComponent;
import mod.steamnsteel.client.gui.components.ScrollbarGuiComponent;
import mod.steamnsteel.client.gui.model.ProjectTableRecipe;
import mod.steamnsteel.inventory.ProjectTableContainer;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codew on 5/01/2016.
 */
public class ProjectTableGui extends SteamNSteelGui {
    private static final GuiTexture TEXTURE = new GuiTexture(getResourceLocation("SSCraftingTableGUI"), 273, 273);
    private final Minecraft client;
    private GuiTextField searchField;
    private List<ProjectTableRecipe> recipeList;
    private ProjectTableRecipeGuiComponent[] recipeGuiComponents;
    private ScrollPaneGuiComponent recipeListGuiComponent;
    private ArrayList<ProjectTableRecipe> filteredList;
    private ScrollbarGuiComponent scrollbarGuiComponent;

    public ProjectTableGui(InventoryPlayer playerInventory) {
        super(new ProjectTableContainer(playerInventory));
        client = Minecraft.getMinecraft();
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

        searchField = new GuiTextField(0, fontRendererObj, guiLeft + 9, guiTop + 9, 151, fontRendererObj.FONT_HEIGHT);
        searchField.setMaxStringLength(60);
        searchField.setEnableBackgroundDrawing(false);
        searchField.setVisible(true);
        searchField.setTextColor(16777215);
        searchField.setFocused(true);

        scrollbarGuiComponent = new ScrollbarGuiComponent(mc.getTextureManager(), TEXTURE);

        //Temporary Item List:
        recipeList = Arrays.asList(new ProjectTableRecipe[]{
                new ProjectTableRecipe(new ItemStack(ModBlock.blockSteel, 1), new ItemStack(ModItem.ingotSteel, 15)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new ProjectTableRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64))
        });
        filteredList = Lists.newArrayList(recipeList);

        recipeListGuiComponent = new ScrollPaneGuiComponent(176, 66, mc)
                .setScrollbar(scrollbarGuiComponent)
                .setItemRendererTemplate(new ProjectTableRecipeGuiComponent(fontRendererObj, mc.getTextureManager(), itemRender, TEXTURE))
                .setVisibleItemCount(5)
                .setItems(filteredList);

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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(TEXTURE.getTextureLocation());

        final int xStart = (width - xSize) / 2;
        final int yStart = (height - ySize) / 2;

        drawModalRectWithCustomSizedTexture(xStart, yStart, 0, 0, xSize, ySize, 273, 273);

        recipeListGuiComponent.setLocation(xStart + 8, yStart + 24);
        recipeListGuiComponent.drawComponent();

        searchField.drawTextBox();

        scrollbarGuiComponent.setLocation(xStart + 156, yStart + 24);
        scrollbarGuiComponent.drawComponent();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ)
    {

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
        super.mouseClicked(mouseX, mouseY, mouseButton);

        
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
