package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class AISwarmWander<T extends EntityCreature & ISwarmer> extends AISwarmBase<T>
{
    private final int chance;
    private final float speed;
    public double xPos;
    public double yPos;
    public double zPos;

    /**
     * An AI task that makes the entity only wander within its home region.
     * @param entity
     * @param speed
     */
    public AISwarmWander(T entity, int chance, float speed)
    {
        super(entity);
        this.chance = chance;
        this.speed = speed;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        if (entity.getNavigator().noPath() && entity.getRNG().nextInt(chance) == 0)
        {
            Vec3 target = findRandomTarget();
            if (target != null)
            {
                xPos = target.xCoord;
                yPos = target.yCoord;
                zPos = target.zCoord;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean continueExecuting()
    {
        return !entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting()
    {
        entity.getNavigator().tryMoveToXYZ(xPos, yPos, zPos, speed);
    }

    private Vec3 findRandomTarget()
    {
        if (entity.getSwarm() != null)
        {
            //        for (int tries = 0; tries < 10; tries++)
//        {
            double x = (entity.getSwarm().getHomeChunkCoord().getX() * 16) + entity.getRNG().nextInt(16);
            double z = (entity.getSwarm().getHomeChunkCoord().getZ() * 16) + entity.getRNG().nextInt(16);
            double y = entity.getSwarm().getHomeBlockCoord().getY() + MathHelper.getRandomDoubleInRange(entity.getRNG(), -3, 3);

            //PathEntity path = entity.getNavigator().getPathToXYZ(x, y, z);
            //if (path != null)
            //{
            return Vec3.createVectorHelper(x, y, z);
            //}
//        }
//        return null;
        }
        else return RandomPositionGenerator.findRandomTarget(entity, 16, 4);
    }
}
