package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.texturing.wall.RuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class OneByOneWallFeature extends ProceduralWallFeatureBase
{
    private final long featureMask;
    private RuinWallTexture ruinWallTexture;

    public OneByOneWallFeature(RuinWallTexture ruinWallTexture, String name, Layer layer)
    {
        super(name, layer);
        this.ruinWallTexture = ruinWallTexture;

        featureMask = RuinWallTexture.FEATURE_PLATE_BL_CORNER | RuinWallTexture.FEATURE_PLATE_BR_CORNER |
                RuinWallTexture.FEATURE_PLATE_TL_CORNER | RuinWallTexture.FEATURE_PLATE_TR_CORNER |
                RuinWallTexture.FEATURE_EDGE_BOTTOM | RuinWallTexture.FEATURE_EDGE_LEFT |
                RuinWallTexture.FEATURE_EDGE_RIGHT | RuinWallTexture.FEATURE_EDGE_TOP;
    }

    @Override
    public boolean isFeatureValid(TextureContext context)
    {
        if (!ruinWallTexture.isBlockPartOfWallAndUnobstructed(context))
        {
            return false;
        }

        final boolean aboveBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.ABOVE);
        final boolean belowBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.BELOW);

        if (aboveBlockIsClear && belowBlockIsClear)
        {
            return true;
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
            int yPos = random.nextInt(16);
            int zPos = random.nextInt(18) - 1;

            features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 1, 1));
        }
        return features;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long featureProperties)
    {
        if (otherLayerFeature instanceof TopBandWallFeature || otherLayerFeature instanceof BottomBandWallFeature)
        {
            return Behaviour.CANNOT_EXIST;
        }
        if ((featureProperties & featureMask) != 0)
        {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }
}
