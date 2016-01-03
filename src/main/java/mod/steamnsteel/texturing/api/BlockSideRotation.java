package mod.steamnsteel.texturing.api;

import net.minecraft.util.EnumFacing;

public class BlockSideRotation
{
    private static final EnumFacing[][] ROTATION_MATRIX = {
            //{EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN},
            {EnumFacing.EAST, EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.NORTH},
            //{EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN, EnumFacing.UNKNOWN},
            {EnumFacing.EAST, EnumFacing.WEST, EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH},
            {EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.DOWN, EnumFacing.UP},
            {EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.UP},
            {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP},
            {EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.UP}
    };

    public static EnumFacing forOrientation(TextureDirection direction, EnumFacing orientation)
    {
        return ROTATION_MATRIX[orientation.ordinal()][direction.ordinal()];
    }

}
