package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.wall.RemnantRuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.util.BlockPos;

import java.util.*;

public class OneByOneWallFeature extends ProceduralWallFeatureBase
{
    private final long featureMask;
    private RemnantRuinWallTexture ruinWallTexture;

    public OneByOneWallFeature(RemnantRuinWallTexture ruinWallTexture, String name, Layer layer)
    {
        super(name, layer);
        this.ruinWallTexture = ruinWallTexture;

        featureMask = RemnantRuinWallTexture.FEATURE_PLATE_BL_CORNER | RemnantRuinWallTexture.FEATURE_PLATE_BR_CORNER |
                RemnantRuinWallTexture.FEATURE_PLATE_TL_CORNER | RemnantRuinWallTexture.FEATURE_PLATE_TR_CORNER |
                RemnantRuinWallTexture.FEATURE_EDGE_BOTTOM | RemnantRuinWallTexture.FEATURE_EDGE_LEFT |
                RemnantRuinWallTexture.FEATURE_EDGE_RIGHT | RemnantRuinWallTexture.FEATURE_EDGE_TOP;
    }

    @Override
    public boolean isFeatureValid(SpriteRequest request)
    {
        if (!ruinWallTexture.isBlockPartOfWallAndUnobstructed(request))
        {
            return false;
        }

        final boolean aboveBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(request, TextureDirection.ABOVE);
        final boolean belowBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(request, TextureDirection.BELOW);

        if (aboveBlockIsClear && belowBlockIsClear)
        {
            return true;
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

            features.add(new FeatureInstance(this, new BlockPos(xPos, yPos, zPos), 1, 1, 1));
        }
        return features;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long traits)
    {
        if (otherLayerFeature instanceof TopBandWallFeature || otherLayerFeature instanceof BottomBandWallFeature)
        {
            return Behaviour.CANNOT_EXIST;
        }
        if ((traits & featureMask) != 0)
        {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }
}
