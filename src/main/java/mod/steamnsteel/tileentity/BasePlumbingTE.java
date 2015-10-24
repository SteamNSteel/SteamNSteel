package mod.steamnsteel.tileentity;

import mod.steamnsteel.api.plumbing.IDelegatedTileEntityBlock;
import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public abstract class BasePlumbingTE extends SteamNSteelTE implements IPipeTileEntity
{
    protected void sendUpdate()
    {
        Logger.info("%s - Notifying Block Change - %s", worldObj.isRemote ? "client" : "server", toString());
        markDirty();
        worldObj.notifyNeighborsOfStateChange(getPos(), getBlockType());
        worldObj.markBlockForUpdate(getPos());
    }

    protected IPipeTileEntity getPipeTileEntityInDirection(EnumFacing offset) {
        if (offset == null) {
            return null;
        }
        final BlockPos blockCoord = getPos().offset(offset);

        TileEntity te = worldObj.getTileEntity(blockCoord);
        if (!(te instanceof IPipeTileEntity))
        {
            Block b = worldObj.getBlockState(blockCoord).getBlock();
            if (b instanceof IDelegatedTileEntityBlock) {
                te = ((IDelegatedTileEntityBlock) b).getDelegatedTileEntity(worldObj, blockCoord);
            }
        }

        if (te instanceof IPipeTileEntity)
        {
            return (IPipeTileEntity) te;
        }

        return null;
    }

    @Override
    public abstract boolean isSideConnected(EnumFacing direction);

    @Override
    public abstract boolean tryConnect(EnumFacing direction);

    @Override
    public abstract boolean canConnect(EnumFacing opposite);

    protected abstract void writePlumbingToNBT(NBTTagCompound nbt);
    protected abstract void readPlumbingFromNBT(NBTTagCompound nbt);

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
    public final void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writePlumbingToNBT(nbt);
    }

    @Override
    public final void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        readPlumbingFromNBT(nbt);
    }
}
