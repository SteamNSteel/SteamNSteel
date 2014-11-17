package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;

public class AISwarmTarget<T extends EntityCreature & ISwarmer> extends AISwarmBase<T>
{
    protected final int range;

    public AISwarmTarget(T entity, int range)
    {
        super(entity);
        this.range = range;
    }

    @Override
    public boolean continueExecuting()
    {
        EntityLivingBase entitylivingbase = entity.getAttackTarget();

        return entitylivingbase != null && entitylivingbase.isEntityAlive() && entity.getDistanceSqToEntity(entitylivingbase) <= (range * range)
                && entity.getEntitySenses().canSee(entitylivingbase) && (!(entitylivingbase instanceof EntityPlayerMP)
                || !((EntityPlayerMP) entitylivingbase).theItemInWorldManager.isCreative());
    }
}
