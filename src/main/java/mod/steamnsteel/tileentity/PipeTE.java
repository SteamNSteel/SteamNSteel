package mod.steamnsteel.tileentity;

import com.google.common.base.Objects;
import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
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
        TileEntity tileEntity;
        IPipeTileEntity pipeEntity;
        boolean changed = false;

        calculateValidDirections();

        if (validDirections.size() > 0)
        {
            for (ImmutablePair<ForgeDirection, ForgeDirection> pair : validDirections)
            {
                final ForgeDirection left = pair.getLeft();
                final ForgeDirection right = pair.getRight();
                IPipeTileEntity teA = (IPipeTileEntity) getWorldBlockCoord().offset(left).getTileEntity(worldObj);
                IPipeTileEntity teB = (IPipeTileEntity) getWorldBlockCoord().offset(right).getTileEntity(worldObj);

                if (teA.canConnect(left.getOpposite()) && teB.canConnect(right.getOpposite()))
                {
                    teA.tryConnect(left.getOpposite());
                    teB.tryConnect(right.getOpposite());

                    endA = left;
                    endB = right;
                    break;
                }
            }
        } else {
            for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i) {
                final ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[i];
                TileEntity block = getWorldBlockCoord().offset(direction).getTileEntity(worldObj);
                if (block instanceof IPipeTileEntity)
                {
                    IPipeTileEntity pipeTE = (IPipeTileEntity)block;
                    if (pipeTE.canConnect(direction.getOpposite())) {
                        pipeTE.tryConnect(direction.getOpposite());

                        endA = direction;
                        endB = direction.getOpposite();
                        break;
                    }
                }
            }
        }

        boolean endAIsConnected = false;
        boolean endBIsConnected = false;

        tileEntity = getWorldBlockCoord().offset(endA).getTileEntity(worldObj);
        if (tileEntity instanceof IPipeTileEntity) {
            pipeEntity = (IPipeTileEntity) tileEntity;
            endAIsConnected = pipeEntity.tryConnect(endA.getOpposite());
        }

        tileEntity = getWorldBlockCoord().offset(endB).getTileEntity(worldObj);
        if (tileEntity instanceof IPipeTileEntity) {
            pipeEntity = (IPipeTileEntity) tileEntity;
            endBIsConnected = pipeEntity.tryConnect(endB.getOpposite());
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
            TileEntity block = getWorldBlockCoord().offset(ForgeDirection.VALID_DIRECTIONS[i]).getTileEntity(worldObj);
            if (block instanceof IPipeTileEntity)
            {
                for (int j = i + 1; j < ForgeDirection.VALID_DIRECTIONS.length; ++j)
                {
                    block = getWorldBlockCoord().offset(ForgeDirection.VALID_DIRECTIONS[j]).getTileEntity(worldObj);
                    if (block instanceof IPipeTileEntity) {
                        validDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.VALID_DIRECTIONS[i], ForgeDirection.VALID_DIRECTIONS[j]));
                    }
                }
            }
        }

    }

    private void recalculateVisuals()
    {
        if (worldObj != null && !worldObj.isRemote) return;

        boolean shouldRenderAsCorner = endB != endA.getOpposite();

        PipeTE endAPipe = null;
        PipeTE endBPipe = null;

        TileEntity te;
        te = getWorldBlockCoord().offset(endA).getTileEntity(worldObj);
        if (te != null && te instanceof PipeTE) {
            endAPipe = (PipeTE) te;
        }

        te = getWorldBlockCoord().offset(endB).getTileEntity(worldObj);
        if (te != null && te instanceof PipeTE) {
            endBPipe = (PipeTE) te;
        }

        if (this.shouldRenderAsCorner != shouldRenderAsCorner) {
            this.shouldRenderAsCorner = shouldRenderAsCorner;
            if (endAPipe != null) {
                endAPipe.recalculateVisuals();
            }
            if (endBPipe != null) {
                endBPipe.recalculateVisuals();
            }
        }


        Logger.info("%s - Recalculating Visuals  - %s", worldObj.isRemote ? "client" : "server", toString());
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
//        Logger.info("%s - Wrote to NBT - %s - endA:%s%s, endB:%s%s", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), endA, endAIsConnected ? "+" : "-", endB, endBIsConnected ? "+" : "-");
        Logger.info("%s - Wrote to NBT           - %s", worldObj.isRemote ? "client" : "server", this.toString());
    }

    public boolean shouldRenderAsCorner()
    {
        return shouldRenderAsCorner;
    }

    public void rotatePipe()
    {
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

        PipeTE prevEndATE;
        PipeTE newEndATE;

        prevEndATE = getPipeTEAtOffset(endA);
        newEndATE = getPipeTEAtOffset(newEndA);

        PipeTE prevEndBTE = getPipeTEAtOffset(endB);
        PipeTE newEndBTE = getPipeTEAtOffset(newEndB);

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

    private PipeTE getPipeTEAtOffset(ForgeDirection offset ) {
        TileEntity te = getWorldBlockCoord().offset(offset).getTileEntity(worldObj);
        if (te instanceof PipeTE) {
            return (PipeTE)te;
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
        TileEntity tileEntity;
        this.endA = orientation;
        tileEntity = getWorldBlockCoord().offset(endA).getTileEntity(worldObj);
        endAIsConnected = tileEntity instanceof IPipeTileEntity && ((IPipeTileEntity) tileEntity).isDirectionConnected(endA.getOpposite());
        this.endB = orientation.getOpposite();
        tileEntity = getWorldBlockCoord().offset(endB).getTileEntity(worldObj);
        endBIsConnected = tileEntity instanceof IPipeTileEntity && ((IPipeTileEntity) tileEntity).isDirectionConnected(endB.getOpposite());
    }
}
