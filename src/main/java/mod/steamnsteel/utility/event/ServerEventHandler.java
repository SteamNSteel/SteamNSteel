package mod.steamnsteel.utility.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import mod.steamnsteel.entity.SteamSpiderEntity;
import mod.steamnsteel.entity.Swarm;
import mod.steamnsteel.entity.SwarmManager;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraftforge.event.world.WorldEvent;

public class ServerEventHandler
{
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (!event.world.isRemote)
        {
            SwarmManager swarmManager = (SwarmManager) event.world.perWorldStorage.loadData(SwarmManager.class, "swarms");
            if (swarmManager == null)
            {
                swarmManager = new SwarmManager(event.world);
                event.world.perWorldStorage.setData("swarms", swarmManager);
            }
            else
            {
                swarmManager.setWorld(event.world);
            }
            SwarmManager.swarmManagers.put(event.world, swarmManager);
            Swarm swarm = new Swarm<SteamSpiderEntity>(event.world, ChunkCoord.of(0, 0), ChunkBlockCoord.of(0, 0, 0), SteamSpiderEntity.class);
            swarmManager.addSwarm(swarm);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        if (!event.world.isRemote)
        {
            SwarmManager.swarmManagers.remove(event.world);
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END && SwarmManager.swarmManagers.containsKey(event.world))
        {
            SwarmManager.swarmManagers.get(event.world).tick();
        }
    }
}
