package mod.steamnsteel.texturing;

import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.texturing.feature.*;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import java.util.Random;

public class RuinWallTexture extends ProceduralConnectedTexture
{
    public static final int FEATURE_PLATE_TL_CORNER = 1 << 8;
    public static final int FEATURE_PLATE_TR_CORNER = 1 << 9;
    public static final int FEATURE_PLATE_BL_CORNER = 1 << 10;
    public static final int FEATURE_PLATE_BR_CORNER = 1 << 11;

    private static final int LAYER_PLATE = 1;
    private static final int LAYER_CROWN_AND_BASE = 2;
    private static final int LAYER_DOODADS = 3;
    private PlateRuinWallFeature featurePlate;
    private PipesRuinWallFeature featurePipes;
    private OneByOneWallFeature featureVent;
    private OneByOneWallFeature featureValve;
    private OneByOneWallFeature featureScreen;
    private ThreeByOneWallFeature featureHorizontalMetalTear;
    private TopBandWallFeature featureCrown;
    private BottomBandWallFeature featureBase;

    @Override
    protected void registerFeatures(IFeatureRegistry features)
    {
        featurePlate = new PlateRuinWallFeature(this, LAYER_PLATE);
        featureCrown = new TopBandWallFeature(this, "Crown", LAYER_CROWN_AND_BASE);
        featureBase = new BottomBandWallFeature(this, "Base", LAYER_CROWN_AND_BASE);
        featurePipes = new PipesRuinWallFeature(this, LAYER_DOODADS);
        featureVent = new OneByOneWallFeature(this, "Vent", LAYER_DOODADS);
        featureValve = new OneByOneWallFeature(this, "Valve", LAYER_DOODADS);
        featureScreen = new OneByOneWallFeature(this, "Screen", LAYER_DOODADS);
        featureHorizontalMetalTear = new ThreeByOneWallFeature(this, "HorizontalMetalTear", LAYER_DOODADS);

        features.registerFeature(featurePlate);
        features.registerFeature(featureCrown);
        features.registerFeature(featureBase);
        features.registerFeature(featurePipes);
        features.registerFeature(featureVent);
        features.registerFeature(featureValve);
        features.registerFeature(featureScreen);
        features.registerFeature(featureHorizontalMetalTear);
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
                .andCondition(featurePlate.getFeatureId())
                        //Hacks around a hard to detect issue of two pipes spawning in a 1x4 stack.
                        //An alternative would be to replace these with a 1x1 texture.
                .andCondition(featurePipes.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM)
                .andCondition(featurePipes.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | RIGHT)
                .andCondition(featurePipes.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT)
                .andCondition(featurePipes.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT | RIGHT);

        textures.useTexture("Wall_DD1_PipeA")
                .forCondition(featurePipes.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(LEFT | featurePipes.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(RIGHT | featurePipes.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(LEFT | RIGHT | featurePipes.getFeatureId() | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_DDVent")
                .forCondition(featureVent.getFeatureId())
                .andCondition(featureVent.getFeatureId() | LEFT)
                .andCondition(featureVent.getFeatureId() | RIGHT)
                .andCondition(featureVent.getFeatureId() | LEFT | RIGHT);

        textures.useTexture("Wall_DDScreen")
                .forCondition(featureScreen.getFeatureId())
                .andCondition(featureScreen.getFeatureId() | LEFT)
                .andCondition(featureScreen.getFeatureId() | RIGHT)
                .andCondition(featureScreen.getFeatureId() | LEFT | RIGHT);

        textures.useTexture("Wall_DDValve")
                .forCondition(featureValve.getFeatureId())
                .andCondition(featureValve.getFeatureId() | LEFT)
                .andCondition(featureValve.getFeatureId() | RIGHT)
                .andCondition(featureValve.getFeatureId() | LEFT | RIGHT);

        textures.useTexture("Wall_DDLongPipe_L")
                .forCondition(featureHorizontalMetalTear.getFeatureId() | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DDLongPipe_M")
                .forCondition(featureHorizontalMetalTear.getFeatureId());

        textures.useTexture("Wall_DDLongPipe_R")
                .forCondition(featureHorizontalMetalTear.getFeatureId() | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_DD1_PipeB")
                .forCondition(featurePipes.getFeatureId() | FEATURE_EDGE_BOTTOM)
                .andCondition(LEFT | featurePipes.getFeatureId() | FEATURE_EDGE_BOTTOM)
                .andCondition(RIGHT | featurePipes.getFeatureId() | FEATURE_EDGE_BOTTOM)
                .andCondition(LEFT | RIGHT | featurePipes.getFeatureId() | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_DD2_PEdgeVL")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_LEFT)
                .andCondition(featurePlate.getFeatureId() | LEFT | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DD2_PEdgeVR")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT)
                .andCondition(featurePlate.getFeatureId() | RIGHT | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_DD2_PEdgeHU")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(featureCrown.getFeatureId() | TOP | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_DD2_PEdgeHD")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM)
                .andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_DD2_PEdgeRDC")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                .andCondition(featurePlate.getFeatureId() | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                .andCondition(featurePlate.getFeatureId() | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                .andCondition(featurePlate.getFeatureId() | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BR_CORNER)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeRUC")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                .andCondition(featurePlate.getFeatureId() | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                .andCondition(featurePlate.getFeatureId() | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                .andCondition(featurePlate.getFeatureId() | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TR_CORNER)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeLDC")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                .andCondition(featurePlate.getFeatureId() | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                .andCondition(featurePlate.getFeatureId() | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                .andCondition(featurePlate.getFeatureId() | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BL_CORNER)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeLUC")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                .andCondition(featurePlate.getFeatureId() | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                .andCondition(featurePlate.getFeatureId() | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TL_CORNER)
                .andCondition(featurePlate.getFeatureId() | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER)
                .andCondition(featurePlate.getFeatureId() | featureCrown.getFeatureId() | TOP | LEFT | RIGHT | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerRDC_S")
                .forCondition(featurePlate.getFeatureId() | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerRUC_S")
                .forCondition(featurePlate.getFeatureId() | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLDC_S")
                .forCondition(featurePlate.getFeatureId() | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLUC_S")
                .forCondition(featurePlate.getFeatureId() | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLURDC_S")
                .forCondition(featurePlate.getFeatureId() | FEATURE_PLATE_TL_CORNER | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLDRUC_S")
                .forCondition(featurePlate.getFeatureId() | FEATURE_PLATE_BL_CORNER | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_CrownM_EdgeL")
                .forCondition(featureCrown.getFeatureId() | TOP | LEFT)
                .andCondition(featureCrown.getFeatureId() | TOP | LEFT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(featureCrown.getFeatureId() | TOP | LEFT | featurePlate.getFeatureId());

        textures.useTexture("Wall_CrownM_EdgeR")
                .forCondition(featureCrown.getFeatureId() | TOP | RIGHT)
                .andCondition(featureCrown.getFeatureId() | TOP | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(featureCrown.getFeatureId() | TOP | RIGHT | featurePlate.getFeatureId());

        textures.useTexture("Wall_CrownM_AllEdge")
                .forCondition(featureCrown.getFeatureId() | TOP | LEFT | RIGHT);

        textures.useTexture("Wall_CrownM_Center")
                .forCondition(featureCrown.getFeatureId() | TOP)
                .andCondition(featureCrown.getFeatureId() | TOP | featurePlate.getFeatureId() | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_CrownM_DDPanelCL")
                .forCondition(featureCrown.getFeatureId() | TOP | featurePlate.getFeatureId() | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_CrownM_DDPanelCR")
                .forCondition(featureCrown.getFeatureId() | TOP | featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_BaseM_EdgeL")
                .forCondition(featureBase.getFeatureId() | BOTTOM | LEFT)
                .andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_EdgeR")
                .forCondition(featureBase.getFeatureId() | BOTTOM | RIGHT)
                .andCondition(featureBase.getFeatureId() | BOTTOM | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_AllEdge")
                .forCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT);

        textures.useTexture("Wall_BaseM_Center")
                .forCondition(featureBase.getFeatureId() | BOTTOM)
                .andCondition(featureBase.getFeatureId() | BOTTOM | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_DDPanelCL")
                .forCondition(featureBase.getFeatureId() | BOTTOM | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_BaseM_DDPanelCR")
                .forCondition(featureBase.getFeatureId() | BOTTOM | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_CrownM_EdgeL_DDPanelCL_S")
                .forCondition(featureCrown.getFeatureId() | TOP | LEFT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                .andCondition(featureCrown.getFeatureId() | TOP | LEFT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_CrownM_EdgeL_DDPanelCR_S")
                .forCondition(featureCrown.getFeatureId() | TOP | LEFT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_CrownM_EdgeR_DDPanelCR_S")
                .forCondition(featureCrown.getFeatureId() | TOP | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                .andCondition(featureCrown.getFeatureId() | TOP | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER)        ;

        textures.useTexture("Wall_CrownM_EdgeR_DDPanelCL_S")
                .forCondition(featureCrown.getFeatureId() | TOP | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_CrownM_AllEdge_DDPanelCR_S")
                .forCondition(featureCrown.getFeatureId() | TOP | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                .andCondition(featureCrown.getFeatureId() | TOP | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_CrownM_AllEdge_DDPanelCL_S")
                .forCondition(featureCrown.getFeatureId() | TOP | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                .andCondition(featureCrown.getFeatureId() | TOP | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_BaseM_EdgeL_DDPanelCL_S")
                .forCondition(featureBase.getFeatureId() | BOTTOM | LEFT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                .andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_BaseM_EdgeL_DDPanelCR_S")
                .forCondition(featureBase.getFeatureId() | BOTTOM | LEFT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_BaseM_EdgeR_DDPanelCL_S")
                .forCondition(featureBase.getFeatureId() | BOTTOM | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_BaseM_EdgeR_DDPanelCR_S")
                .forCondition(featureBase.getFeatureId() | BOTTOM | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                .andCondition(featureBase.getFeatureId() | BOTTOM | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_BaseM_AllEdge_DDPanelCR_S")
                .forCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                        //.andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_BR_CORNER
                .andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_BaseM_AllEdge_DDPanelCL_S")
                .forCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                        //.andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_BL_CORNER
                .andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);
    }

    @Override
    protected boolean isCompatibleBlock(TextureContext context, Block block)
    {
        return (block instanceof PlotoniumRuinWall);
    }

    /*protected int getTexturePropertiesForSide(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
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
            ForgeDirection back = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BACKWARDS, orientation);
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
                long subProperties = feature.getSubProperties(blockAccess, worldBlockCoord, orientation);

                if ((subProperties & FEATURE_MASK) != 0)
                {
                    blockProperties |= subProperties;
                }
            }

            if ((blockProperties & BOTTOM) == BOTTOM)
            {
                blockProperties |= featureBase.getFeatureId();
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
                blockProperties |= featureCrown.getFeatureId();
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
    }*/

    private int getCrownSplitOpportunity(WorldBlockCoord worldBlockCoord)
    {
        int x = worldBlockCoord.getX();
        int y = worldBlockCoord.getY();
        int z = worldBlockCoord.getZ();
        //return (worldBlockCoord.getX() * 7) + (worldBlockCoord.getY() * (worldBlockCoord.getX() | worldBlockCoord.getZ())) + (~worldBlockCoord.getZ() * 31);
        Random r = new Random(x * y * z * 31);
        return r.nextInt();
    }


    /*public boolean checkRuinWallAndNotObscured(IBlockAccess blockAccess, WorldBlockCoord startingBlock, ForgeDirection back)
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
    }*/


    /*protected String describeTextureProperties(int blockProperties)
    {
        StringBuilder sb = new StringBuilder();
        if ((blockProperties & FEATURE_PLATE) == FEATURE_PLATE)
        {
            sb.append("Plate,");
        }
        if ((blockProperties & featureCrown.getFeatureId()) == featureCrown.getFeatureId())
        {
            sb.append("Crown,");
        }
        if ((blockProperties & featureBase.getFeatureId()) == featureBase.getFeatureId())
        {
            sb.append("Base,");
        }
        if ((blockProperties & FEATURE_PIPES) == FEATURE_PIPES)
        {
            sb.append("Pipes,");
        }
        if ((blockProperties & featureVent.getFeatureId()) == featureVent.getFeatureId())
        {
            sb.append("Vent,");
        }
        if ((blockProperties & featureScreen.getFeatureId()) == featureScreen.getFeatureId())
        {
            sb.append("Screen,");
        }
        if ((blockProperties & featureValve.getFeatureId()) == featureValve.getFeatureId())
        {
            sb.append("Valve,");
        }
        if ((blockProperties & featureHorizontalMetalTear.getFeatureId()) == featureHorizontalMetalTear.getFeatureId())
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
    }*/


}
