package mod.steamnsteel.texturing;

interface ITextureConditionOrNewSet extends ITextureConditionSet {
    IAdditionalTextureConditionOrNewSet forCondition(long condition);
}
