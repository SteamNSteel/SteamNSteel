package mod.steamnsteel.texturing.api.traitspriteregistry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * A Fluent Interface for registering IIcons for use in a procedural texture
 */
public class TraitSpriteRegistry implements ISpriteDefinitionStart, ITraitSetOrNewSpriteDefinition, IAdditionalTraitSetOrNewSpriteDefinition, ISpriteLibrary
{
    private final TextureMap textureMap;

    private TextureAtlasSprite currentSprite = null;
    private HashMap<EnumFacing, HashMap<Long, TextureAtlasSprite>> sprites = new HashMap<>();
    private Collection<EnumFacing> activeSides = new ArrayList<>();

    public TraitSpriteRegistry(TextureMap textureMap)
    {
        this.textureMap = textureMap;

        for (final EnumFacing side : EnumFacing.VALUES)
        {
            activeSides.add(side);
            sprites.put(side, new HashMap<Long, TextureAtlasSprite>());
        }
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
     * Sets the sides that will receive the following trait sets.
     * @param sides The list of sides affected by the next set of sprite definitions.
     */
    @Override
    public void forSides(EnumFacing[] sides, ISideTraitList sideTraitList)
    {
        final Collection<EnumFacing> existingSides = this.activeSides;
        activeSides = new ArrayList<>();
        activeSides.addAll(Arrays.asList(sides));

        sideTraitList.register();

        activeSides = existingSides;
    }

    /**
     * Applies a Trait Set to an Icon
     *
     * @param traitSet the Trait Set to apply to an Icon
     * @return an interface that will allow you to specify additional Trait Sets, or create a new Icon Definition.
     */
    public IAdditionalTraitSetOrNewSpriteDefinition forTraitSet(long traitSet)
    {
        for (final EnumFacing activeSide : activeSides)
        {
            final HashMap<Long, TextureAtlasSprite> spriteList = sprites.get(activeSide);
            spriteList.put(traitSet, currentSprite);
        }


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
    public TextureAtlasSprite getTextureFor(EnumFacing side, long traitSet)
    {
        return sprites.get(side).get(traitSet);
    }
}
