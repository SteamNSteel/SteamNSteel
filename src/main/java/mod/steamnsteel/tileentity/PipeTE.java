package mod.steamnsteel.tileentity;

import com.google.common.base.Objects;
import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.blockParts.BlockPart;
import mod.steamnsteel.utility.blockParts.BlockPartConfiguration;
import mod.steamnsteel.utility.blockParts.ITileEntityWithParts;
import mod.steamnsteel.utility.PartSets;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.awt.font.FontRenderContext;
import java.util.LinkedList;
import java.util.List;

public class PipeTE extends SteamNSteelTE implements IPipeTileEntity, ITileEntityWithParts
{
    private static final String NBT_END_A = "endA";
    private static final String NBT_END_A_CONNECTED = "endAConnected";
    private static final String NBT_END_B = "endB";
    private static final String NBT_END_B_CONNECTED = "endBConnected";

    private boolean shouldRenderAsCorner;
    private BlockPartConfiguration blockPartConfiguration = new BlockPartConfiguration(PartSets.Pipe);
    private boolean useAlternateModel;

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

    @Override
    public BlockPartConfiguration getBlockPartConfiguration() {
        return blockPartConfiguration;
    }

    public void checkEnds()
    {
        IPipeTileEntity pipeEntity;
        boolean changed = false;

        List<ImmutablePair<ForgeDirection, ForgeDirection>> validDirections = recalculateValidDirections();
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

            if (endA != left || endB != right)
            {
                changed = true;
                endA = left;
                endB = right;
            }
        }

        if (this.endAIsConnected != endAIsConnected || this.endBIsConnected != endBIsConnected) {
            changed = true;
            this.endAIsConnected = endAIsConnected;
            this.endBIsConnected = endBIsConnected;
        }

