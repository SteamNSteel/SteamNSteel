package mod.steamnsteel.texturing.api.traitregistry;

import mod.steamnsteel.texturing.api.IProceduralWallFeature;
import mod.steamnsteel.texturing.api.Layer;

public interface IFeatureRegistry
{
    void registerFeature(IProceduralWallFeature feature);

    long registerFeatureProperty(String description);

    Layer registerLayer(String plate, boolean allowRandomization);
}
