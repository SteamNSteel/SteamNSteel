package mod.steamnsteel.texturing.wall;

import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.api.traiticonregistry.IIconDefinitionStart;
import mod.steamnsteel.texturing.api.traitregistry.IFeatureRegistry;
import mod.steamnsteel.texturing.feature.*;
import net.minecraft.block.Block;

/**
 * Defines the Ruin wall texture.
 */
public class RuinWallTexture extends ProceduralConnectedTexture
{
    public static long FEATURE_PLATE_TL_CORNER;
    public static long FEATURE_PLATE_TR_CORNER;
    public static long FEATURE_PLATE_BL_CORNER;
    public static long FEATURE_PLATE_BR_CORNER;
    public static long ALTERNATE;

    public static Layer LAYER_PLATE;
    public static Layer LAYER_BASE;
    public static Layer LAYER_CROWN;
    public static Layer LAYER_DOODADS;

    public PlateRuinWallFeature featurePlate;
    private PipesRuinWallFeature featurePipes;
    private OneByOneWallFeature featureVent;
    private OneByOneWallFeature featureValve;
    private OneByOneWallFeature featureScreen;
    private HorizontalMetalTearRuinWallFeature featureHorizontalMetalTear;
    private TopBandWallFeature featureCrown;
    private BottomBandWallFeature featureBase;
    private LongPipeRuinWallFeature featureLongPipe;
    private VerticalMetalTearRuinWallFeature featureVerticalMetalTear;

    @Override
    protected void registerFeatures(IFeatureRegistry features)
    {
        LAYER_PLATE = features.registerLayer("Plate", true);
        LAYER_BASE = features.registerLayer("Base", false);
        LAYER_CROWN = features.registerLayer("Crown", false);
        LAYER_DOODADS = features.registerLayer("Doodads", true);

        featurePlate = new PlateRuinWallFeature(this, LAYER_PLATE);
        featureBase = new BottomBandWallFeature(this, "Base", LAYER_BASE);
        featureCrown = new TopBandWallFeature(this, "Crown", LAYER_CROWN);
        featurePipes = new PipesRuinWallFeature(this, LAYER_DOODADS);
        featureVent = new OneByOneWallFeature(this, "Vent", LAYER_DOODADS);
        featureValve = new OneByOneWallFeature(this, "Valve", LAYER_DOODADS);
        featureScreen = new OneByOneWallFeature(this, "Screen", LAYER_DOODADS);
        featureHorizontalMetalTear = new HorizontalMetalTearRuinWallFeature(this, "HorizontalMetalTear", LAYER_DOODADS);
        featureVerticalMetalTear = new VerticalMetalTearRuinWallFeature(this, "VerticalMetalTear", LAYER_DOODADS);
        featureLongPipe = new LongPipeRuinWallFeature(this, "LongPipe", LAYER_DOODADS);

        features.registerFeature(featurePlate);
        features.registerFeature(featureCrown);
        features.registerFeature(featureBase);
        features.registerFeature(featurePipes);
        features.registerFeature(featureVent);
        features.registerFeature(featureValve);
        features.registerFeature(featureScreen);
        features.registerFeature(featureHorizontalMetalTear);
        features.registerFeature(featureVerticalMetalTear);
        features.registerFeature(featureLongPipe);

        FEATURE_PLATE_TL_CORNER = features.registerFeatureProperty("Plate_TLC");
        FEATURE_PLATE_TR_CORNER = features.registerFeatureProperty("Plate_TRC");
        FEATURE_PLATE_BL_CORNER = features.registerFeatureProperty("Plate_BLC");
        FEATURE_PLATE_BR_CORNER = features.registerFeatureProperty("Plate_BRC");

        ALTERNATE = features.registerFeatureProperty("Alternate");
    }

