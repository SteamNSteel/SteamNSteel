package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;

//TODO add mutex bits
abstract class AISwarmBase<T extends EntityLiving & ISwarmer> extends EntityAIBase
{
    public static final TargetSelector selector = new TargetSelector();
    protected T entity;

    public AISwarmBase(T entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() {
        return !entity.isDead && entity.getSwarm() != null;
    }

    @SuppressWarnings("unchecked")
    protected List<EntityPlayer> getNearbyThreats(ChunkCoord homeChunk, int range) {
        ChunkBlockCoord homeBlockCoord = entity.getSwarm().getHomeBlockCoord();
        return entity.worldObj.selectEntitiesWithinAABB(EntityPlayer.class,
                AxisAlignedBB.getBoundingBox((homeChunk.getX() * 16) - range, homeBlockCoord.getY() - 16, (homeChunk.getZ() * 16) - range,
                        (homeChunk.getX() * 16) + 16 + range, homeBlockCoord.getY() + 16, (homeChunk.getZ() * 16) + 16 + range), selector);
    }

    private static class TargetSelector implements IEntitySelector
    {
        @Override
        public boolean isEntityApplicable(Entity entity)
        {
            EntityPlayer player = (EntityPlayer) entity;
            return !player.capabilities.isCreativeMode;
        }
    }
}
