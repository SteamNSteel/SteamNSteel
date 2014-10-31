package mod.steamnsteel.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwarmManager extends WorldSavedData
{
    public static final Map<World, SwarmManager> swarmManagers = new HashMap<World, SwarmManager>();
    private World world;
    private int tickCounter;
    private final List<Swarm> swarmList = new ArrayList<Swarm>();

    public SwarmManager()
    {
        super("swarms");
    }

    public SwarmManager(World world)
    {
        this();
        this.world = world;
        markDirty();
    }

    public void addSwarm(Swarm swarm)
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
        for (Swarm swarm : swarmList)
        {
            world.theProfiler.startSection("swarmX" + swarm.getHomeChunkCoord().getX() + ":Z" + swarm.getHomeChunkCoord().getZ());
            swarm.update();
            if (swarm.isValid())
            {
                swarmList.remove(swarm);
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

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {

    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {

    }
}
