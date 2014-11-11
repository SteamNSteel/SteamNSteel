package mod.steamnsteel.texturing.api.iconregistry;

/**
 * Applies an additional condition to a texture
 */
public interface IAdditionalTextureConditionOrNewSet extends ITextureConditionSet
{
    /**
     * Applies an additional condition to a texture
     *
     * @param condition the featureId bits for a condition
     * @return an interface that will allow you to specify additional conditions, or create a new texture.
     */
    IAdditionalTextureConditionOrNewSet andCondition(long condition);
}
