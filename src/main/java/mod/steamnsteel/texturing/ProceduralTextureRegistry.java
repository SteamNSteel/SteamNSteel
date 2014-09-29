package mod.steamnsteel.texturing;

import mod.steamnsteel.TheMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import java.util.HashMap;

class ProceduralTextureRegistry implements ITextureConditionSet, ITextureConditionOrNewSet, IAdditionalTextureConditionOrNewSet
{
    private final IIconRegister iconRegister;

    private IIcon currentIcon = null;
    private HashMap<Long, IIcon> icons = new HashMap<Long, IIcon>();

    public ProceduralTextureRegistry(IIconRegister iconRegister)
    {
        this.iconRegister = iconRegister;
    }

    public ITextureConditionOrNewSet useTexture(String textureName)
    {
        currentIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":" + textureName);
        return this;
    }

    public IAdditionalTextureConditionOrNewSet forCondition(long condition)
    {
        icons.put(condition, currentIcon);
        return this;
    }

    @Override
    public IAdditionalTextureConditionOrNewSet andCondition(long condition)
    {
        return forCondition(condition);
    }

    public IIcon getTextureFor(long condition)
    {
        return icons.get(condition);
    }
}
