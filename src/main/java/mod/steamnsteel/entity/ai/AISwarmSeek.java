package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import mod.steamnsteel.entity.Swarm;
import mod.steamnsteel.entity.SwarmManager;
import net.minecraft.entity.EntityLiving;

public class AISwarmSeek<T extends EntityLiving & ISwarmer> extends AISwarmBase<T>
{
    private final int startRange;
    private final int maxRange;
    private final int maxSeekTime;
    private final int maxRetries;
    private final int sleepTime;
    private final boolean formOnFail;
    private int currRange;
    private int currSeekTime;
    private int retries;
    private long lastTryTime;

    /**
     * Creates an AI task for seeking the nearest swarm this entity can join. The task will start off at a minimum range
     * specified then increase over the maxSeekTime until it reaches maxRange and maxSeekTime in which case the task will
     * terminate. The task will then not trigger for {@code sleepTime} in which case it will repeat until {@code maxRetries}
     * has been reached where the task will no longer run. The {@link #retries} will <b>NOT</b> persist over reloads and
     * will reset to 0 when the {@link #entity} is loaded again.
     * If {@code formOnFail} is true, then the entity will attempt to create a swarm at its current location. If it manages
     * to successfully create one then {@link #retries} is reset to 0.
     * @param entity The entity
     * @param startRange The starting range
     * @param maxRange The max range
     * @param maxSeekTime The max seek time
     * @param maxRetries The max retries
     * @param sleepTime The ticks to wait between retries
     * @param formOnFail Whether to create a swarm after max retries
     */
    public AISwarmSeek(T entity, int startRange, int maxRange, int maxSeekTime, int maxRetries, int sleepTime, boolean formOnFail)
    {
        super(entity);
        this.startRange = startRange;
        this.maxRange = maxRange;
        this.maxSeekTime = maxSeekTime;
        this.maxRetries = maxRetries;
        this.sleepTime = sleepTime;
        this.formOnFail = formOnFail;
    }

    @Override
    public boolean shouldExecute()
    {
        return entity.getSwarm() == null && retries < maxRetries && lastTryTime + sleepTime < entity.worldObj.getTotalWorldTime();
    }

    @Override
    public boolean continueExecuting()
    {
        return shouldExecute() && currSeekTime < maxSeekTime;
    }

    @Override
    public void startExecuting()
    {
        currRange = startRange;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateTask()
    {
        SwarmManager swarmManager = SwarmManager.swarmManagers.get(entity.worldObj);
        Swarm<T> swarm = swarmManager.getNearestSwarmToEntity(entity, (Class<T>) entity.getClass(), currRange); //Cast cause yay generics
        if (swarm != null)
        {
            entity.setSwarm(swarm);
        }
        else
        {
            currSeekTime++;
            currRange += (maxRange - startRange) / maxSeekTime;

            //If formOnFail is true, this is our last attempt so attempt to form a spawn ourself
            if (currSeekTime >= maxSeekTime && retries >= maxRetries && formOnFail)
            {
                swarm = swarmManager.getSwarmForClass((Class<T>) entity.getClass());
                if (swarm != null)
                {
                    entity.setSwarm(swarm);
                    retries = -1;
                }
            }
        }
    }

    @Override
    public void resetTask()
    {
        currRange = startRange;
        currSeekTime = 0;
        retries++;
        lastTryTime = entity.worldObj.getTotalWorldTime();
    }
}
