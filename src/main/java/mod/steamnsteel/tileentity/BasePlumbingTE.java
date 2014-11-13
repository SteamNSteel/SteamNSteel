package mod.steamnsteel.tileentity;

import mod.steamnsteel.api.plumbing.IDelegatedTileEntityBlock;
import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BasePlumbingTE extends SteamNSteelTE implements IPipeTileEntity
{
    protected void sendUpdate()
    {
        Logger.info("%s - Notifying Block Change - %s", worldObj.isRemote ? "client" : "server", toString());
        markDirty();
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    protected IPipeTileEntity getPipeTileEntityInDirection(ForgeDirection offset) {
        if (offset == null) {
            return null;
        }
        final WorldBlockCoord blockCoord = getWorldBlockCoord().offset(offset);

        TileEntity te = blockCoord.getTileEntity(worldObj);
        if (!(te instanceof IPipeTileEntity))
        {
            Block b = blockCoord.getBlock(worldObj);
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
    public abstract boolean isSideConnected(ForgeDirection direction);

    @Override
    public abstract boolean tryConnect(ForgeDirection direction);

    @Override
    public abstract boolean canConnect(ForgeDirection opposite);

    protected abstract void writePlumbingToNBT(NBTTagCompound nbt);
    protected abstract void readPlumbingFromNBT(NBTTagCompound nbt);

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
