package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.texturing.wall.RuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class PipesRuinWallFeature extends ProceduralWallFeatureBase
{

    private final long featureMask;
    private RuinWallTexture texture;

    public PipesRuinWallFeature(RuinWallTexture texture, int layer)
    {
        super("Pipes", layer);
        this.texture = texture;
        featureMask = RuinWallTexture.FEATURE_PLATE_BL_CORNER | RuinWallTexture.FEATURE_PLATE_BR_CORNER |
                RuinWallTexture.FEATURE_PLATE_TL_CORNER | RuinWallTexture.FEATURE_PLATE_TR_CORNER |
                RuinWallTexture.FEATURE_EDGE_BOTTOM | RuinWallTexture.FEATURE_EDGE_LEFT |
                RuinWallTexture.FEATURE_EDGE_RIGHT | RuinWallTexture.FEATURE_EDGE_TOP;
    }

    @Override
    public boolean isFeatureValid(TextureContext context)
    {
        if (!texture.isBlockPartOfWallAndUnobstructed(context))
        {
            return false;
        }

        final boolean aboveBlockIsClear = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.ABOVE);
        final boolean aboveBlockFeatureIsCompatible = texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, false, TextureDirection.ABOVE);
        final boolean belowBlockIsClear = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.BELOW);
        final boolean belowBlockFeatureIsCompatible = texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, false, TextureDirection.BELOW);

        //boolean aboveValid2 = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.ABOVE, TextureDirection.ABOVE);
        //boolean belowValid2 = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.BELOW, TextureDirection.BELOW);

        boolean plateAIsPresent, plateBIsPresent, plateCIsPresent, plateDIsPresent;

        if (aboveBlockIsClear && !aboveBlockFeatureIsCompatible && belowBlockIsClear && belowBlockFeatureIsCompatible)
        {
            plateAIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE);
            plateBIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            plateCIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW);
            plateDIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW, TextureDirection.BELOW);
            boolean finalCheck = texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, false, TextureDirection.BELOW, TextureDirection.BELOW);

            if (!finalCheck && plateAIsPresent && plateBIsPresent && plateCIsPresent && plateDIsPresent) {
                return true;
            }
            if (!finalCheck && !plateAIsPresent && !plateBIsPresent && !plateCIsPresent && !plateDIsPresent) {
                return true;
            }

            return false;
        }

        if (belowBlockIsClear && !belowBlockFeatureIsCompatible && aboveBlockIsClear && aboveBlockFeatureIsCompatible)
        {
            plateAIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE, TextureDirection.ABOVE);
            plateBIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE);
            plateCIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            plateDIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW);

            boolean finalCheck = texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, false, TextureDirection.ABOVE, TextureDirection.ABOVE);


            if (!finalCheck && plateAIsPresent && plateBIsPresent && plateCIsPresent && plateDIsPresent) {
                return true;
            }
            if (!finalCheck && !plateAIsPresent && !plateBIsPresent && !plateCIsPresent && !plateDIsPresent) {
                return true;
            }

            return false;
        }


        return false;
    }

    @Override
    public Collection<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        Random random = new Random(Objects.hash(chunkCoord, getFeatureId(), 6));

        final int featureCount = 64;

        List<FeatureInstance> features = new ArrayList<FeatureInstance>(featureCount);
        //Generate Pipe features
        for (int i = 0; i < featureCount; ++i)
        {
            int xPos = random.nextInt(18) - 1;
            int yPos = random.nextInt(15);
            int zPos = random.nextInt(18) - 1;

            features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 2, 1));
        }
        return features;
    }

    @Override
    public long getSubProperties(TextureContext context)
    {
        long subProperties = 0;
        IProceduralWallFeature aboveBlockFeature = texture.getValidFeature(context, getLayer(), TextureDirection.ABOVE);
        IProceduralWallFeature belowBlockFeature = texture.getValidFeature(context, getLayer(), TextureDirection.BELOW);

        if (!(aboveBlockFeature instanceof PipesRuinWallFeature))
        {
            subProperties |= texture.FEATURE_EDGE_TOP;
        }
        if (!(belowBlockFeature instanceof PipesRuinWallFeature))
        {
            subProperties |= texture.FEATURE_EDGE_BOTTOM;
        }

        final long FEATURE_EDGE_TOP_AND_BOTTOM = texture.FEATURE_EDGE_TOP | texture.FEATURE_EDGE_BOTTOM;

        //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
        //subProperties &= getFeatureId() | FEATURE_EDGE_TOP_AND_BOTTOM;
        return subProperties;// | currentProperties;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long featureProperties)
    {
        if (otherLayerFeature instanceof TopBandWallFeature || otherLayerFeature instanceof BottomBandWallFeature)
        {
            return Behaviour.CANNOT_EXIST;
        }
        if ((featureProperties & featureMask) != 0) {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }
}
