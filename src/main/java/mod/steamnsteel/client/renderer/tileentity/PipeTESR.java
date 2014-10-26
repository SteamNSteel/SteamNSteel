package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.client.renderer.model.PipeModel;
import mod.steamnsteel.tileentity.PipeTE;
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
        // Open Render buffer
        GL11.glPushMatrix();

        GL11.glTranslatef(0f, 0.5f, 0f);
        GL11.glTranslatef(OFFSET.getLeft(), OFFSET.getMiddle(), OFFSET.getRight());

        GL11.glPushMatrix();
        // Inherent adjustments to model

        ForgeDirection endA = te.getEndADirection();
        ForgeDirection endB = te.getEndBConnected();

        // Orient the model to match the placement
        boolean renderCorner = te.shouldRenderAsCorner();
        if (!renderCorner)
        {
            switch (endA)
            {
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
                case DOWN:
                    GL11.glRotatef(180, 0.0f, 0.0f, 1.0f);
                    break;
            }
        } else
        {
            switch (endA)
            {
                case WEST:

                    switch (endB)
                    {
                        case NORTH:
                            GL11.glRotatef(90, 1f, 0f, 0f);
                            break;
                        case SOUTH:
                            GL11.glRotatef(270, 1f, 0f, 0f);
                            break;
                        case UP:
                            break;
                        case DOWN:
                            GL11.glRotatef(180, 1f, 0f, 0f);
                            break;
                    }
                    GL11.glRotatef(270, 0f, 1f, 0f);
                    break;
                case EAST:
                    switch (endB)
                    {
                        case NORTH:
                            GL11.glRotatef(90, 1f, 0f, 0f);
                            break;
                        case SOUTH:
                            GL11.glRotatef(270, 1f, 0f, 0f);
                            break;
                        case UP:
                            break;
                        case DOWN:
                            GL11.glRotatef(180, 1f, 0f, 0f);
                            break;
                    }
                    GL11.glRotatef(90, 0f, 1f, 0f);
                    break;
                case NORTH:
                    switch (endB)
                    {
                        case WEST:
                            GL11.glRotatef(90, 0f, 0f, 1f);
                            break;
                        case EAST:
                            GL11.glRotatef(270, 0f, 0f, 1f);
                            break;
                        case UP:
                            break;
                        case DOWN:
                            GL11.glRotatef(180, 0f, 0f, 1f);
                            break;
                    }
                    GL11.glRotatef(180, 0f, 1f, 0f);
                    break;
                case SOUTH:
                    switch (endB)
                    {
                        case WEST:
                            GL11.glRotatef(90, 0f, 0f, 1f);
                            break;
                        case EAST:
                            GL11.glRotatef(270, 0f, 0f, 1f);
                            break;
                        case UP:
                            break;
                        case DOWN:
                            GL11.glRotatef(180, 0f, 0f, 1f);
                            break;
                    }
                    break;
            }
        }
        GL11.glTranslatef(0f, -0.5f, 0f);
        bindTexture(TEXTURE);
        if (renderCorner)
        {
            model.renderPipeCorner();
        } else
        {
            model.renderPipeStraight();
        }

        GL11.glPopMatrix();


        if (endA != null && !te.isEndAConnected())
        {
            GL11.glPushMatrix();
            renderCap(endA);
            GL11.glPopMatrix();
        }

        if (endB != null && !te.isEndBConnected())
        {
            GL11.glPushMatrix();
            renderCap(endB);
            GL11.glPopMatrix();
        }

        // Close Render Buffer
        GL11.glPopMatrix();
    }

    private void renderCap(ForgeDirection direction)
    {
        switch (direction)
        {
            case EAST:
                GL11.glRotatef(-90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
                break;
            case WEST:
                GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(-180, 1.0F, 0.0F, 0.0F);
                break;
            case NORTH:
                GL11.glRotatef(-90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
                break;
            case DOWN:
                //GL11.glRotatef(-180, 0.0f, 0.0f, 1.0f);
                break;
            case UP:
                GL11.glRotatef(-180, 0.0f, 0.0f, 1.0f);
                break;
        }
        GL11.glTranslatef(0f, -0.5f, 0f);
        model.renderPipeCap();
    }
}
