package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
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
        /*ForgeDirection back = BlockSideRotation.forOrientation(TextureDirection.BACKWARDS, orientation);

        if (!texture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord, back))
        {
            return false;
        }

        ForgeDirection left = BlockSideRotation.forOrientation(TextureDirection.LEFT, orientation);
        ForgeDirection right = BlockSideRotation.forOrientation(TextureDirection.RIGHT, orientation);

        IProceduralWallFeature blockFeature;
        boolean blockIsValid;
        blockFeature = texture.getFeatureAt(worldBlockCoord.offset(left));
        blockIsValid = texture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(left), back);
        final boolean leftBlockIsValid = blockIsValid && blockFeature instanceof ThreeByOneWallFeature && blockFeature.getFeatureId() == getFeatureId();

        blockIsValid = texture.isFeatureAtCoordUsableAndOfType(worldBlockCoord.offset(left), getFeatureId());

        blockFeature = texture.getFeatureAt(worldBlockCoord.offset(right));
        blockIsValid = texture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(right), back);
        final boolean rightBlockIsValid = blockIsValid && blockFeature instanceof ThreeByOneWallFeature && blockFeature.getFeatureId() == getFeatureId();

        if (leftBlockIsValid && rightBlockIsValid) {
            return true;
        }

        blockFeature = texture.getFeatureAt(worldBlockCoord.offset(left).offset(left));
        blockIsValid = texture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(left).offset(left), back);
        final boolean leftLeftBlockIsValid = blockIsValid && blockFeature instanceof ThreeByOneWallFeature && blockFeature.getFeatureId() == getFeatureId();
        if (leftBlockIsValid && leftLeftBlockIsValid)
        {
            return true;
        }

        blockFeature = texture.getFeatureAt(worldBlockCoord.offset(right).offset(right));
        blockIsValid = texture.isBlockPartOfWallAndUnobstructed(blockAccess, worldBlockCoord.offset(right).offset(right), back);
        final boolean rightRightBlockIsValid = blockIsValid && blockFeature instanceof ThreeByOneWallFeature && blockFeature.getFeatureId() == getFeatureId();
        if (rightBlockIsValid && rightRightBlockIsValid)
        {
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

            if (random.nextBoolean())
            {
                features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 3, 1, 1));
            } else {
                features.add(new FeatureInstance(this, WorldBlockCoord.of(xPos, yPos, zPos), 1, 1, 3));
            }
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
    public long getSubProperties(TextureContext context)
    {
        /*ForgeDirection left = BlockSideRotation.forOrientation(TextureDirection.LEFT, orientation);
        ForgeDirection right = BlockSideRotation.forOrientation(TextureDirection.RIGHT, orientation);

        long subProperties = getFeatureId();
        IProceduralWallFeature leftBlockFeature = texture.getValidFeature(blockAccess, worldBlockCoord.offset(left), orientation);
        IProceduralWallFeature rightBlockFeature = texture.getValidFeature(blockAccess, worldBlockCoord.offset(right), orientation);

        if (!(leftBlockFeature instanceof ThreeByOneWallFeature) || leftBlockFeature.getFeatureId() != getFeatureId())
        {
            subProperties |= texture.FEATURE_EDGE_LEFT;
        }
        if (!(rightBlockFeature instanceof ThreeByOneWallFeature) || rightBlockFeature.getFeatureId() != getFeatureId())
        {
            subProperties |= texture.FEATURE_EDGE_RIGHT;
        }

        final long FEATURE_EDGE_TOP_AND_BOTTOM = texture.FEATURE_EDGE_LEFT | texture.FEATURE_EDGE_RIGHT;

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
