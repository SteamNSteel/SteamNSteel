package mod.steamnsteel.texturing.api.traiticonregistry;

import mod.steamnsteel.TheMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import java.util.HashMap;

/**
 * A Fluent Interface for registering IIcons for use in a procedural texture
 */
public class TraitIconRegistry implements IIconDefinitionStart, ITraitSetOrNewIconDefinition, IAdditionalTraitSetOrNewIconDefinition
{
    private final IIconRegister iconRegister;

    private IIcon currentIcon = null;
    private HashMap<Long, IIcon> icons = new HashMap<Long, IIcon>();

    public TraitIconRegistry(IIconRegister iconRegister)
    {
        this.iconRegister = iconRegister;
    }

    /**
     * Starts a new set of Trait Sets for an Icon
     *
     * @param iconName The name of the icon
     * @return an interface to add Trait Sets, or create a new Icon Definition
     */
    public ITraitSetOrNewIconDefinition useIconNamed(String iconName)
    {
        currentIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":" + iconName);
        return this;
    }

    /**
     * Applies a Trait Set to an Icon
     *
     * @param traitSet the Trait Set to apply to an Icon
     * @return an interface that will allow you to specify additional Trait Sets, or create a new Icon Definition.
     */
    public IAdditionalTraitSetOrNewIconDefinition forTraitSet(long traitSet)
    {
        icons.put(traitSet, currentIcon);
        return this;
    }

    /**
     * Applies an additional Trait Set to an Icon
     *
     * @param traitSet the traitSet to apply to the icon
     * @return an interface that will allow you to specify additional Trait Sets, or create a new icon.
     */
    @Override
    public IAdditionalTraitSetOrNewIconDefinition andTraitSet(long traitSet)
    {
        return forTraitSet(traitSet);
    }

    /**
     * Retrieves an IIcon for a Trait Set, or null if no Trait Set was found.
     *
     * @param traitSet The Trait Set to match
     * @return The appropriate IIcon.
     */
    public IIcon getTextureFor(long traitSet)
    {
        return icons.get(traitSet);
    }
}
