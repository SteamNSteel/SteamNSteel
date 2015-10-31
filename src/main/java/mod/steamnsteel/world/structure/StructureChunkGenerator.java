package mod.steamnsteel.world.structure;

import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.world.WorldGen;
import mod.steamnsteel.world.structure.remnantruins.Ruin;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.Rectangle;

public class StructureChunkGenerator
{
    private final World world;
    private final int chunkX;
    private final int chunkZ;
    private final Ruin ruin;
    private final Rectangle intersection;

    public StructureChunkGenerator(World world, int chunkX, int chunkZ, Ruin ruin, Rectangle intersection)
    {
        this.world = world;

        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.ruin = ruin;
        this.intersection = intersection;
    }

    public void generate()
    {

        if (!ruin.hasGenerationStarted())
        {
            Logger.info("Creating Structure (type:%s) at %d, %d", ruin.schematic.resource, intersection.getX(), intersection.getY());
            ruin.setGenerationStarted();
        }

        if (ruin.height == null) {
            Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
            ruin.height = chunk.getLowestHeight() - ruin.schematic.heightOffset;
            //TODO: Persist to world?
        }

        WorldGen.schematicLoader.renderSchematicToSingleChunk(
                ruin.schematic.resource,
                world,
                new BlockPos(intersection.getX(), ruin.height, intersection.getY()),
                chunkX, chunkZ,
                EnumFacing.NORTH,
                false);

    }
}
