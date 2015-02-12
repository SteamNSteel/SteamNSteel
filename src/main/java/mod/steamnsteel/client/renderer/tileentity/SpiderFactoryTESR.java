package mod.steamnsteel.client.renderer.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.block.machine.SpiderFactoryBlock;
import mod.steamnsteel.client.renderer.model.SpiderFactoryModel;
import mod.steamnsteel.tileentity.SpiderFactoryTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SpiderFactoryTESR extends SteamNSteelTESR
{
    public static final ResourceLocation TEXTURE = getResourceLocation(SpiderFactoryBlock.NAME);

    private final SpiderFactoryModel model = new SpiderFactoryModel();

    @Override
    public void renderTileEntityAt(TileEntity te, double x1, double y1, double z1, float partialTickTime)
    {
        SpiderFactoryTE factoryTE = (SpiderFactoryTE) te;
        if (factoryTE.isSlave()) return;

        bindTexture(TEXTURE);
        GL11.glPushMatrix();
        GL11.glTranslated(x1, y1, z1);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glRotatef(180F, 1F, 0F, 0F);

        model.render();
        GL11.glPopMatrix();
    }
}
