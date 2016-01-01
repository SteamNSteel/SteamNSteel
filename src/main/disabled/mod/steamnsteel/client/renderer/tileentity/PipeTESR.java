package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.client.renderer.model.PipeAlternateModel;
import mod.steamnsteel.client.renderer.model.PipeModel;
import mod.steamnsteel.tileentity.BasePlumbingTE;
import mod.steamnsteel.tileentity.PipeTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class PipeTESR extends SteamNSteelTESR
{
    public static ResourceLocation TEXTURE = getResourceLocation(PipeBlock.NAME);
    public static ResourceLocation TEXTURE_ALTERNATE = getResourceLocation(PipeBlock.NAME + "-alt");
    public static ResourceLocation TEXTURE_CAP = getResourceLocation(PipeBlock.NAME + "_cap");
    public static ResourceLocation TEXTURE_JUNCTION_BOX = getResourceLocation(PipeBlock.NAME + "_junctionbox");
    private final PipeModel model = new PipeModel();
    private final PipeAlternateModel alternateModel = new PipeAlternateModel();
    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static final ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(0.5f, 0.0f, 0.5f);

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float tick, int whatDoesThisDo)
    {
        if (tileEntity instanceof PipeTE)
        {
            final PipeTE te = (PipeTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) posX, (float) posY, (float) posZ);

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

        EnumFacing endA = te.getEndADirection();
        EnumFacing endB = te.getEndBConnected();

        final int NORTH_WEST = 1;
        final int NORTH_EAST = 2;
        final int SOUTH_WEST = 3;
        final int SOUTH_EAST = 4;

        int renderType = 0;

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
                            break;
                        case SOUTH:
                            break;
                        case UP:
                            break;
                        case DOWN:
                            break;
                    }
                    break;
                case EAST:
                    switch (endB)
                    {
                        case NORTH:
                            break;
                        case SOUTH:
                            break;
                        case UP:
                            break;
                        case DOWN:
                            break;
                    }
                    break;
                case NORTH:
                    switch (endB)
                    {
                        case WEST:
                            GL11.glRotatef(-90, 0f, 1f, 0f);
                            GL11.glRotatef(90, 0f, 0f, 1f);
                            renderType = NORTH_WEST;
                            break;
                        case EAST:
                            GL11.glRotatef(90, 0f, 1f, 0f);
                            GL11.glRotatef(-90, 0f, 0f, 1f);
                            renderType = NORTH_EAST;
                            break;
/*                        case UP:
                            break;
                        case DOWN:
                            break;*/
                    }

                    break;
                case SOUTH:
                    switch (endB)
                    {
                        case WEST:
                            GL11.glRotatef(90, 0f, 0f, 1f);
                            GL11.glRotatef(0, 0f, 0f, 1f);
                            renderType = SOUTH_WEST;
                            break;
                        case EAST:
                            GL11.glRotatef(90, 0f, 1f, 0f);
                            GL11.glRotatef(90, 0f, 0f, 1f);
                            renderType = SOUTH_EAST;
                            break;
/*                        case UP:
                            break;
                        case DOWN:
                            break;*/
                    }
                    break;
                case DOWN:

                    switch (endB) {
                        case SOUTH:
                            GL11.glRotatef(180, 0f, 0f, 1f);
                            renderType = SOUTH_WEST;
                            break;
                        case NORTH:
                            GL11.glRotatef(180, 1f, 0f, 0f);
                            renderType = SOUTH_EAST;
                            break;
                        case EAST:
                            GL11.glRotatef(90, 1f, 0f, 0f);
                            GL11.glRotatef(90, 0f, 1f, 0f);
                            GL11.glRotatef(90, 0f, 0f, 1f);
                            renderType = SOUTH_EAST;
                            break;
                        case WEST:
                            GL11.glRotatef(90, 1f, 0f, 0f);
                            GL11.glRotatef(90, 0f, 0f, 1f);
                            GL11.glRotatef(0, 0f, 0f, 1f);
                            renderType = SOUTH_WEST;
                            break;
                    }
                    break;
                case UP:
                    switch (endB) {
                        case SOUTH:
                            GL11.glRotatef(180, 0f, 1f, 0f);
                            GL11.glRotatef(-90, 1f, 0f, 0f);
                            renderType = SOUTH_EAST;
                            break;
                        case NORTH:
                            renderType = NORTH_EAST;
                            GL11.glRotatef(-90, 1f, 0f, 0f);
                            GL11.glRotatef(0, 0f, 1f, 0f);
                            break;
                        case EAST:
                            renderType = NORTH_EAST;
                            GL11.glRotatef(90, 1f, 0f, 0f);
                            GL11.glRotatef(90, 0f, 1f, 0f);
                            GL11.glRotatef(-90, 0f, 0f, 1f);
                            break;
                        case WEST:
                            GL11.glRotatef(90, 1f, 0f, 0f);
                            GL11.glRotatef(-90, 0f, 1f, 0f);
                            GL11.glRotatef(90, 0f, 0f, 1f);
                            renderType = NORTH_WEST;
                            break;
                    }

                    break;
            }
        }
        GL11.glTranslatef(0f, -0.5f, 0f);
        if (renderCorner)
        {
            switch (renderType) {
                case 1: // NORTH_WEST
                    if (!te.shouldUseAlternateModel())
                    {
                        bindTexture(TEXTURE);
                        model.renderPipeCornerNW();
                    } else
                    {
                        bindTexture(TEXTURE_ALTERNATE);
                        alternateModel.renderPipeCornerNW();
                    }
                    break;
                case 2: // NORTH EAST
                    bindTexture(TEXTURE);
                    model.renderPipeCornerNE();
                    break;
                case 3: // SOUTH WEST
                    bindTexture(TEXTURE);
                    model.renderPipeCornerSW();
                    break;
                case 4:
                    if (!te.shouldUseAlternateModel())
                    {
                        bindTexture(TEXTURE);
                        model.renderPipeCornerSE();
                    } else
                    {
                        bindTexture(TEXTURE_ALTERNATE);
                        //FIXME: This should be SE, not NW, but I need a new texture/model for that.
                        alternateModel.renderPipeCornerNW();
                        //alternateModel.renderPipeCornerSE();

                    }
                    break;
                default:
                    bindTexture(TEXTURE_JUNCTION_BOX);
                    model.renderJunctionBox();
                    break;
            }

            //model.renderPipeCorner();
        } else
        {
            bindTexture(TEXTURE);
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

    private void renderCap(EnumFacing direction)
    {
        switch (direction)
        {
            case EAST:
                GL11.glRotatef(-90, 0.0f, 0.0f, 1.0f);
                break;
            case WEST:
                GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
                break;
            case SOUTH:
                GL11.glRotatef(-90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
                break;
            case NORTH:
                GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
                break;
            case DOWN:
                GL11.glRotatef(-180, 0.0f, 0.0f, 1.0f);
                break;
            case UP:
                break;
        }
        GL11.glTranslatef(0f,  (5f * (1f / 16f)), 0f);
        model.renderPipeOpening();
    }
}
