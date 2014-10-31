package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import net.minecraft.entity.EntityLiving;

public class AISwarmOnHurt<T extends EntityLiving & ISwarmer> extends AISwarmBase<T>
{
    public AISwarmOnHurt(T entity)
    {
        super(entity);
    }

    public void startExecuting()
    {
        entity.setAttackTarget(entity.getAITarget());

        if (entity.getSwarm() != null)
        {
            entity.getSwarm().setAttackTarget(entity.getAITarget());
        }

        super.startExecuting();
    }
}
