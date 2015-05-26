package mod.steamnsteel.client.gui;

import cpw.mods.fml.common.FMLLog;
import mod.steamnsteel.api.voxbox.IVoxBoxDictionary;
import mod.steamnsteel.api.voxbox.IVoxBoxEntry;
import mod.steamnsteel.api.voxbox.VoxBoxHandler;
import mod.steamnsteel.inventory.VoxBoxContainer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class VoxBoxGui extends SteamNSteelGui {
    private final static ResourceLocation TEXTURE = getResourceLocation("voxbox");
    private static final int TEXT_COLOR = 0xA0A0A0; //Light gray
    final int textBoxWidth = 169;
    final int textBoxHeight = 128;
    final int startX = 3;
    final int startY = 35;
    final int yIncrement = 10;
    final int paddingLeft = 10, paddingRight = 10; //Padding on either side of the box for text

    public static final int BUTTON_ID = 1010;

    public VoxBoxGui(InventoryPlayer playerInv){
        super(new VoxBoxContainer(playerInv));
        this.xSize = 173;
        this.ySize = 247;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);

        FontRenderer renderer = fontRendererObj;
        renderer.setUnicodeFlag(true);//Makes text smaller and uniform, same font as in botania's lexicon

        if(inventorySlots.getSlot(0).getHasStack()){
            IVoxBoxDictionary dictionary = VoxBoxHandler.voxBoxLibrary.get();
            IVoxBoxEntry entry = dictionary.getEntry(inventorySlots.getSlot(0).getStack());//The first slot is the ghost slot
            int line = 0;

            if(entry != null) {
                String entryText = StatCollector.translateToLocal(entry.getDialogueKey());
                String[] lines = entryText.split("<br>");//Split on the line break sequence

                for (String s : lines) {
                    if ((startX + paddingLeft) + renderer.getStringWidth(s) > textBoxWidth - paddingRight) {
                        FMLLog.warning("Line %d does not fit properly in voxbox gui!", line + 1);
                    }
                    if (line * yIncrement + startY > textBoxHeight) {
                        FMLLog.warning("Text does not fit within the voxbox gui text area!");
                    }

                    this.drawString(renderer, s, startX + paddingLeft, startY + line * yIncrement, TEXT_COLOR);

                    line++;
                }
            }else{
                this.drawString(renderer, "No entry found for item.", startX + paddingLeft * 2, startY, TEXT_COLOR);
            }
        }else{
            this.drawString(renderer, "No entry found for item.", startX + paddingLeft * 2, startY, TEXT_COLOR);
        }

        renderer.setUnicodeFlag(false);//Return text to normal
        //TODO draw text for vox box entry
    }

    @Override
    protected String getInventoryName() {
        return "VoxBox";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);

        final int xStart = (width - xSize) / 2;
        final int yStart = (height - ySize) / 2;
        drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
    }


}
