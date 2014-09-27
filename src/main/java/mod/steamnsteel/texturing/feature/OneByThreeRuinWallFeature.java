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

public class OneByThreeRuinWallFeature implements IProceduralWallFeature
{
    private RuinWallTexture ruinWallTexture;
    private final int featureId;

    public OneByThreeRuinWallFeature(RuinWallTexture ruinWallTexture, int featureId)
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

        ForgeDirection left = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.LEFT, orientation);
        ForgeDirection right = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.RIGHT, orientation);

        IProceduralWallFeature blockFeature;
        boolean blockIsValid;
        blockFeature = ruinWallTexture.getFeatureAt(worldBlockCoord.offset(left));
        blockIsValid = ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left), back);
        final boolean leftBlockIsValid = blockIsValid && blockFeature instanceof OneByThreeRuinWallFeature && blockFeature.getFeatureId() == this.featureId;

        blockFeature = ruinWallTexture.getFeatureAt(worldBlockCoord.offset(right));
        blockIsValid = ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right), back);
        final boolean rightBlockIsValid = blockIsValid && blockFeature instanceof OneByThreeRuinWallFeature && blockFeature.getFeatureId() == this.featureId;

        if (leftBlockIsValid && rightBlockIsValid) {
            return true;
        }

        blockFeature = ruinWallTexture.getFeatureAt(worldBlockCoord.offset(left).offset(left));
        blockIsValid = ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left).offset(left), back);
        final boolean leftLeftBlockIsValid = blockIsValid && blockFeature instanceof OneByThreeRuinWallFeature && blockFeature.getFeatureId() == this.featureId;
        if (leftBlockIsValid && leftLeftBlockIsValid)
        {
            return true;
        }

        blockFeature = ruinWallTexture.getFeatureAt(worldBlockCoord.offset(right).offset(right));
        blockIsValid = ruinWallTexture.checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right).offset(right), back);
        final boolean rightRightBlockIsValid = blockIsValid && blockFeature instanceof OneByThreeRuinWallFeature && blockFeature.getFeatureId() == this.featureId;
        if (rightBlockIsValid && rightRightBlockIsValid)
        {
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

            if (random.nextBoolean())
            {
                features.add(new ProceduralConnectedTexture.FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 3, 1, 1));
            } else {
                features.add(new ProceduralConnectedTexture.FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 1, 3));
            }
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
        ForgeDirection left = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.LEFT, orientation);
        ForgeDirection right = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.RIGHT, orientation);

        int subProperties = featureId;
        IProceduralWallFeature leftBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(left), orientation);
        IProceduralWallFeature rightBlockFeature = ruinWallTexture.getValidFeature(blockAccess, worldBlockCoord.offset(right), orientation);

        if (!(leftBlockFeature instanceof OneByThreeRuinWallFeature) || leftBlockFeature.getFeatureId() != featureId)
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_LEFT;
        }
        if (!(rightBlockFeature instanceof OneByThreeRuinWallFeature) || rightBlockFeature.getFeatureId() != featureId)
        {
            subProperties |= ruinWallTexture.FEATURE_EDGE_RIGHT;
        }

        final int FEATURE_EDGE_TOP_AND_BOTTOM = ruinWallTexture.FEATURE_EDGE_LEFT | ruinWallTexture.FEATURE_EDGE_RIGHT;

        //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
        subProperties &= featureId | FEATURE_EDGE_TOP_AND_BOTTOM;
         return subProperties;
    }
}
