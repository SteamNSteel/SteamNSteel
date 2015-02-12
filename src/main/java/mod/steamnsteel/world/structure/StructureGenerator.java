package mod.steamnsteel.world.structure;

import net.minecraft.world.World;

public abstract class StructureGenerator
{
    public abstract StructureChunkGenerator getStructureChunkToGenerate(World world, int chunkX, int chunkZ);
}