    @Override
    protected void registerIcons(IIconDefinitionStart textures)
    {
        final long pipesFeatureId = featurePipes.getFeatureId();
        final long plateFeatureId = featurePlate.getFeatureId();
        final long crownFeatureId = featureCrown.getFeatureId();
        final long ventFeatureId = featureVent.getFeatureId();
        final long screenFeatureId = featureScreen.getFeatureId();
        final long valveFeatureId = featureValve.getFeatureId();
        final long baseFeatureId = featureBase.getFeatureId();
        final long horizontalMetalTearFeatureId = featureHorizontalMetalTear.getFeatureId();
        final long verticalMetalTearFeatureId = featureVerticalMetalTear.getFeatureId();
        final long longPipeFeatureId = featureLongPipe.getFeatureId();

        textures.useIconNamed("ruinWallPlotonium")
                .forTraitSet(DEFAULT)
                .andTraitSet(LEFT)
                .andTraitSet(RIGHT)
                .andTraitSet(LEFT | RIGHT)
                .andTraitSet(TOP | BOTTOM)
                .andTraitSet(plateFeatureId)
                        //Hacks around a hard to detect issue of two pipes spawning in a 1x4 stack.
                        //An alternative would be to replace these with a 1x1 texture.
                .andTraitSet(pipesFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM)
                .andTraitSet(pipesFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | RIGHT)
                .andTraitSet(pipesFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT)
                .andTraitSet(pipesFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT | RIGHT);

        textures.useIconNamed("Wall_DD1_PipeA")
                .forTraitSet(pipesFeatureId | FEATURE_EDGE_TOP)
                .andTraitSet(pipesFeatureId | plateFeatureId | FEATURE_EDGE_TOP)
                .andTraitSet(LEFT | pipesFeatureId | FEATURE_EDGE_TOP)
                .andTraitSet(RIGHT | pipesFeatureId | FEATURE_EDGE_TOP)
                .andTraitSet(LEFT | RIGHT | pipesFeatureId | FEATURE_EDGE_TOP);

        textures.useIconNamed("Wall_DD1_PipeB")
                .forTraitSet(pipesFeatureId | FEATURE_EDGE_BOTTOM)
                .andTraitSet(pipesFeatureId | plateFeatureId | FEATURE_EDGE_BOTTOM)
                .andTraitSet(LEFT | pipesFeatureId | FEATURE_EDGE_BOTTOM)
                .andTraitSet(RIGHT | pipesFeatureId | FEATURE_EDGE_BOTTOM)
                .andTraitSet(LEFT | RIGHT | pipesFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useIconNamed("Wall_DD2_PEdgeVL")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_LEFT)
                .andTraitSet(plateFeatureId | LEFT | FEATURE_EDGE_LEFT);

        textures.useIconNamed("Wall_DD2_PEdgeVR")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_RIGHT)
                .andTraitSet(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT);

