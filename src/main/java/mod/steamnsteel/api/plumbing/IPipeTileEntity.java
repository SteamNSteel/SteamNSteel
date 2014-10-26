package mod.steamnsteel.api.plumbing;

import net.minecraftforge.common.util.ForgeDirection;

public interface IPipeTileEntity
{
    boolean isDirectionConnected(ForgeDirection opposite);

    boolean tryConnect(ForgeDirection opposite);

    boolean canConnect(ForgeDirection opposite);
}
