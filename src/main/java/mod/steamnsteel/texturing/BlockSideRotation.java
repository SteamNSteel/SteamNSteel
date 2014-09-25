package mod.steamnsteel.texturing;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockSideRotation
{
    private static final ForgeDirection[][] ROTATION_MATRIX = {
            {ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN},
            {ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN},
            {ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN}
    };

    public static ForgeDirection forOrientation(TextureDirection direction, ForgeDirection orientation)
    {
        return ROTATION_MATRIX[orientation.ordinal()][direction.ordinal()];
    }

    public enum TextureDirection {
        LEFT,
        RIGHT,
        BACK,
        FORWARD,
        BELOW,
        ABOVE
    }
}
