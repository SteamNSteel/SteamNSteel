package mod.steamnsteel.api;

import mod.steamnsteel.api.crafting.ICraftingManager;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is fired during PostInit on the MinecraftForge.EVENT_BUS to notify dependant mods that SteamNSteel's
 * capabilities are available for use.
 */
@SuppressWarnings("unused") //This is an API class
public class SteamNSteelInitializedEvent extends Event {
    private final ICraftingManager craftingManager;

    public SteamNSteelInitializedEvent(ICraftingManager craftingManager)
    {
        this.craftingManager = craftingManager;
    }


    public ICraftingManager getCraftingManager()
    {
        return craftingManager;
    }
}