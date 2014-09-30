package mod.steamnsteel.texturing;

import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Random;

public class TextureContext
{
    private ForgeDirection orientation;
    private ForgeDirection[] directions;

    public ForgeDirection getForwardDirection()
    {
        return forwardDirection;
    }


    private ForgeDirection forwardDirection;

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

    private ForgeDirection leftDirection;
    private ForgeDirection rightDirection;
    private ForgeDirection backDirection;
    private ForgeDirection upDirection;
    private ForgeDirection downDirection;
    private IBlockAccess blockAccess;
    private WorldBlockCoord worldBlockCoord;

    private TextureContext() {}
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

        directions = new ForgeDirection[6];
        directions[TextureDirection.LEFT.ordinal()] = leftDirection;
        directions[TextureDirection.RIGHT.ordinal()] = rightDirection;
        directions[TextureDirection.ABOVE.ordinal()] = upDirection;
        directions[TextureDirection.BELOW.ordinal()] = downDirection;
        directions[TextureDirection.BACKWARDS.ordinal()] = backDirection;
        directions[TextureDirection.FORWARD.ordinal()] = forwardDirection;
    }

    public TextureContext forLocation(WorldBlockCoord blockCoord) {
        TextureContext newContext = new TextureContext();
        newContext.blockAccess = blockAccess;
        newContext.worldBlockCoord = blockCoord;
        newContext.orientation = orientation;
        newContext.leftDirection = leftDirection;
        newContext.rightDirection = rightDirection;
        newContext.backDirection = backDirection;
        newContext.upDirection = upDirection;
        newContext.downDirection = downDirection;
        newContext.forwardDirection = forwardDirection;
        newContext.directions = directions;
        return newContext;
    }

    public boolean useAlternateVersion(float probability)
    {
        int x = worldBlockCoord.getX();
        int y = worldBlockCoord.getY();
        int z = worldBlockCoord.getZ();
        Random r = new Random(x * y * z * 31);
        return probability > r.nextFloat();
    }

    public ForgeDirection getOrientation()
    {
        return orientation;
    }

    public ForgeDirection getForgeDirection(TextureDirection textureDirection)
    {
        return directions[textureDirection.ordinal()];
    }
}
