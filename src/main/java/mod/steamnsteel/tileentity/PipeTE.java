package mod.steamnsteel.tileentity;

import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.block.machine.PipeJunctionBlock;
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
    //List<ImmutablePair<ForgeDirection, ForgeDirection>> potentialValidDirections = new ArrayList<ImmutablePair<ForgeDirection, ForgeDirection>>();

    @Override
    public void updateEntity()
    {
        /*potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.UP, ForgeDirection.DOWN));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.NORTH, ForgeDirection.SOUTH));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.EAST, ForgeDirection.WEST));

        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.NORTH, ForgeDirection.EAST));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.EAST, ForgeDirection.SOUTH));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.SOUTH, ForgeDirection.WEST));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.WEST, ForgeDirection.NORTH));

        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.UP, ForgeDirection.NORTH));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.UP, ForgeDirection.EAST));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.UP, ForgeDirection.SOUTH));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.UP, ForgeDirection.WEST));

        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.DOWN, ForgeDirection.NORTH));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.DOWN, ForgeDirection.EAST));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.DOWN, ForgeDirection.SOUTH));
        potentialValidDirections.add(new ImmutablePair<ForgeDirection, ForgeDirection>(ForgeDirection.DOWN, ForgeDirection.WEST));*/


        WorldBlockCoord worldBlockCoord = WorldBlockCoord.of(xCoord, yCoord, zCoord);

        validDirections.clear();

        boolean currentEndsAreValid = false;
        ForgeDirection firstValidEndA = null;
        ForgeDirection firstValidEndB = null;

        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i) {
            final ForgeDirection endADirection = ForgeDirection.VALID_DIRECTIONS[i];
            WorldBlockCoord neighbour = worldBlockCoord.offset(endADirection);
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
                    WorldBlockCoord neighbourB = worldBlockCoord.offset(endBDirection);
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

        if (!currentEndsAreValid) {
            endA = firstValidEndA;
            endB = firstValidEndB;
        }

        int ends = 0;

        if (endA != null) {
            TileEntity te = worldBlockCoord.offset(endA).getTileEntity(worldObj);
            if (te != null && te instanceof PipeTE) {
                final PipeTE pipeTE = (PipeTE) te;
                final ForgeDirection endAOpposite = endA.getOpposite();
                if (pipeTE.endA == endAOpposite || pipeTE.endB == endAOpposite) {
                    ends++;
                }
            }
        }

        if (endB != null)
        {
            TileEntity te = worldBlockCoord.offset(endB).getTileEntity(worldObj);
            if (te != null && te instanceof PipeTE) {
                final PipeTE pipeTE = (PipeTE) te;
                final ForgeDirection endBOpposite = endB.getOpposite();
                if (pipeTE.endA == endBOpposite || pipeTE.endB == endBOpposite) {
                    ends++;
                }
            }
        }

        if (ends == 2 && endB != endA.getOpposite()) {
            ends = 3;
        }
        renderType = ends;
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
    }

    public int getRenderType()
    {
        return renderType;
    }

    public void rotatePipe()
    {
        int length = validDirections.size();
        if (length == 0) {return;}

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
        endA = selectedEnds.getLeft();
        endB = selectedEnds.getRight();
    }
}
