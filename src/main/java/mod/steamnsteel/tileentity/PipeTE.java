package mod.steamnsteel.tileentity;

import com.google.common.base.Objects;
import mod.steamnsteel.api.plumbing.IPipeTileEntity;
import mod.steamnsteel.utility.blockParts.BlockPart;
import mod.steamnsteel.utility.blockParts.BlockPartConfiguration;
import mod.steamnsteel.utility.blockParts.ITileEntityWithParts;
import mod.steamnsteel.utility.PartSets;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.LinkedList;
import java.util.List;


public class PipeTE extends BasePlumbingTE implements ITileEntityWithParts
{
    private static final String NBT_END_A = "endA";
    private static final String NBT_END_A_CONNECTED = "endAConnected";
    private static final String NBT_END_B = "endB";
    private static final String NBT_END_B_CONNECTED = "endBConnected";

    private boolean shouldRenderAsCorner;
    private BlockPartConfiguration blockPartConfiguration = new BlockPartConfiguration(PartSets.Pipe);
    private boolean useAlternateModel;

    public EnumFacing getEndADirection()
    {
        return endA;
    }
    public EnumFacing getEndBConnected()
    {
        return endB;
    }

    public boolean isEndAConnected() {
        return endAIsConnected;
    }

    public boolean isEndBConnected() {
        return endBIsConnected;
    }

    private EnumFacing endA = EnumFacing.EAST;
    private EnumFacing endB = EnumFacing.WEST;

    private boolean endAIsConnected;
    private boolean endBIsConnected;

    @Override
    public BlockPartConfiguration getBlockPartConfiguration() {
        return blockPartConfiguration;
    }

    /**
     * Verifies that the current ends are valid using the following ruels:
     * 1) If the current end configuration is valid, use that
     * 2) If it is not valid, use the first valid configuration with two ends connected
     * 3) If that is not possible, use the first valid end that is valid, and make a straight pipe
     * 4) Otherwise, leave the pipe as is.
     */
    public void checkEnds()
    {
        IPipeTileEntity pipeEntity;
        boolean changed = false;

        List<ImmutablePair<EnumFacing, EnumFacing>> validDirections = recalculateValidDirections();
        ImmutablePair<EnumFacing, EnumFacing> connectionToMake = null;

        //Check if current ends are valid, or use the first pair that is.
        for (ImmutablePair<EnumFacing, EnumFacing> pair : validDirections)
        {
            final EnumFacing left = pair.getLeft();
            final EnumFacing right = pair.getRight();

            if (getPipeTileEntityInDirection(left).canConnect(left.getOpposite()) && getPipeTileEntityInDirection(right).canConnect(right.getOpposite()))
            {
                if (connectionToMake == null || (left == endA && right == endB)) {
                    connectionToMake = pair;
                }
            }
        }

        //If none were found, find the first single end that is connected and connect to that and it's opposite.
        if (connectionToMake == null)
        {
            for (int i = 0; i < EnumFacing.VALUES.length; ++i)
            {
                final EnumFacing direction = EnumFacing.VALUES[i];
                pipeEntity = getPipeTileEntityInDirection(direction);
                if (pipeEntity != null && pipeEntity.canConnect(direction.getOpposite()))
                {
                    connectionToMake = new ImmutablePair<EnumFacing, EnumFacing>(direction, direction.getOpposite());
                    break;
                }
            }
        }

        boolean endAIsConnected = false;
        boolean endBIsConnected = false;

        //If we're going to make a connection
        if (connectionToMake != null) {
            final EnumFacing left = connectionToMake.getLeft();
            final EnumFacing right = connectionToMake.getRight();

            //We're connected to endA if we can make a connection
            endAIsConnected = getPipeTileEntityInDirection(left).tryConnect(left.getOpposite());

            pipeEntity = getPipeTileEntityInDirection(right);
            //If we couldn't connect to endA, there's no point in checking endB
            if (pipeEntity != null) {
                //We're connected to endB is we can make a connection
                endBIsConnected = pipeEntity.tryConnect(right.getOpposite());
            }

            //if the ends have changed, we'll flag it for later and set the ends
            if (endA != left || endB != right)
            {
                changed = true;
                endA = left;
                endB = right;
            }
        }

        //It's also possible that a connection to a neighbour has just occurred. this also counts as a change.
        if (this.endAIsConnected != endAIsConnected || this.endBIsConnected != endBIsConnected) {
            changed = true;
            this.endAIsConnected = endAIsConnected;
            this.endBIsConnected = endBIsConnected;
        }

        //It's also possible that endA and endB need to be swapped. This is also a change.
        changed |= orderEnds();

        //So if we've made a change after all that, we need to send it to the other side.
        if (changed) {
            //Logger.info("%s - Updating block-Changed - %s", worldObj.isRemote ? "client" : "server", toString());
            sendUpdate();
        } else {
            //Logger.info("%s - Updating block         - %s", worldObj.isRemote ? "client" : "server", toString());
        }

        //We'll now update the BlockPart configuration and enable the sides that have neighbouring blocks.
        for (int i = 0; i < EnumFacing.VALUES.length; ++i) {
            final EnumFacing direction = EnumFacing.VALUES[i];
            BlockPart part = blockPartConfiguration.getBlockPartByKey(direction);

            IPipeTileEntity tileEntity = getPipeTileEntityInDirection(direction);

            blockPartConfiguration.setEnabled(part, tileEntity != null);
        }

        //Finally, if we're on the client, we should recalculate any visuals.
        if (worldObj.isRemote) {
            recalculateVisuals();
        }
    }

