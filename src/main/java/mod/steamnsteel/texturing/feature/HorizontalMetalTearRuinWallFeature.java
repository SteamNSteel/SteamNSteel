package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.wall.RuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class HorizontalMetalTearRuinWallFeature extends ProceduralWallFeatureBase
{
    private final long plateEdgeMask;
    private final long FEATURE_EDGE_TOP_AND_BOTTOM;
    private RuinWallTexture texture;

    public HorizontalMetalTearRuinWallFeature(RuinWallTexture texture, String name, Layer layer)
    {
        super(name, layer);
        this.texture = texture;
        plateEdgeMask = RuinWallTexture.FEATURE_PLATE_BL_CORNER | RuinWallTexture.FEATURE_PLATE_BR_CORNER |
                RuinWallTexture.FEATURE_PLATE_TL_CORNER | RuinWallTexture.FEATURE_PLATE_TR_CORNER |
                RuinWallTexture.FEATURE_EDGE_BOTTOM | RuinWallTexture.FEATURE_EDGE_LEFT |
                RuinWallTexture.FEATURE_EDGE_RIGHT | RuinWallTexture.FEATURE_EDGE_TOP;

        FEATURE_EDGE_TOP_AND_BOTTOM = ProceduralConnectedTexture.FEATURE_EDGE_LEFT | ProceduralConnectedTexture.FEATURE_EDGE_RIGHT;
    }

    @Override
    public boolean isFeatureValid(IconRequest request)
    {
        if (!texture.isBlockPartOfWallAndUnobstructed(request))
        {
            return false;
        }

        boolean leftBlockIsValid = texture.isFeatureAtCoordVisibleAndCompatible(request, getLayer(), this, false, TextureDirection.LEFT);
        boolean rightBlockIsValid = texture.isFeatureAtCoordVisibleAndCompatible(request, getLayer(), this, false, TextureDirection.RIGHT);
        boolean plateAIsPresent;
        boolean plateBIsPresent;
        boolean placeCIsPresent;
        boolean plateDIsPresent;
        boolean plateEIsPresent;

        if (leftBlockIsValid && rightBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.LEFT, TextureDirection.LEFT);
            plateBIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.LEFT);
            placeCIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            plateDIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.RIGHT);
            plateEIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.RIGHT, TextureDirection.RIGHT);

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

        final boolean leftLeftBlockIsValid = texture.isFeatureAtCoordVisibleAndCompatible(request, getLayer(), this, false, TextureDirection.LEFT, TextureDirection.LEFT);
        if (leftBlockIsValid && leftLeftBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.LEFT, TextureDirection.LEFT, TextureDirection.LEFT);
            plateBIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.LEFT, TextureDirection.LEFT);
            placeCIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.LEFT);
            plateDIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            plateEIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.RIGHT);

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

        final boolean rightRightBlockIsValid = texture.isFeatureAtCoordVisibleAndCompatible(request, getLayer(), this, false, TextureDirection.RIGHT, TextureDirection.RIGHT);

        if (rightBlockIsValid && rightRightBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.LEFT);
            plateBIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            placeCIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.RIGHT);
            plateDIsPresent = texture.isFeatureAtCoordCompatibleWith(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.RIGHT, TextureDirection.RIGHT);
            plateEIsPresent = texture.isFeatureAtCoordVisibleAndCompatible(request, RuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.RIGHT, TextureDirection.RIGHT, TextureDirection.RIGHT);

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
        Random random = new Random(Objects.hash(chunkCoord, getFeatureTraitId(), 13));

        final int featureCount = 64;

        List<FeatureInstance> features = new ArrayList<FeatureInstance>(featureCount);
        //Generate Pipe features
        for (int i = 0; i < featureCount; ++i)
        {
            int xPos = random.nextInt(18) - 1;
            int yPos = random.nextInt(16);
            int zPos = random.nextInt(18) - 1;

            if (random.nextBoolean())
            {
                features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 3, 1, 1));
            } else
            {
                features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 1, 3));
            }
        }
        return features;
    }

    @Override
    public long getTraits(IconRequest request)
    {
        long subProperties = 0;

        boolean isLeftValid = texture.isFeatureAtCoordVisibleAndCompatible(request, getLayer(), this, true, TextureDirection.LEFT);
        boolean isRightValid = texture.isFeatureAtCoordVisibleAndCompatible(request, getLayer(), this, true, TextureDirection.RIGHT);

        if (!isLeftValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_LEFT;
        } else if (!isRightValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_RIGHT;
        } else if (request.useAlternateVersion(1 / 7f))
        {
            subProperties |= RuinWallTexture.ALTERNATE;
        }

        //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
        subProperties &= getFeatureTraitId() | FEATURE_EDGE_TOP_AND_BOTTOM;
        return subProperties;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long traits)
    {
        if (otherLayerFeature instanceof TopBandWallFeature || otherLayerFeature instanceof BottomBandWallFeature)
        {
            return Behaviour.CANNOT_EXIST;
        }
        if ((traits & plateEdgeMask) != 0)
        {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }
}
