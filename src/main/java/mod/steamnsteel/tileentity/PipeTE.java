package mod.steamnsteel.tileentity;

import com.google.common.base.Objects;
import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.ArrayList;
import java.util.List;

public class PipeTE extends SteamNSteelTE implements IPipeTileEntity
{
    private static final String NBT_END_A = "endA";
    private static final String NBT_END_A_CONNECTED = "endAConnected";
    private static final String NBT_END_B = "endB";
    private static final String NBT_END_B_CONNECTED = "endBConnected";

    private boolean shouldRenderAsCorner;

    public ForgeDirection getEndADirection()
    {
        return endA;
    }
    public ForgeDirection getEndBConnected()
    {
        return endB;
    }

    public boolean isEndAConnected() {
        return endAIsConnected;
    }

    public boolean isEndBConnected() {
        return endBIsConnected;
    }

    private ForgeDirection endA = ForgeDirection.EAST;
    private ForgeDirection endB = ForgeDirection.WEST;

    private boolean endAIsConnected;
    private boolean endBIsConnected;

    List<ImmutablePair<ForgeDirection, ForgeDirection>> validDirections = new ArrayList<ImmutablePair<ForgeDirection, ForgeDirection>>();

    public void checkEnds()
    {
        IPipeTileEntity pipeEntity;
        boolean changed = false;

        calculateValidDirections();
        ImmutablePair<ForgeDirection, ForgeDirection> connectionToMake = null;

        for (ImmutablePair<ForgeDirection, ForgeDirection> pair : validDirections)
        {
            final ForgeDirection left = pair.getLeft();
            final ForgeDirection right = pair.getRight();

            if (getPipeTileEntityInDirection(left).canConnect(left.getOpposite()) && getPipeTileEntityInDirection(right).canConnect(right.getOpposite()))
            {
                if (connectionToMake == null || (left == endA && right == endB)) {
                    connectionToMake = pair;
                }
            }
        }

        if (connectionToMake == null)
        {
            for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
            {
                final ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[i];
                pipeEntity = getPipeTileEntityInDirection(direction);
                if (pipeEntity != null && pipeEntity.canConnect(direction.getOpposite()))
                {
                    connectionToMake = new ImmutablePair<ForgeDirection, ForgeDirection>(direction, direction.getOpposite());
                    break;
                }
            }
        }

        boolean endAIsConnected = false;
        boolean endBIsConnected = false;

        if (connectionToMake != null) {
            final ForgeDirection left = connectionToMake.getLeft();
            final ForgeDirection right = connectionToMake.getRight();

            endAIsConnected = getPipeTileEntityInDirection(left).tryConnect(left.getOpposite());

            pipeEntity = getPipeTileEntityInDirection(right);
            if (pipeEntity != null) {
                endBIsConnected = pipeEntity.tryConnect(right.getOpposite());
            }

            endA = left;
            endB = right;
        }

        if (this.endAIsConnected != endAIsConnected || this.endBIsConnected != endBIsConnected) {
            changed = true;
            this.endAIsConnected = endAIsConnected;
            this.endBIsConnected = endBIsConnected;
        }

        if (changed) {
            Logger.info("%s - Updating block-Changed - %s", worldObj.isRemote ? "client" : "server", toString());
            sendUpdate();
        } else {
            Logger.info("%s - Updating block         - %s", worldObj.isRemote ? "client" : "server", toString());
        }

        if (worldObj.isRemote) {
            recalculateVisuals();
        }
    }

