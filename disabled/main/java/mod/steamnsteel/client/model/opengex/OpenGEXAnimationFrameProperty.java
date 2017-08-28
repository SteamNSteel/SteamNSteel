package mod.steamnsteel.client.model.opengex;

import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by codew on 4/11/2015.
 */
public enum OpenGEXAnimationFrameProperty implements IUnlistedProperty<OpenGEXState>
{
    instance;

    @Override
    public String getName()
    {
        return "OpenGEXAnimationTime";
    }

    @Override
    public boolean isValid(OpenGEXState value)
    {
        return value instanceof OpenGEXState;
    }

    @Override
    public Class<OpenGEXState> getType()
    {
        return OpenGEXState.class;
    }

    @Override
    public String valueToString(OpenGEXState value)
    {
        return value.toString();
    }
}
