package mod.steamnsteel.client.gui;

import com.google.common.collect.ImmutableList;
import mod.steamnsteel.inventory.ProjectTableContainer;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by codew on 5/01/2016.
 */
public class ProjectTableGui extends SteamNSteelGui {
    private static final ResourceLocation TEXTURE = getResourceLocation("SSCraftingTableGUI");
    private GuiTextField searchField;
    private List<SNSCraftingRecipe> recipeList;


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

        searchField = new GuiTextField(0, fontRendererObj, guiLeft + 9, guiTop + 9, 151, fontRendererObj.FONT_HEIGHT);
        searchField.setMaxStringLength(60);
        searchField.setEnableBackgroundDrawing(false);
        searchField.setVisible(true);
        searchField.setTextColor(16777215);
        searchField.setFocused(true);

        //Temporary Item List:
        recipeList = Arrays.asList(new SNSCraftingRecipe[]{
                new SNSCraftingRecipe(new ItemStack(ModBlock.blockSteel, 1), new ItemStack(ModItem.ingotSteel, 15)),
                new SNSCraftingRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new SNSCraftingRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new SNSCraftingRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64)),
                new SNSCraftingRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64))

        });
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(TEXTURE);

        final int xStart = (width - xSize) / 2;
        final int yStart = (height - ySize) / 2;

        drawModalRectWithCustomSizedTexture(xStart, yStart, 0, 0, xSize, ySize, 273, 273);

        for (int i = 0; i < recipeList.size(); ++i) {
            mc.getTextureManager().bindTexture(TEXTURE);
            final int y = yStart + 24 + (23 * i);
            final int x = xStart + 8;
            drawModalRectWithCustomSizedTexture(x, y, 0, 227, 142, 23, 273, 273);


            GlStateManager.enableRescaleNormal();
            final SNSCraftingRecipe recipe = recipeList.get(i);
            final ImmutableList<ItemStack> output = recipe.output;
            final ItemStack outputItemStack = output.get(0);
            if (output.size() == 1 && outputItemStack.getItem() != null)
            {
                RenderHelper.enableGUIStandardItemLighting();
                itemRender.renderItemIntoGUI(outputItemStack, x + 2, y + 3);
                RenderHelper.disableStandardItemLighting();

                fontRendererObj.drawStringWithShadow(recipe.dislayName, x + 2 + 20, (float)y + 8, 16777215);
            }

            int inputItemCount = recipe.input.size();

            for (int j = 0; j < inputItemCount; ++j) {
                final ItemStack inputItemStack = recipe.input.get(j);

                final String requiredItemCount = String.format("%d", inputItemStack.stackSize);
                final int textWidth = fontRendererObj.getStringWidth(requiredItemCount);


                RenderHelper.enableGUIStandardItemLighting();
                itemRender.renderItemIntoGUI(inputItemStack, (xStart + 149) - ((16 + 2) * (j + 1)) , y + 3);
                RenderHelper.disableStandardItemLighting();

                GlStateManager.depthFunc(GL11.GL_ALWAYS);
                fontRendererObj.drawStringWithShadow(requiredItemCount, (xStart + 149) - ((16 + 2) * (j + 0)) - textWidth - 1 , y + 12, 16777215);
                GlStateManager.depthFunc(GL11.GL_LEQUAL);


            }

            GlStateManager.disableRescaleNormal();

        }

        searchField.drawTextBox();
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

    private void updateSearch()
    {
        //FIXME: make something to search in.
    }

    private class SNSCraftingRecipe {
        ImmutableList<ItemStack> output;
        ImmutableList<ItemStack> input;
        String dislayName;

        public SNSCraftingRecipe(Collection<ItemStack> input, String displayName, Collection<ItemStack> output)
        {
            this.input = ImmutableList.copyOf(input);
            this.dislayName = displayName;
            this.output = ImmutableList.copyOf(output);
        }

        public SNSCraftingRecipe(Collection<ItemStack> input, ItemStack output)
        {
            this.input = ImmutableList.copyOf(input);
            this.dislayName = output.getDisplayName();
            this.output = ImmutableList.of(output);
        }

        public SNSCraftingRecipe(ItemStack output, ItemStack... input)
        {
            this.input = ImmutableList.copyOf(input);

            String proposedName = output.getDisplayName();

            if (fontRendererObj.getStringWidth(proposedName) > 64) {
                while (fontRendererObj.getStringWidth(proposedName + "...") > 64) {
                    proposedName = proposedName.substring(0, proposedName.length() - 2);
                }
                proposedName += "...";
            }

            dislayName = proposedName;
            this.output = ImmutableList.of(output);
        }
    }
}
