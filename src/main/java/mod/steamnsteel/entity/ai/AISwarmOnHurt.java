package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import net.minecraft.entity.EntityCreature;

public class AISwarmOnHurt<T extends EntityCreature & ISwarmer> extends AISwarmTarget<T>
{
    public AISwarmOnHurt(T entity)
    {
        super(entity, 32);
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
