package mod.steamnsteel.texturing.api;

import mod.steamnsteel.utility.position.ChunkCoord;

public interface IProceduralWallFeature
{
    /**
     * Retrieves a set of FeatureInstances for the specified chunk
     *
     * @param chunkCoord the chunk to get instances for
     * @return the Features present in the chunk
     */
    Iterable<FeatureInstance> getFeatureInstancesFor(ChunkCoord chunkCoord);

    /**
     * @return the traitId of this feature
     */
    long getFeatureTraitId();

    /**
     * gets the traits present at the location of the IconRequest
     *
     * @param request the request to examine
     * @return the traits present at the specified location
     */
    long getTraits(IconRequest request);

    /**
     * @param featureTraitId The Id of this feature.
     */
    void setFeatureTraitId(long featureTraitId);

    /**
     * @return the name of the feature
     */
    String getName();

    /**
     * @return the layer the feature is defined on
     */
    Layer getLayer();

    /**
     * Checks if this feature can coexist with another feature on another layer.
     * @param otherLayerFeature the other feature
     * @param traits the traits applied to the other layer
     * @return the behaviour of them coexisting
     */
    Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long traits);

    /**
     * checks if the feature is valid at a location
     * @param request the request containing the location
     * @return true if the feature can exist at the location
     */
    boolean isFeatureValid(IconRequest request);

    /**
     * returns a list of traits that this feature is incompatible with. These features will be removed from the Trait Set
     * @return incompatible traits.
     */
    long getIncompatibleTraits();
}
