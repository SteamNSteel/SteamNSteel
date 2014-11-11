package mod.steamnsteel.texturing.api;

import mod.steamnsteel.utility.position.ChunkCoord;

public interface IProceduralWallFeature
{
    Iterable<FeatureInstance> getFeatureInstancesFor(ChunkCoord chunkCoord);

    long getFeatureId();

    long getSubProperties(TextureContext context);

    void setFeatureId(long featureId);

    String getName();

    Layer getLayer();

    Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long featureProperties);

    boolean isFeatureValid(TextureContext context);

    long getIncompatibleProperties();
}
