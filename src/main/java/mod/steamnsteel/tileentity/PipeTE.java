package mod.steamnsteel.tileentity;

import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeTE extends SteamNSteelTE
{
    int[] connections = new int[6];

    public int[] getConnections()
    {
        return connections;
    }

    public int getRenderState()
    {
        return renderState;
    }

    public ForgeDirection getRenderOrientation()
    {
        if (renderOrientation == null) return ForgeDirection.EAST;
        return renderOrientation;
    }

    private int renderState;
    private ForgeDirection renderOrientation;

    @Override
    public void updateEntity()
    {
        WorldBlockCoord worldBlockCoord = WorldBlockCoord.of(xCoord, yCoord, zCoord);
        int matchedDirections = 0;
        ForgeDirection primaryDirection = null;
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            WorldBlockCoord neighbour = worldBlockCoord.offset(direction);
            boolean isMatch = neighbour.getBlock(worldObj) instanceof PipeBlock;
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
            default:
                renderState = 4; // Junction?
                break;
        }

        super.updateEntity();
    }
}
