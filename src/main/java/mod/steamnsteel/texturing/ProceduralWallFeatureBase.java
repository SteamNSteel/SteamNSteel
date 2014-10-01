package mod.steamnsteel.texturing;

import com.google.common.collect.Iterables;
import mod.steamnsteel.utility.position.ChunkCoord;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class ProceduralWallFeatureBase implements IProceduralWallFeature
{
    private final String name;
    private final int layer;
    private long featureId;

    HashMap<ChunkCoord, Collection<FeatureInstance>> cachedFeatures = new HashMap<ChunkCoord, Collection<FeatureInstance>>();

    protected ProceduralWallFeatureBase(String name, int layer)
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

    public int getLayer()
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
    public Iterable<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord) {
        Iterable<FeatureInstance> featureResults = new ArrayList<FeatureInstance>();

        featureResults = Iterables.concat(
                getCachedFeatures(chunkCoord),
                getCachedFeatures(ChunkCoord.of(chunkCoord.getX()-1, chunkCoord.getZ())),
                getCachedFeatures(ChunkCoord.of(chunkCoord.getX(), chunkCoord.getZ()-1)),
                getCachedFeatures(ChunkCoord.of(chunkCoord.getX()-1, chunkCoord.getZ()-1))
        );

        return featureResults;
    }

    private Collection<FeatureInstance> getCachedFeatures(ChunkCoord chunkCoord) {
        Collection<FeatureInstance> result = cachedFeatures.get(chunkCoord);
        if (result == null) {
            result = getFeaturesIn(chunkCoord);
            cachedFeatures.put(chunkCoord, result);
        }
        return result;
    }


    protected abstract Collection<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord);


}
