package mod.steamnsteel.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class AIRangeBurstAttack<T extends EntityLiving & IRangedAttackMob> extends EntityAIBase
{
    private final T entity;
    private final double chaseSpeed;
    private final float attackRange;
    private final int maxAttackTime;
    private final int cooldown;

    private EntityLivingBase attackTarget;
    private int seeTime;
    private int attackingTime;
    private long lastAttackTime;

    /**
     * Creates an AI task for attacking with a burst range attack. This means a range attack that fires at 1 per tick
     * for {@link #maxAttackTime} then goes onto a cooldown as specified by cooldown. This is mostly
     * for quick, sharp attacks.
     * @param entity The entity
     * @param chaseSpeed The chasing speed
     * @param attackRange The range when this entity will start attacking
     * @param maxAttackTime The max time the entity will attack for
     * @param cooldown The cooldown between attacks
     */
    public AIRangeBurstAttack(T entity, double chaseSpeed, float attackRange, int maxAttackTime, int cooldown)
    {
        this.entity = entity;
        this.chaseSpeed = chaseSpeed;
        this.attackRange = attackRange;
        this.maxAttackTime = maxAttackTime;
        this.cooldown = cooldown;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        return entity.getAttackTarget() != null && lastAttackTime + cooldown < entity.worldObj.getTotalWorldTime();
    }

    @Override
    public void startExecuting()
    {
        attackTarget = entity.getAttackTarget();
    }

    @Override
    public boolean continueExecuting()
    {
        return (shouldExecute() || !entity.getNavigator().noPath()) && attackingTime < maxAttackTime;
    }

    @Override
    public void updateTask()
    {
        double distanceToTarget = entity.getDistanceSq(attackTarget.posX, attackTarget.boundingBox.minY, attackTarget.posZ);
        boolean canSee = entity.getEntitySenses().canSee(attackTarget);

        seeTime = canSee ? seeTime + 1 : 0;

        if (distanceToTarget <= (double)(attackRange * attackRange) && seeTime >= 10)
        {
            entity.getNavigator().clearPathEntity();
        }
        else
        {
            entity.getNavigator().tryMoveToEntityLiving(attackTarget, chaseSpeed);
        }

        entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
        float f;

        if (MathHelper.sqrt_double(distanceToTarget) < attackRange)
        {
            if (distanceToTarget > (double)(attackRange * attackRange) || !canSee)
            {
                return;
            }

            f = MathHelper.sqrt_double(distanceToTarget) / attackRange;
            float f1 = MathHelper.clamp_float(f, 0.1F, 1.0F);

            entity.attackEntityWithRangedAttack(attackTarget, f1);
            attackingTime++;
        }
    }

    @Override
    public void resetTask()
    {
        attackTarget = null;
        seeTime = 0;
        attackingTime = 0;
        lastAttackTime = entity.worldObj.getTotalWorldTime();
    }
}
