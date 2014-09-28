package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class PipesRuinWallFeature extends ProceduralWallFeatureBase
{

    private RuinWallTexture ruinWallTexture;

    public PipesRuinWallFeature(RuinWallTexture ruinWallTexture, int layer)
    {
        super("Pipes", layer);
        this.ruinWallTexture = ruinWallTexture;
    }

    @Override
    public boolean isFeatureValid(TextureContext context)
    {
        if (!ruinWallTexture.isBlockPartOfWallAndUnobstructed(context))
        {
            return false;
        }

        final boolean aboveBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.ABOVE);
        final boolean aboveBlockFeatureIsCompatible = ruinWallTexture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, TextureDirection.ABOVE);
        final boolean belowBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.BELOW);
        final boolean belowBlockFeatureIsCompatible = ruinWallTexture.isFeatureAtCoordCompatibleWith(context, getLayer(), this, TextureDirection.BELOW);

        boolean aboveValid2 = ruinWallTexture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.ABOVE, TextureDirection.ABOVE);
        boolean belowValid2 = ruinWallTexture.isBlockPartOfWallAndUnobstructed(context, TextureDirection.BELOW, TextureDirection.BELOW);


        if (aboveBlockIsClear && !aboveBlockFeatureIsCompatible && belowBlockIsClear && belowBlockFeatureIsCompatible && belowValid2)
        {
            return true;
        }

        if (belowBlockIsClear && !belowBlockFeatureIsCompatible && aboveBlockIsClear && aboveBlockFeatureIsCompatible && aboveValid2)
        {
            return true;
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
    public boolean canIntersect(IProceduralWallFeature feature)
    {
        if (feature instanceof PlateRuinWallFeature)
        {
            return true;
        }
        return false;
    }

    @Override
    public long getSubProperties(TextureContext context, long currentProperties)
    {
        //ForgeDirection below = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);
        //ForgeDirection above = BlockSideRotation.forOrientation(TextureDirection.ABOVE, orientation);

        long subProperties = getFeatureId();
        IProceduralWallFeature aboveBlockFeature = ruinWallTexture.getValidFeature(context, getLayer(), TextureDirection.ABOVE);
        IProceduralWallFeature belowBlockFeature = ruinWallTexture.getValidFeature(context, getLayer(), TextureDirection.BELOW);

        if (!(aboveBlockFeature instanceof PipesRuinWallFeature))
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_TOP;
        }
        if (!(belowBlockFeature instanceof PipesRuinWallFeature))
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_BOTTOM;
        }

        final long FEATURE_EDGE_TOP_AND_BOTTOM = ruinWallTexture.FEATURE_EDGE_TOP | ruinWallTexture.FEATURE_EDGE_BOTTOM;

        //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
        subProperties &= getFeatureId() | FEATURE_EDGE_TOP_AND_BOTTOM;
        return subProperties | currentProperties;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature)
    {
        if (otherLayerFeature instanceof TopBandWallFeature || otherLayerFeature instanceof BottomBandWallFeature) {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }
}
