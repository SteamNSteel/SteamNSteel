package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.PipeRedstoneValveBlock;
import mod.steamnsteel.block.machine.PipeValveBlock;
import mod.steamnsteel.client.renderer.model.PipeRedstoneValveModel;
import mod.steamnsteel.tileentity.PipeRedstoneValveTE;
import mod.steamnsteel.tileentity.PipeValveTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class PipeRedstoneValveTESR extends SteamNSteelTESR
{
    //Uses the textures from the PipeValveBlock
    public static ResourceLocation TEXTURE = getResourceLocation(PipeValveBlock.NAME);
    public static ResourceLocation TEXTURE_ON = getResourceLocation(PipeRedstoneValveBlock.NAME + "_on");
    public static ResourceLocation TEXTURE_OFF = getResourceLocation(PipeRedstoneValveBlock.NAME + "_off");

    private final PipeRedstoneValveModel model = new PipeRedstoneValveModel();
    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static final ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(0.5f, 0.5f, 0.5f);

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float tick, int whatDoesThisDo)
    {
        if (tileEntity instanceof PipeRedstoneValveTE)
        {
            final PipeRedstoneValveTE te = (PipeRedstoneValveTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) posX, (float) posY, (float) posZ);

            renderRedstoneValve(te);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderRedstoneValve(PipeRedstoneValveTE te)
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

        bindTexture(PipeValveTESR.TEXTURE);
        model.renderBody();


        bindTexture(TEXTURE);

        if (true) {
            bindTexture(TEXTURE_ON);

        }
        model.renderRedstoneValve();

        //bindTexture(TEXTURE);
        //model.renderAll();

        // Close Render Buffer
        GL11.glPopMatrix();
    }
}
