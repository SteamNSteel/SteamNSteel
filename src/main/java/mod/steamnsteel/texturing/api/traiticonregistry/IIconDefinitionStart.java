package mod.steamnsteel.texturing.api.traiticonregistry;

import net.minecraft.util.ResourceLocation;

/**
 * Starts a declaration of a new Icon
 */
public interface IIconDefinitionStart
{
    /**
     * Starts a new set of Trait Sets for an Icon
     *
     * @param icon The name of the icon
     * @return an interface to add Trait Sets, or create a new Icon Definition
     */
    ITraitSetOrNewIconDefinition useIconNamed(ResourceLocation icon);
}
