package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.client.renderer.model.PipeModel;
import mod.steamnsteel.tileentity.PipeJunctionTE;
import mod.steamnsteel.tileentity.PipeTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class PipeJunctionTESR extends SteamNSteelTESR
{
    public static ResourceLocation TEXTURE = getResourceLocation(PipeBlock.NAME);
    public static ResourceLocation TEXTURE_CAP = getResourceLocation(PipeBlock.NAME + "_cap");
    public static ResourceLocation TEXTURE_JUNCTION_BOX = getResourceLocation(PipeBlock.NAME + "_junctionbox");
    private final PipeModel model = new PipeModel();
    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static final ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(0.5f, 0.0f, 0.5f);

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        if (tileEntity instanceof PipeJunctionTE)
        {
            final PipeJunctionTE te = (PipeJunctionTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) x, (float) y, (float) z);

            renderJunction(te);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderJunction(PipeJunctionTE te)
    {

        final int x = te.xCoord;
        final int y = te.yCoord;
        final int z = te.zCoord;
        final World world = te.getWorldObj();

        // Open Render buffer
        GL11.glPushMatrix();

        // Inherent adjustments to model

        // Orient the model to match the placement
        GL11.glTranslatef(OFFSET.getLeft(), OFFSET.getMiddle(), OFFSET.getRight());
        GL11.glTranslatef(0f, 0f, 0f);
        bindTexture(TEXTURE_JUNCTION_BOX);
        model.renderJunctionBox();

        // Close Render Buffer
        GL11.glPopMatrix();
    }
}
