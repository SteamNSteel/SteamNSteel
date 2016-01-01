package mod.steamnsteel.texturing.api;

import net.minecraft.util.EnumFacing;

public class BlockSideRotation
{
    private static final EnumFacing[][] ROTATION_MATRIX = {
            {EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN},
            {EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN},
            {EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.UNKNOWN},
            {EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.UNKNOWN},
            {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.UNKNOWN},
            {EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.UNKNOWN}
    };

    public static EnumFacing forOrientation(TextureDirection direction, EnumFacing orientation)
    {
        return ROTATION_MATRIX[orientation.ordinal()][direction.ordinal()];
    }

}
