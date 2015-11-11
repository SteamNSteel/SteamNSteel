package mod.steamnsteel.texturing.api.traitspriteregistry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import java.util.HashMap;

/**
 * A Fluent Interface for registering IIcons for use in a procedural texture
 */
public class TraitSpriteRegistry implements ISpriteDefinitionStart, ITraitSetOrNewSpriteDefinition, IAdditionalTraitSetOrNewSpriteDefinition
{
    private final TextureMap textureMap;

    private TextureAtlasSprite currentSprite = null;
    private HashMap<Long, TextureAtlasSprite> sprites = new HashMap<Long, TextureAtlasSprite>();

    public TraitSpriteRegistry(TextureMap textureMap)
    {
        this.textureMap = textureMap;
    }

    /**
     * Starts a new set of Trait Sets for an Icon
     *
     * @param spriteLocation The name of the icon
     * @return an interface to add Trait Sets, or create a new Icon Definition
     */
    public ITraitSetOrNewSpriteDefinition useSpriteNamed(ResourceLocation spriteLocation)
    {
        currentSprite = textureMap.registerSprite(spriteLocation);
        return this;
    }

    /**
     * Applies a Trait Set to an Icon
     *
     * @param traitSet the Trait Set to apply to an Icon
     * @return an interface that will allow you to specify additional Trait Sets, or create a new Icon Definition.
     */
    public IAdditionalTraitSetOrNewSpriteDefinition forTraitSet(long traitSet)
    {
        sprites.put(traitSet, currentSprite);
        return this;
    }

    /**
     * Applies an additional Trait Set to an Icon
     *
     * @param traitSet the traitSet to apply to the icon
     * @return an interface that will allow you to specify additional Trait Sets, or create a new icon.
     */
    @Override
    public IAdditionalTraitSetOrNewSpriteDefinition andTraitSet(long traitSet)
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
        return sprites.get(traitSet);
    }
}
