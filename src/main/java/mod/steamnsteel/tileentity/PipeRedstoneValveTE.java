package mod.steamnsteel.tileentity;

import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.PartSets;
import mod.steamnsteel.utility.blockparts.BlockPartConfiguration;
import mod.steamnsteel.utility.blockparts.ITileEntityWithParts;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;

public class PipeRedstoneValveTE extends SteamNSteelTE implements IPipeTileEntity, ITileEntityWithParts
{
    private BlockPartConfiguration blockPartConfiguration = new BlockPartConfiguration(PartSets.Pipe);

    private EnumFacing endA = EnumFacing.EAST;
    private EnumFacing endB = EnumFacing.WEST;

    @Override
    public BlockPartConfiguration getBlockPartConfiguration() {
        return blockPartConfiguration;
    }

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
        boolean updated = false;

        if (endA == opposite) {
            endA = endB.getOpposite();
            updated = true;
        } else if (endB == opposite) {
            endB = endA.getOpposite();
            updated = true;
        }

        if (updated) {
            sendUpdate();
        }
    }

    private void sendUpdate()
    {
        markDirty();
        world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
        //FIXME: Do I still need to do this?
        //worldObj.markBlockForUpdate(getPos());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new SPacketUpdateTileEntity(getPos(), 1, nbt);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
    }
}
