package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.api.traitspriteregistry.ISpriteDefinitionStart;
import net.minecraft.util.ResourceLocation;

/**
 * Use this class to define a set of traits that are best described as a sequential group of features, like repeating
 * textures.
 */
public interface IMultiTraitFeature
{
    /**
     * The number of discrete states that this feature has.
     * @return
     */
    int getSequentialStateCount();

    void registerTextures(ISpriteDefinitionStart textureDefinitions, ResourceLocation baseResourceLocation);
}
