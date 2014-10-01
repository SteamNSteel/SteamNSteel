package mod.steamnsteel.texturing.wall;

import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.texturing.IFeatureRegistry;
import mod.steamnsteel.texturing.ITextureConditionSet;
import mod.steamnsteel.texturing.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.TextureContext;
import mod.steamnsteel.texturing.feature.*;
import net.minecraft.block.Block;

public class RuinWallTexture extends ProceduralConnectedTexture
{
    public static long FEATURE_PLATE_TL_CORNER;
    public static long FEATURE_PLATE_TR_CORNER;
    public static long FEATURE_PLATE_BL_CORNER;
    public static long FEATURE_PLATE_BR_CORNER;
    public static long ALTERNATE;

    public static final int LAYER_PLATE = 1;
    public static final int LAYER_BASE = 2;
    public static final int LAYER_CROWN = 3;
    public static final int LAYER_DOODADS = 4;

    public PlateRuinWallFeature featurePlate;
    private PipesRuinWallFeature featurePipes;
    private OneByOneWallFeature featureVent;
    private OneByOneWallFeature featureValve;
    private OneByOneWallFeature featureScreen;
    private HorizontalMetalTearRuinWallFeature featureHorizontalMetalTear;
    private TopBandWallFeature featureCrown;
    private BottomBandWallFeature featureBase;
    private LongPipeRuinWallFeature featureLongPipe;

    @Override
    protected void registerFeatures(IFeatureRegistry features)
    {
        featurePlate = new PlateRuinWallFeature(this, LAYER_PLATE);
        featureBase = new BottomBandWallFeature(this, "Base", LAYER_BASE);
        featureCrown = new TopBandWallFeature(this, "Crown", LAYER_CROWN);
        featurePipes = new PipesRuinWallFeature(this, LAYER_DOODADS);
        featureVent = new OneByOneWallFeature(this, "Vent", LAYER_DOODADS);
        featureValve = new OneByOneWallFeature(this, "Valve", LAYER_DOODADS);
        featureScreen = new OneByOneWallFeature(this, "Screen", LAYER_DOODADS);
        featureHorizontalMetalTear = new HorizontalMetalTearRuinWallFeature(this, "HorizontalMetalTear", LAYER_DOODADS);
        featureLongPipe = new LongPipeRuinWallFeature(this, "LongPipe", LAYER_DOODADS);

        features.registerFeature(featurePlate);
        features.registerFeature(featureCrown);
        features.registerFeature(featureBase);
        features.registerFeature(featurePipes);
        features.registerFeature(featureVent);
        features.registerFeature(featureValve);
        features.registerFeature(featureScreen);
        features.registerFeature(featureHorizontalMetalTear);
        features.registerFeature(featureLongPipe);

        FEATURE_PLATE_TL_CORNER = features.registerFeatureProperty("Plate_TLC");
        FEATURE_PLATE_TR_CORNER = features.registerFeatureProperty("Plate_TRC");
        FEATURE_PLATE_BL_CORNER = features.registerFeatureProperty("Plate_BLC");
        FEATURE_PLATE_BR_CORNER = features.registerFeatureProperty("Plate_BRC");

        ALTERNATE = features.registerFeatureProperty("Alternate");
    }

