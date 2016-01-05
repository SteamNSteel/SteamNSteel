package mod.steamnsteel.client.gui;

import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.inventory.ProjectTableContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by codew on 5/01/2016.
 */
public class ProjectTableGui extends SteamNSteelGui {
    private static final ResourceLocation TEXTURE = getResourceLocation("SSCraftingTableGUI");

    public ProjectTableGui(InventoryPlayer playerInventory) {
        super(new ProjectTableContainer(playerInventory));
    }

    @Override
    protected String getInventoryName() {
        return "Project Table";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(TEXTURE);

        final int xStart = (width - xSize) / 2;
        final int yStart = (height - ySize) / 2;
        drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);

        int scaleAdjustment;
        /*if (te.isActive())
        {
            scaleAdjustment = te.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(xStart + 43, yStart + 26 + 23 - scaleAdjustment, 176, 12 - scaleAdjustment, 14, scaleAdjustment + 2);
        }

        scaleAdjustment = te.getCookProgressScaled(24);
        drawTexturedModalRect(xStart + 78, yStart + 35, 176, 14, scaleAdjustment + 1, 16);*/
    }
}
