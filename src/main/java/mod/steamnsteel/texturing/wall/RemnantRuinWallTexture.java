package mod.steamnsteel.texturing.wall;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.resource.structure.RemnantRuinWallBlock;
import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.api.traitspriteregistry.ISpriteDefinitionStart;
import mod.steamnsteel.texturing.api.traitregistry.IFeatureRegistry;
import mod.steamnsteel.texturing.api.traitspriteregistry.ISpriteDefinitionStart.ISideTraitList;
import mod.steamnsteel.texturing.feature.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import scala.util.Left;

/**
 * Defines the Ruin wall texture.
 */
public class RemnantRuinWallTexture extends ProceduralConnectedTexture
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
    public static Layer LAYER_TOP_AND_BOTTOM;

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
    private ResourceLocation defaultSpriteLocation;
    private NullFeature featureNull;

    EnumFacing[] cardinalDirections = new EnumFacing[] { EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST};
    EnumFacing[] topAndBottomDirections = new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN};


    @Override
    protected void registerFeatures(IFeatureRegistry features)
    {
        LAYER_PLATE = features.registerLayer("Plate", true, cardinalDirections);
        LAYER_BASE = features.registerLayer("Base", false, cardinalDirections);
        LAYER_CROWN = features.registerLayer("Crown", false, cardinalDirections);
        LAYER_DOODADS = features.registerLayer("Doodads", true, cardinalDirections);

        LAYER_TOP_AND_BOTTOM = features.registerLayer("TopAndBottom", false, topAndBottomDirections);

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
        featureNull = new NullFeature("Null", LAYER_TOP_AND_BOTTOM);

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
        features.registerFeature(featureNull);

        FEATURE_PLATE_TL_CORNER = features.registerTrait("Plate_TLC");
        FEATURE_PLATE_TR_CORNER = features.registerTrait("Plate_TRC");
        FEATURE_PLATE_BL_CORNER = features.registerTrait("Plate_BLC");
        FEATURE_PLATE_BR_CORNER = features.registerTrait("Plate_BRC");

        ALTERNATE = features.registerTrait("Alternate");
    }

    @Override
    protected void registerSprites(final ISpriteDefinitionStart textures)
    {
        final long pipesTraitId = featurePipes.getFeatureTraitId();
        final long plateTraitId = featurePlate.getFeatureTraitId();
        final long crownTraitId = featureCrown.getFeatureTraitId();
        final long ventTraitId = featureVent.getFeatureTraitId();
        final long screenTraitId = featureScreen.getFeatureTraitId();
        final long valveTraitId = featureValve.getFeatureTraitId();
        final long baseTraitId = featureBase.getFeatureTraitId();
        final long horizontalMetalTearTraitId = featureHorizontalMetalTear.getFeatureTraitId();
        final long verticalMetalTearTraitId = featureVerticalMetalTear.getFeatureTraitId();
        final long longPipeTraitId = featureLongPipe.getFeatureTraitId();

        defaultSpriteLocation = makeResourceLocation("remnantRuinWall/Wall_Default");
        textures.useSpriteNamed(defaultSpriteLocation)
                .forTraitSet(DEFAULT)
                .andTraitSet(LEFT)
                .andTraitSet(RIGHT)
                .andTraitSet(LEFT | RIGHT)
                .andTraitSet(TOP | BOTTOM);

        textures.forSides(topAndBottomDirections, new ISideTraitList() {
            @Override
            public void register()
            {
                textures.useSpriteNamed(defaultSpriteLocation)
                        .forTraitSet(featureNull.getFeatureTraitId())
                ;
            }
        });

        textures.forSides(cardinalDirections, new ISpriteDefinitionStart.ISideTraitList() {
            @Override
            public void register()
            {
                textures.useSpriteNamed(defaultSpriteLocation)
                        .forTraitSet(plateTraitId)
                        //Hacks around a hard to detect issue of two pipes spawning in a 1x4 stack.
                        //An alternative would be to replace these with a 1x1 texture.
                        .andTraitSet(pipesTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(pipesTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | RIGHT)
                        .andTraitSet(pipesTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT)
                        .andTraitSet(pipesTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM | LEFT | RIGHT)
                        .andTraitSet(longPipeTraitId | FEATURE_EDGE_LEFT | FEATURE_EDGE_RIGHT | plateTraitId)
                        .andTraitSet(longPipeTraitId | FEATURE_EDGE_LEFT | FEATURE_EDGE_RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD1_PipeA"))
                        .forTraitSet(pipesTraitId | FEATURE_EDGE_TOP)
                        .andTraitSet(pipesTraitId | plateTraitId | FEATURE_EDGE_TOP)
                        .andTraitSet(LEFT | pipesTraitId | FEATURE_EDGE_TOP)
                        .andTraitSet(RIGHT | pipesTraitId | FEATURE_EDGE_TOP)
                        .andTraitSet(LEFT | RIGHT | pipesTraitId | FEATURE_EDGE_TOP);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD1_PipeB"))
                        .forTraitSet(pipesTraitId | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(pipesTraitId | plateTraitId | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(LEFT | pipesTraitId | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(RIGHT | pipesTraitId | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(LEFT | RIGHT | pipesTraitId | FEATURE_EDGE_BOTTOM);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeVL"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_LEFT)
                        .andTraitSet(plateTraitId | LEFT | FEATURE_EDGE_LEFT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeVR"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_RIGHT)
                        .andTraitSet(plateTraitId | RIGHT | FEATURE_EDGE_RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeHU"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_TOP)
                        .andTraitSet(crownTraitId | TOP | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_TOP);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeHD"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(baseTraitId | BOTTOM | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeRDC"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(plateTraitId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                        .andTraitSet(plateTraitId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BL_CORNER)
                        .andTraitSet(plateTraitId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_BR_CORNER)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeRUC"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                        .andTraitSet(plateTraitId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                        .andTraitSet(plateTraitId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TL_CORNER)
                        .andTraitSet(plateTraitId | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_PLATE_TR_CORNER)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeRUCbent"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP | ALTERNATE)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP | ALTERNATE | RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeLUCbent"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP | ALTERNATE)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP | ALTERNATE | LEFT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeLDC"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(plateTraitId | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                        .andTraitSet(plateTraitId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BR_CORNER)
                        .andTraitSet(plateTraitId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_BL_CORNER)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeLUC"))
                        .forTraitSet(plateTraitId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                        .andTraitSet(plateTraitId | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                        .andTraitSet(plateTraitId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TL_CORNER)
                        .andTraitSet(plateTraitId | LEFT | FEATURE_EDGE_LEFT | FEATURE_PLATE_TR_CORNER)
                        .andTraitSet(plateTraitId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER)
                        .andTraitSet(plateTraitId | crownTraitId | TOP | LEFT | RIGHT | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeInnerRDC_S"))
                        .forTraitSet(plateTraitId | FEATURE_PLATE_BR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeInnerRUC_S"))
                        .forTraitSet(plateTraitId | FEATURE_PLATE_TR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeInnerLDC_S"))
                        .forTraitSet(plateTraitId | FEATURE_PLATE_BL_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeInnerLUC_S"))
                        .forTraitSet(plateTraitId | FEATURE_PLATE_TL_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeInnerLURDC_S"))
                        .forTraitSet(plateTraitId | FEATURE_PLATE_TL_CORNER | FEATURE_PLATE_BR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD2_PEdgeInnerLDRUC_S"))
                        .forTraitSet(plateTraitId | FEATURE_PLATE_BL_CORNER | FEATURE_PLATE_TR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD3_Vent"))
                        .forTraitSet(ventTraitId)
                        .andTraitSet(ventTraitId | LEFT)
                        .andTraitSet(ventTraitId | RIGHT)
                        .andTraitSet(ventTraitId | LEFT | RIGHT)
                        .andTraitSet(ventTraitId | plateTraitId);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD4_Screen"))
                        .forTraitSet(screenTraitId)
                        .andTraitSet(screenTraitId | LEFT)
                        .andTraitSet(screenTraitId | RIGHT)
                        .andTraitSet(screenTraitId | LEFT | RIGHT)
                        .andTraitSet(screenTraitId | plateTraitId);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD5_Valve"))
                        .forTraitSet(valveTraitId)
                        .andTraitSet(valveTraitId | LEFT)
                        .andTraitSet(valveTraitId | RIGHT)
                        .andTraitSet(valveTraitId | LEFT | RIGHT)
                        .andTraitSet(valveTraitId | plateTraitId);


                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DDLongPipe_L"))
                        .forTraitSet(horizontalMetalTearTraitId | FEATURE_EDGE_LEFT)
                        .andTraitSet(horizontalMetalTearTraitId | FEATURE_EDGE_LEFT | LEFT)
                        .andTraitSet(horizontalMetalTearTraitId | plateTraitId | FEATURE_EDGE_LEFT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DDLongPipe_M"))
                        .forTraitSet(horizontalMetalTearTraitId)
                        .andTraitSet(horizontalMetalTearTraitId | plateTraitId);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DDLongPipe_R"))
                        .forTraitSet(horizontalMetalTearTraitId | FEATURE_EDGE_RIGHT)
                        .andTraitSet(horizontalMetalTearTraitId | FEATURE_EDGE_RIGHT | RIGHT)
                        .andTraitSet(horizontalMetalTearTraitId | plateTraitId | FEATURE_EDGE_RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DDLongPipe_U"))
                        .forTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_TOP)
                        .andTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_TOP | TOP)
                        .andTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_TOP | LEFT)
                        .andTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_TOP | RIGHT)
                        .andTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_TOP | LEFT | RIGHT)
                        .andTraitSet(verticalMetalTearTraitId | plateTraitId | FEATURE_EDGE_TOP);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DDLongPipe_M2"))
                        .forTraitSet(verticalMetalTearTraitId)
                        .andTraitSet(verticalMetalTearTraitId | LEFT)
                        .andTraitSet(verticalMetalTearTraitId | RIGHT)
                        .andTraitSet(verticalMetalTearTraitId | LEFT | RIGHT)
                        .andTraitSet(verticalMetalTearTraitId | plateTraitId);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DDLongPipe_D"))
                        .forTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_BOTTOM)
                        .andTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_BOTTOM | BOTTOM)
                        .andTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_BOTTOM | LEFT)
                        .andTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_BOTTOM | RIGHT)
                        .andTraitSet(verticalMetalTearTraitId | FEATURE_EDGE_BOTTOM | LEFT | RIGHT)
                        .andTraitSet(verticalMetalTearTraitId | plateTraitId | FEATURE_EDGE_BOTTOM);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD6_PipeCapL"))
                        .forTraitSet(longPipeTraitId | FEATURE_EDGE_LEFT)
                        .andTraitSet(longPipeTraitId | FEATURE_EDGE_LEFT | LEFT)
                        .andTraitSet(longPipeTraitId | plateTraitId | FEATURE_EDGE_LEFT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD6_PipeSec1"))
                        .forTraitSet(longPipeTraitId)
                        .andTraitSet(longPipeTraitId | plateTraitId);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_DD6_PipeCapR"))
                        .forTraitSet(longPipeTraitId | FEATURE_EDGE_RIGHT)
                        .andTraitSet(longPipeTraitId | FEATURE_EDGE_RIGHT | RIGHT)
                        .andTraitSet(longPipeTraitId | plateTraitId | FEATURE_EDGE_RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_EdgeL"))
                        .forTraitSet(crownTraitId | TOP | LEFT)
                        .andTraitSet(crownTraitId | TOP | LEFT | plateTraitId | FEATURE_EDGE_TOP)
                        .andTraitSet(crownTraitId | TOP | LEFT | plateTraitId);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_EdgeR"))
                        .forTraitSet(crownTraitId | TOP | RIGHT)
                        .andTraitSet(crownTraitId | TOP | RIGHT | plateTraitId | FEATURE_EDGE_TOP)
                        .andTraitSet(crownTraitId | TOP | RIGHT | plateTraitId);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_AllEdge"))
                        .forTraitSet(crownTraitId | TOP | LEFT | RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_Center"))
                        .forTraitSet(crownTraitId | TOP)
                        .andTraitSet(crownTraitId | TOP | plateTraitId | FEATURE_EDGE_TOP);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_DDPanelCL"))
                        .forTraitSet(crownTraitId | TOP | plateTraitId | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_DDPanelCR"))
                        .forTraitSet(crownTraitId | TOP | plateTraitId | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_EdgeL"))
                        .forTraitSet(baseTraitId | BOTTOM | LEFT)
                        .andTraitSet(baseTraitId | BOTTOM | LEFT | plateTraitId | FEATURE_EDGE_BOTTOM);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_EdgeR"))
                        .forTraitSet(baseTraitId | BOTTOM | RIGHT)
                        .andTraitSet(baseTraitId | BOTTOM | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_AllEdge"))
                        .forTraitSet(baseTraitId | BOTTOM | LEFT | RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_Center"))
                        .forTraitSet(baseTraitId | BOTTOM)
                        .andTraitSet(baseTraitId | BOTTOM | plateTraitId | FEATURE_EDGE_BOTTOM);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_DDPanelCL"))
                        .forTraitSet(baseTraitId | BOTTOM | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_DDPanelCR"))
                        .forTraitSet(baseTraitId | BOTTOM | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_EdgeL_DDPanelCL_S"))
                        .forTraitSet(crownTraitId | TOP | LEFT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                        .andTraitSet(crownTraitId | TOP | LEFT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_EdgeL_DDPanelCR_S"))
                        .forTraitSet(crownTraitId | TOP | LEFT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_EdgeR_DDPanelCR_S"))
                        .forTraitSet(crownTraitId | TOP | RIGHT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                        .andTraitSet(crownTraitId | TOP | RIGHT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_EdgeR_DDPanelCL_S"))
                        .forTraitSet(crownTraitId | TOP | RIGHT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_AllEdge_DDPanelCR_S"))
                        .forTraitSet(crownTraitId | TOP | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT)
                        .andTraitSet(crownTraitId | TOP | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_PLATE_BR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_CrownM_AllEdge_DDPanelCL_S"))
                        .forTraitSet(crownTraitId | TOP | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT)
                        .andTraitSet(crownTraitId | TOP | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_TOP | FEATURE_PLATE_BL_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_EdgeL_DDPanelCL_S"))
                        .forTraitSet(baseTraitId | BOTTOM | LEFT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                        .andTraitSet(baseTraitId | BOTTOM | LEFT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_EdgeL_DDPanelCR_S"))
                        .forTraitSet(baseTraitId | BOTTOM | LEFT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_EdgeR_DDPanelCL_S"))
                        .forTraitSet(baseTraitId | BOTTOM | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_EdgeR_DDPanelCR_S"))
                        .forTraitSet(baseTraitId | BOTTOM | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                        .andTraitSet(baseTraitId | BOTTOM | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_AllEdge_DDPanelCR_S"))
                        .forTraitSet(baseTraitId | BOTTOM | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT)
                        .andTraitSet(baseTraitId | BOTTOM | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TR_CORNER);

                textures.useSpriteNamed(makeResourceLocation("remnantRuinWall/Wall_BaseM_AllEdge_DDPanelCL_S"))
                        .forTraitSet(baseTraitId | BOTTOM | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT)
                        .andTraitSet(baseTraitId | BOTTOM | LEFT | RIGHT | plateTraitId | FEATURE_EDGE_BOTTOM | FEATURE_PLATE_TL_CORNER);
            }
        });


    }

    private ResourceLocation makeResourceLocation(String spriteResourceLocation)
    {
        return new ResourceLocation(TheMod.MOD_ID, "blocks/" + spriteResourceLocation);
    }

    @Override
    protected boolean isCompatibleBlock(SpriteRequest request, IBlockState blockState)
    {
        return blockState.getBlock() instanceof RemnantRuinWallBlock;
    }

    @Override
    public TextureAtlasSprite getDefaultTextureForSide(EnumFacing side)
    {
        return getSpriteForTraitSet(side, DEFAULT);
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return getSpriteForTraitSet(EnumFacing.NORTH, DEFAULT);
    }
}
