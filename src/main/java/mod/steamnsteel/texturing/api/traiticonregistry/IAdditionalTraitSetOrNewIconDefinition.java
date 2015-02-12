package mod.steamnsteel.texturing.api.traiticonregistry;

/**
 * Applies an additional Trait Set to an Icon
 */
public interface IAdditionalTraitSetOrNewIconDefinition extends IIconDefinitionStart
{
    /**
     * Applies an additional Trait Set to an Icon
     *
     * @param traitSet the traitSet to apply to the icon
     * @return an interface that will allow you to specify additional Trait Sets, or create a new icon.
     */
    IAdditionalTraitSetOrNewIconDefinition andTraitSet(long traitSet);
}
