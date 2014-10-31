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

    /**
     * Sets the swarm for the entity
     * @param swarm The swarm
     */
    public void setSwarm(Swarm swarm);
}
