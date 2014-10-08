package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.client.renderer.model.CupolaModel;
import mod.steamnsteel.client.renderer.model.PipeModel;
import mod.steamnsteel.tileentity.CupolaTE;
import mod.steamnsteel.tileentity.PipeTE;
import mod.steamnsteel.utility.Orientation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class PipeTESR extends SteamNSteelTESR
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
        if (tileEntity instanceof PipeTE)
        {
            final PipeTE te = (PipeTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) x, (float) y, (float) z);

            renderPipe(te);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderPipe(PipeTE te)
    {

        final int x = te.xCoord;
        final int y = te.yCoord;
        final int z = te.zCoord;
        final World world = te.getWorldObj();

        // Open Render buffer
        GL11.glPushMatrix();

        // Inherent adjustments to model


        // Orient the model to match the placement
        final ForgeDirection orientation = te.getRenderOrientation();

        GL11.glTranslatef(OFFSET.getLeft(), OFFSET.getMiddle(), OFFSET.getRight());
        switch (orientation) {
            case EAST:
                GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
                break;
            case WEST:
                GL11.glRotatef(-90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(-180, 1.0F, 0.0F, 0.0F);
                break;
            case NORTH:
                GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glRotatef(-90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
                break;
        }
        GL11.glTranslatef(0f, -0.5f, 0f);

        switch (te.getRenderState()) {
            case 0:
                bindTexture(TEXTURE);
                model.renderPipe2xC();
                break;
            case 1:
                bindTexture(TEXTURE);
                model.renderPipe1xC();
                break;
            case 2:
                bindTexture(TEXTURE);
                model.renderPipe0C();
                break;
            case 3:
                bindTexture(TEXTURE);
                model.renderPipe1xCrnr();
                break;
            case 4:
                bindTexture(TEXTURE_JUNCTION_BOX);
                model.renderJunctionBox();
                break;
        }

        // Close Render Buffer
        GL11.glPopMatrix();
    }
}
