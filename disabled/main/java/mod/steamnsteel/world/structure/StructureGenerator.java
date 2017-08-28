package mod.steamnsteel.world.structure;

import net.minecraft.world.World;
import java.util.Optional;

public abstract class StructureGenerator
{
    public abstract Optional<StructureChunkGenerator> getStructureChunkToGenerate(World world, int chunkX, int chunkZ);
}
