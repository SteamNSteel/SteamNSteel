package mod.steamnsteel.tileentity;

import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.block.SteamNSteelBlock;
import net.minecraft.util.EnumFacing;

public class PipeJunctionTE extends SteamNSteelTE implements IPipeTileEntity
{
    @Override
    public boolean isSideConnected(EnumFacing opposite)
    {
        return false;
    }

    @Override
    public boolean tryConnect(EnumFacing opposite)
    {
        return false;
    }

    @Override
    public boolean canConnect(EnumFacing opposite)
    {
        return false;
    }

    @Override
    public void recalculateVisuals()
    {

    }

    @Override
    public void disconnect(EnumFacing opposite)
    {

    }
}
