package mod.steamnsteel.client.model.pct;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * WARNING: The PropertyBlockAccess unlisted property is usable for models that inevitably need to read from the world during
 * generation as part of an ISmartBlockModel. It is not safe to use unless you restrict yourself to only reading
 * Block states, reading tile entities is not guaranteed to be thread safe and may crash your world/eat your firstborn.
 */
public class PropertyBlockAccess implements IUnlistedProperty<IBlockAccess>
{
    private final String name;
    private final Predicate<IBlockAccess> validator;

    public PropertyBlockAccess(String name)
    {
        this(name, Predicates.<IBlockAccess>alwaysTrue());
    }

    public PropertyBlockAccess(String name, Predicate<IBlockAccess> validator)
    {
        this.name = name;
        this.validator = validator;
    }

    public String getName()
    {
        return name;
    }

    public boolean isValid(IBlockAccess value)
    {
        return validator.apply(value);
    }

    public Class<IBlockAccess> getType()
    {
        return IBlockAccess.class;
    }

    public String valueToString(IBlockAccess value)
    {
        return value.toString();
    }
}