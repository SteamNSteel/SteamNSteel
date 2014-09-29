package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class ThreeByOneWallFeature extends ProceduralWallFeatureBase
{
    private ProceduralConnectedTexture texture;

    public ThreeByOneWallFeature(ProceduralConnectedTexture texture, String name, int layer)
    {
        super(name, layer);
        this.texture = texture;
    }

    @Override
    public boolean isFeatureValid(TextureContext context)
    {
        if (!texture.isBlockPartOfWallAndUnobstructed(context))
        {
            return false;
        }

        final boolean leftBlockIsValid = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.LEFT) &&
                texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, TextureDirection.LEFT);

        final boolean rightBlockIsValid = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.RIGHT) &&
                texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, TextureDirection.RIGHT);

        if (leftBlockIsValid && rightBlockIsValid)
        {
            return true;
        }

        final boolean leftLeftBlockIsValid = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.LEFT, TextureDirection.LEFT) &&
                texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, TextureDirection.LEFT, TextureDirection.LEFT);

        if (leftBlockIsValid && leftLeftBlockIsValid)
        {
            return true;
        }

        final boolean rightRightBlockIsValid = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.RIGHT, TextureDirection.RIGHT) &&
                texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, TextureDirection.RIGHT, TextureDirection.RIGHT);

        if (rightBlockIsValid && rightRightBlockIsValid)
        {
            return true;
        }
        return false;
    }

    @Override
    public Collection<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        Random random = new Random(Objects.hash(chunkCoord, getFeatureId(), 13));

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
    public long getSubProperties(TextureContext context, long currentProperties)
    {
        long subProperties = getFeatureId();

        boolean isLeftValid = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.LEFT) &&
                texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, TextureDirection.LEFT);
        boolean isRightValid = texture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.RIGHT) &&
                texture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, TextureDirection.RIGHT);

        if (!isLeftValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_LEFT;
        }
        if (!isRightValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_RIGHT;
        }

        final long FEATURE_EDGE_TOP_AND_BOTTOM = ProceduralConnectedTexture.FEATURE_EDGE_LEFT | ProceduralConnectedTexture.FEATURE_EDGE_RIGHT;

        //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
        subProperties &= getFeatureId() | FEATURE_EDGE_TOP_AND_BOTTOM;
        return subProperties | currentProperties;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature)
    {
        if (otherLayerFeature instanceof TopBandWallFeature || otherLayerFeature instanceof BottomBandWallFeature)
        {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }
}
