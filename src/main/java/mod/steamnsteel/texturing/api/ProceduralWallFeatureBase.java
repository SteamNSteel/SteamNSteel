package mod.steamnsteel.texturing.api;

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
     * @return the traitId of this feature
     */
    @Override
    public final long getFeatureTraitId()
    {
        return featureId;
    }

    /**
     * @param featureTraitId The Id of this feature.
     */
    @Override
    public final void setFeatureTraitId(long featureTraitId)
    {
        this.featureId = featureTraitId;
    }

    /**
     * @return the layer the feature is defined on
     */
    public final Layer getLayer()
    {
        return layer;
    }

    /**
     * @return the name of the feature
     */
    public final String getName()
    {
        return name;
    }

    /**
     * gets the traits present at the location of the IconRequest
     *
     * @param request the request to examine
     * @return the traits present at the specified location
     */
    @Override
    public long getTraits(IconRequest request)
    {
        return 0;
    }

    /**
     * @return a bit mask of features that are incompatible with this feature. I.e, a monitor and a valve can't
     * co-exist.
     */
    @Override
    public long getIncompatibleTraits()
    {
        return 0;
    }

    /**
     * Retrieves a set of FeatureInstances for the specified chunk
     *
     * @param chunkCoord the chunk to get instances for
     * @return the Features present in the chunk
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
