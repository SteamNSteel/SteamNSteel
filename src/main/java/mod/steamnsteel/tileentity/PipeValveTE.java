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
import net.minecraftforge.common.util.ForgeDirection;

public class PipeValveTE extends SteamNSteelTE implements IPipeTileEntity, ITileEntityWithParts
{
    private BlockPartConfiguration blockPartConfiguration = new BlockPartConfiguration(PartSets.Pipe);


    private ForgeDirection valveDirection;
    private ForgeDirection orientation;

    private boolean isEndAConnected = false;
    private boolean isEndBConnected = false;

    @Override
    public BlockPartConfiguration getBlockPartConfiguration() {
        return blockPartConfiguration;
    }

    @Override
    public boolean isSideConnected(ForgeDirection opposite)
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
    public boolean tryConnect(ForgeDirection opposite)
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
    public boolean canConnect(ForgeDirection opposite)
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
    public void disconnect(ForgeDirection opposite)
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
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
    }


    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
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

    public void setOrientation(ForgeDirection orientation)
    {
        this.orientation = orientation;
    }
}
