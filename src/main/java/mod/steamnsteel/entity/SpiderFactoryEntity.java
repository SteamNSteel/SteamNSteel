package mod.steamnsteel.entity;

import mod.steamnsteel.factory.IFactoryEntity;
import mod.steamnsteel.utility.NBTHelper;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class SpiderFactoryEntity extends EntityMob implements IFactoryEntity {

    public static final String NAME = "spiderFactory";
    private Swarm swarm;
    private WorldBlockCoord masterLocation;

    public SpiderFactoryEntity(World p_i1738_1_) {
        super(p_i1738_1_);
        setSize(1.36F, 1.32F);
        setRotationAndSize(ForgeDirection.EAST);
    }

    @Override
    protected void applyEntityAttributes()
    {
        //TODO attributes
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0D);
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(10000000D);
    }

    @Override
    protected boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public void onUpdate() {

        super.onUpdate();
        this.posX = this.prevPosX;
        this.posZ = this.prevPosZ;
        this.posY = this.prevPosY;
        //this.boundingBox.minZ = this.posZ;
        //this.boundingBox.maxZ = this.boundingBox.minZ + 1.4f;
        //setSize(1.0f, 1.3f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        //setRotationAndSize(ForgeDirection.EAST);
    }

    private void setRotationAndSize(ForgeDirection dir) {
        //TODO this kinda needs to be fixed :/
/*        this.boundingBox.maxX = this.boundingBox.minX + (width * (dir.offsetX + 1));
        this.boundingBox.maxZ = this.boundingBox.minZ + (width * (dir.offsetZ + 1));
        this.boundingBox.maxY = this.boundingBox.minY + height;*/
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    protected void collideWithEntity(Entity entity)
    {
        super.collideWithEntity(entity);
    }

    @Override
    public void addVelocity(double p_70024_1_, double p_70024_3_, double p_70024_5_)
    {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);

        masterLocation = NBTHelper.readWorldBlockCoord(nbt, "masterBlockLocation");
        Logger.info("Reading %s", nbt);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);

        NBTHelper.writeWorldBlockCoord(nbt, "masterBlockLocation", masterLocation);
        Logger.info("Writing %s", nbt);
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return super.attackEntityFrom(p_70097_1_, p_70097_2_);
    }

    public void setMasterBlockLocation(WorldBlockCoord worldBlockCoord) {
        masterLocation = worldBlockCoord;
    }

    @Override
    public void onDeath(DamageSource p_70645_1_) {
        super.onDeath(p_70645_1_);

        if (!worldObj.isRemote) {
            masterLocation.setBlock(worldObj, Blocks.air, 0, 3);
            boolean flag = worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            flag = false;
            worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 2.0f, flag);
        }

        worldObj.removeEntity(this);
    }
}
