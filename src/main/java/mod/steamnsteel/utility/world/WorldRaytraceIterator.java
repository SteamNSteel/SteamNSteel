package mod.steamnsteel.utility.world;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WorldRaytraceIterator
{
    private final World world;
    private final Vec3 startLocation;
    private final Vec3 direction;
    private final MovingObjectPosition currentBlock;
    private int currentLocationX;
    private int currentLocationY;
    private int currentLocationZ;
    private int locationX;
    private int locationY;
    private int locationZ;
    private boolean valid;
    private int blockLimit;

    public WorldRaytraceIterator(World world, Vec3 location, Vec3 direction)
    {
        this.world = world;
        this.startLocation = location;
        this.direction = direction;


        if (Double.isNaN(location.xCoord) || Double.isNaN(location.yCoord) || Double.isNaN(location.zCoord))
        {
            valid = false;
        }
        if (Double.isNaN(direction.xCoord) || Double.isNaN(direction.yCoord) || Double.isNaN(direction.zCoord))
        {
            valid = false;
        }

        currentLocationX = MathHelper.floor_double(direction.xCoord);
        currentLocationY = MathHelper.floor_double(direction.yCoord);
        currentLocationZ = MathHelper.floor_double(direction.zCoord);
        locationX = MathHelper.floor_double(location.xCoord);
        locationY = MathHelper.floor_double(location.yCoord);
        locationZ = MathHelper.floor_double(location.zCoord);

        blockLimit = 200;

        currentBlock = getInitialBlock();
        if (currentBlock == null) valid = false;
    }

    private MovingObjectPosition getInitialBlock() {

        boolean p_147447_3_ = false;
        boolean p_147447_4_ = false;

        Block block = world.getBlock(locationX, locationY, locationZ);
        int metadata = world.getBlockMetadata(locationX, locationY, locationZ);
        MovingObjectPosition movingobjectposition = null;
        if ((!p_147447_4_ || block.getCollisionBoundingBoxFromPool(world, locationX, locationY, locationZ) != null) && block.canCollideCheck(metadata, p_147447_3_))
        {
            movingobjectposition = block.collisionRayTrace(world, locationX, locationY, locationZ, startLocation, direction);
        }

        return movingobjectposition;
    }

    public MovingObjectPosition findNextBlock()
    {
        boolean p_147447_3_ = false;
        boolean p_147447_4_ = false;
        boolean p_147447_5_ = false;

        MovingObjectPosition movingobjectposition2 = null;

        while (blockLimit-- >= 0)
        {
            if (Double.isNaN(startLocation.xCoord) || Double.isNaN(startLocation.yCoord) || Double.isNaN(startLocation.zCoord))
            {
                return null;
            }

            if (locationX == currentLocationX && locationY == currentLocationY && locationZ == currentLocationZ)
            {
                return p_147447_5_ ? movingobjectposition2 : null;
            }

            boolean flag6 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d0 = 999.0D;
            double d1 = 999.0D;
            double d2 = 999.0D;

            if (currentLocationX > locationX)
            {
                d0 = (double) locationX + 1.0D;
            }
            else if (currentLocationX < locationX)
            {
                d0 = (double) locationX + 0.0D;
            }
            else
            {
                flag6 = false;
            }

            if (currentLocationY > locationY)
            {
                d1 = (double) locationY + 1.0D;
            }
            else if (currentLocationY < locationY)
            {
                d1 = (double) locationY + 0.0D;
            }
            else
            {
                flag3 = false;
            }

            if (currentLocationZ > locationZ)
            {
                d2 = (double) locationZ + 1.0D;
            }
            else if (currentLocationZ < locationZ)
            {
                d2 = (double) locationZ + 0.0D;
            }
            else
            {
                flag4 = false;
            }

            double d3 = 999.0D;
            double d4 = 999.0D;
            double d5 = 999.0D;
            double d6 = direction.xCoord - startLocation.xCoord;
            double d7 = direction.yCoord - startLocation.yCoord;
            double d8 = direction.zCoord - startLocation.zCoord;

            if (flag6)
            {
                d3 = (d0 - startLocation.xCoord) / d6;
            }

            if (flag3)
            {
                d4 = (d1 - startLocation.yCoord) / d7;
            }

            if (flag4)
            {
                d5 = (d2 - startLocation.zCoord) / d8;
            }

            byte hitSide;

            if (d3 < d4 && d3 < d5)
            {
                if (currentLocationX > locationX)
                {
                    hitSide = 4;
                }
                else
                {
                    hitSide = 5;
                }

                startLocation.xCoord = d0;
                startLocation.yCoord += d7 * d3;
                startLocation.zCoord += d8 * d3;
            }
            else if (d4 < d5)
            {
                if (currentLocationY > locationY)
                {
                    hitSide = 0;
                }
                else
                {
                    hitSide = 1;
                }

                startLocation.xCoord += d6 * d4;
                startLocation.yCoord = d1;
                startLocation.zCoord += d8 * d4;
            }
            else
            {
                if (currentLocationZ > locationZ)
                {
                    hitSide = 2;
                }
                else
                {
                    hitSide = 3;
                }

                startLocation.xCoord += d6 * d5;
                startLocation.yCoord += d7 * d5;
                startLocation.zCoord = d2;
            }

            Vec3 vec32 = Vec3.createVectorHelper(startLocation.xCoord, startLocation.yCoord, startLocation.zCoord);
            locationX = (int)(vec32.xCoord = (double)MathHelper.floor_double(startLocation.xCoord));

            if (hitSide == 5)
            {
                --locationX;
                ++vec32.xCoord;
            }

            locationY = (int)(vec32.yCoord = (double)MathHelper.floor_double(startLocation.yCoord));

            if (hitSide == 1)
            {
                --locationY;
                ++vec32.yCoord;
            }

            locationZ = (int)(vec32.zCoord = (double)MathHelper.floor_double(startLocation.zCoord));

            if (hitSide == 3)
            {
                --locationZ;
                ++vec32.zCoord;
            }

            Block block1 = world.getBlock(locationX, locationY, locationZ);
            int l1 = world.getBlockMetadata(locationX, locationY, locationZ);

            if (!p_147447_4_ || block1.getCollisionBoundingBoxFromPool(world, locationX, locationY, locationZ) != null)
            {
                if (block1.canCollideCheck(l1, p_147447_3_))
                {
                    MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(world, locationX, locationY, locationZ, startLocation, direction);

                    if (movingobjectposition1 != null)
                    {
                        return movingobjectposition1;
                    }
                }
                else
                {
                    movingobjectposition2 = new MovingObjectPosition(locationX, locationY, locationZ, hitSide, startLocation, false);
                }
            }
        }

        return p_147447_5_ ? movingobjectposition2 : null;
    }
}
