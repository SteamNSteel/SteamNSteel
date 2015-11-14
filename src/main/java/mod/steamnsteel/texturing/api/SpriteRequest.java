package mod.steamnsteel.texturing.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * The texture context is used to pass around many common properties required to calculate a feature set.
 */
public class SpriteRequest
{
    private EnumFacing orientation;
    private EnumFacing[] directions;

    /**
     * @return the EnumFacing that corresponds to moving forward
     */
    public EnumFacing getForwardDirection()
    {
        return forwardDirection;
    }


    private EnumFacing forwardDirection;

    /**
     * @return the EnumFacing that corresponds to moving left
     */
    public EnumFacing getLeftDirection()
    {
        return leftDirection;
    }

    /**
     * @return the EnumFacing that corresponds to moving right
     */
    public EnumFacing getRightDirection()
    {
        return rightDirection;
    }

    /**
     * @return the EnumFacing that corresponds to moving backwards
     */
    public EnumFacing getBackwardDirection()
    {
        return backDirection;
    }

    /**
     * @return the EnumFacing that corresponds to moving upwards
     */
    public EnumFacing getUpDirection()
    {
        return upDirection;
    }

    /**
     * @return the EnumFacing that corresponds to moving downwards
     */
    public EnumFacing getDownDirection()
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
    public BlockPos getWorldBlockCoord()
    {
        return worldBlockCoord;
    }

    private EnumFacing leftDirection;
    private EnumFacing rightDirection;
    private EnumFacing backDirection;
    private EnumFacing upDirection;
    private EnumFacing downDirection;
    private IBlockAccess blockAccess;
    private BlockPos worldBlockCoord;

    private SpriteRequest() {}

    final static Field worldObjField;

    static {
        final String[] remappedFieldNames = ObfuscationReflectionHelper.remapFieldNames(ChunkCache.class.getName(), "worldObj", "field_72815_e");
        worldObjField = ReflectionHelper.findField(ChunkCache.class, remappedFieldNames);
    }

    public SpriteRequest(IBlockAccess blockAccess, BlockPos worldBlockCoord, EnumFacing side)
    {
        if (blockAccess instanceof ChunkCache) {
            try {
                blockAccess = (IBlockAccess)worldObjField.get(blockAccess);
            } catch (IllegalAccessException e) {
                throw new TexturingException("Unable to access worldObj on ChunkCache");
            }
        }
        this.blockAccess = blockAccess;
        //this.blockAccess = Minecraft.getMinecraft().theWorld;
        this.worldBlockCoord = worldBlockCoord;
        orientation = side;
        leftDirection = BlockSideRotation.forOrientation(TextureDirection.LEFT, orientation);
        rightDirection = BlockSideRotation.forOrientation(TextureDirection.RIGHT, orientation);
        backDirection = BlockSideRotation.forOrientation(TextureDirection.BACKWARDS, orientation);
        upDirection = BlockSideRotation.forOrientation(TextureDirection.ABOVE, orientation);
        downDirection = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);
        forwardDirection = BlockSideRotation.forOrientation(TextureDirection.BELOW, orientation);

        directions = new EnumFacing[6];
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
    public SpriteRequest forLocation(BlockPos blockCoord)
    {
        SpriteRequest newContext = new SpriteRequest();
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
     * @return the EnumFacing of the side.
     */
    public EnumFacing getOrientation()
    {
        return orientation;
    }

    /**
     * returns the corresponding EnumFacing for a TextureDirection within this context.
     *
     * @param textureDirection the TextureDirection to convert
     * @return the correlated EnumFacing
     */
    public EnumFacing getEnumFacing(TextureDirection textureDirection)
    {
        return directions[textureDirection.ordinal()];
    }
}
