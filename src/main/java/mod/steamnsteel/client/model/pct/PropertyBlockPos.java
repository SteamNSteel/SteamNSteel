package mod.steamnsteel.client.model.pct;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyBlockPos implements IUnlistedProperty<BlockPos>
{
    private final String name;
    private final Predicate<BlockPos> validator;

    public PropertyBlockPos(String name)
    {
        this(name, Predicates.<BlockPos>alwaysTrue());
    }

    public PropertyBlockPos(String name, Predicate<BlockPos> validator)
    {
        this.name = name;
        this.validator = validator;
    }

    public String getName()
    {
        return name;
    }

    public boolean isValid(BlockPos value)
    {
        return validator.apply(value);
    }

    public Class<BlockPos> getType()
    {
        return BlockPos.class;
    }

    public String valueToString(BlockPos value)
    {
        return value.toString();
    }
}