package mod.steamnsteel.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class SpiderFactoryEntity extends EntityMob {

    public static final String NAME = "spiderFactory";
    private Swarm swarm;

    public SpiderFactoryEntity(World p_i1738_1_) {
        super(p_i1738_1_);
        setSize(0.8F, 1.5F);
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
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        setRotationAndSize(ForgeDirection.EAST);
    }

    private void setRotationAndSize(ForgeDirection dir) {
        //TODO this kinda needs to be fixed :/
        this.boundingBox.maxX = this.boundingBox.minX + (width * (dir.offsetX + 1));
        this.boundingBox.maxZ = this.boundingBox.minZ + (width * (dir.offsetZ + 1));
        this.boundingBox.maxY = this.boundingBox.minY + height;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity p_82167_1_) {}
}