        textures.useIconNamed("Wall_DD2_PEdgeHU")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_TOP)
                .andTraitSet(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP);

        textures.useIconNamed("Wall_DD2_PEdgeHD")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_BOTTOM)
                .andTraitSet(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useIconNamed("Wall_DD2_PEdgeRDC")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                .andTraitSet(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                .andTraitSet(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                .andTraitSet(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BR_CORNER)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeRUC")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                .andTraitSet(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                .andTraitSet(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                .andTraitSet(plateFeatureId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TR_CORNER)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeRUCbent")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP | ALTERNATE)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP | ALTERNATE | RIGHT);

        textures.useIconNamed("Wall_DD2_PEdgeLUCbent")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP | ALTERNATE)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP | ALTERNATE | LEFT);

        textures.useIconNamed("Wall_DD2_PEdgeLDC")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                .andTraitSet(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                .andTraitSet(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                .andTraitSet(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BL_CORNER)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeLUC")
                .forTraitSet(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                .andTraitSet(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                .andTraitSet(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TL_CORNER)
                .andTraitSet(plateFeatureId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                .andTraitSet(plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER)
                .andTraitSet(plateFeatureId | crownFeatureId | TOP | LEFT | RIGHT | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeInnerRDC_S")
                .forTraitSet(plateFeatureId | FEATURE_PLATE_BR_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeInnerRUC_S")
                .forTraitSet(plateFeatureId | FEATURE_PLATE_TR_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeInnerLDC_S")
                .forTraitSet(plateFeatureId | FEATURE_PLATE_BL_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeInnerLUC_S")
                .forTraitSet(plateFeatureId | FEATURE_PLATE_TL_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeInnerLURDC_S")
                .forTraitSet(plateFeatureId | FEATURE_PLATE_TL_CORNER | FEATURE_PLATE_BR_CORNER);

        textures.useIconNamed("Wall_DD2_PEdgeInnerLDRUC_S")
                .forTraitSet(plateFeatureId | FEATURE_PLATE_BL_CORNER | FEATURE_PLATE_TR_CORNER);

        textures.useIconNamed("Wall_DD3_Vent")
                .forTraitSet(ventFeatureId)
                .andTraitSet(ventFeatureId | LEFT)
                .andTraitSet(ventFeatureId | RIGHT)
                .andTraitSet(ventFeatureId | LEFT | RIGHT)
                .andTraitSet(ventFeatureId | plateFeatureId);

        textures.useIconNamed("Wall_DD4_Screen")
                .forTraitSet(screenFeatureId)
                .andTraitSet(screenFeatureId | LEFT)
                .andTraitSet(screenFeatureId | RIGHT)
                .andTraitSet(screenFeatureId | LEFT | RIGHT)
                .andTraitSet(screenFeatureId | plateFeatureId);

        textures.useIconNamed("Wall_DD5_Valve")
                .forTraitSet(valveFeatureId)
                .andTraitSet(valveFeatureId | LEFT)
                .andTraitSet(valveFeatureId | RIGHT)
                .andTraitSet(valveFeatureId | LEFT | RIGHT)
                .andTraitSet(valveFeatureId | plateFeatureId);


        textures.useIconNamed("Wall_DDLongPipe_L")
                .forTraitSet(horizontalMetalTearFeatureId | FEATURE_EDGE_LEFT)
                .andTraitSet(horizontalMetalTearFeatureId | FEATURE_EDGE_LEFT | LEFT)
                .andTraitSet(horizontalMetalTearFeatureId | plateFeatureId | FEATURE_EDGE_LEFT);

        textures.useIconNamed("Wall_DDLongPipe_M")
                .forTraitSet(horizontalMetalTearFeatureId)
                .andTraitSet(horizontalMetalTearFeatureId | plateFeatureId);

        textures.useIconNamed("Wall_DDLongPipe_R")
                .forTraitSet(horizontalMetalTearFeatureId | FEATURE_EDGE_RIGHT)
                .andTraitSet(horizontalMetalTearFeatureId | FEATURE_EDGE_RIGHT | RIGHT)
                .andTraitSet(horizontalMetalTearFeatureId | plateFeatureId | FEATURE_EDGE_RIGHT);

        textures.useIconNamed("Wall_DDLongPipe_U")
                .forTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_TOP)
                .andTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_TOP | TOP)
                .andTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_TOP | LEFT)
                .andTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_TOP | RIGHT)
                .andTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_TOP | LEFT | RIGHT)
                .andTraitSet(verticalMetalTearFeatureId | plateFeatureId | FEATURE_EDGE_TOP);

        textures.useIconNamed("Wall_DDLongPipe_M2")
                .forTraitSet(verticalMetalTearFeatureId)
                .andTraitSet(verticalMetalTearFeatureId | LEFT)
                .andTraitSet(verticalMetalTearFeatureId | RIGHT)
                .andTraitSet(verticalMetalTearFeatureId | LEFT | RIGHT)
                .andTraitSet(verticalMetalTearFeatureId | plateFeatureId);

        textures.useIconNamed("Wall_DDLongPipe_D")
                .forTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_BOTTOM)
                .andTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_BOTTOM | BOTTOM)
                .andTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_BOTTOM | LEFT)
                .andTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_BOTTOM | RIGHT)
                .andTraitSet(verticalMetalTearFeatureId | FEATURE_EDGE_BOTTOM | LEFT | RIGHT)


                .andTraitSet(verticalMetalTearFeatureId | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useIconNamed("Wall_DD6_PipeCapL")
                .forTraitSet(longPipeFeatureId | FEATURE_EDGE_LEFT)
                .andTraitSet(longPipeFeatureId | FEATURE_EDGE_LEFT | LEFT)
                .andTraitSet(longPipeFeatureId | plateFeatureId | FEATURE_EDGE_LEFT);

        textures.useIconNamed("Wall_DD6_PipeSec1")
                .forTraitSet(longPipeFeatureId)
                .andTraitSet(longPipeFeatureId | plateFeatureId);

        textures.useIconNamed("Wall_DD6_PipeCapR")
                .forTraitSet(longPipeFeatureId | FEATURE_EDGE_RIGHT)
                .andTraitSet(longPipeFeatureId | FEATURE_EDGE_RIGHT | RIGHT)
                .andTraitSet(longPipeFeatureId | plateFeatureId | FEATURE_EDGE_RIGHT);

        textures.useIconNamed("Wall_CrownM_EdgeL")
                .forTraitSet(crownFeatureId | TOP | LEFT)
                .andTraitSet(crownFeatureId | TOP | LEFT | plateFeatureId | FEATURE_EDGE_TOP)
                .andTraitSet(crownFeatureId | TOP | LEFT | plateFeatureId);

        textures.useIconNamed("Wall_CrownM_EdgeR")
                .forTraitSet(crownFeatureId | TOP | RIGHT)
                .andTraitSet(crownFeatureId | TOP | RIGHT | plateFeatureId | FEATURE_EDGE_TOP)
                .andTraitSet(crownFeatureId | TOP | RIGHT | plateFeatureId);

        textures.useIconNamed("Wall_CrownM_AllEdge")
                .forTraitSet(crownFeatureId | TOP | LEFT | RIGHT);

        textures.useIconNamed("Wall_CrownM_Center")
                .forTraitSet(crownFeatureId | TOP)
                .andTraitSet(crownFeatureId | TOP | plateFeatureId | FEATURE_EDGE_TOP);

        textures.useIconNamed("Wall_CrownM_DDPanelCL")
                .forTraitSet(crownFeatureId | TOP | plateFeatureId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP);

        textures.useIconNamed("Wall_CrownM_DDPanelCR")
                .forTraitSet(crownFeatureId | TOP | plateFeatureId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP);

        textures.useIconNamed("Wall_BaseM_EdgeL")
                .forTraitSet(baseFeatureId | BOTTOM | LEFT)
                .andTraitSet(baseFeatureId | BOTTOM | LEFT | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useIconNamed("Wall_BaseM_EdgeR")
                .forTraitSet(baseFeatureId | BOTTOM | RIGHT)
                .andTraitSet(baseFeatureId | BOTTOM | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useIconNamed("Wall_BaseM_AllEdge")
                .forTraitSet(baseFeatureId | BOTTOM | LEFT | RIGHT);

        textures.useIconNamed("Wall_BaseM_Center")
                .forTraitSet(baseFeatureId | BOTTOM)
                .andTraitSet(baseFeatureId | BOTTOM | plateFeatureId | FEATURE_EDGE_BOTTOM);

        textures.useIconNamed("Wall_BaseM_DDPanelCL")
                .forTraitSet(baseFeatureId | BOTTOM | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

        textures.useIconNamed("Wall_BaseM_DDPanelCR")
                .forTraitSet(baseFeatureId | BOTTOM | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

        textures.useIconNamed("Wall_CrownM_EdgeL_DDPanelCL_S")
                .forTraitSet(crownFeatureId | TOP | LEFT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                .andTraitSet(crownFeatureId | TOP | LEFT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useIconNamed("Wall_CrownM_EdgeL_DDPanelCR_S")
                .forTraitSet(crownFeatureId | TOP | LEFT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT);

        textures.useIconNamed("Wall_CrownM_EdgeR_DDPanelCR_S")
                .forTraitSet(crownFeatureId | TOP | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                .andTraitSet(crownFeatureId | TOP | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useIconNamed("Wall_CrownM_EdgeR_DDPanelCL_S")
                .forTraitSet(crownFeatureId | TOP | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT);

        textures.useIconNamed("Wall_CrownM_AllEdge_DDPanelCR_S")
                .forTraitSet(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                .andTraitSet(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

        textures.useIconNamed("Wall_CrownM_AllEdge_DDPanelCL_S")
                .forTraitSet(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                .andTraitSet(crownFeatureId | TOP | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

        textures.useIconNamed("Wall_BaseM_EdgeL_DDPanelCL_S")
                .forTraitSet(baseFeatureId | BOTTOM | LEFT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                .andTraitSet(baseFeatureId | BOTTOM | LEFT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

        textures.useIconNamed("Wall_BaseM_EdgeL_DDPanelCR_S")
                .forTraitSet(baseFeatureId | BOTTOM | LEFT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

        textures.useIconNamed("Wall_BaseM_EdgeR_DDPanelCL_S")
                .forTraitSet(baseFeatureId | BOTTOM | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

        textures.useIconNamed("Wall_BaseM_EdgeR_DDPanelCR_S")
                .forTraitSet(baseFeatureId | BOTTOM | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                .andTraitSet(baseFeatureId | BOTTOM | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useIconNamed("Wall_BaseM_AllEdge_DDPanelCR_S")
                .forTraitSet(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                .andTraitSet(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

        textures.useIconNamed("Wall_BaseM_AllEdge_DDPanelCL_S")
                .forTraitSet(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                .andTraitSet(baseFeatureId | BOTTOM | LEFT | RIGHT | plateFeatureId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);
    }


    @Override
    protected boolean isCompatibleBlock(IconRequest request, Block block)
    {
        return (block instanceof PlotoniumRuinWall);
    }
}
