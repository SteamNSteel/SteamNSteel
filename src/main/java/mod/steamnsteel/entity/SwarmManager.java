package mod.steamnsteel.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mod.steamnsteel.utility.gson.AnnotationExlusion;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import java.util.*;

public class SwarmManager extends WorldSavedData
{
    private static final Gson gson = new GsonBuilder().setExclusionStrategies(AnnotationExlusion.INSTANCE).create();
    public static final Map<World, SwarmManager> swarmManagers = new HashMap<World, SwarmManager>();

    private World world;
    private int tickCounter;
    private final List<Swarm<? extends ISwarmer>> swarmList = new ArrayList<Swarm<? extends ISwarmer>>();

    public SwarmManager(String string)
    {
        super(string);
    }

    public SwarmManager(World world)
    {
        this("swarms");
        this.world = world;
        markDirty();
    }

    public <T extends EntityLiving & ISwarmer> void addSwarm(Swarm<T> swarm)
    {
        swarmList.add(swarm);
    }

    public void setWorld(World world)
    {
        this.world = world;
        for (Swarm swarm : swarmList)
        {
            swarm.setWorld(world);
        }
    }

    public void tick()
    {
        world.theProfiler.startSection("swarmUpdate");
        tickCounter++;
        Iterator<Swarm<? extends ISwarmer>> iterator = swarmList.iterator();
        while (iterator.hasNext())
        {
            Swarm swarm = iterator.next();
            swarm.buildSwarmEntitiesList();
            world.theProfiler.startSection("swarmX" + swarm.getHomeChunkCoord().getX() + ":Z" + swarm.getHomeChunkCoord().getZ());
            swarm.update(tickCounter);
            if (!swarm.isValid())
            {
                iterator.remove();
            }
            world.theProfiler.endSection();
        }
        world.theProfiler.endSection();

        //Mark for save every 30 seconds
        if (tickCounter % 600 == 0)
        {
            markDirty();
        }
    }

    /**
     * Returns the nearest {@link mod.steamnsteel.entity.Swarm} for the provided entity and it's class.
     * @param entity The entity
     * @param clazz The class
     * @param maxDistance Max distance the swarm can be away
     * @param <T> The type
     * @return The swarm
     */
    @SuppressWarnings("unchecked")
    public <T extends EntityLiving & ISwarmer> Swarm<T> getNearestSwarmToEntity(T entity, Class<T> clazz, int maxDistance)
    {
        Swarm<T> nearestSwarm = null;
        double distance = Float.MAX_VALUE;
        for (Swarm<? extends ISwarmer> swarm : swarmList)
        {
            //Has to exact match
            if (swarm.clazz == clazz)
            {
                ChunkCoord coord = swarm.getHomeChunkCoord();
                double dis = entity.getDistanceSq(coord.getX() + 8, swarm.getHomeBlockCoord().getY(), swarm.getHomeChunkCoord().getZ() + 8);
                if ((nearestSwarm == null || dis < distance) && dis <= maxDistance)
                {
                    nearestSwarm = (Swarm<T>) swarm;
                    distance = dis;
                }
            }
        }
        return nearestSwarm;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        //Load swarm list
        NBTTagList nbttaglist = nbtTagCompound.getTagList("Swarms", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound tagCompound = nbttaglist.getCompoundTagAt(i);
            SwarmType swarmType = SwarmType.values()[tagCompound.getInteger("SwarmType")];
            Swarm swarm = gson.fromJson(tagCompound.getString("Data"), swarmType.typeToken.getType());
            swarm.readFromNBT(tagCompound);
            addSwarm(swarm);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        //Save swarm list
        NBTTagList nbtTagList = new NBTTagList();
        for (Swarm swarm : swarmList)
        {
            if (swarm.shouldPersist())
            {
                NBTTagCompound swarmNBT = new NBTTagCompound();
                swarmNBT.setInteger("SwarmType", swarm.getSwarmType().ordinal());
                swarmNBT.setString("Data", gson.toJson(swarm));
                nbtTagList.appendTag(swarmNBT);
            }
        }
        nbtTagCompound.setTag("Swarms", nbtTagList);
    }

    public enum SwarmType
    {
        STEAMSPIDERSWARM(new TypeToken<Swarm<SteamSpiderEntity>>(){});

        public final TypeToken typeToken;

        SwarmType(TypeToken type)
        {
            this.typeToken = type;
        }
    }
}
