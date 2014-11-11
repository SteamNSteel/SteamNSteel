package mod.steamnsteel.texturing;

import mod.steamnsteel.utility.position.ChunkCoord;
import java.util.Collection;
import java.util.HashMap;

public abstract class ProceduralWallFeatureBase implements IProceduralWallFeature
{
    private final String name;
    private final Layer layer;
    private long featureId;

    //TODO: Find a proper caching object to do this.
    HashMap<ChunkCoord, Collection<FeatureInstance>> cachedFeatures = new HashMap<ChunkCoord, Collection<FeatureInstance>>();

    protected ProceduralWallFeatureBase(String name, Layer layer)
    {
        this.name = name;
        this.layer = layer;
    }

    /**
     * @return the Id of this feature
     */

    @Override
    public final long getFeatureId()
    {
        return featureId;
    }

    /**
     * @param featureId The Id of this feature.
     */
    @Override
    public final void setFeatureId(long featureId)
    {
        this.featureId = featureId;
    }

    public final Layer getLayer()
    {
        return layer;
    }

    public final String getName()
    {
        return name;
    }

    /**
     * TODO: Comment this!
     *
     * @param context
     * @return
     */
    @Override
    public long getSubProperties(TextureContext context)
    {
        return 0;
    }

    /**
     * @return a bit mask of features that are incompatible with this feature. I.e, a monitor and a valve can't
     * co-exist.
     */
    @Override
    public long getIncompatibleProperties()
    {
        return 0;
    }

    /**
     * Retrieves a set of Feature Instances for the specified chunk
     *
     * @param chunkCoord
     * @return
     */
    @Override
    public Iterable<FeatureInstance> getFeatureInstancesFor(ChunkCoord chunkCoord)
    {
        return getCachedFeatures(chunkCoord);
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


    /**
     * Implementers of this method should provide a set of feature instances for a given 16x16 chunk.
     *
     * @param chunkCoord
     * @return
     */
    protected abstract Collection<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord);


}
