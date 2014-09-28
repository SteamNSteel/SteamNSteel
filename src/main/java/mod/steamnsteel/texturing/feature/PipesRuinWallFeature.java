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
    public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        /*ForgeDirection back = BlockSideRotation.forOrientation(TextureDirection.BACKWARDS, orientation);

        if (!ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord, back))
        {
            return false;
        }

        ForgeDirection below = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(TextureDirection.ABOVE, orientation);

        final boolean aboveBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(above), back);
        final IProceduralWallFeature aboveBlockFeature = ruinWallTexture.getFeatureAt(worldBlockCoord.offset(above));
        final boolean belowBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(below), back);
        final IProceduralWallFeature belowBlockFeature = ruinWallTexture.getFeatureAt(worldBlockCoord.offset(below));

        boolean aboveValid2 = (ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(above).offset(above), back));
        boolean belowValid2 = (ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(below).offset(below), back));

        if (aboveBlockIsClear && !(aboveBlockFeature instanceof PipesRuinWallFeature) && belowBlockIsClear && belowBlockFeature instanceof PipesRuinWallFeature && belowValid2)
        {
            return true;
        }

        if (belowBlockIsClear && !(belowBlockFeature instanceof PipesRuinWallFeature) && aboveBlockIsClear && aboveBlockFeature instanceof PipesRuinWallFeature && aboveValid2)
        {
            return true;
        }*/


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
    public long getSubProperties(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        /*ForgeDirection below = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(TextureDirection.ABOVE, orientation);

        long subProperties = getFeatureId();
        IProceduralWallFeature aboveBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(above), orientation);
        IProceduralWallFeature belowBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(below), orientation);

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
        return subProperties;*/
        return 0;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature)
    {
        return Behaviour.COEXIST;
    }
}
