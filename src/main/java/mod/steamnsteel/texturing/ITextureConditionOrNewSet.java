package mod.steamnsteel.texturing;

public interface ITextureConditionOrNewSet extends ITextureConditionSet {
    IAdditionalTextureConditionOrNewSet forCondition(long condition);
}
