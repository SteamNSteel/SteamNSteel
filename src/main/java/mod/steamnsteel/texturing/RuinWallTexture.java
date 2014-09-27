package mod.steamnsteel.texturing;

import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.texturing.feature.OneByOneRuinWallFeature;
import mod.steamnsteel.texturing.feature.OneByThreeRuinWallFeature;
import mod.steamnsteel.texturing.feature.PipesRuinWallFeature;
import mod.steamnsteel.texturing.feature.PlateRuinWallFeature;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RuinWallTexture extends ProceduralConnectedTexture
{
    static final int FEATURE_CROWN = 1 << 12;
    static final int FEATURE_BASE = 1 << 13;

    public static final int FEATURE_PIPES = 1 << 14;
    public static final int FEATURE_VENT = 1 << 15;
    public static final int FEATURE_SCREEN = 1 << 16;
    public static final int FEATURE_VALVE = 1 << 17;
    public static final int FEATURE_PLATE = 1 << 18;
    public static final int FEATURE_METAL_TEAR_H = 1 << 19;

    final int FEATURE_MASK = FEATURE_PLATE | FEATURE_PIPES | FEATURE_SCREEN | FEATURE_VALVE | FEATURE_VENT | FEATURE_METAL_TEAR_H;

    public static final int FEATURE_PLATE_TL_CORNER = 1 << 8;
    public static final int FEATURE_PLATE_TR_CORNER = 1 << 9;
    public static final int FEATURE_PLATE_BL_CORNER = 1 << 10;
    public static final int FEATURE_PLATE_BR_CORNER = 1 << 11;

    @Override
    protected HashMap<Integer, IProceduralWallFeature> getFeatures()
    {
        HashMap<Integer, IProceduralWallFeature> features = new HashMap<Integer, IProceduralWallFeature>();
        features.put(FEATURE_PIPES, new PipesRuinWallFeature(this, FEATURE_PIPES));
        features.put(FEATURE_VENT, new OneByOneRuinWallFeature(this, FEATURE_VENT));
        features.put(FEATURE_VALVE, new OneByOneRuinWallFeature(this, FEATURE_VALVE));
        features.put(FEATURE_SCREEN, new OneByOneRuinWallFeature(this, FEATURE_SCREEN));
        features.put(FEATURE_PLATE, new PlateRuinWallFeature(this, FEATURE_PLATE));
        features.put(FEATURE_METAL_TEAR_H, new OneByThreeRuinWallFeature(this, FEATURE_METAL_TEAR_H));

        return features;
    }

    @Override
    protected void registerIcons(ITextureConditionSet textures)
    {
        textures.useTexture("ruinWallPlotonium")
                .forCondition(DEFAULT)
                .andCondition(LEFT)
                .andCondition(RIGHT)
                .andCondition(LEFT | RIGHT)
                .andCondition(TOP | BOTTOM)
                .andCondition(FEATURE_PLATE)
                        //Hacks around a hard to detect issue of two pipes spawning in a 1x4 stack.
                        //An alternative would be to replace these with a 1x1 texture.
                .andCondition(FEATURE_PIPES | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM)
                .andCondition(FEATURE_PIPES | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | RIGHT)
                .andCondition(FEATURE_PIPES | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT)
                .andCondition(FEATURE_PIPES | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT | RIGHT);

        textures.useTexture("Wall_DD1_PipeA")
                .forCondition(FEATURE_PIPES | FEATURE_EDGE_TOP)
                .andCondition(LEFT | FEATURE_PIPES | FEATURE_EDGE_TOP)
                .andCondition(RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP)
                .andCondition(LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_DDVent")
                .forCondition(FEATURE_VENT)
                .andCondition(FEATURE_VENT | LEFT)
                .andCondition(FEATURE_VENT | RIGHT)
                .andCondition(FEATURE_VENT | LEFT | RIGHT);

        textures.useTexture("Wall_DDScreen")
                .forCondition(FEATURE_SCREEN)
                .andCondition(FEATURE_SCREEN | LEFT)
                .andCondition(FEATURE_SCREEN | RIGHT)
                .andCondition(FEATURE_SCREEN | LEFT | RIGHT);

        textures.useTexture("Wall_DDValve")
                .forCondition(FEATURE_VALVE)
                .andCondition(FEATURE_VALVE | LEFT)
                .andCondition(FEATURE_VALVE | RIGHT)
                .andCondition(FEATURE_VALVE | LEFT | RIGHT);

        textures.useTexture("Wall_DDLongPipe_L")
                .forCondition(FEATURE_METAL_TEAR_H | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DDLongPipe_M")
                .forCondition(FEATURE_METAL_TEAR_H);

        textures.useTexture("Wall_DDLongPipe_R")
                .forCondition(FEATURE_METAL_TEAR_H | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_DD1_PipeB")
                .forCondition(FEATURE_PIPES | FEATURE_EDGE_BOTTOM)
                .andCondition(LEFT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM)
                .andCondition(RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM)
                .andCondition(LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_DD2_PEdgeVL")
                .forCondition(FEATURE_PLATE | FEATURE_EDGE_LEFT)
                .andCondition(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DD2_PEdgeVR")
                .forCondition(FEATURE_PLATE | FEATURE_EDGE_RIGHT)
                .andCondition(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_DD2_PEdgeHU")
                .forCondition(FEATURE_PLATE | FEATURE_EDGE_TOP)
                .andCondition(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_DD2_PEdgeHD")
                .forCondition(FEATURE_PLATE | FEATURE_EDGE_BOTTOM)
                .andCondition(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_DD2_PEdgeRDC")
                .forCondition(FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                .andCondition(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                .andCondition(FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                .andCondition(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                .andCondition(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BR_CORNER)
                .andCondition(FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeRUC")
                .forCondition(FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                .andCondition(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                .andCondition(FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                .andCondition(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                .andCondition(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TR_CORNER)
                .andCondition(FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeLDC")
                .forCondition(FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                .andCondition(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                .andCondition(FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                .andCondition(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                .andCondition(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BL_CORNER)
                .andCondition(FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeLUC")
                .forCondition(FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                .andCondition(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                .andCondition(FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                .andCondition(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TL_CORNER)
                .andCondition(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                .andCondition(FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER)
                .andCondition(FEATURE_PLATE | FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerRDC_S")
                .forCondition(FEATURE_PLATE | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerRUC_S")
                .forCondition(FEATURE_PLATE | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLDC_S")
                .forCondition(FEATURE_PLATE | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLUC_S")
                .forCondition(FEATURE_PLATE | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLURDC_S")
                .forCondition(FEATURE_PLATE | FEATURE_PLATE_TL_CORNER | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLDRUC_S")
                .forCondition(FEATURE_PLATE | FEATURE_PLATE_BL_CORNER | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_CrownM_EdgeL")
                .forCondition(FEATURE_CROWN | TOP | LEFT)
                .andCondition(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP)
                .andCondition(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE);

        textures.useTexture("Wall_CrownM_EdgeR")
                .forCondition(FEATURE_CROWN | TOP | RIGHT)
                .andCondition(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP)
                .andCondition(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE);

        textures.useTexture("Wall_CrownM_AllEdge")
                .forCondition(FEATURE_CROWN | TOP | LEFT | RIGHT);

        textures.useTexture("Wall_CrownM_Center")
                .forCondition(FEATURE_CROWN | TOP)
                .andCondition(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_CrownM_DDPanelCL")
                .forCondition(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_CrownM_DDPanelCR")
                .forCondition(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_BaseM_EdgeL")
                .forCondition(FEATURE_BASE | BOTTOM | LEFT)
                .andCondition(FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_EdgeR")
                .forCondition(FEATURE_BASE | BOTTOM | RIGHT)
                .andCondition(FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_AllEdge")
                .forCondition(FEATURE_BASE | BOTTOM | LEFT | RIGHT);

        textures.useTexture("Wall_BaseM_Center")
                .forCondition(FEATURE_BASE | BOTTOM)
                .andCondition(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_DDPanelCL")
                .forCondition(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_BaseM_DDPanelCR")
                .forCondition(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_CrownM_EdgeL_DDPanelCL_S")
                .forCondition(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                .andCondition(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_CrownM_EdgeL_DDPanelCR_S")
                .forCondition(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_CrownM_EdgeR_DDPanelCR_S")
                .forCondition(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                .andCondition(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER)        ;

        textures.useTexture("Wall_CrownM_EdgeR_DDPanelCL_S")
                .forCondition(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_CrownM_AllEdge_DDPanelCR_S")
                .forCondition(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                .andCondition(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_CrownM_AllEdge_DDPanelCL_S")
                .forCondition(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                .andCondition(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_BaseM_EdgeL_DDPanelCL_S")
                .forCondition(FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                .andCondition(FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_BaseM_EdgeL_DDPanelCR_S")
                .forCondition(FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_BaseM_EdgeR_DDPanelCL_S")
                .forCondition(FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_BaseM_EdgeR_DDPanelCR_S")
                .forCondition(FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                .andCondition(FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_BaseM_AllEdge_DDPanelCR_S")
                .forCondition(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                        //.andCondition(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_BR_CORNER
                .andCondition(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_BaseM_AllEdge_DDPanelCL_S")
                .forCondition(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                        //.andCondition(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_BL_CORNER
                .andCondition(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);
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

            IProceduralWallFeature feature = getValidFeature(blockAccess, worldBlockCoord, orientation);
            if (feature != null)
            {
                int subProperties = feature.getSubProperties(blockAccess, worldBlockCoord, orientation);

                if ((subProperties & FEATURE_MASK) != 0)
                {
                    blockProperties |= subProperties;
                }
            }

            if ((blockProperties & BOTTOM) == BOTTOM)
            {
                blockProperties |= FEATURE_BASE;
                blockProperties &= (~TOP); //Force the TOP bit off Not sure if I should do this...
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left).offset(below), back))
                {
                    blockProperties |= LEFT;
                }
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right).offset(below), back))
                {
                    blockProperties |= RIGHT;
                }

                //Break up the bases
                if ((getCrownSplitOpportunity(worldBlockCoord) & 14) == 0)
                {
                    blockProperties |= LEFT;
                }
                if ((getCrownSplitOpportunity(worldBlockCoord.offset(right)) & 14) == 0)
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

                //Break up the crowns
                if ((getCrownSplitOpportunity(worldBlockCoord) & 14) == 0)
                {
                    blockProperties |= LEFT;
                }
                if ((getCrownSplitOpportunity(worldBlockCoord.offset(right)) & 14) == 0)
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

    private int getCrownSplitOpportunity(WorldBlockCoord worldBlockCoord)
    {
        int x = worldBlockCoord.getX();
        int y = worldBlockCoord.getY();
        int z = worldBlockCoord.getZ();
        //return (worldBlockCoord.getX() * 7) + (worldBlockCoord.getY() * (worldBlockCoord.getX() | worldBlockCoord.getZ())) + (~worldBlockCoord.getZ() * 31);
        Random r = new Random(x * y * z * 31);
        return r.nextInt();
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
        if ((blockProperties & FEATURE_VENT) == FEATURE_VENT)
        {
            sb.append("Vent,");
        }
        if ((blockProperties & FEATURE_SCREEN) == FEATURE_SCREEN)
        {
            sb.append("Screen,");
        }
        if ((blockProperties & FEATURE_VALVE) == FEATURE_VALVE)
        {
            sb.append("Valve,");
        }
        if ((blockProperties & FEATURE_METAL_TEAR_H) == FEATURE_METAL_TEAR_H)
        {
            sb.append("Metal Tear H,");
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

        if ((blockProperties & FEATURE_PLATE_TL_CORNER) == FEATURE_PLATE_TL_CORNER)
        {
            sb.append("TLCRNR,");
        }
        if ((blockProperties & FEATURE_PLATE_TR_CORNER) == FEATURE_PLATE_TR_CORNER)
        {
            sb.append("TRCRNR,");
        }
        if ((blockProperties & FEATURE_PLATE_BL_CORNER) == FEATURE_PLATE_BL_CORNER)
        {
            sb.append("BLCRNR,");
        }
        if ((blockProperties & FEATURE_PLATE_BR_CORNER) == FEATURE_PLATE_BR_CORNER)
        {
            sb.append("BRCRNR,");
        }

        return sb.toString();
    }


}
