package mod.steamnsteel.entity;

import mod.steamnsteel.entity.ai.*;
import mod.steamnsteel.proxy.Proxies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SteamSpiderEntity extends EntityMob implements ISwarmer, IRangedAttackMob
{
    public static final String NAME = "steamSpider";
    private Swarm swarm;

    public SteamSpiderEntity(World world)
    {
        super(world);
        //TODO Proper AI tasks
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAILeapAtTarget(this, 0.5F));
        tasks.addTask(1, new AISwarmReturnHome<SteamSpiderEntity>(this, 256, 1.2F, true));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        tasks.addTask(2, new AIRangeBurstAttack<SteamSpiderEntity>(this, 1.2D, 4F, 40, 1200));
        tasks.addTask(4, new AISwarmWander<SteamSpiderEntity>(this, 60, 1.0F));
        //tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        //tasks.addTask(8, new EntityAILookIdle(this));
        tasks.addTask(8, new AISwarmSeek<SteamSpiderEntity>(this, 0, 50, 100, 3, 1200, false)); //This should be removed if we want spiders to become "dumb" when their host is killed
        targetTasks.addTask(1, new AISwarmOnHurt<SteamSpiderEntity>(this));
        targetTasks.addTask(2, new AISwarmDefendHome<SteamSpiderEntity>(this, 16));
        setSize(0.35F, 0.8F);
        renderDistanceWeight = 128F;
    }

    @Override
    protected void applyEntityAttributes()
    {
        //TODO attributes
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D); //Same speed as player walking
        //getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue();
        //getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(3.0D);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(12, (byte) 0); //Hostile status
    }

    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //Weird stuff to actually get us to face in the correct directions
        renderYawOffset = rotationYaw;
        rotationPitch = 0; //0 pitch cause we can't actually look "up"
        if (getAttackTarget() != null)
        {
            faceEntity(getAttackTarget(), 10F, 0F);
        }

        //Steam particles
        if (worldObj.isRemote)
        {
            //Position of point on a circle. Used to calculate where the exit pipes are for smoke/steam
            double rot = Math.toRadians(renderYawOffset - 90F + (rand.nextBoolean() ? 12F : -12F));
            double radius = 0.27D;
            double x = posX + (radius * Math.cos(rot));
            double z = posZ + (radius * Math.sin(rot));
            if (isInWater())
            {
                worldObj.spawnParticle("bubble", x, posY + 0.61, z, 0, 0, 0);
            }
            else
            {
                Proxies.render.spawnParticle("smoke", worldObj, x, posY + 0.61, z, 0, 0, 0, 0.5F);
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_)
    {
        this.setLastAttacker(p_70652_1_);
        return false;
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
    }

    @Override
    public float getEyeHeight()
    {
        return 0.2F;
    }

    @Override
    protected boolean isValidLightLevel()
    {
        return true;
    }

    @Override
    public Swarm getSwarm()
    {
        return swarm;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setSwarm(Swarm swarm)
    {
        if (this.swarm != null)
        {
            swarm.removeEntity(this);
        }

        this.swarm = swarm;
        swarm.addEntity(this);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase targetEntity, float range)
    {
        if (worldObj.getTotalWorldTime() % 1 == 0)
        {
            //TODO reduce number of SteamProjectileEntity spawns. Two every two ticks is not ideal
            double rot;
            double radius = 0.325;
            SteamProjectileEntity steamProj = new SteamProjectileEntity(worldObj, this, 0.9F + MathHelper.randomFloatClamp(getRNG(), -0.2F, 0.1F));
            rot = Math.toRadians(renderYawOffset + 90F + 12F);
            steamProj.setPosition(posX + (radius * Math.cos(rot)), posY + getEyeHeight(), posZ + (radius * Math.sin(rot)));
            steamProj.motionX += MathHelper.getRandomDoubleInRange(rand, -0.2D, 0.2D);
            steamProj.motionZ += MathHelper.getRandomDoubleInRange(rand, -0.2D, 0.2D);
            worldObj.spawnEntityInWorld(steamProj);

            steamProj = new SteamProjectileEntity(worldObj, this, 0.9F + MathHelper.randomFloatClamp(getRNG(), -0.2F, 0.1F));
            rot = Math.toRadians(renderYawOffset + 90F - 12F);
            steamProj.setPosition(posX + (radius * Math.cos(rot)), posY + getEyeHeight(), posZ + (radius * Math.sin(rot)));
            steamProj.motionX += MathHelper.getRandomDoubleInRange(rand, -0.2D, 0.2D);
            steamProj.motionZ += MathHelper.getRandomDoubleInRange(rand, -0.2D, 0.2D);
            worldObj.spawnEntityInWorld(steamProj);
        }
    }
}
