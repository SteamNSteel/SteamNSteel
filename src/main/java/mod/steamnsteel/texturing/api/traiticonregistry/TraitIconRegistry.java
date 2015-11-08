package mod.steamnsteel.texturing.api.traiticonregistry;

import mod.steamnsteel.TheMod;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import java.util.HashMap;

/**
 * A Fluent Interface for registering IIcons for use in a procedural texture
 */
public class TraitIconRegistry implements IIconDefinitionStart, ITraitSetOrNewIconDefinition, IAdditionalTraitSetOrNewIconDefinition
{
    private final TextureMap iconRegister;

    private TextureAtlasSprite currentIcon = null;
    private HashMap<Long, TextureAtlasSprite> icons = new HashMap<Long, TextureAtlasSprite>();

    public TraitIconRegistry(TextureMap iconRegister)
    {
        this.iconRegister = iconRegister;
    }

    /**
     * Starts a new set of Trait Sets for an Icon
     *
     * @param icon The name of the icon
     * @return an interface to add Trait Sets, or create a new Icon Definition
     */
    public ITraitSetOrNewIconDefinition useIconNamed(ResourceLocation icon)
    {
        currentIcon = iconRegister.registerSprite(icon);
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
    public TextureAtlasSprite getTextureFor(long traitSet)
    {
        return icons.get(traitSet);
    }
}
