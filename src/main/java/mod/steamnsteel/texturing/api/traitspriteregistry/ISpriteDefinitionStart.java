package mod.steamnsteel.texturing.api.traitspriteregistry;

import net.minecraft.util.ResourceLocation;

/**
 * Starts a declaration of a new Icon
 */
public interface ISpriteDefinitionStart
{
    /**
     * Starts a new set of Trait Sets for an Icon
     *
     * @param spriteLocation The name of the spriteLocation
     * @return an interface to add Trait Sets, or create a new Icon Definition
     */
    ITraitSetOrNewSpriteDefinition useSpriteNamed(ResourceLocation spriteLocation);
}
