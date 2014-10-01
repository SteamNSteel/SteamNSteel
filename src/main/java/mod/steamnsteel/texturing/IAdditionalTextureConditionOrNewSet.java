package mod.steamnsteel.texturing;

public interface IAdditionalTextureConditionOrNewSet extends ITextureConditionSet
{
    IAdditionalTextureConditionOrNewSet andCondition(long condition);
}
