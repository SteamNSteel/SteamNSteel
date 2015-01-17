package mod.steamnsteel.tileentity;

import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.block.SteamNSteelBlock;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeJunctionTE extends SteamNSteelTE implements IPipeTileEntity
{
    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    @Override
    public boolean isSideConnected(ForgeDirection opposite)
    {
        return false;
    }

    @Override
    public boolean tryConnect(ForgeDirection opposite)
    {
        return false;
    }

    @Override
    public boolean canConnect(ForgeDirection opposite)
    {
        return false;
    }

    @Override
    public void recalculateVisuals()
    {

    }

    @Override
    public void disconnect(ForgeDirection opposite)
    {

    }
}
