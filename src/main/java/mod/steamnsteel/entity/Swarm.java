package mod.steamnsteel.entity;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

//TODO saving/serialising
public class Swarm<T extends EntityLiving & ISwarmer>
{
    private World world;
    //private WeakReference<Object> spiderFactory; //TODO implement
    private final Multiset<String> threatCount = HashMultiset.create();
    private final Set<T> swarmerEntities = Collections.newSetFromMap(new WeakHashMap<T, Boolean>());
    private final ChunkCoord homeChunkCoord;
    private final ChunkBlockCoord homeBlockCoord; //This could be replaced with the spiderFactory in the future
    private ChunkCoord currPosition;

    public Swarm(World world, ChunkCoord homeChunkCoord, ChunkBlockCoord homeBlockCoord)
    {
        this.world = world;
        this.homeChunkCoord = homeChunkCoord;
        this.homeBlockCoord = homeBlockCoord;
        this.currPosition = homeChunkCoord;
    }

    public void update()
    {

        @SuppressWarnings("unchecked")
        List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(
                homeChunkCoord.getX(), homeBlockCoord.getY() - 16, homeChunkCoord.getZ(), homeChunkCoord.getX() + 16,
                homeBlockCoord.getY() + 16, homeChunkCoord.getZ() + 16));

        if (playerList != null && playerList.size() > 0)
        {
            HashMultiset<String> playersToAdd = HashMultiset.create();
            //Increase threat count for players in home radius
            for (EntityPlayer player : playerList) {
                playersToAdd.add(player.getCommandSenderName());
                changeThreatCount(player.getCommandSenderName(), 1);
            }
            //Decrease threat count for players no longer in home area
            for (String playerName : threatCount.elementSet()) {
                if (!playersToAdd.contains(playerName))
                {
                    threatCount.remove(playerName);
                }
            }
        }
    }

    public void setWorld(World world)
    {
        this.world = world;
    }

    public void setAttackTarget(EntityLivingBase entityLiving)
    {
        for (T entity : swarmerEntities)
        {
            entity.setAttackTarget(entityLiving);
        }
    }

    //TODO implement
    private EntityPlayer findNearestTarget(boolean ignoreThreat)
    {
        if (currPosition == null)
        {
            determineCurrentPosition();
        }
        //world.getEntitiesWithinAABBExcludingEntity(EntityPlayer.class, AxisAlignedBB.getBoundingBox())
        return null;
    }

    /**
     * This loops through the entities that are a part of this swarm and tried to determine a {@link mod.steamnsteel.utility.position.ChunkCoord}
     * for them based on where the majority of the swarm is.
     */
    private void determineCurrentPosition()
    {
        Multiset<ChunkCoord> chunkCoords = HashMultiset.create();
        for (T entity : swarmerEntities)
        {
            chunkCoords.add(ChunkCoord.of(MathHelper.floor_double(entity.posX) >> 4, MathHelper.floor_double(entity.posY) >> 4));
        }
        currPosition = Multisets.copyHighestCountFirst(chunkCoords).iterator().next(); //Little trick to get the highest count
    }

    public ChunkCoord getHomeChunkCoord()
    {
        return homeChunkCoord;
    }

    public ChunkBlockCoord getHomeBlockCoord()
    {
        return homeBlockCoord;
    }

    /**
     * Changes the threat count for the players name. Min threat is 0, max is 100. Value is clamped between 0 and 100 so if a
     * larger value is passed in, it is still capped.
     * Threat may be used by {@link mod.steamnsteel.entity.ISwarmer}'s when finding or determining if a certain player
     * should be attacked over another one. By default, threat is decreased by 1 every tick as long as said isn't in the
     * Swarm's home (this is done in {@link #update()}). Similarly, threat is increased by 1 as long as the player is in
     * the home region
     * @param name The players name
     * @param valueChange The value change
     */
    public void changeThreatCount(String name, int valueChange)
    {
        int newValue = MathHelper.clamp_int(getThreatValue(name) + valueChange, 0, 100);
        threatCount.setCount(name, valueChange);
    }

    public int getThreatValue(String name)
    {
        if (threatCount.contains(name))
        {
            return threatCount.count(name);
        }
        return 0;
    }

    /**
     * Checks if this current swarm is valid and if not, return null. By default checks if {@link #swarmerEntities} is not
     * empty.
     * @return If swarm is valid
     */
    public boolean isValid()
    {
        return swarmerEntities.size() > 0;
    }
}
