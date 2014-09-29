package mod.steamnsteel.texturing;

interface IAdditionalTextureConditionOrNewSet extends ITextureConditionSet
{
    IAdditionalTextureConditionOrNewSet andCondition(long condition);
}
