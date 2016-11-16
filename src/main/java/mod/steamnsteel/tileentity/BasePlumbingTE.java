package mod.steamnsteel.tileentity;

import mod.steamnsteel.api.plumbing.IDelegatedTileEntityBlock;
import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;

public abstract class BasePlumbingTE extends SteamNSteelTE implements IPipeTileEntity
{
    protected void sendUpdate()
    {
        Logger.info("%s - Notifying Block Change - %s", world.isRemote ? "client" : "server", toString());
        markDirty();
        world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
        //FIXME: Do I still need to do this?
        //worldObj.markBlockForUpdate(getPos());
    }

    protected IPipeTileEntity getPipeTileEntityInDirection(EnumFacing offset) {
        if (offset == null) {
            return null;
        }
        final BlockPos blockCoord = getPos().offset(offset);

        TileEntity te = world.getTileEntity(blockCoord);
        if (!(te instanceof IPipeTileEntity))
        {
            Block b = world.getBlockState(blockCoord).getBlock();
            if (b instanceof IDelegatedTileEntityBlock) {
                te = ((IDelegatedTileEntityBlock) b).getDelegatedTileEntity(world, blockCoord);
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writePlumbingToNBT(nbt);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        readPlumbingFromNBT(nbt);
    }
}
