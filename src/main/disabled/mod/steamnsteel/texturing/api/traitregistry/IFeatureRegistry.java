package mod.steamnsteel.texturing.api.traitregistry;

import mod.steamnsteel.texturing.api.IProceduralWallFeature;
import mod.steamnsteel.texturing.api.Layer;

public interface IFeatureRegistry
{
    /**
     * Registers a feature
     *
     * @param feature The Feature
     */
    void registerFeature(IProceduralWallFeature feature);

    long registerTrait(String description);

    /**
     * Registers a new layer.
     * The allowRandomization parameter allows you to specify that y values above 16 will be altered so as to not provide a sterile wall
     *
     * @param name               The name of the layer
     * @param allowRandomization true, if the layer should be randomized
     * @return The created layer
     */
    Layer registerLayer(String name, boolean allowRandomization);
}
