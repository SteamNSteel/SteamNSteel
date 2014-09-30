package mod.steamnsteel.texturing;

import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
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
                .andCondition(featurePipes.getFeatureId() | featurePlate.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(LEFT | featurePipes.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(RIGHT | featurePipes.getFeatureId() | FEATURE_EDGE_TOP)
                .andCondition(LEFT | RIGHT | featurePipes.getFeatureId() | FEATURE_EDGE_TOP);

        textures.useTexture("Wall_DD1_PipeB")
                .forCondition(featurePipes.getFeatureId() | FEATURE_EDGE_BOTTOM)
                .andCondition(featurePipes.getFeatureId() | featurePlate.getFeatureId() | FEATURE_EDGE_BOTTOM)
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

        textures.useTexture("Wall_DD2_PEdgeRUCbent")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP | ALTERNATE)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP | ALTERNATE | RIGHT);

        textures.useTexture("Wall_DD2_PEdgeLUCbent")
                .forCondition(featurePlate.getFeatureId() | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP | ALTERNATE)
                .andCondition(featurePlate.getFeatureId() | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP | ALTERNATE | LEFT);

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

        textures.useTexture("Wall_DD3_Vent")
                .forCondition(featureVent.getFeatureId())
                .andCondition(featureVent.getFeatureId() | LEFT)
                .andCondition(featureVent.getFeatureId() | RIGHT)
                .andCondition(featureVent.getFeatureId() | LEFT | RIGHT)
                .andCondition(featureVent.getFeatureId() | featurePlate.getFeatureId());

        textures.useTexture("Wall_DD4_Screen")
                .forCondition(featureScreen.getFeatureId())
                .andCondition(featureScreen.getFeatureId() | LEFT)
                .andCondition(featureScreen.getFeatureId() | RIGHT)
                .andCondition(featureScreen.getFeatureId() | LEFT | RIGHT)
                .andCondition(featureScreen.getFeatureId() | featurePlate.getFeatureId());

        textures.useTexture("Wall_DD5_Valve")
                .forCondition(featureValve.getFeatureId())
                .andCondition(featureValve.getFeatureId() | LEFT)
                .andCondition(featureValve.getFeatureId() | RIGHT)
                .andCondition(featureValve.getFeatureId() | LEFT | RIGHT)
                .andCondition(featureValve.getFeatureId() | featurePlate.getFeatureId());

        textures.useTexture("Wall_DDLongPipe_L")
                .forCondition(featureHorizontalMetalTear.getFeatureId() | FEATURE_EDGE_LEFT)
                .andCondition(featureHorizontalMetalTear.getFeatureId() | FEATURE_EDGE_LEFT | LEFT)
                .andCondition(featureHorizontalMetalTear.getFeatureId() | featurePlate.getFeatureId() | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DDLongPipe_M")
                .forCondition(featureHorizontalMetalTear.getFeatureId())
                .andCondition(featureHorizontalMetalTear.getFeatureId() | featurePlate.getFeatureId());

        textures.useTexture("Wall_DDLongPipe_R")
                .forCondition(featureHorizontalMetalTear.getFeatureId() | FEATURE_EDGE_RIGHT)
                .andCondition(featureHorizontalMetalTear.getFeatureId() | FEATURE_EDGE_RIGHT | RIGHT)
                .andCondition(featureHorizontalMetalTear.getFeatureId() | featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT);

        textures.useTexture("Wall_DD6_PipeCapL")
                .forCondition(featureLongPipe.getFeatureId() | FEATURE_EDGE_LEFT)
                .andCondition(featureLongPipe.getFeatureId() | FEATURE_EDGE_LEFT | LEFT)
                .andCondition(featureLongPipe.getFeatureId() | featurePlate.getFeatureId() | FEATURE_EDGE_LEFT);

        textures.useTexture("Wall_DD6_PipeSec1")
                .forCondition(featureLongPipe.getFeatureId())
                .andCondition(featureLongPipe.getFeatureId() | featurePlate.getFeatureId());

        textures.useTexture("Wall_DD6_PipeCapR")
                .forCondition(featureLongPipe.getFeatureId() | FEATURE_EDGE_RIGHT)
                .andCondition(featureLongPipe.getFeatureId() | FEATURE_EDGE_RIGHT | RIGHT)
                .andCondition(featureLongPipe.getFeatureId() | featurePlate.getFeatureId() | FEATURE_EDGE_RIGHT);

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
                .andCondition(featureCrown.getFeatureId() | TOP | RIGHT | featurePlate.getFeatureId() | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

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
}
