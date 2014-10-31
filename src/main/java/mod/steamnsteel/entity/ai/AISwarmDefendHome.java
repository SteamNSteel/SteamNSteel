package mod.steamnsteel.entity.ai;

import mod.steamnsteel.entity.ISwarmer;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;

public class AISwarmDefendHome<T extends EntityLiving & ISwarmer> extends AISwarmBase<T>
{
    private final int range;

    //Defends against any enemy that enters into its range. TODO should this check be in SpiderSwarm instead? Means less calls
    public AISwarmDefendHome(T entity, int range)
    {
        super(entity);
        this.range = range;
    }

    @Override
    public boolean shouldExecute()
    {
        if (super.shouldExecute() && entity.getAttackTarget() == null) {
            List<EntityPlayer> playerList = getNearbyThreats(entity.getSwarm().getHomeChunkCoord(), range);
            return (playerList != null && playerList.size() > 0);
        }
        return false;
    }

    @Override
    public void startExecuting()
    {
        List<EntityPlayer> playerList = getNearbyThreats(entity.getSwarm().getHomeChunkCoord(), range);
        if (playerList != null) {
            ChunkCoord homeChunk = entity.getSwarm().getHomeChunkCoord();
            WorldBlockCoord worldBlockCoord = homeChunk.localToWorldCoords(entity.getSwarm().getHomeBlockCoord());

            //Get closest player. TODO should we have better threat determining? Look in Swarm
            EntityPlayer closestPlayer = null;
            double closestDistance = 0;
            for (EntityPlayer player : playerList) {
                double dis = player.getDistance(worldBlockCoord.getX(), worldBlockCoord.getY(), worldBlockCoord.getZ());
                if (closestPlayer == null || dis < closestDistance)
                {
                    closestPlayer = player;
                    closestDistance = dis;
                }
            }
            if (closestPlayer != null)
            {
                entity.getSwarm().setAttackTarget(closestPlayer);
            }
        }
        super.startExecuting();
    }
}
