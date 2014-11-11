package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.wall.RuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class VerticalMetalTearRuinWallFeature extends ProceduralWallFeatureBase
{
    private final long plateEdgeMask;
    private final long FEATURE_EDGE_LEFT_AND_RIGHT;
    private RuinWallTexture texture;

    public VerticalMetalTearRuinWallFeature(RuinWallTexture texture, String name, Layer layer)
    {
        super(name, layer);
        this.texture = texture;
        plateEdgeMask = RuinWallTexture.FEATURE_PLATE_BL_CORNER | RuinWallTexture.FEATURE_PLATE_BR_CORNER |
                RuinWallTexture.FEATURE_PLATE_TL_CORNER | RuinWallTexture.FEATURE_PLATE_TR_CORNER |
                RuinWallTexture.FEATURE_EDGE_BOTTOM | RuinWallTexture.FEATURE_EDGE_LEFT |
                RuinWallTexture.FEATURE_EDGE_RIGHT | RuinWallTexture.FEATURE_EDGE_TOP;

        FEATURE_EDGE_LEFT_AND_RIGHT = ProceduralConnectedTexture.FEATURE_EDGE_TOP | ProceduralConnectedTexture.FEATURE_EDGE_BOTTOM;
    }

    @Override
    public boolean isFeatureValid(TextureContext context)
    {
        if (!texture.isBlockPartOfWallAndUnobstructed(context))
        {
            return false;
        }

        boolean aboveBlockIsValid = texture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.ABOVE);
        boolean belowBlockIsValid = texture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.BELOW);
        boolean plateAIsPresent;
        boolean plateBIsPresent;
        boolean placeCIsPresent;
        boolean plateDIsPresent;
        boolean plateEIsPresent;

        if (aboveBlockIsValid && belowBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE, TextureDirection.ABOVE);
            plateBIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE);
            placeCIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            plateDIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW);
            plateEIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW, TextureDirection.BELOW);

            if (plateBIsPresent && plateDIsPresent && placeCIsPresent && plateAIsPresent && plateEIsPresent)
            {
                return true;
            }
            if (!plateBIsPresent && !plateDIsPresent && !placeCIsPresent && !plateAIsPresent && !plateEIsPresent)
            {
                return true;
            }
            return false;
        }

        final boolean aboveAboveBlockIsValid = texture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.ABOVE, TextureDirection.ABOVE);
        if (aboveBlockIsValid && aboveAboveBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE, TextureDirection.ABOVE, TextureDirection.ABOVE);
            plateBIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.LEFT, TextureDirection.ABOVE);
            placeCIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE);
            plateDIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            plateEIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW);

            if (plateBIsPresent && plateDIsPresent && placeCIsPresent && plateAIsPresent && plateEIsPresent)
            {
                return true;
            }
            if (!plateBIsPresent && !plateDIsPresent && !placeCIsPresent && !plateAIsPresent && !plateEIsPresent)
            {
                return true;
            }
            return false;
        }

        final boolean belowBelowBlockIsValid = texture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.BELOW, TextureDirection.BELOW);

        if (belowBlockIsValid && belowBelowBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE);
            plateBIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            placeCIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW);
            plateDIsPresent = texture.isFeatureAtCoordCompatibleWith(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW, TextureDirection.BELOW);
            plateEIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(context, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW, TextureDirection.BELOW, TextureDirection.BELOW);

            if (plateBIsPresent && plateDIsPresent && placeCIsPresent && plateAIsPresent && plateEIsPresent)
            {
                return true;
            }
            if (!plateBIsPresent && !plateDIsPresent && !placeCIsPresent && !plateAIsPresent && !plateEIsPresent)
            {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public Collection<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord)
    {
        Random random = new Random(Objects.hash(chunkCoord, getFeatureId(), 13));

        final int featureCount = 64;

        List<FeatureInstance> features = new ArrayList<FeatureInstance>(featureCount);
        //Generate Pipe features
        for (int i = 0; i < featureCount; ++i)
        {
            int xPos = random.nextInt(18) - 1;
            int yPos = random.nextInt(13);
            int zPos = random.nextInt(18) - 1;
            features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 3, 1));
        }
        return features;
    }

    @Override
    public long getSubProperties(TextureContext context)
    {
        long subProperties = 0;

        boolean isAboveValid = texture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, true, TextureDirection.ABOVE);
        boolean isBelowValid = texture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, true, TextureDirection.BELOW);

        if (!isAboveValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_TOP;
        } else if (!isBelowValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_BOTTOM;
        } else if (context.useAlternateVersion(1 / 7f))
        {
            subProperties |= RuinWallTexture.ALTERNATE;
        }

        //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
        subProperties &= getFeatureId() | FEATURE_EDGE_LEFT_AND_RIGHT;
        return subProperties;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long featureProperties)
    {
        if (otherLayerFeature instanceof TopBandWallFeature || otherLayerFeature instanceof BottomBandWallFeature)
        {
            return Behaviour.CANNOT_EXIST;
        }
        if ((featureProperties & plateEdgeMask) != 0)
        {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }
}
