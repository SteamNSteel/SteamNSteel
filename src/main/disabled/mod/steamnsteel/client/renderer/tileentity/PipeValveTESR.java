package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.PipeValveBlock;
import mod.steamnsteel.client.renderer.model.PipeValveModel;
import mod.steamnsteel.tileentity.PipeValveTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class PipeValveTESR extends SteamNSteelTESR
{
    public static ResourceLocation TEXTURE = getResourceLocation(PipeValveBlock.NAME);
    private final PipeValveModel model = new PipeValveModel();
    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static final ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(0.5f, 0.5f, 0.5f);

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float tick, int whatDoesThisDo)
    {
        if (tileEntity instanceof PipeValveTE)
        {
            final PipeValveTE te = (PipeValveTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) posX, (float) posY, (float) posZ);

            renderValve(te);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderValve(PipeValveTE te)
    {
        // Open Render buffer
        GL11.glPushMatrix();

        // Inherent adjustments to model

        // Orient the model to match the placement
        GL11.glTranslatef(OFFSET.getLeft(), OFFSET.getMiddle(), OFFSET.getRight());
        bindTexture(PipeTESR.TEXTURE);
        model.renderPipe();
        model.renderOpeningA();
        model.renderOpeningB();

        bindTexture(TEXTURE);
        model.renderBody();
        model.renderValve();

        // Close Render Buffer
        GL11.glPopMatrix();
    }
}
