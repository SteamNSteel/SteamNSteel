package mod.steamnsteel.tileentity;

import com.google.common.base.Objects;
import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.block.machine.PipeJunctionBlock;
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
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.ArrayList;
import java.util.List;

public class PipeTE extends SteamNSteelTE
{
    private static final String NBT_END_A = "endA";
    private static final String NBT_END_B = "endB";

    private int renderType;

    public ForgeDirection getEndA()
    {
        return endA;
    }

    public ForgeDirection getEndB()
    {
        return endB;
    }

    private ForgeDirection endA;
    private ForgeDirection endB;

    List<ImmutablePair<ForgeDirection, ForgeDirection>> validDirections = new ArrayList<ImmutablePair<ForgeDirection, ForgeDirection>>();

    public void checkEnds()
    {
        validDirections.clear();

        boolean currentEndsAreValid = false;
        ForgeDirection firstValidEndA = null;
        ForgeDirection firstValidEndB = null;

        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i) {
            final ForgeDirection endADirection = ForgeDirection.VALID_DIRECTIONS[i];
            WorldBlockCoord neighbour = getWorldBlockCoord().offset(endADirection);
            final Block block = neighbour.getBlock(worldObj);
            boolean isMatch = block instanceof PipeBlock || block instanceof PipeJunctionBlock;
            if (isMatch) {
                if (firstValidEndA == null) {
                    firstValidEndA = endADirection;
                }
                //Looping from i+1 to VALID_DIRECTIONS.length removes duplicate entries, since
                //{endA = NORTH, endB = EAST} and {endA = EAST, endB = NORTH} are functionally and visually equivalent.
                for (int j = i + 1; j < ForgeDirection.VALID_DIRECTIONS.length; ++j) {
                    final ForgeDirection endBDirection = ForgeDirection.VALID_DIRECTIONS[j];
                    WorldBlockCoord neighbourB = getWorldBlockCoord().offset(endBDirection);
                    final Block blockB = neighbourB.getBlock(worldObj);
                    isMatch = blockB instanceof PipeBlock || blockB instanceof PipeJunctionBlock;
                    if (isMatch) {
                        if (firstValidEndB == null) {
                            firstValidEndB = endBDirection;
                        }
                        validDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(endADirection, endBDirection));
                        if (endADirection == endA && endBDirection == endB) {
                            currentEndsAreValid = true;
                        }
                    }
                }
            }
        }

        if (!currentEndsAreValid && !(endA == firstValidEndA && endB == firstValidEndB)) {
            //Logger.info("Changing ends: endA:%s endB:%s, %s", firstValidEndA, firstValidEndB, worldObj.isRemote ? "client" : "server");
            Logger.info("%s - Updating block - %s - endA:%s endB:%s", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), firstValidEndA, firstValidEndB);
            endA = firstValidEndA;
            endB = firstValidEndB;
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

        int ends = 0;

        PipeTE endAPipe = null;

        if (endA != null) {
            TileEntity te = getWorldBlockCoord().offset(endA).getTileEntity(worldObj);
            if (te != null && te instanceof PipeTE) {
                endAPipe = (PipeTE) te;
                final ForgeDirection endAOpposite = endA.getOpposite();
                if (endAPipe.endA == endAOpposite || endAPipe.endB == endAOpposite) {
                    ends++;
                }
            }
        }

        PipeTE endBPipe = null;
        if (endB != null)
        {
            TileEntity te = getWorldBlockCoord().offset(endB).getTileEntity(worldObj);
            if (te != null && te instanceof PipeTE) {
                endBPipe = (PipeTE) te;
                final ForgeDirection endBOpposite = endB.getOpposite();
                if (endBPipe.endA == endBOpposite || endBPipe.endB == endBOpposite) {
                    ends++;
                }
            }
        }

        if (ends == 2 && endB != endA.getOpposite()) {
            ends = 3;
        }

        if (renderType != ends) {
            renderType = ends;
            if (endAPipe != null) {
                endAPipe.recalculateVisuals();
            }
            if (endBPipe != null) {
                endBPipe.recalculateVisuals();
            }
        }


        Logger.info("%s - Recalculating Visuals - %s - endA:%s, endB:%s, renderType:%d", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), endA, endB, renderType);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        endA = nbt.hasKey(NBT_END_A) ? ForgeDirection.getOrientation(nbt.getByte(NBT_END_A)) : null;
        endB = nbt.hasKey(NBT_END_B) ? ForgeDirection.getOrientation(nbt.getByte(NBT_END_B)) : null;
        if (endA == ForgeDirection.UNKNOWN) {
            endA = null;
        }
        if (endB == ForgeDirection.UNKNOWN) {
            endB = null;
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
        nbt.setByte(NBT_END_B, (byte)(endB != null ? endB : ForgeDirection.UNKNOWN).ordinal());
        Logger.info("%s - Wrote to NBT - %s - endA:%s, endB:%s", worldObj.isRemote ? "client" : "server", getWorldBlockCoord(), endA, endB);
    }

    public int getRenderType()
    {
        return renderType;
    }

    public void rotatePipe()
    {
        int length = validDirections.size();
        if (length <= 1) {return;}

        int i = 0;
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

        PipeTE prevEndATE = null;
        PipeTE newEndATE = null;

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
                .add("endB", endB)
                .add("renderType", renderType)
                .add("worldBlockCoord", this.getWorldBlockCoord())
                .toString();
    }
}
