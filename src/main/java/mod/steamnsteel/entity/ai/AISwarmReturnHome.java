package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;

public class AISwarmReturnHome<T extends EntityLiving & ISwarmer> extends AISwarmBase<T>
{
    private final int range;
    private final float speed;
    private final boolean ignoreCurrPath;

    /**
     * This creates an AI task for making the swarm entity return home (note, only makes the entity it's assigned to return
     * home, does not affect swarm). The entity will only return home if it is beyond max range from the home chunk.
     * You can tell the entity to ignore its current path to force it return home if it is a territorial entity.
     * @param entity The entity
     * @param range The range
     * @param speed The speed
     * @param ignoreCurrPath Whether it should return home regardless of path
     */
    public AISwarmReturnHome(T entity, int range, float speed, boolean ignoreCurrPath)
    {
        super(entity);
        this.range = range;
        this.speed = speed;
        this.ignoreCurrPath = ignoreCurrPath;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        if (super.shouldExecute() && (entity.getNavigator().noPath() || ignoreCurrPath)) {
            ChunkCoord homeChunkCoords = entity.getSwarm().getHomeChunkCoord();
            if (entity.getDistanceSq((homeChunkCoords.getX() * 16) + 8, entity.getSwarm().getHomeBlockCoord().getY(), (homeChunkCoords.getZ() * 16) + 8) > range + 16)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void startExecuting()
    {
        entity.setAttackTarget(null);
        WorldBlockCoord worldBlockCoord = entity.getSwarm().getHomeChunkCoord().localToWorldCoords(entity.getSwarm().getHomeBlockCoord());
        entity.getNavigator().tryMoveToXYZ(worldBlockCoord.getX(), worldBlockCoord.getY(), worldBlockCoord.getZ(), speed);
    }

    @Override
    public void updateTask()
    {
        entity.setAttackTarget(null);
        if (entity.getNavigator().noPath())
        {
            //This will basically only be called if getting to the main home position failed so find random point around
            WorldBlockCoord worldBlockCoord = entity.getSwarm().getHomeChunkCoord().localToWorldCoords(entity.getSwarm().getHomeBlockCoord());
            entity.getNavigator().tryMoveToXYZ(worldBlockCoord.getX() + MathHelper.getRandomIntegerInRange(entity.getRNG(), -4, 4),
                    worldBlockCoord.getY(), worldBlockCoord.getZ() + MathHelper.getRandomIntegerInRange(entity.getRNG(), -4, 4), speed);
        }
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
    }

    @Override
    public boolean isInterruptible()
    {
        return false;
    }
}
