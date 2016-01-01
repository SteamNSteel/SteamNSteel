package mod.steamnsteel.tileentity;

import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.PartSets;
import mod.steamnsteel.utility.blockParts.BlockPartConfiguration;
import mod.steamnsteel.utility.blockParts.ITileEntityWithParts;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

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
        worldObj.notifyNeighborsOfStateChange(getPos(), getBlockType());
        worldObj.markBlockForUpdate(getPos());
    }

    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(getPos(), 1, nbt);
    }


    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
    }
}
