package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.BlockSideRotation;
import mod.steamnsteel.texturing.IProceduralWallFeature;
import mod.steamnsteel.texturing.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.RuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class OneByOneRuinWallFeature implements IProceduralWallFeature
{
    private RuinWallTexture ruinWallTexture;
    private final int featureId;

    public OneByOneRuinWallFeature(RuinWallTexture ruinWallTexture, int featureId)
    {
        this.ruinWallTexture = ruinWallTexture;
        this.featureId = featureId;
    }

    @Override
    public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        ForgeDirection back = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BACK, orientation);

        if (!ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord, back))
        {
            return false;
        }

        ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);

        final boolean aboveBlockIsClear = ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above), back);
        final boolean belowBlockIsClear = ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below), back);

        if (aboveBlockIsClear && belowBlockIsClear) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<ProceduralConnectedTexture.FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        Random random = new Random(Objects.hash(chunkCoord, featureId, 13));

        final int featureCount = 64;

        List<ProceduralConnectedTexture.FeatureInstance> features = new ArrayList<ProceduralConnectedTexture.FeatureInstance>(featureCount);
        //Generate Pipe features
        for (int i = 0; i < featureCount; ++i)
        {
            int xPos = random.nextInt(18) - 1;
            int yPos = random.nextInt(16);
            int zPos = random.nextInt(18) - 1;

            features.add(new ProceduralConnectedTexture.FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 1, 1));
        }
        return features;
    }

    @Override
    public int getFeatureId()
    {
        return featureId;
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
    public int getSubProperties(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        return featureId;
    }
}
