package mod.steamnsteel.world.structure;

import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.world.WorldGen;
import mod.steamnsteel.world.structure.remnantruins.RuinSchematic;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.Rectangle;

public class StructureChunkGenerator
{
    private final World world;
    private final int chunkX;
    private final int chunkZ;
    private final RuinSchematic schematic;
    private final Rectangle intersection;

    public StructureChunkGenerator(World world, int chunkX, int chunkZ, RuinSchematic schematic, Rectangle intersection)
    {
        this.world = world;

        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.schematic = schematic;
        this.intersection = intersection;
    }

    public void generate()
    {
        Logger.info("Creating schematic at %d, %d", intersection.getX(), intersection.getY());

        WorldGen.schematicLoader.renderSchematicToSingleChunk(
                schematic.resource,
                world,
                intersection.getX(), 80, intersection.getY(),
                chunkX, chunkZ,
                ForgeDirection.NORTH,
                false);

    }
}
