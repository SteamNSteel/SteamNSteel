package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class OneByOneWallFeature extends ProceduralWallFeatureBase
{
    private RuinWallTexture ruinWallTexture;

    public OneByOneWallFeature(RuinWallTexture ruinWallTexture, String name, int layer)
    {
        super(name, layer);
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
        final boolean belowBlockIsClear = ruinWallTexture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(below), back);

        if (aboveBlockIsClear && belowBlockIsClear) {
            return true;
        }*/
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

            features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 1, 1));
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
        return 0;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature)
    {
        return Behaviour.COEXIST;
    }
}