    private void calculateValidDirections()
    {
        validDirections.clear();

        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i) {
            IPipeTileEntity tileEntity = getPipeTileEntityInDirection(ForgeDirection.VALID_DIRECTIONS[i]);
            if (tileEntity != null)
            {
                for (int j = i + 1; j < ForgeDirection.VALID_DIRECTIONS.length; ++j)
                {
                    tileEntity = getPipeTileEntityInDirection(ForgeDirection.VALID_DIRECTIONS[j]);
                    if (tileEntity != null) {
                        validDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.VALID_DIRECTIONS[i], ForgeDirection.VALID_DIRECTIONS[j]));
                    }
                }
            }
        }
    }

    public void recalculateVisuals()
    {
        if (worldObj == null || !worldObj.isRemote) return;
        this.shouldRenderAsCorner = endB != endA.getOpposite();
        Logger.info("%s - Recalculating Visuals  - %s", "client", toString());
    }

    @Override
    public void disconnect(ForgeDirection opposite)
    {
        if (endA == opposite) {
            endA = null;
            endAIsConnected = false;
            sendUpdate();
        } else if (endB == opposite) {
            endB = null;
            endBIsConnected = false;
            sendUpdate();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        endA = nbt.hasKey(NBT_END_A) ? ForgeDirection.getOrientation(nbt.getByte(NBT_END_A)) : ForgeDirection.EAST;
        endAIsConnected = nbt.hasKey(NBT_END_A_CONNECTED) && nbt.getBoolean(NBT_END_A_CONNECTED);
        endB = nbt.hasKey(NBT_END_B) ? ForgeDirection.getOrientation(nbt.getByte(NBT_END_B)) : ForgeDirection.WEST;
        endBIsConnected = nbt.hasKey(NBT_END_B_CONNECTED) && nbt.getBoolean(NBT_END_B_CONNECTED);

        if (endA == ForgeDirection.UNKNOWN) {
            endA = ForgeDirection.EAST;
        }
        if (endB == ForgeDirection.UNKNOWN) {
            endB = ForgeDirection.WEST;
        }
        Logger.info("%s - Read from NBT          - %s", worldObj == null ? "worldObj not available" : worldObj.isRemote ? "client" : "server", toString());
        if (worldObj != null && worldObj.isRemote)
        {
            recalculateVisuals();
        }
    }

    private void sendUpdate()
    {
        markDirty();
        Logger.info("%s - Notifying Block Change - %s", worldObj.isRemote ? "client" : "server", toString());
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
        //Persist null as unknown.
        nbt.setByte(NBT_END_A, (byte)(endA != null ? endA : ForgeDirection.UNKNOWN).ordinal());
        nbt.setBoolean(NBT_END_A_CONNECTED, endAIsConnected);
        nbt.setByte(NBT_END_B, (byte) (endB != null ? endB : ForgeDirection.UNKNOWN).ordinal());
        nbt.setBoolean(NBT_END_B_CONNECTED, endBIsConnected);

        Logger.info("%s - Wrote to NBT           - %s", worldObj.isRemote ? "client" : "server", this.toString());
    }

    public boolean shouldRenderAsCorner()
    {
        return shouldRenderAsCorner;
    }

    public void rotatePipe()
    {
        calculateValidDirections();
        int length = validDirections.size();
        if (length <= 1) {return;}

        int i;
        for (i = 0; i < length; ++i) {
            ImmutablePair<ForgeDirection, ForgeDirection> pipeEnds = validDirections.get(i);
            if (pipeEnds.getLeft() == this.endA && pipeEnds.getRight() == this.endB)
            {
                break;
            }
        }
        i = (++i) % length;

        ImmutablePair<ForgeDirection, ForgeDirection> selectedEnds = validDirections.get(i);
        ForgeDirection newEndA = selectedEnds.getLeft();
        ForgeDirection newEndB = selectedEnds.getRight();

        Logger.info("%s - Rotating Block - %s - old(A:%s, B:%s) - new(A:%s, B:%s)", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), endA, endB, newEndA, newEndB);

        IPipeTileEntity prevEndATE;
        IPipeTileEntity newEndATE;

        prevEndATE = getPipeTileEntityInDirection(endA);
        newEndATE = getPipeTileEntityInDirection(newEndA);

        IPipeTileEntity prevEndBTE = getPipeTileEntityInDirection(endB);
        IPipeTileEntity newEndBTE = getPipeTileEntityInDirection(newEndB);

        boolean endAChanged = endA != newEndA;
        boolean endBChanged = endB != newEndB;

        endA = newEndA;
        endB = newEndB;

        if (endAChanged && prevEndATE != null) {
            prevEndATE.recalculateVisuals();
        }
        if (endBChanged && prevEndBTE != null) {
            prevEndBTE.recalculateVisuals();
        }
        if (endAChanged && newEndATE != null) {
            newEndATE.recalculateVisuals();
        }
        if (endBChanged && newEndBTE != null) {
            newEndBTE.recalculateVisuals();
        }

        sendUpdate();
    }

    private IPipeTileEntity getPipeTileEntityInDirection(ForgeDirection offset) {
        TileEntity te = getWorldBlockCoord().offset(offset).getTileEntity(worldObj);
        if (te instanceof IPipeTileEntity) {
            return (IPipeTileEntity)te;
        }
        return null;
    }


    @Override
    public String toString()
    {
        Objects.ToStringHelper stringHelper = Objects.toStringHelper(this)
                .add("worldBlockCoord", this.getWorldBlockCoord())
                .add("endA", endA)
                .add("endAConnected", endAIsConnected)
                .add("endB", endB)
                .add("endBConnected", endBIsConnected);

        if (worldObj != null && worldObj.isRemote) {
            stringHelper = stringHelper
                    .add("shouldRenderAsCorner", shouldRenderAsCorner);
        }

        return stringHelper.toString();
    }

    @Override
    public boolean isDirectionConnected(ForgeDirection direction)
    {
        return endA == direction || endB == direction;
    }

    @Override
    public boolean tryConnect(ForgeDirection direction)
    {
        ForgeDirection previousEndA = endA;
        ForgeDirection previousEndB = endB;
        boolean previousEndAConnected = endAIsConnected;
        boolean previousEndBConnected = endBIsConnected;

        boolean connected = false;
        if (endA == direction) {
            endAIsConnected = true;
            connected = true;
        } else if (!endAIsConnected && endB != direction) {
            endA = direction;
            endAIsConnected = true;
            connected = true;
        } else if (endB == direction) {
            endBIsConnected = true;
            connected = true;
        } else if (!endBIsConnected) {
            endB = direction;
            endBIsConnected = true;
            connected = true;
        }

        if (endA != previousEndA || endB != previousEndB || endAIsConnected != previousEndAConnected || endBIsConnected != previousEndBConnected) {
            sendUpdate();
        }

        return connected;
    }

    @Override
    public boolean canConnect(ForgeDirection opposite)
    {
        return !endAIsConnected || !endBIsConnected || endA == opposite || endB == opposite;
    }

    public void setOrientation(ForgeDirection orientation)
    {
        IPipeTileEntity tileEntity;

        this.endA = orientation;
        tileEntity = getPipeTileEntityInDirection(endA);
        endAIsConnected = tileEntity != null && tileEntity.isDirectionConnected(endA.getOpposite());

        this.endB = orientation.getOpposite();
        tileEntity = getPipeTileEntityInDirection(endB);
        endBIsConnected = tileEntity != null && tileEntity.isDirectionConnected(endB.getOpposite());
    }

    public void detach()
    {
        IPipeTileEntity tileEntity;

        tileEntity = getPipeTileEntityInDirection(endA);
        if (endAIsConnected && tileEntity != null) {
            tileEntity.disconnect(endA.getOpposite());
        }

        tileEntity = getPipeTileEntityInDirection(endB);
        if (endBIsConnected && tileEntity != null) {
            tileEntity.disconnect(endB.getOpposite());
        }
    }
}
