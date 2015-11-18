package mod.steamnsteel.api.steamtransport;

/**
 * Steam transports are blocks in the world that can produce, consume or transport steam and condensation.
 *
 * Temperature dictates the rate that condensation is produced.
 *
 * Use the addSteam(double) method to produce steam and introduce it to the network.
 * Use the takeSteam(double) method to consume steam and remove it from the network.
 *
 * Steam can only be taken from the current block. Do not set the maximum steam to 0, as it will be unable to contain
 * any steam at all.
 */
@SuppressWarnings("unused") //This is an API.
public interface ISteamTransport
{
    /**
     * Attempt to add steam to the transport. If the amount of steam exceeds it's capacity, it will be filled to capacity.
     * @param unitsOfSteam The amount of steam available to add.
     * @return The amount of actual steam added.
     */
    double addSteam(double unitsOfSteam);

    /**
     * Attempt to add water to the transport. If the amount of water exceeds it's capacity, it will be filled to capacity.
     * @param unitsOfWater The amount of water available to add
     * @return The amount of actual water added.
     */
    double addCondensate(double unitsOfWater);

    /**
     * Attempts to take a desired amount of steam from a transport. If more steam is requested than is available, the
     * available amount will be provided.
     * @param desiredUnitsOfSteam The desired about of steam to be taken
     * @return the actual amount of steam taken.
     */
    double takeSteam(double desiredUnitsOfSteam);

    /**
     * Attempts to take a desired amount of condensate from a transport. If more condensate is requested than is available
     * the available amount will be provided.
     * @param desiredUnitsOfWater The desired amount of condensate to be taken
     * @return the actual amount of condensate taken.
     */
    double takeCondensate(double desiredUnitsOfWater);

    /**
     * Sets the upper bound the amount of steam that can be possibly stored in this transport.
     * @param maximumUnitsOfSteam the maximum number of units this transport can store.
     * @return The transport for fluent chaining.
     */
    ISteamTransport setMaximumSteam(double maximumUnitsOfSteam);

    /**
     * Sets the upport bound of the amount of condensate that can be stored in this transport.
     * @param maximimUnitsOfWater the maximum number of units this transport can store.
     * @return The transport for fluent chaining.
     */
    ISteamTransport setMaximumCondensate(double maximimUnitsOfWater);

    /**
     * Sets the rate of heat conduction for this transport. Heat is a function of steam density. The higher the density,
     * the hotter the pipe wants to be.
     * @param heatConductivity How quickly heat conducts. 0.0 - 1.0
     * @return The transport for fluent chaining.
     */
    ISteamTransport setHeatConductivity(double heatConductivity);

    /**
     * Used for debugging purposes for conditional breakpoints.
     */
    void toggleDebug();

    /**
     * Used for debugging to tell if this block is one that should trigger a breakpoint.
     * @return if debugging should be enabled for this block.
     */
    boolean getShouldDebug();

    /**
     * @return Gets the amount of steam that is stored
     */
    double getSteamStored();

    /**
     * @return Gets the amount of condensate that is stored.
     */
    double getCondensateStored();

    /**
     * @return The maximum amount of condensate that could possibly be stored.
     */
    double getMaximumCondensate();

    /**
     * @return The rate at which heat conducts through this transport.
     */
    double getHeatConductivity();

    /**
     * @return the maximum amount of steam that could possibly be stored.
     */
    double getMaximumSteam();

    /**
     * @return the density of the steam, taking into account the amount of steam and condensate.
     */
    double getCalculatedSteamDensity();

    /**
     * @return the maximum amount of steam that can be stored, after taking into account how much condensate is in the pipes
     */
    double getCalculatedMaximumSteam();

    /**
     * @return gets the current temperature for the pipe.
     */
    double getTemperature();

    /**
     * @return true if it's possible to transfer water or condensate above.
     */
    boolean canTransportAbove();
    /**
     * @return true if it's possible to transfer water or condensate below.
     */
    boolean canTransportBelow();
    /**
     * @return true if it's possible to transfer water or condensate north.
     */
    boolean canTransportNorth();
    /**
     * @return true if it's possible to transfer water or condensate south.
     */
    boolean canTransportSouth();
    /**
     * @return true if it's possible to transfer water or condensate east.
     */
    boolean canTransportEast();
    /**
     * @return true if it's possible to transfer water or condensate west.
     */
    boolean canTransportWest();
}