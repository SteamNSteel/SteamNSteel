package mod.steamnsteel.api;

import mod.steamnsteel.api.steamtransport.ISteamTransportRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is fired during PostInit on the MinecraftForge.EVENT_BUS to notify dependant mods that SteamNSteel's
 * capabilities are available for use.
 */
@SuppressWarnings("unused") //This is an API class
public class SteamNSteelInitializedEvent extends Event {
    private final ISteamTransportRegistry steamTransportRegistry;

    public SteamNSteelInitializedEvent(ISteamTransportRegistry steamTransportRegistry)
    {
        this.steamTransportRegistry = steamTransportRegistry;
    }

    /**
     * Use the SteamTransportRegistry to manage locations in a dimension that have steam transports.
     * @return the SteamTransportRegistry provided by SteamNSteel.
     */
    public ISteamTransportRegistry getSteamTransportRegistry()
    {
        return steamTransportRegistry;
    }
}
