package mod.steamnsteel.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class AIRangeAttack<T extends EntityLiving & IRangedAttackMob> extends EntityAIBase
{
    private final T entity;
    private final double chaseSpeed;
    private final int maxRangeAttackRate;
    private final int minRangeAttackRate;
    private final float attackRange;
    private final float maxAttackRange;

    private EntityLivingBase attackTarget;
    private int seeTime;
    private int rangedAttackTime;

    public AIRangeAttack(T entity, double chaseSpeed, int minRangeAttackRate, int maxRangeAttackRate, float attackRange, float maxAttackRange)
    {
        this.entity = entity;
        this.chaseSpeed = chaseSpeed;
        this.minRangeAttackRate = minRangeAttackRate;
        this.maxRangeAttackRate = maxRangeAttackRate;
        this.attackRange = attackRange;
        this.maxAttackRange = maxAttackRange;
    }

    @Override
    public boolean shouldExecute()
    {
        return entity.getAttackTarget() != null;
    }

    @Override
    public void startExecuting()
    {
        attackTarget = entity.getAttackTarget();
    }

    @Override
    public boolean continueExecuting()
    {
        return shouldExecute() || !entity.getNavigator().noPath();
    }

    @Override
    public void updateTask()
    {
        double distanceToTarget = entity.getDistanceSq(attackTarget.posX, attackTarget.boundingBox.minY, attackTarget.posZ);
        boolean canSee = entity.getEntitySenses().canSee(attackTarget);

        seeTime = canSee ? seeTime + 1 : 0;

        if (distanceToTarget <= (double)(attackRange * attackRange) && seeTime >= 20)
        {
            entity.getNavigator().clearPathEntity();
        }
        else
        {
            entity.getNavigator().tryMoveToEntityLiving(attackTarget, chaseSpeed);
        }

        entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
        float f;

        if (--rangedAttackTime == 0)
        {
            if (distanceToTarget > (double)(maxAttackRange * maxAttackRange) || !canSee)
            {
                return;
            }

            f = MathHelper.sqrt_double(distanceToTarget) / attackRange;
            float f1 = MathHelper.clamp_float(f, 0.1F, 1.0F);

            entity.attackEntityWithRangedAttack(attackTarget, f1);
            rangedAttackTime = MathHelper.floor_float(f * (float)(maxRangeAttackRate - minRangeAttackRate) + (float)minRangeAttackRate);
        }
        else if (rangedAttackTime < 0)
        {
            f = MathHelper.sqrt_double(distanceToTarget) / attackRange;
            rangedAttackTime = MathHelper.floor_float(f * (float)(maxRangeAttackRate - minRangeAttackRate) + (float)minRangeAttackRate);
        }
    }

    @Override
    public void resetTask()
    {
        attackTarget = null;
        seeTime = 0;
        rangedAttackTime = -1;
    }
}
