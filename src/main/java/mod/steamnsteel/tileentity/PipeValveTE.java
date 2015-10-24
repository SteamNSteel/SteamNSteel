package mod.steamnsteel.tileentity;

import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.PartSets;
import mod.steamnsteel.utility.blockParts.BlockPartConfiguration;
import mod.steamnsteel.utility.blockParts.ITileEntityWithParts;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

public class PipeValveTE extends SteamNSteelTE implements IPipeTileEntity, ITileEntityWithParts
{
    private BlockPartConfiguration blockPartConfiguration = new BlockPartConfiguration(PartSets.Pipe);

    private EnumFacing valveDirection;
    private EnumFacing orientation;

    private boolean isEndAConnected = false;
    private boolean isEndBConnected = false;

    @Override
    public BlockPartConfiguration getBlockPartConfiguration() {
        return blockPartConfiguration;
    }

    @Override
    public boolean isSideConnected(EnumFacing opposite)
    {
        if (opposite == orientation && isEndAConnected) {
            return true;
        }
        if (opposite == orientation.getOpposite() && isEndBConnected) {
            return true;
        }

        return false;
    }

    @Override
    public boolean tryConnect(EnumFacing opposite)
    {
        if (opposite == orientation) {
            isEndAConnected = true;
            return true;
        }
        if (opposite == orientation.getOpposite()) {
            isEndBConnected = true;
            return true;
        }
        if (!isEndAConnected && !isEndBConnected) {
            orientation = opposite;
            isEndAConnected = true;
        }

        return false;
    }

    @Override
    public boolean canConnect(EnumFacing opposite)
    {
        if (opposite == orientation || opposite == orientation.getOpposite()) {
            return true;
        }
        if (!isEndAConnected && !isEndBConnected) {
            return true;
        }
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

        if (opposite == orientation && isEndAConnected) {
            isEndAConnected = false;
            updated = true;
        } else if (opposite == orientation.getOpposite() && isEndBConnected) {
            isEndBConnected = false;
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

    public void setOrientation(EnumFacing orientation)
    {
        this.orientation = orientation;
    }
}
