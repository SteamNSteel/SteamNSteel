package mod.steamnsteel.utility.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import mod.steamnsteel.entity.SwarmManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public class ServerEventHandler
{
    static {
        ServerEventHandler serverEventHandler = new ServerEventHandler();
        FMLCommonHandler.instance().bus().register(serverEventHandler);
        MinecraftForge.EVENT_BUS.register(serverEventHandler);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        SwarmManager swarmManager = (SwarmManager) event.world.perWorldStorage.loadData(SwarmManager.class, "swarms");
        if (swarmManager == null)
        {
            swarmManager = new SwarmManager(event.world);
        }
        else
        {
            swarmManager.setWorld(event.world);
        }
        SwarmManager.swarmManagers.put(event.world, swarmManager);
        event.world.perWorldStorage.setData("swarms", swarmManager);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        SwarmManager.swarmManagers.remove(event.world);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (SwarmManager.swarmManagers.containsKey(event.world))
        {
            SwarmManager.swarmManagers.get(event.world).tick();
        }
    }
}
