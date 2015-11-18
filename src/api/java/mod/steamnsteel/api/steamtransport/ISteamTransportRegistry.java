package mod.steamnsteel.api.steamtransport;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * The ISteamTransportRegistry is used for creating ISteamTransports for use in Tile Entities.
 *
 * When registering a location in the world as having being a SteamTransport, you must specify which directions can
 * be connected to. For example, a boiler may only connect on the UP vector as that's where steam comes out.
 */
@SuppressWarnings("unused") //This is an API class
public interface ISteamTransportRegistry
{
    /**
     * Registers a location in a dimension as having a SteamTransport present there.
     * @param pos The location in the dimension
     * @param dimensionId The dimension the SteamTransport exists in
     * @param forgeDirection The directions that are valid to connect to.
     * @return the SteamTransport at that location.
     */
    ISteamTransport registerSteamTransport(BlockPos pos, int dimensionId, EnumFacing[] forgeDirection);

    /**
     * Marks a position in a dimension as no longer having a SteamTransport at that location
     * @param pos The location in the dimension
     * @param dimensionId The dimension the SteamTransport used to exist in.
     */
    void destroySteamTransport(BlockPos pos, int dimensionId);
}
