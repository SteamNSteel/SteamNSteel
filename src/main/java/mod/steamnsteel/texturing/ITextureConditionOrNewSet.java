package mod.steamnsteel.texturing;

/**
 * Applies a condition to a texture
 */
public interface ITextureConditionOrNewSet extends ITextureConditionSet
{
    /**
     * Applies a condition to a texture
     *
     * @param condition the featureId bits for a condition
     * @return an interface that will allow you to specify additional conditions, or create a new texture.
     */
    IAdditionalTextureConditionOrNewSet forCondition(long condition);
}
