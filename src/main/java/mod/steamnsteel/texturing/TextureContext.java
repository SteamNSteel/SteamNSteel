package mod.steamnsteel.texturing;

import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class TextureContext
{
    private final ForgeDirection orientation;
    private final Block[] blocks;

    public ForgeDirection getForwardDirection()
    {
        return forwardDirection;
    }


    private final ForgeDirection forwardDirection;

    public ForgeDirection getLeftDirection()
    {
        return leftDirection;
    }

    public ForgeDirection getRightDirection()
    {
        return rightDirection;
    }

    public ForgeDirection getBackwardDirection()
    {
        return backDirection;
    }

    public ForgeDirection getUpDirection()
    {
        return upDirection;
    }

    public ForgeDirection getDownDirection()
    {
        return downDirection;
    }

    public IBlockAccess getBlockAccess()
    {
        return blockAccess;
    }

    public WorldBlockCoord getWorldBlockCoord()
    {
        return worldBlockCoord;
    }

    private final ForgeDirection leftDirection;
    private final ForgeDirection rightDirection;
    private final ForgeDirection backDirection;
    private final ForgeDirection upDirection;
    private final ForgeDirection downDirection;
    private final IBlockAccess blockAccess;
    private final WorldBlockCoord worldBlockCoord;

    public TextureContext(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
    {
        this.blockAccess = blockAccess;
        this.worldBlockCoord = worldBlockCoord;
        orientation = ForgeDirection.getOrientation(side);
        leftDirection = BlockSideRotation.forOrientation(TextureDirection.LEFT, orientation);
        rightDirection = BlockSideRotation.forOrientation(TextureDirection.RIGHT, orientation);
        backDirection = BlockSideRotation.forOrientation(TextureDirection.BACKWARDS, orientation);
        upDirection = BlockSideRotation.forOrientation(TextureDirection.ABOVE, orientation);
        downDirection = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);
        forwardDirection = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);

        blocks = new Block[6];
        blocks[TextureDirection.LEFT.ordinal()] = worldBlockCoord.offset(leftDirection).getBlock(blockAccess);
        blocks[TextureDirection.RIGHT.ordinal()] = worldBlockCoord.offset(rightDirection).getBlock(blockAccess);
        blocks[TextureDirection.ABOVE.ordinal()] = worldBlockCoord.offset(upDirection).getBlock(blockAccess);
        blocks[TextureDirection.BELOW.ordinal()] = worldBlockCoord.offset(downDirection).getBlock(blockAccess);
        blocks[TextureDirection.BACKWARDS.ordinal()] = worldBlockCoord.offset(backDirection).getBlock(blockAccess);
        blocks[TextureDirection.FORWARD.ordinal()] = worldBlockCoord.offset(forwardDirection).getBlock(blockAccess);
    }

    public ForgeDirection getOrientation()
    {
        return orientation;
    }

    public Block getNearbyBlock(TextureDirection direction)
    {
        return blocks[direction.ordinal()];
    }
}
