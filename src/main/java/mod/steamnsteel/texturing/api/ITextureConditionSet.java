package mod.steamnsteel.texturing.api;

/**
 * Starts a declaration of a new texture
 */
public interface ITextureConditionSet
{
    /**
     * Starts a new set of conditions for a texture
     *
     * @param textureName The name of the texture
     * @return an interface to add conditions, or create a new texture
     */
    ITextureConditionOrNewSet useTexture(String textureName);
}