    @Override
    protected void registerIcons(ITextureConditionSet textures)
    {
        final long pipesFeatureId = featurePipes.getFeatureId();
        final long plateFeatureId = featurePlate.getFeatureId();
        final long crownFeatureId = featureCrown.getFeatureId();
        final long ventFeatureId = featureVent.getFeatureId();
        final long screenFeatureId = featureScreen.getFeatureId();
        final long valveFeatureId = featureValve.getFeatureId();
        final long baseFeatureId = featureBase.getFeatureId();
        final long horizontalMetalTearFeatureId = featureHorizontalMetalTear.getFeatureId();
        final long longPipeFeatureId = featureLongPipe.getFeatureId();

        textures.useTexture("ruinWallPlotonium")
                .forCondition(DEFAULT)
                .andCondition(LEFT)
                .andCondition(RIGHT)
                .andCondition(LEFT | RIGHT)
                .andCondition(TOP | BOTTOM)
                .andCondition(plateFeatureId)
                        //Hacks around a hard to detect issue of two pipes spawning in a 1x4 stack.
                        //An alternative would be to replace these with a 1x1 texture.
                .andCondition(pipesFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM)
                .andCondition(pipesFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | RIGHT)
                .andCondition(pipesFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT)
                .andCondition(pipesFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT | RIGHT);

        textures.useTexture("Wall_DD1_PipeA")
                .forCondition(pipesFeatureId | FEATURE_EDGE_TOP)
                .andCondition(pipesFeatureId | plateFeatureId | FEATURE_EDGE_TOP)
                .andCondition(LEFT | pipesFeatureId | FEATURE_EDGE_TOP)
                .andCondition(RIGHT | pipesFeatureId | FEATURE_EDGE_TOP)
                .andCondition(LEFT | RIGHT | pipesFeatureId | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_DD1_PipeB")
                .forCondition(pipesFeatureId | FEATURE_EDGE_BOTTOM)
                .andCondition(pipesFeatureId | plateFeatureId | FEATURE_EDGE_BOTTOM)
                .andCondition(LEFT | pipesFeatureId | FEATURE_EDGE_BOTTOM)
                .andCondition(RIGHT | pipesFeatureId | FEATURE_EDGE_BOTTOM)
                .andCondition(LEFT | RIGHT | pipesFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_DD2_PEdgeVL")
                .forCondition(plateFeatureId | FEATURE_EDGE_LEFT)
                .andCondition(plateFeatureId | LEFT | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DD2_PEdgeVR")
                .forCondition(plateFeatureId | FEATURE_EDGE_RIGHT)
                .andCondition(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_DD2_PEdgeHU")
                .forCondition(plateFeatureId | FEATURE_EDGE_TOP)
                .andCondition(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_DD2_PEdgeHD")
                .forCondition(plateFeatureId | FEATURE_EDGE_BOTTOM)
                .andCondition(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_DD2_PEdgeRDC")
                .forCondition(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                .andCondition(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                .andCondition(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                .andCondition(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                .andCondition(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BR_CORNER)
                .andCondition(plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeRUC")
                .forCondition(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                .andCondition(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                .andCondition(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                .andCondition(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                .andCondition(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TR_CORNER)
                .andCondition(plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeRUCbent")
                .forCondition(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP | ALTERNATE)
                .andCondition(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP | ALTERNATE | RIGHT);

        textures.useTexture("Wall_DD2_PEdgeLUCbent")
                .forCondition(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP | ALTERNATE)
                .andCondition(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP | ALTERNATE | LEFT);

        textures.useTexture("Wall_DD2_PEdgeLDC")
                .forCondition(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                .andCondition(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                .andCondition(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                .andCondition(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                .andCondition(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BL_CORNER)
                .andCondition(plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeLUC")
                .forCondition(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                .andCondition(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                .andCondition(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                .andCondition(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TL_CORNER)
                .andCondition(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                .andCondition(plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER)
                .andCondition(plateFeatureId | crownFeatureId | TOP | LEFT | RIGHT | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerRDC_S")
                .forCondition(plateFeatureId | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerRUC_S")
                .forCondition(plateFeatureId | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLDC_S")
                .forCondition(plateFeatureId | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLUC_S")
                .forCondition(plateFeatureId | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLURDC_S")
                .forCondition(plateFeatureId | FEATURE_PLATE_TL_CORNER | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_DD2_PEdgeInnerLDRUC_S")
                .forCondition(plateFeatureId | FEATURE_PLATE_BL_CORNER | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_DD3_Vent")
                .forCondition(ventFeatureId)
                .andCondition(ventFeatureId | LEFT)
                .andCondition(ventFeatureId | RIGHT)
                .andCondition(ventFeatureId | LEFT | RIGHT)
                .andCondition(ventFeatureId | plateFeatureId);

        textures.useTexture("Wall_DD4_Screen")
                .forCondition(screenFeatureId)
                .andCondition(screenFeatureId | LEFT)
                .andCondition(screenFeatureId | RIGHT)
                .andCondition(screenFeatureId | LEFT | RIGHT)
                .andCondition(screenFeatureId | plateFeatureId);

        textures.useTexture("Wall_DD5_Valve")
                .forCondition(valveFeatureId)
                .andCondition(valveFeatureId | LEFT)
                .andCondition(valveFeatureId | RIGHT)
                .andCondition(valveFeatureId | LEFT | RIGHT)
                .andCondition(valveFeatureId | plateFeatureId);


        textures.useTexture("Wall_DDLongPipe_L")
                .forCondition(horizontalMetalTearFeatureId | FEATURE_EDGE_LEFT)
                .andCondition(horizontalMetalTearFeatureId | FEATURE_EDGE_LEFT | LEFT)
                .andCondition(horizontalMetalTearFeatureId | plateFeatureId | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DDLongPipe_M")
                .forCondition(horizontalMetalTearFeatureId)
                .andCondition(horizontalMetalTearFeatureId | plateFeatureId);

        textures.useTexture("Wall_DDLongPipe_R")
                .forCondition(horizontalMetalTearFeatureId | FEATURE_EDGE_RIGHT)
                .andCondition(horizontalMetalTearFeatureId | FEATURE_EDGE_RIGHT | RIGHT)
                .andCondition(horizontalMetalTearFeatureId | plateFeatureId | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_DD6_PipeCapL")
                .forCondition(longPipeFeatureId | FEATURE_EDGE_LEFT)
                .andCondition(longPipeFeatureId | FEATURE_EDGE_LEFT | LEFT)
                .andCondition(longPipeFeatureId | plateFeatureId | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DD6_PipeSec1")
                .forCondition(longPipeFeatureId)
                .andCondition(longPipeFeatureId | plateFeatureId);

        textures.useTexture("Wall_DD6_PipeCapR")
                .forCondition(longPipeFeatureId | FEATURE_EDGE_RIGHT)
                .andCondition(longPipeFeatureId | FEATURE_EDGE_RIGHT | RIGHT)
                .andCondition(longPipeFeatureId | plateFeatureId | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_CrownM_EdgeL")
                .forCondition(crownFeatureId | TOP | LEFT)
                .andCondition(crownFeatureId | TOP | LEFT | plateFeatureId | FEATURE_EDGE_TOP)
                .andCondition(crownFeatureId | TOP | LEFT | plateFeatureId);

        textures.useTexture("Wall_CrownM_EdgeR")
                .forCondition(crownFeatureId | TOP | RIGHT)
                .andCondition(crownFeatureId | TOP | RIGHT | plateFeatureId | FEATURE_EDGE_TOP)
                .andCondition(crownFeatureId | TOP | RIGHT | plateFeatureId);

        textures.useTexture("Wall_CrownM_AllEdge")
                .forCondition(crownFeatureId | TOP | LEFT | RIGHT);

        textures.useTexture("Wall_CrownM_Center")
                .forCondition(crownFeatureId | TOP)
                .andCondition(crownFeatureId | TOP | plateFeatureId | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_CrownM_DDPanelCL")
                .forCondition(crownFeatureId | TOP | plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_CrownM_DDPanelCR")
                .forCondition(crownFeatureId | TOP | plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_BaseM_EdgeL")
                .forCondition(baseFeatureId | BOTTOM | LEFT)
                .andCondition(baseFeatureId | BOTTOM | LEFT | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_EdgeR")
                .forCondition(baseFeatureId | BOTTOM | RIGHT)
                .andCondition(baseFeatureId | BOTTOM | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_AllEdge")
                .forCondition(baseFeatureId | BOTTOM | LEFT | RIGHT);

        textures.useTexture("Wall_BaseM_Center")
                .forCondition(baseFeatureId | BOTTOM)
                .andCondition(baseFeatureId | BOTTOM | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useTexture("Wall_BaseM_DDPanelCL")
                .forCondition(baseFeatureId | BOTTOM | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_BaseM_DDPanelCR")
                .forCondition(baseFeatureId | BOTTOM | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_CrownM_EdgeL_DDPanelCL_S")
                .forCondition(crownFeatureId | TOP | LEFT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                .andCondition(crownFeatureId | TOP | LEFT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_CrownM_EdgeL_DDPanelCR_S")
                .forCondition(crownFeatureId | TOP | LEFT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_CrownM_EdgeR_DDPanelCR_S")
                .forCondition(crownFeatureId | TOP | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                .andCondition(crownFeatureId | TOP | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_CrownM_EdgeR_DDPanelCL_S")
                .forCondition(crownFeatureId | TOP | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_CrownM_AllEdge_DDPanelCR_S")
                .forCondition(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                .andCondition(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useTexture("Wall_CrownM_AllEdge_DDPanelCL_S")
                .forCondition(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                .andCondition(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useTexture("Wall_BaseM_EdgeL_DDPanelCL_S")
                .forCondition(baseFeatureId | BOTTOM | LEFT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                .andCondition(baseFeatureId | BOTTOM | LEFT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

        textures.useTexture("Wall_BaseM_EdgeL_DDPanelCR_S")
                .forCondition(baseFeatureId | BOTTOM | LEFT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_BaseM_EdgeR_DDPanelCL_S")
                .forCondition(baseFeatureId | BOTTOM | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_BaseM_EdgeR_DDPanelCR_S")
                .forCondition(baseFeatureId | BOTTOM | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                .andCondition(baseFeatureId | BOTTOM | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_BaseM_AllEdge_DDPanelCR_S")
                .forCondition(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                        //.andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_BR_CORNER
                .andCondition(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useTexture("Wall_BaseM_AllEdge_DDPanelCL_S")
                .forCondition(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                        //.andCondition(featureBase.getFeatureId() | BOTTOM | LEFT | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_BL_CORNER
                .andCondition(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);
    }

    @Override
    protected boolean isCompatibleBlock(TextureContext context, Block block)
    {
        return (block instanceof PlotoniumRuinWall);
    }
}
