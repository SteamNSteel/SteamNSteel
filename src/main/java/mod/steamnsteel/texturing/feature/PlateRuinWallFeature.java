package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.texturing.wall.RuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class PlateRuinWallFeature extends ProceduralWallFeatureBase
{
    private final long FEATURE_EDGE_TOP_AND_BOTTOM;
    private final long FEATURE_EDGE_LEFT_AND_RIGHT;
    private RuinWallTexture ruinWallTexture;

    public PlateRuinWallFeature(RuinWallTexture ruinWallTexture, int layer)
    {
        super("Pipes", layer);
        this.ruinWallTexture = ruinWallTexture;

        FEATURE_EDGE_TOP_AND_BOTTOM = ProceduralConnectedTexture.FEATURE_EDGE_TOP | ProceduralConnectedTexture.FEATURE_EDGE_BOTTOM;
        FEATURE_EDGE_LEFT_AND_RIGHT = ProceduralConnectedTexture.FEATURE_EDGE_LEFT | ProceduralConnectedTexture.FEATURE_EDGE_RIGHT;
    }

    public boolean isFeatureValid(TextureContext context)
    {
        if (!ruinWallTexture.isBlockPartOfWallAndUnobstructed(context))
        {
            return false;
        }

        boolean leftValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.LEFT);
        boolean rightValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.RIGHT);

        if (!leftValid && !rightValid)
        {
            return false;
        }
        boolean aboveValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.ABOVE);
        boolean belowValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.BELOW);

        if (!aboveValid && !belowValid)
        {
            return false;
        }

        //check for a cluster of 4 - Automatically valid
        //check above and left
        if (aboveValid && leftValid &&
                ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.ABOVE, TextureDirection.LEFT))
        {
            return true;
        }
        //check above and right
        if (aboveValid && rightValid &&
                ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.ABOVE, TextureDirection.RIGHT))
        {
            return true;
        }
        //check below and left
        if (belowValid && leftValid &&
                ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.BELOW, TextureDirection.LEFT))
        {
            return true;
        }
        //check below and right
        if (belowValid && rightValid &&
                ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, false, TextureDirection.BELOW, TextureDirection.RIGHT))
        {
            return true;
        }

        //We have encountered an S or Z shape;
        return false;
    }

    @Override
    public Collection<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        final int featureCount = 64;

        Random random = new Random(Objects.hash(chunkCoord, getFeatureId(), 6));

        List<FeatureInstance> features = new ArrayList<FeatureInstance>(featureCount);

        //Generate plate features
        for (int i = 0; i < featureCount; ++i)
        {
            int xPos = random.nextInt(18) - 1;
            int yPos = random.nextInt(16);
            int zPos = random.nextInt(18) - 1;

            int width = random.nextInt(7) + 1;
            int height = random.nextInt(7) + 1;
            int depth = random.nextInt(7) + 1;

            features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), width, height, depth));
        }

        return features;
    }

    @Override
    public long getSubProperties(TextureContext context)
    {
        long subProperties = 0;
        boolean isLeftBlockValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, true, TextureDirection.LEFT);
        boolean isRightBlockValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, true, TextureDirection.RIGHT);
        boolean isAboveBlockValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, true, TextureDirection.ABOVE);
        boolean isBelowBlockValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this, true, TextureDirection.BELOW);

        if (!isLeftBlockValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_LEFT;
        }
        if (!isRightBlockValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_RIGHT;
        }

        if (!isAboveBlockValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_TOP;
        }
        if (!isBelowBlockValid)
        {
            subProperties |= ProceduralConnectedTexture.FEATURE_EDGE_BOTTOM;
        }


        //Plates cannot be 1 high or 1 wide.
        if ((subProperties & FEATURE_EDGE_TOP_AND_BOTTOM) == FEATURE_EDGE_TOP_AND_BOTTOM)
        {
            return 0;
        } else if ((subProperties & FEATURE_EDGE_LEFT_AND_RIGHT) == FEATURE_EDGE_LEFT_AND_RIGHT)
        {
            return 0;
        }

        //Calculate corner cases

        if (isAboveBlockValid && isLeftBlockValid)
        {
            boolean isCornerValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this,
                    true, TextureDirection.ABOVE, TextureDirection.LEFT);
            if (!isCornerValid)
            {
                subProperties |= RuinWallTexture.FEATURE_PLATE_TL_CORNER;
            }
        }
        if (isAboveBlockValid&& isRightBlockValid)
        {
            boolean isCornerValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this,
                    true, TextureDirection.ABOVE, TextureDirection.RIGHT);
            if (!isCornerValid)
            {
                subProperties |= RuinWallTexture.FEATURE_PLATE_TR_CORNER;
            }
        }

        if (isBelowBlockValid&& isLeftBlockValid)
        {
            boolean isCornerValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this,
                    true, TextureDirection.BELOW, TextureDirection.LEFT);
            if (!isCornerValid)
            {
                subProperties |= RuinWallTexture.FEATURE_PLATE_BL_CORNER;
            }
        }
        if (isBelowBlockValid && isRightBlockValid)
        {
            boolean isCornerValid = ruinWallTexture.isFeatureAtCoordVisibleAndCompatible(context, getLayer(), this,
                    true, TextureDirection.BELOW, TextureDirection.RIGHT);

            if (!isCornerValid)
            {
                subProperties |= RuinWallTexture.FEATURE_PLATE_BR_CORNER;
            }
        }

        final long topAndLeft = RuinWallTexture.FEATURE_EDGE_TOP | RuinWallTexture.FEATURE_EDGE_LEFT;
        final long topAndRight = RuinWallTexture.FEATURE_EDGE_TOP | RuinWallTexture.FEATURE_EDGE_RIGHT;
        if ((subProperties & topAndLeft) == topAndLeft || (subProperties & topAndRight) == topAndRight) {
            if (context.useAlternateVersion(0.2f)) {
                subProperties |= RuinWallTexture.ALTERNATE;
            }
        }

        return subProperties;
    }

    @Override
    public String getName()
    {
        return "Plate";
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long featureProperties)
    {
        return Behaviour.COEXIST;
    }
}
