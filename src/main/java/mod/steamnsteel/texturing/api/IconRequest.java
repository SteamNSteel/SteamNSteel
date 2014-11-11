package mod.steamnsteel.texturing.api;

import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Random;

/**
 * The texture context is used to pass around many common properties required to calculate a feature set.
 */
public class IconRequest
{
    private ForgeDirection orientation;
    private ForgeDirection[] directions;

    /**
     * @return the ForgeDirection that corresponds to moving forward
     */
    public ForgeDirection getForwardDirection()
    {
        return forwardDirection;
    }


    private ForgeDirection forwardDirection;

    /**
     * @return the ForgeDirection that corresponds to moving left
     */
    public ForgeDirection getLeftDirection()
    {
        return leftDirection;
    }

    /**
     * @return the ForgeDirection that corresponds to moving right
     */
    public ForgeDirection getRightDirection()
    {
        return rightDirection;
    }

    /**
     * @return the ForgeDirection that corresponds to moving backwards
     */
    public ForgeDirection getBackwardDirection()
    {
        return backDirection;
    }

    /**
     * @return the ForgeDirection that corresponds to moving upwards
     */
    public ForgeDirection getUpDirection()
    {
        return upDirection;
    }

    /**
     * @return the ForgeDirection that corresponds to moving downwards
     */
    public ForgeDirection getDownDirection()
    {
        return downDirection;
    }

    /**
     * @return the the object that cab be used to access blocks in the world.
     */
    public IBlockAccess getBlockAccess()
    {
        return blockAccess;
    }

    /**
     * @return the coordinate of the block being studied.
     */
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

    private IconRequest() {}

    public IconRequest(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
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

    /**
     * Creates a new context for a related location. Directions are maintained.
     *
     * @param blockCoord the new coordinate to relate to
     * @return a new TextureContext for that location
     */
    public IconRequest forLocation(WorldBlockCoord blockCoord)
    {
        IconRequest newContext = new IconRequest();
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

    /**
     * a common method for calculating the probability of an alternate version of a feature.
     *
     * @param probability the chance that the feature will be selected.
     * @return true if the alternate version should be used.
     */
    public boolean useAlternateVersion(float probability)
    {
        int x = worldBlockCoord.getX();
        int y = worldBlockCoord.getY();
        int z = worldBlockCoord.getZ();
        Random r = new Random(x * y * z * 31);
        return probability > r.nextFloat();
    }

    /**
     * @return the ForgeDirection of the side.
     */
    public ForgeDirection getOrientation()
    {
        return orientation;
    }

    /**
     * returns the corresponding ForgeDirection for a TextureDirection within this context.
     *
     * @param textureDirection the TextureDirection to convert
     * @return the correlated ForgeDirection
     */
    public ForgeDirection getForgeDirection(TextureDirection textureDirection)
    {
        return directions[textureDirection.ordinal()];
    }
}
