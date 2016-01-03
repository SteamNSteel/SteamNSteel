package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.wall.RemnantRuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.util.BlockPos;

import java.util.*;

public class VerticalMetalTearRuinWallFeature extends ProceduralWallFeatureBase
{
    private final long plateEdgeMask;
    private final long FEATURE_EDGE_LEFT_AND_RIGHT;
    private RemnantRuinWallTexture texture;

    public VerticalMetalTearRuinWallFeature(RemnantRuinWallTexture texture, String name, Layer layer)
    {
        super(name, layer);
        this.texture = texture;
        plateEdgeMask = RemnantRuinWallTexture.FEATURE_PLATE_BL_CORNER | RemnantRuinWallTexture.FEATURE_PLATE_BR_CORNER |
                RemnantRuinWallTexture.FEATURE_PLATE_TL_CORNER | RemnantRuinWallTexture.FEATURE_PLATE_TR_CORNER |
                RemnantRuinWallTexture.FEATURE_EDGE_BOTTOM | RemnantRuinWallTexture.FEATURE_EDGE_LEFT |
                RemnantRuinWallTexture.FEATURE_EDGE_RIGHT | RemnantRuinWallTexture.FEATURE_EDGE_TOP;

        FEATURE_EDGE_LEFT_AND_RIGHT = ProceduralConnectedTexture.FEATURE_EDGE_TOP | ProceduralConnectedTexture.FEATURE_EDGE_BOTTOM;
    }

    @Override
    public boolean isFeatureValid(SpriteRequest request)
    {
        if (!texture.isBlockPartOfWallAndUnobstructed(request))
        {
            return false;
        }

        boolean aboveBlockIsValid = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, getLayer(), this, false, TextureDirection.ABOVE);
        boolean belowBlockIsValid = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, getLayer(), this, false, TextureDirection.BELOW);
        boolean plateAIsPresent;
        boolean plateBIsPresent;
        boolean placeCIsPresent;
        boolean plateDIsPresent;
        boolean plateEIsPresent;

        if (aboveBlockIsValid && belowBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE, TextureDirection.ABOVE);
            plateBIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE);
            placeCIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            plateDIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW);
            plateEIsPresent = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW, TextureDirection.BELOW);

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

        final boolean aboveAboveBlockIsValid = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, getLayer(), this, false, TextureDirection.ABOVE, TextureDirection.ABOVE);
        if (aboveBlockIsValid && aboveAboveBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE, TextureDirection.ABOVE, TextureDirection.ABOVE);
            plateBIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.LEFT, TextureDirection.ABOVE);
            placeCIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE);
            plateDIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            plateEIsPresent = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW);

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

        final boolean belowBelowBlockIsValid = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, getLayer(), this, false, TextureDirection.BELOW, TextureDirection.BELOW);

        if (belowBlockIsValid && belowBelowBlockIsValid)
        {
            plateAIsPresent = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.ABOVE);
            plateBIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false);
            placeCIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW);
            plateDIsPresent = texture.isFeatureAtOffsetOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW, TextureDirection.BELOW);
            plateEIsPresent = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, RemnantRuinWallTexture.LAYER_PLATE, texture.featurePlate, false, TextureDirection.BELOW, TextureDirection.BELOW, TextureDirection.BELOW);

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
            int yPos = random.nextInt(13);
            int zPos = random.nextInt(18) - 1;
            features.add(new FeatureInstance(this, new BlockPos(xPos, yPos, zPos), 1, 3, 1));
        }
        return features;
    }

    @Override
    public long getTraits(SpriteRequest request)
    {
        long subProperties = 0;

        boolean isAboveValid = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, getLayer(), this, true, TextureDirection.ABOVE);
        boolean isBelowValid = texture.isFeatureAtOffsetPartOfWallUnobstructedAndOfType(request, getLayer(), this, true, TextureDirection.BELOW);

        if (!isAboveValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_TOP;
        } else if (!isBelowValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_BOTTOM;
        } else if (request.useAlternateVersion(1 / 7f))
        {
            subProperties |= RemnantRuinWallTexture.ALTERNATE;
        }

        //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
        subProperties &= getFeatureTraitId() | FEATURE_EDGE_LEFT_AND_RIGHT;
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
