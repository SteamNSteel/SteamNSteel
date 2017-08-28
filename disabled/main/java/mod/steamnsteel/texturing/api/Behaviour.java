package mod.steamnsteel.texturing.api;

/**
 * Defines what will happen across layers when two features co-exist.
 */
public enum Behaviour
{
    /**
     * Declares that a feature will replace an existing feature
     */
    REPLACES,
    /**
     * Declares that two features can co-exist
     */
    COEXIST,
    /**
     * Declares that a feature cannot exist with another feature
     */
    CANNOT_EXIST
}