    /**
     * Reorders the pipe ends so that endA has a lower ordinal than endB. This helps with texturing.
     * @return
     */
    private boolean orderEnds()
    {
        if (endB == null || (endA != null && endB.ordinal() >= endA.ordinal())) {
            return false;
        }

        EnumFacing endSwap = endA;
        endA = endB;
        endB = endSwap;

        boolean isConnectedSwap = endAIsConnected;
        endAIsConnected = endBIsConnected;
        endBIsConnected = isConnectedSwap;
        return true;
    }

    private static final List<ImmutablePair<EnumFacing, EnumFacing>> PIPE_ROTATIONS = new LinkedList<ImmutablePair<EnumFacing, EnumFacing>>();

    /**
     * Calculates the minimum list of possible commutative rotations
     */
    private synchronized static void calculatePipeRotations() {
        if (PIPE_ROTATIONS.isEmpty())
        {
            for (int i = 0; i < EnumFacing.VALUES.length; ++i)
            {
                for (int j = i + 1; j < EnumFacing.VALUES.length; ++j)
                {
                    PIPE_ROTATIONS.add(new ImmutablePair<EnumFacing, EnumFacing>(EnumFacing.VALUES[i], EnumFacing.VALUES[j]));
                }
            }
        }
    }


    /**
     * Calculates a list of valid directions a pipe could potentially be facing.
     * @return A list of ends which are valid for the pipe.
     */
    private List<ImmutablePair<EnumFacing, EnumFacing>> recalculateValidDirections()
    {
        if (PIPE_ROTATIONS.isEmpty()) {
            calculatePipeRotations();
        }

        List<ImmutablePair<EnumFacing, EnumFacing>> validDirections = new LinkedList<ImmutablePair<EnumFacing, EnumFacing>>();
        for (ImmutablePair<EnumFacing, EnumFacing> pair : PIPE_ROTATIONS)
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
        //Render as a corner if endB is not the opposite of endA
        this.shouldRenderAsCorner = endB != endA.getOpposite();

        //There are two edge cases where we need an alternative model
        useAlternateModel = false;

        //If the ends are NORTH and WEST or they are SOUTH and EAST
        if ((endA == EnumFacing.NORTH && endB == EnumFacing.WEST) ||
                endA == EnumFacing.SOUTH && endB == EnumFacing.EAST) {
            //And the tile entity in the z access
            final IPipeTileEntity pipeTileEntity = getPipeTileEntityInDirection(endA);
            if (pipeTileEntity instanceof BasePlumbingTE) {
                final BasePlumbingTE pipeTE = (BasePlumbingTE)pipeTileEntity;
                //Is a straight pipe going north/south
                if (pipeTE.isSideConnected(EnumFacing.NORTH) && pipeTE.isSideConnected(EnumFacing.SOUTH)) {
                    useAlternateModel = true;
                }
            }
        }
    }

    /**
     * Disconnects a side from the pipe. The end result is that if one end is still connected, it will be endA, and
     * endB will be set to the opposite direction as endA.
     * @param opposite
     */
    @Override
    public void disconnect(EnumFacing opposite)
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

