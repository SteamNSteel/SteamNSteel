package mod.steamnsteel.texturing.api.traiticonregistry;

/**
 * Applies a Trait Set to an Icon
 */
public interface ITraitSetOrNewIconDefinition extends IIconDefinitionStart
{
    /**
     * Applies a Trait Set to an Icon
     *
     * @param traitSet the Trait Set to apply to an Icon
     * @return an interface that will allow you to specify additional Trait Sets, or create a new Icon Definition.
     */
    IAdditionalTraitSetOrNewIconDefinition forTraitSet(long traitSet);
}
