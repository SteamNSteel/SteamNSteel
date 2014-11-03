package mod.steamnsteel.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import mod.steamnsteel.proxy.Proxies;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

//NOTE: This is an invisible entity!
public class SteamProjectileEntity extends EntityThrowable implements IEntityAdditionalSpawnData
{
    public static final String NAME = "steamProjectile";
    private static final DamageSource STEAM_DAMAGE = new DamageSource("steam").setDamageBypassesArmor();

    public int maxAge = 40;

    public SteamProjectileEntity(World world)
    {
        super(world);
    }

    public SteamProjectileEntity(World world, EntityLivingBase entity, float speed)
    {
        super(world, entity);
        maxAge = (int) (40 * speed);
        motionX = -MathHelper.sin((float) (Math.toRadians(-rotationYaw)) * MathHelper.cos((float) Math.toRadians(rotationPitch))) * speed;
        motionZ = MathHelper.cos((float) (Math.toRadians(-rotationYaw)) * MathHelper.cos((float) Math.toRadians(rotationPitch))) * speed;
        motionX += entity.motionX;
        motionZ += entity.motionZ;
        motionY = 0;
    }

    @Override
    public void onUpdate()
    {
        if (ticksExisted > maxAge)
        {
            setDead();
        }

        if (worldObj.isRemote)
        {
            float lifeTime = (maxAge - ticksExisted) / (float) maxAge;
            if (worldObj.rand.nextFloat() < lifeTime)
            {
                Proxies.render.spawnParticle("steam", worldObj, posX, posY, posZ, motionX / 2F, motionY, motionZ / 2F, 0.7F);
            }
        }

        super.onUpdate();
        motionX *= 0.86F;
        motionZ *= 0.86F;
    }

    @Override
    protected float getGravityVelocity()
    {
        return -0.005F;
    }

    @Override
    protected void onImpact(MovingObjectPosition mop)
    {
        if (mop.entityHit != getThrower())
        {
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
            {
                mop.entityHit.attackEntityFrom(STEAM_DAMAGE, ((maxAge - ticksExisted) / (float) maxAge) * 1.5F);
            }
            setDead();
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(maxAge);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        maxAge = additionalData.readInt();
    }
}