    /**
     * Rotates the pipe around the axis that are valid.
     */
    public void rotatePipe()
    {
        //Find all the sides we could possibly connect to
        List<ImmutablePair<EnumFacing, EnumFacing>> validDirections = recalculateValidDirections();
        int length = validDirections.size();
        //If there is none, or just one, then return, there's no point in doing anything.
        if (length <= 1) {return;}

        //Out of all the valid ends, find the current one (if there is one). We'll start from here.
        int i;
        for (i = 0; i < length; ++i) {
            ImmutablePair<EnumFacing, EnumFacing> pipeEnds = validDirections.get(i);
            if (pipeEnds.left == this.endA && pipeEnds.right == this.endB)
            {
                break;
            }
        }
        //When we hit this number, we've looped around to the beginning
        int stop = i % length;
        ImmutablePair<EnumFacing, EnumFacing> selectedEnds = null;
        //Iterate forward from the current set, looping around, until we hit our stopping index.
        while ((i = (++i) % length) != stop) {
            ImmutablePair<EnumFacing, EnumFacing> pipeEnds = validDirections.get(i);
            IPipeTileEntity pipe = getPipeTileEntityInDirection(pipeEnds.left);
            //Check to see if it's possible to connect to this pipe to endA (it might already have both ends connected)
            if (pipe == null || pipe.canConnect(pipeEnds.left.getOpposite())) {
                pipe = getPipeTileEntityInDirection(pipeEnds.right);
                //Now check to see if it's possible for the pipe at the other end to connect to endB
                if (pipe == null || pipe.canConnect(pipeEnds.right.getOpposite())) {
                    //We've found a valid selection
                    selectedEnds = pipeEnds;
                    break;
                }
            }
        }

        //If we haven't found any valid ends, leave everything alone and just exit here.
        if (selectedEnds == null) {
            return;
        }
        EnumFacing newEndA = selectedEnds.getLeft();
        EnumFacing newEndB = selectedEnds.getRight();

        IPipeTileEntity prevEndATileEntity;
        IPipeTileEntity newEndATileEntity;

        prevEndATileEntity = getPipeTileEntityInDirection(endA);
        newEndATileEntity = getPipeTileEntityInDirection(newEndA);

        IPipeTileEntity prevEndBTileEntity = getPipeTileEntityInDirection(endB);
        IPipeTileEntity newEndBTileEntity = getPipeTileEntityInDirection(newEndB);

        boolean endAChanged = endA != newEndA;
        boolean endBChanged = endB != newEndB;

        //TODO: Why am I not using tryConnect on the opposite ends?
        endA = newEndA;
        endB = newEndB;
        orderEnds();

        //Notify any neighbouring blocks that have been changed that they need to recalculate their visuals
        if (endAChanged && prevEndATileEntity != null) {
            prevEndATileEntity.recalculateVisuals();
        }
        if (endBChanged && prevEndBTileEntity != null) {
            prevEndBTileEntity.recalculateVisuals();
        }
        if (endAChanged && newEndATileEntity != null) {
            newEndATileEntity.recalculateVisuals();
        }
        if (endBChanged && newEndBTileEntity != null) {
            newEndBTileEntity.recalculateVisuals();
        }

        //TODO: Remind myself why this is last?
        sendUpdate();
    }

    @Override
    public String toString()
    {
        Objects.ToStringHelper stringHelper = Objects.toStringHelper(this)
                .add("worldBlockCoord", this.getPos())
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

    /**
     * Checks if the specified side is connected to either end of this pipe
     * @param side the side of the pipe to check
     * @return true if the side is connected
     */
    @Override
    public boolean isSideConnected(EnumFacing side)
    {
        //TODO: not using endAisConnected??
        return endA == side || endB == side;
    }

    /**
     * Attempts to make a connection to an available end.
     * @param side the side of the pipe to connect to
     * @return true if a connection was made
     */
    @Override
    public boolean tryConnect(EnumFacing side)
    {
        EnumFacing previousEndA = endA;
        EnumFacing previousEndB = endB;
        boolean previousEndAConnected = endAIsConnected;
        boolean previousEndBConnected = endBIsConnected;

        boolean connected = false;
        if (endA == side) {
            endAIsConnected = true;
            connected = true;
        } else if (!endAIsConnected && endB != side) {
            endA = side;
            endAIsConnected = true;
            connected = true;
        } else if (endB == side) {
            endBIsConnected = true;
            connected = true;
        } else if (!endBIsConnected) {
            endB = side;
            endBIsConnected = true;
            connected = true;
        }

        if (endA != previousEndA || endB != previousEndB || endAIsConnected != previousEndAConnected || endBIsConnected != previousEndBConnected) {
            orderEnds();
            sendUpdate();
        }

        return connected;
    }

    /**
     * Checks if there is a free side to connect to, or true if it's already connected
     * @param side the desired side to connect to
     * @return true if a connection is possible, or already made
     */
    @Override
    public boolean canConnect(EnumFacing side)
    {
        return !endAIsConnected || !endBIsConnected || endA == side || endB == side;
    }

    public void setOrientation(EnumFacing orientation)
    {
        IPipeTileEntity tileEntity;

        this.endA = orientation;
        tileEntity = getPipeTileEntityInDirection(endA);
        endAIsConnected = tileEntity != null && tileEntity.isSideConnected(endA.getOpposite());

        this.endB = orientation.getOpposite();
        tileEntity = getPipeTileEntityInDirection(endB);
        endBIsConnected = tileEntity != null && tileEntity.isSideConnected(endB.getOpposite());
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
    public void writePlumbingToNBT(NBTTagCompound nbt)
    {
        nbt.setByte(NBT_END_A, (byte)endA.ordinal());
        nbt.setBoolean(NBT_END_A_CONNECTED, endAIsConnected);
        nbt.setByte(NBT_END_B, (byte)endB.ordinal());
        nbt.setBoolean(NBT_END_B_CONNECTED, endBIsConnected);
    }

    @Override
    public void readPlumbingFromNBT(NBTTagCompound nbt)
    {
        endA = EnumFacing.VALUES[nbt.getByte(NBT_END_A)];
        endAIsConnected = nbt.getBoolean(NBT_END_A_CONNECTED);
        endB = EnumFacing.VALUES[nbt.getByte(NBT_END_B)];
        endBIsConnected = nbt.getBoolean(NBT_END_B_CONNECTED);

        if (worldObj != null && worldObj.isRemote)
        {
            recalculateVisuals();
        }
    }

    public boolean shouldRenderAsCorner()
    {
        return shouldRenderAsCorner;
    }

    public boolean shouldUseAlternateModel()
    {
        return useAlternateModel;
    }
}
