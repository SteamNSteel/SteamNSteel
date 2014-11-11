package mod.steamnsteel.texturing.api;

import mod.steamnsteel.utility.position.ChunkCoord;

public interface IProceduralWallFeature
{
    Iterable<FeatureInstance> getFeatureInstancesFor(ChunkCoord chunkCoord);

    long getFeatureId();

    long getSubProperties(IconRequest context);

    void setFeatureId(long featureId);

    String getName();

    Layer getLayer();

    Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long featureProperties);

    boolean isFeatureValid(IconRequest request);

    long getIncompatibleProperties();
}
