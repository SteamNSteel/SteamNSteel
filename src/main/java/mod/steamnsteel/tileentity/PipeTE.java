package mod.steamnsteel.tileentity;

import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.block.machine.PipeJunctionBlock;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.ArrayList;
import java.util.List;

public class PipeTE extends SteamNSteelTE
{
    private int renderType;

    public ForgeDirection getEndA()
    {
        if (endA == null) return ForgeDirection.EAST;
        return endA;
    }

    public ForgeDirection getEndB()
    {
        if (endB == null) return getEndA().getOpposite();
        return endB;
    }

    private ForgeDirection endA;
    private ForgeDirection endB;

    /*public int getRenderState()
    {
        return renderState;
    }*/

    /*public ForgeDirection getRenderOrientation()
    {
        if (renderOrientation == null) return ForgeDirection.EAST;
        return renderOrientation;
    }*/

    //private int renderState;
    //private ForgeDirection renderOrientation;
    List<ImmutablePair<ForgeDirection, ForgeDirection>> validDirections = new ArrayList<ImmutablePair<ForgeDirection, ForgeDirection>>();

    @Override
    public void updateEntity()
    {
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
            ends++;
        }

        if (endB != null)
        {
            ends++;
        }

        if (ends == 2 && endB != endA.getOpposite()) {
            ends = 3;
        }
        renderType = ends;





        /*ForgeDirection primaryDirection = null;
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            WorldBlockCoord neighbour = worldBlockCoord.offset(direction);
            final Block block = neighbour.getBlock(worldObj);
            boolean isMatch = block instanceof PipeBlock || block instanceof PipeJunctionBlock;
            if (isMatch) {
                matchedDirections++;
                primaryDirection = primaryDirection == null ? direction : primaryDirection;
            }
            connections[direction.ordinal()] = isMatch ? 1 : 0;
        }

        renderOrientation = primaryDirection;
        switch (matchedDirections) {
            case 0:
                renderState = 0; // No pipes connected, need double ended pipe
                break;
            case 1:
                renderState = 1; // One pipe connected, one ended pipe, oriented to primary;
                break;
            case 2:
                if (connections[primaryDirection.getOpposite().ordinal()] == 1) {
                    renderState = 2; // Two pipes connected, Straight connection
                } else {
                    renderState = 3; // Two pipes connected, Elbow joint
                    //FIXME: Find rotation
                    //renderRotation =
                }
                break;
            /*default:
                renderState = 4; // Junction?
                break;
        }*/
    }

    public int getRenderType()
    {
        return renderType;
    }
}