        changed |= orderEnds();

        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i) {
            final ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[i];
            BlockPart part = blockPartConfiguration.getBlockPartByKey(direction);

            IPipeTileEntity tileEntity = getPipeTileEntityInDirection(direction);

            blockPartConfiguration.setEnabled(part, tileEntity != null);
        }

        if (changed) {
            //Logger.info("%s - Updating block-Changed - %s", worldObj.isRemote ? "client" : "server", toString());
            sendUpdate();
        }/* else {
            Logger.info("%s - Updating block         - %s", worldObj.isRemote ? "client" : "server", toString());
        }*/

        if (worldObj.isRemote) {
            recalculateVisuals();
        }
    }

    private boolean orderEnds()
    {
        if (endB == null || (endA != null && endB.ordinal() >= endA.ordinal())) {
            return false;
        }

        ForgeDirection endSwap = endA;
        endA = endB;
        endB = endSwap;

        boolean isConnectedSwap = endAIsConnected;
        endAIsConnected = endBIsConnected;
        endBIsConnected = isConnectedSwap;
        return true;
    }

    private static final List<ImmutablePair<ForgeDirection, ForgeDirection>> PIPE_ROTATIONS = new LinkedList<ImmutablePair<ForgeDirection, ForgeDirection>>();

    private synchronized static void calculatePipeRotations() {
        if (PIPE_ROTATIONS.isEmpty())
        {
            for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
            {
                for (int j = i + 1; j < ForgeDirection.VALID_DIRECTIONS.length; ++j)
                {
                    PIPE_ROTATIONS.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.VALID_DIRECTIONS[i], ForgeDirection.VALID_DIRECTIONS[j]));
                }
            }
        }
    }


    /**
     * Calculates a list of valid directions a pipe could potentially be facing.
     * @return A list of ends which are valid for the pipe.
     */
    private List<ImmutablePair<ForgeDirection, ForgeDirection>> recalculateValidDirections()
    {
        if (PIPE_ROTATIONS.isEmpty()) {
            calculatePipeRotations();
        }

        List<ImmutablePair<ForgeDirection, ForgeDirection>> validDirections = new LinkedList<ImmutablePair<ForgeDirection, ForgeDirection>>();
        for (ImmutablePair<ForgeDirection, ForgeDirection> pair : PIPE_ROTATIONS)
        {
            if (getPipeTileEntityInDirection(pair.left) != null && getPipeTileEntityInDirection(pair.right) != null) {
                validDirections.add(pair);
            }
        }

        return validDirections;
    }

    /**
     * Recalculates any properties used in the rendering process. This is used only by the client.
     */
    public void recalculateVisuals()
    {
        if (worldObj == null || !worldObj.isRemote) return;
        this.shouldRenderAsCorner = endB != endA.getOpposite();
        useAlternateModel = false;
        if (endA == ForgeDirection.NORTH && endB == ForgeDirection.WEST) {
            final IPipeTileEntity pipeTileEntity = getPipeTileEntityInDirection(ForgeDirection.NORTH);
            if (pipeTileEntity instanceof PipeTE) {
                final PipeTE pipeTE = (PipeTE)pipeTileEntity;
                if (pipeTE.endA == ForgeDirection.NORTH && pipeTE.endB == ForgeDirection.SOUTH) {
                    useAlternateModel = true;
                }
            }
        } else if (endA == ForgeDirection.SOUTH && endB == ForgeDirection.EAST) {

        }
    }

    @Override
    public void disconnect(ForgeDirection opposite)
    {
        boolean updated = false;

        if (endA == opposite) {
            endA = endB.getOpposite();
            endAIsConnected = false;
            updated = true;
        } else if (endB == opposite) {
            endB = endA.getOpposite();
            endBIsConnected = false;
            updated = true;
        }

        if (updated) {
            orderEnds();
            sendUpdate();
        }
    }

    private void sendUpdate()
    {
        markDirty();
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean shouldRenderAsCorner()
    {
        return shouldRenderAsCorner;
    }

    public void rotatePipe()
    {
        List<ImmutablePair<ForgeDirection, ForgeDirection>> validDirections = recalculateValidDirections();
        int length = validDirections.size();
        if (length <= 1) {return;}

        int i;
        for (i = 0; i < length; ++i) {
            ImmutablePair<ForgeDirection, ForgeDirection> pipeEnds = validDirections.get(i);
            if (pipeEnds.left == this.endA && pipeEnds.right == this.endB)
            {
                break;
            }
        }
        int stop = i % length;
        ImmutablePair<ForgeDirection, ForgeDirection> selectedEnds = null;
        while ((i = (++i) % length) != stop) {
            ImmutablePair<ForgeDirection, ForgeDirection> pipeEnds = validDirections.get(i);
            IPipeTileEntity pipe = getPipeTileEntityInDirection(pipeEnds.left);
            if (pipe == null || pipe.canConnect(pipeEnds.left.getOpposite())) {
                pipe = getPipeTileEntityInDirection(pipeEnds.right);
                if (pipe == null || pipe.canConnect(pipeEnds.right.getOpposite())) {
                    selectedEnds = pipeEnds;
                    break;
                }
            }
        }

        if (selectedEnds == null) {
            return;
        }
        ForgeDirection newEndA = selectedEnds.getLeft();
        ForgeDirection newEndB = selectedEnds.getRight();

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
        orderEnds();

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
        if (offset == null) {
            return null;
        }
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
            orderEnds();
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
        orderEnds();
        sendUpdate();
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
        nbt.setByte(NBT_END_A, (byte)endA.ordinal());
        nbt.setBoolean(NBT_END_A_CONNECTED, endAIsConnected);
        nbt.setByte(NBT_END_B, (byte)endB.ordinal());
        nbt.setBoolean(NBT_END_B_CONNECTED, endBIsConnected);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        endA = ForgeDirection.getOrientation(nbt.getByte(NBT_END_A));
        endAIsConnected = nbt.getBoolean(NBT_END_A_CONNECTED);
        endB = ForgeDirection.getOrientation(nbt.getByte(NBT_END_B));
        endBIsConnected = nbt.getBoolean(NBT_END_B_CONNECTED);

        if (worldObj != null && worldObj.isRemote)
        {
            recalculateVisuals();
        }
    }

    public boolean shouldUseAlternateModel()
    {
        return useAlternateModel;
    }
}
