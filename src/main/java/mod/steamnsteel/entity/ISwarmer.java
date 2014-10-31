package mod.steamnsteel.entity;

/**
 * Implemented by any entity that is part of a swarm
 */
public interface ISwarmer
{
    /**
     * Gets the swarm for the entity
     * @return The swarm
     */
    public Swarm getSwarm();
}
