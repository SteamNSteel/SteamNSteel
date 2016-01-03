package mod.steamnsteel.texturing.api;

import net.minecraft.util.BlockPos;

/**
 * Describes a 3D space of a feature within a chunk.
 */
public class FeatureInstance
{
    private final IProceduralWallFeature featureId;
    private final BlockPos blockCoord;
    private final int width;
    private final int height;
    private final int depth;

    public FeatureInstance(IProceduralWallFeature featureId, BlockPos blockCoord, int width, int height, int depth)
    {

        this.featureId = featureId;
        this.blockCoord = blockCoord;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public IProceduralWallFeature getFeature()
    {
        return featureId;
    }

    public BlockPos getBlockCoord()
    {
        return blockCoord;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getDepth()
    {
        return depth;
    }
}
