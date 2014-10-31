package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.entity.EntityLiving;

//TODO move to Swarm?
public class AISwarmReturnHome<T extends EntityLiving & ISwarmer> extends AISwarmBase<T>
{
    private final int range;
    private final float speed;
    private final boolean ignoreCurrPath;
    private boolean returningHome = false; //This is true if we are currently pathing home

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
    }

    @Override
    public boolean shouldExecute()
    {
        if (super.shouldExecute() && !returningHome && (!entity.getNavigator().noPath() || ignoreCurrPath)) {
            ChunkCoord homeChunkCoords = entity.getSwarm().getHomeChunkCoord();
            if (entity.getDistance(homeChunkCoords.getX() + 8, entity.getSwarm().getHomeBlockCoord().getY(), homeChunkCoords.getZ() + 8) > range + 8)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void startExecuting()
    {
        ChunkCoord homeChunkCoords = entity.getSwarm().getHomeChunkCoord();
        WorldBlockCoord worldBlockCoord = homeChunkCoords.localToWorldCoords(entity.getSwarm().getHomeBlockCoord());
        //TODO add some variation?
        entity.getNavigator().setPath(entity.getNavigator().getPathToXYZ(worldBlockCoord.getX(), worldBlockCoord.getY(), worldBlockCoord.getZ()), speed);
        returningHome = true;
    }

    @Override
    public void resetTask()
    {
        returningHome = false;
        super.resetTask();
    }
}
