package mod.steamnsteel.texturing;

import mod.steamnsteel.TheMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import java.util.HashMap;

/**
 * A Fluent Interface for registering IIcons for use in a procedural texture
 */
class ProceduralTextureRegistry implements ITextureConditionSet, ITextureConditionOrNewSet, IAdditionalTextureConditionOrNewSet
{
    private final IIconRegister iconRegister;

    private IIcon currentIcon = null;
    private HashMap<Long, IIcon> icons = new HashMap<Long, IIcon>();

    public ProceduralTextureRegistry(IIconRegister iconRegister)
    {
        this.iconRegister = iconRegister;
    }

    /**
     * Starts a new set of conditions for a texture
     * @param textureName The name of the texture
     * @return an interface to add conditions, or create a new texture
     */
    public ITextureConditionOrNewSet useTexture(String textureName)
    {
        currentIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":" + textureName);
        return this;
    }

    /**
     * Applies a condition to a texture
     * @param condition the featureId bits for a condition
     * @return an interface that will allow you to specify additional conditions, or create a new texture.
     */
    public IAdditionalTextureConditionOrNewSet forCondition(long condition)
    {
        icons.put(condition, currentIcon);
        return this;
    }

    /**
     * Applies an additional condition to a texture
     * @param condition the featureId bits for a condition
     * @return an interface that will allow you to specify additional conditions, or create a new texture.
     */
    @Override
    public IAdditionalTextureConditionOrNewSet andCondition(long condition)
    {
        return forCondition(condition);
    }

    /**
     * Retrieves an IIcon for a condition, or null if no condition was found.
     * @param condition The Condition to match
     * @return The appropriate IIcon.
     */
    public IIcon getTextureFor(long condition)
    {
        return icons.get(condition);
    }
}
