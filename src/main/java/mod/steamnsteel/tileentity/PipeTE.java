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
        validDirections.clear();

        ForgeDirection firstValidEndA = null;
        IPipeTileEntity firstValidEndATileEntity = null;
        ForgeDirection firstValidEndB = null;
        IPipeTileEntity firstValidEndBTileEntity = null;

        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i) {
            final ForgeDirection endADirection = ForgeDirection.VALID_DIRECTIONS[i];
            WorldBlockCoord neighbour = getWorldBlockCoord().offset(endADirection);
            final TileEntity block = neighbour.getTileEntity(worldObj);
            boolean isMatch = block instanceof IPipeTileEntity;
            if (isMatch) {
                if (firstValidEndA == null) {
                    firstValidEndA = endADirection;
                    firstValidEndATileEntity = (IPipeTileEntity)block;
                }
                //Looping from i+1 to VALID_DIRECTIONS.length removes duplicate entries, since
                //{endA = NORTH, endB = EAST} and {endA = EAST, endB = NORTH} are functionally and visually equivalent.
                for (int j = i + 1; j < ForgeDirection.VALID_DIRECTIONS.length; ++j) {
                    final ForgeDirection endBDirection = ForgeDirection.VALID_DIRECTIONS[j];
                    WorldBlockCoord neighbourB = getWorldBlockCoord().offset(endBDirection);
                    final TileEntity blockB = neighbourB.getTileEntity(worldObj);
                    isMatch = blockB instanceof IPipeTileEntity;
                    if (isMatch) {
                        if (firstValidEndB == null) {
                            firstValidEndB = endBDirection;
                            firstValidEndBTileEntity = (IPipeTileEntity)blockB;
                        }
                        validDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(endADirection, endBDirection));
                    }
                }
            }
        }

        if (firstValidEndB == endA || firstValidEndA == endB) {
            ForgeDirection temp = firstValidEndA;
            firstValidEndA = firstValidEndB;
            firstValidEndB = temp;

            IPipeTileEntity temp2 = firstValidEndATileEntity;
            firstValidEndATileEntity = firstValidEndBTileEntity;
            firstValidEndBTileEntity = temp2;
        }

        boolean changed = false;
        if (!endAIsConnected && firstValidEndATileEntity != null && firstValidEndATileEntity.isDirectionConnected(firstValidEndA.getOpposite())) {
            endA = firstValidEndA;
            firstValidEndATileEntity.tryConnect(firstValidEndA.getOpposite());
            changed = true;
        }

        if (!endBIsConnected && firstValidEndBTileEntity != null && firstValidEndBTileEntity.isDirectionConnected(firstValidEndB.getOpposite())) {
            endB = firstValidEndB;
            firstValidEndBTileEntity.tryConnect(firstValidEndB.getOpposite());
            changed = true;
        }

        if (endA == endB) {
            endB = endA.getOpposite();
            changed = true;
        }

        TileEntity tileEntity;
        tileEntity = getWorldBlockCoord().offset(endA).getTileEntity(worldObj);
        boolean endAIsConnected = tileEntity instanceof IPipeTileEntity && ((IPipeTileEntity) tileEntity).isDirectionConnected(endA.getOpposite());
        tileEntity = getWorldBlockCoord().offset(endB).getTileEntity(worldObj);
        boolean endBIsConnected = tileEntity instanceof IPipeTileEntity && ((IPipeTileEntity) tileEntity).isDirectionConnected(endB.getOpposite());

        if (this.endAIsConnected != endAIsConnected || this.endBIsConnected != endBIsConnected) {
            changed = true;
            this.endAIsConnected = endAIsConnected;
            this.endBIsConnected = endBIsConnected;
        }

        if (changed) {
            Logger.info("%s - Updating block - %s - endA:%s,%s endB:%s,%s", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), firstValidEndA, endAIsConnected, firstValidEndB, endBIsConnected);
            sendUpdate();
        } else {
            Logger.info("%s - Updating block - %s - No Change", worldObj.isRemote ? "client" : "server", getWorldBlockCoord());
        }

        if (worldObj.isRemote) {
            recalculateVisuals();
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


        Logger.info("%s - Recalculating Visuals - %s - endA:%s, endB:%s, shouldRenderAsCorner:%b", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), endA, endB, shouldRenderAsCorner);
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
        Logger.info("%s - Read from NBT - %s - endA:%s, endB:%s", worldObj == null ? "worldObj not available" : worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), endA, endB);
        if (worldObj != null && worldObj.isRemote)
        {
            recalculateVisuals();
        }
    }

    private void sendUpdate()
    {
        markDirty();
        Logger.info("%s - Notifying Block Change - %s - endA:%s endB:%s", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), endA, endB);
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
        Logger.info("%s - Wrote to NBT - %s - endA:%s, endB:%s", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), endA, endB);
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
        return Objects.toStringHelper(this)
                .add("endA", endA)
                .add("endAConnected", endAIsConnected)
                .add("endB", endB)
                .add("endBConnected", endBIsConnected)
                .add("shouldRenderAsCorner", shouldRenderAsCorner)
                .add("worldBlockCoord", this.getWorldBlockCoord())
                .toString();
    }

    @Override
    public boolean isDirectionConnected(ForgeDirection direction)
    {
        return endA == direction || endB == direction;
    }

    @Override
    public boolean tryConnect(ForgeDirection direction)
    {
        if (endA == direction) {
            endAIsConnected = true;
            return true;
        } else if (!endAIsConnected && endB != direction) {
            endA = direction;
            endAIsConnected = true;
            return true;
        } else if (endB == direction) {
            endBIsConnected = true;
            return true;
        } else if (!endBIsConnected) {
            endB = direction;
            endBIsConnected = true;
            return true;
        }
        return false;
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
