package mod.steamnsteel.texturing;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.texturing.feature.PipesRuinWallFeature;
import mod.steamnsteel.texturing.feature.PlateRuinWallFeature;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class RuinWallTexture extends ProceduralConnectedTexture
{
    final int FEATURE_CROWN = 1 << 8;
    final int FEATURE_BASE = 1 << 9;
    final int FEATURE_PLATE = 1 << 10;
    final int FEATURE_PIPES = 1 << 11;

    final int FEATURE_MASK = FEATURE_PLATE | FEATURE_PIPES;


    @Override
    protected HashMap<Integer, IProceduralWallFeature> getFeatures()
    {
        HashMap<Integer, IProceduralWallFeature> features = new HashMap<Integer, IProceduralWallFeature>();
        features.put(FEATURE_PLATE, new PlateRuinWallFeature(this, FEATURE_PLATE));
        features.put(FEATURE_PIPES, new PipesRuinWallFeature(this, FEATURE_PIPES));
        return features;
    }

    @Override
    public Map<String, Integer[]> getIconMap()
    {

        final Map<String, Integer[]> icons = ImmutableMap.<String, Integer[]>builder()
                .put("Wall_Generic", new Integer[]{
                        DEFAULT,
                        LEFT,
                        RIGHT,
                        LEFT | RIGHT,
                        TOP | BOTTOM,
                        FEATURE_PLATE
                })
                .put("Wall_DD1_PipeA", new Integer[]{
                        FEATURE_PIPES | FEATURE_EDGE_TOP,
                        LEFT | FEATURE_PIPES | FEATURE_EDGE_TOP,
                        RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP,
                        LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP
                })
                .put("Wall_DD1_PipeB", new Integer[]{
                        FEATURE_PIPES | FEATURE_EDGE_BOTTOM,
                        LEFT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM,
                        RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM,
                        LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM
                })
                .put("Wall_DD2_PEdgeVL", new Integer[]{
                        FEATURE_PLATE | FEATURE_EDGE_LEFT,
                        FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT
                })
                .put("Wall_DD2_PEdgeVR", new Integer[]{
                        FEATURE_PLATE | FEATURE_EDGE_RIGHT,
                        FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT
                })
                .put("Wall_DD2_PEdgeHU", new Integer[]{
                        FEATURE_PLATE | FEATURE_EDGE_TOP,
                        FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP
                })
                .put("Wall_DD2_PEdgeHD", new Integer[]{
                        FEATURE_PLATE | FEATURE_EDGE_BOTTOM,
                        FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM
                })
                .put("Wall_DD2_PEdgeRDC", new Integer[]{
                        FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM,
                        FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM
                })
                .put("Wall_DD2_PEdgeRUC", new Integer[]{
                        FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP,
                        FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP
                })
                .put("Wall_DD2_PEdgeLDC", new Integer[]{
                        FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM,
                        FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM
                })
                .put("Wall_DD2_PEdgeLUC", new Integer[]{
                        FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP,
                        FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP
                })
                .put("Wall_CrownM_EdgeL", new Integer[]{
                        FEATURE_CROWN | TOP | LEFT,
                        FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP,
                        FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE
                })
                .put("Wall_CrownM_EdgeR", new Integer[]{
                        FEATURE_CROWN | TOP | RIGHT,
                        FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP,
                        FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE
                })
                .put("Wall_CrownM_AllEdge", new Integer[]{
                        FEATURE_CROWN | TOP | LEFT | RIGHT
                })
                .put("Wall_CrownM_Center", new Integer[]{
                        FEATURE_CROWN | TOP,
                        FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_TOP
                })
                .put("Wall_CrownM_DDPanelCL", new Integer[]{
                        FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP
                })
                .put("Wall_CrownM_DDPanelCR", new Integer[]{
                        FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP
                })
                .put("Wall_BaseM_EdgeL", new Integer[]{
                        FEATURE_BASE | BOTTOM | LEFT,
                        FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM
                })
                .put("Wall_BaseM_EdgeR", new Integer[]{
                        FEATURE_BASE | BOTTOM | RIGHT,
                        FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM
                })
                .put("Wall_BaseM_AllEdge", new Integer[]{
                        FEATURE_BASE | BOTTOM | LEFT | RIGHT
                })
                .put("Wall_BaseM_Center", new Integer[]{
                        FEATURE_BASE | BOTTOM,
                        FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM
                })
                .put("Wall_BaseM_DDPanelCL", new Integer[]{
                        FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT
                })
                .put("Wall_BaseM_DDPanelCR", new Integer[]{
                        FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT
                })
                .put("Wall_CrownM_EdgeL_DDPanelCL_S", new Integer[]{
                        FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT
                })
                .put("Wall_CrownM_EdgeL_DDPanelCR_S", new Integer[]{
                        FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT
                })
                .put("Wall_CrownM_EdgeR_DDPanelCR_S", new Integer[]{
                        FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT
                })
                .put("Wall_CrownM_EdgeR_DDPanelCL_S", new Integer[]{
                        FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT
                })
                .put("Wall_CrownM_AllEdge_DDPanelCR_S", new Integer[]{
                        FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT
                })
                .put("Wall_CrownM_AllEdge_DDPanelCL_S", new Integer[]{
                        FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT
                })
                .put("Wall_BaseM_EdgeL_DDPanelCL_S", new Integer[]{
                        FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT
                })
                .put("Wall_BaseM_EdgeL_DDPanelCR_S", new Integer[]{
                        FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT
                })
                .put("Wall_BaseM_EdgeR_DDPanelCL_S", new Integer[]{
                        FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT
                })
                .put("Wall_BaseM_EdgeR_DDPanelCR_S", new Integer[]{
                        FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT
                })
                .put("Wall_BaseM_AllEdge_DDPanelCR_S", new Integer[]{
                        FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT
                })
                .put("Wall_BaseM_AllEdge_DDPanelCL_S", new Integer[]{
                        FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT
                })
                .build();

        return icons;
    }


    protected int getTexturePropertiesForSide(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
    {
        try
        {
            int blockProperties = 0;
            ForgeDirection orientation = ForgeDirection.getOrientation(side);
            if (orientation == ForgeDirection.UP || orientation == ForgeDirection.DOWN)
            {
                return DEFAULT;
            }

            ForgeDirection left = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.LEFT, orientation);
            ForgeDirection right = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.RIGHT, orientation);
            ForgeDirection back = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BACK, orientation);
            ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);
            ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);

            boolean leftIsRuinWallAndNotObscured = checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left), back);
            boolean rightIsRuinWallAndNotObscured = checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right), back);
            boolean aboveIsRuinWallAndNotObscured = checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above), back);
            boolean belowIsRuinWallAndNotObscured = checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below), back);

            if (!aboveIsRuinWallAndNotObscured)
            {
                blockProperties |= TOP;
            }
            if (!belowIsRuinWallAndNotObscured)
            {
                blockProperties |= BOTTOM;
            }
            if (!leftIsRuinWallAndNotObscured)
            {
                blockProperties |= LEFT;
            }
            if (!rightIsRuinWallAndNotObscured)
            {
                blockProperties |= RIGHT;
            }

            int featureId = getValidFeature(blockAccess, worldBlockCoord, orientation);
            if (featureId != NO_FEATURE)
            {
                int subProperties = getSubProperties(blockAccess, worldBlockCoord, orientation, featureId);

                if ((subProperties & FEATURE_MASK) != 0)
                {
                    blockProperties |= subProperties;
                }
            }

            if ((blockProperties & BOTTOM) == BOTTOM)
            {
                blockProperties |= FEATURE_BASE;
                blockProperties &= (-1 ^ TOP); //Force the TOP bit off Not sure if I should do this...
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left).offset(below), back))
                {
                    blockProperties |= LEFT;
                }
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right).offset(below), back))
                {
                    blockProperties |= RIGHT;
                }
            } else if ((blockProperties & TOP) == TOP)
            {
                blockProperties |= FEATURE_CROWN;
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left).offset(above), back))
                {
                    blockProperties |= LEFT;
                }
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right).offset(above), back))
                {
                    blockProperties |= RIGHT;
                }
            }

            return blockProperties;
        } catch (Exception e)
        {
            e.printStackTrace();
            return MISSING_TEXTURE;
        }
    }

    private int getSubProperties(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId)
    {
        ForgeDirection left = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.LEFT, orientation);
        ForgeDirection right = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.RIGHT, orientation);
        ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);

        int subProperties = featureId;
        int leftBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(left), orientation);
        int rightBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(right), orientation);
        int aboveBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(above), orientation);
        int belowBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(below), orientation);

        if (leftBlockFeature != featureId)
        {
            subProperties |= FEATURE_EDGE_LEFT;
        }
        if (rightBlockFeature != featureId)
        {
            subProperties |= FEATURE_EDGE_RIGHT;
        }
        if (aboveBlockFeature != featureId)
        {
            subProperties |= FEATURE_EDGE_TOP;
        }
        if (belowBlockFeature != featureId)
        {
            subProperties |= FEATURE_EDGE_BOTTOM;
        }

        final int FEATURE_EDGE_TOP_AND_BOTTOM = FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM;
        final int FEATURE_EDGE_LEFT_AND_RIGHT = FEATURE_EDGE_LEFT | FEATURE_EDGE_RIGHT;

        switch (featureId)
        {
            case FEATURE_PIPES:
                //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
                subProperties &= featureId | FEATURE_EDGE_TOP_AND_BOTTOM;
                break;
            case FEATURE_PLATE:
                //Plates cannot be a single block wide.
                if ((subProperties & FEATURE_EDGE_TOP_AND_BOTTOM) == FEATURE_EDGE_TOP_AND_BOTTOM)
                {
                    subProperties = 0;
                } else if ((subProperties & FEATURE_EDGE_LEFT_AND_RIGHT) == FEATURE_EDGE_LEFT_AND_RIGHT)
                {
                    subProperties = 0;
                }
                break;
        }
        return subProperties;
    }

    public boolean checkRuinWallAndNotObscured(IBlockAccess blockAccess, WorldBlockCoord startingBlock, ForgeDirection back)
    {
        if (!(startingBlock.getBlock(blockAccess) instanceof PlotoniumRuinWall))
        {
            return false;
        }
        if (startingBlock.offset(back).getBlock(blockAccess).getMaterial().isOpaque())
        {
            return false;
        }
        return true;
    }


    protected String describeTextureProperties(int blockProperties)
    {
        StringBuilder sb = new StringBuilder();
        if ((blockProperties & FEATURE_PLATE) == FEATURE_PLATE)
        {
            sb.append("Plate,");
        }
        if ((blockProperties & FEATURE_CROWN) == FEATURE_CROWN)
        {
            sb.append("Crown,");
        }
        if ((blockProperties & FEATURE_BASE) == FEATURE_BASE)
        {
            sb.append("Base,");
        }
        if ((blockProperties & FEATURE_PIPES) == FEATURE_PIPES)
        {
            sb.append("Pipes,");
        }

        if ((blockProperties & TOP) == TOP)
        {
            sb.append("T,");
        }
        if ((blockProperties & BOTTOM) == BOTTOM)
        {
            sb.append("B,");
        }
        if ((blockProperties & LEFT) == LEFT)
        {
            sb.append("L,");
        }
        if ((blockProperties & RIGHT) == RIGHT)
        {
            sb.append("R,");
        }

        if ((blockProperties & FEATURE_EDGE_TOP) == FEATURE_EDGE_TOP)
        {
            sb.append("FE_T,");
        }
        if ((blockProperties & FEATURE_EDGE_BOTTOM) == FEATURE_EDGE_BOTTOM)
        {
            sb.append("FE_B,");
        }
        if ((blockProperties & FEATURE_EDGE_LEFT) == FEATURE_EDGE_LEFT)
        {
            sb.append("FE_L,");
        }
        if ((blockProperties & FEATURE_EDGE_RIGHT) == FEATURE_EDGE_RIGHT)
        {
            sb.append("FE_R,");
        }

        return sb.toString();
    }


}
