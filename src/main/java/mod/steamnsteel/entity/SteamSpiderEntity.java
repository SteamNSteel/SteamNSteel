package mod.steamnsteel.entity;

import mod.steamnsteel.entity.ai.AISwarmDefendHome;
import mod.steamnsteel.entity.ai.AISwarmOnHurt;
import mod.steamnsteel.entity.ai.AISwarmReturnHome;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SteamSpiderEntity extends EntityCreature implements ISwarmer
{
    public static final String NAME = "steamSpider";
    private Swarm swarm;

    public SteamSpiderEntity(World world)
    {
        super(world);
        //TODO Proper AI tasks
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        tasks.addTask(3, new AISwarmReturnHome<SteamSpiderEntity>(this, 32, 1.2F, true));
        //tasks.addTask(7, new EntityAIWander(this, 1.0D));
        //tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        //tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new AISwarmOnHurt<SteamSpiderEntity>(this));
        targetTasks.addTask(2, new AISwarmDefendHome<SteamSpiderEntity>(this, 16));
        setSize(0.35F, 0.8F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        //TODO attributes
        super.applyEntityAttributes();
        //getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D); //Same speed as player walking
        //getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
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
        if (worldObj.isRemote) {
            //Position of point on a circle. Used to calculate where the exit pipes are for smoke/steam
            double rot = Math.toRadians(renderYawOffset - 90F + (rand.nextBoolean() ? 12F : -12F));
            double radius = 0.27D;
            double x = posX + (radius * Math.cos(rot));
            double z = posZ + (radius * Math.sin(rot));
            Minecraft.getMinecraft().effectRenderer.addEffect(isInWater() ? new EntityBubbleFX(worldObj, x, posY + 0.61, z, 0, 0, 0) : new EntitySmokeFX(worldObj, x, posY + 0.61, z, 0, 0, 0, 0.5F));
        }
    }

    @Override
    public Swarm getSwarm()
    {
        return swarm;
    }
}
