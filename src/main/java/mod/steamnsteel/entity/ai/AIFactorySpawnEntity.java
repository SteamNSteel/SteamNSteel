package mod.steamnsteel.entity.ai;

import mod.steamnsteel.factory.IFactoryEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

/**
 * Created by Steven on 7/04/2015.
 */
public class AIFactorySpawnEntity<T extends EntityLiving & IFactoryEntity> extends EntityAIBase {

    private final int timeToCreateEntity;
    protected T entity;
    private final int timeBetweenSpawns;

    public AIFactorySpawnEntity(T entity, int timeBetweenSpawns, int timeToCreateEntity) {
        this.entity = entity;
        this.timeBetweenSpawns = timeBetweenSpawns;
        this.timeToCreateEntity = timeToCreateEntity;
    }

    @Override
    public boolean shouldExecute() {
        return !entity.isDead && !entity.isSpawning() && entity.getLastBuildTime() < (entity.worldObj.getWorldTime() - timeBetweenSpawns);
    }

    @Override
    public void startExecuting() {
        entity.startBuildCycle();
    }

    @Override
    public void updateTask() {
        if (entity.isSpawning() && entity.getBuildStartTime() > entity.worldObj.getWorldTime() - timeToCreateEntity) {
            entity.finishBuildCycle();
            entity.spawnEntity();
        }
    }
}
