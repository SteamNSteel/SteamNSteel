package mod.steamnsteel.texturing;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class ProceduralWallFeatureBase implements IProceduralWallFeature
{
    private final String name;
    private final Layer layer;
    private long featureId;

    HashMap<ChunkCoord, Collection<FeatureInstance>> cachedFeatures = new HashMap<ChunkCoord, Collection<FeatureInstance>>();

    protected ProceduralWallFeatureBase(String name, Layer layer)
    {
        this.name = name;
        this.layer = layer;
    }

    @Override
    public long getFeatureId()
    {
        return featureId;
    }

    @Override
    public void setFeatureId(long featureId)
    {
        this.featureId = featureId;
    }

    public Layer getLayer()
    {
        return layer;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public long getSubProperties(TextureContext context)
    {
        return 0;
    }

    @Override
    public long getIncompatibleProperties()
    {
        return 0;
    }

    @Override
    public Iterable<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        Iterable<FeatureInstance> featureResults;

        featureResults = Iterables.concat(
                getCachedFeatures(chunkCoord)
                //Iterables.transform(getCachedFeatures(ChunkCoord.of(chunkCoord.getX() - 1, chunkCoord.getZ())), new FeatureTransformerFunction(-1, 0)),
                //Iterables.transform(getCachedFeatures(ChunkCoord.of(chunkCoord.getX(), chunkCoord.getZ() - 1)), new FeatureTransformerFunction(0, -1)),
                //Iterables.transform(getCachedFeatures(ChunkCoord.of(chunkCoord.getX() - 1, chunkCoord.getZ() - 1)), new FeatureTransformerFunction(-1, -1))
        );

        return featureResults;
    }

    private class FeatureTransformerFunction implements Function<FeatureInstance, FeatureInstance>
    {

        private final int offsetX;
        private final int offsetZ;

        public FeatureTransformerFunction(int chunksX, int chunksZ)
        {
            offsetX = chunksX << 4;
            offsetZ = chunksZ << 4;
        }

        @Override
        public FeatureInstance apply(FeatureInstance input)
        {
            return new FeatureInstance(input.getFeature(),
                    WorldBlockCoord.of(
                            input.getBlockCoord().getX() + offsetX,
                            input.getBlockCoord().getY(),
                            input.getBlockCoord().getZ() + offsetZ),
                    input.getWidth(),
                    input.getHeight(),
                    input.getDepth());
        }
    }

    private Collection<FeatureInstance> getCachedFeatures(ChunkCoord chunkCoord)
    {
        Collection<FeatureInstance> result = cachedFeatures.get(chunkCoord);
        if (result == null)
        {
            result = getFeaturesIn(chunkCoord);
            cachedFeatures.put(chunkCoord, result);
        }
        return result;
    }


    protected abstract Collection<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord);


}
