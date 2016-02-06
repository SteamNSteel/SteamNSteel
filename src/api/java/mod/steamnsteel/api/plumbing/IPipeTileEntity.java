package mod.steamnsteel.api.plumbing;

import net.minecraft.util.EnumFacing;

public interface IPipeTileEntity
{
    boolean isSideConnected(EnumFacing opposite);

    boolean tryConnect(EnumFacing opposite);

    boolean canConnect(EnumFacing opposite);

    void recalculateVisuals();

    void disconnect(EnumFacing opposite);
}
