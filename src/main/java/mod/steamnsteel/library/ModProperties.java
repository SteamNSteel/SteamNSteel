package mod.steamnsteel.library;

import mod.steamnsteel.client.model.pct.PropertyBlockAccess;
import mod.steamnsteel.client.model.pct.PropertyBlockPos;

/**
 * Created by codew on 9/11/2015.
 */
public final class ModProperties
{
    private ModProperties() {}

    public static final PropertyBlockPos PROPERTY_BLOCK_POS = new PropertyBlockPos("position");
    public static final PropertyBlockAccess PROPERTY_BLOCK_ACCESS = new PropertyBlockAccess("worldAccess");
}
