package mod.steamnsteel.world;

import com.google.common.collect.ImmutableSet;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.utility.position.BlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.EnumSet;
import java.util.Random;

public class SulfurOreGenerator extends OreGenerator
{
    // Highest "Y" to begin vein
    // 0 - 16 should encompass most naturally occurring lava
    private static final int MAX_HEIGHT = 16;

    // Number of times to attempt new vein (isPotentialVein() insures that most attempts will fail)
    private static final int NUM_ITERATIONS = 128;

    // Average length of vein in blocks (+/- 50%)
    private static final int NUM_BLOCKS_IN_VEIN = 16;
    private static final int NUM_BLOCKS_IN_VEIN_VARIATION = NUM_BLOCKS_IN_VEIN / 2;

    private static final ImmutableSet<ForgeDirection> BRANCH_DIRECTIONS = ImmutableSet.copyOf(EnumSet.of(
            ForgeDirection.UP,
            ForgeDirection.NORTH,
            ForgeDirection.SOUTH,
            ForgeDirection.WEST,
            ForgeDirection.EAST));

    private static void genOreVein(World world, Random rand, BlockCoord coord)
    {
        final int veinSize = NUM_BLOCKS_IN_VEIN + rand.nextInt(NUM_BLOCKS_IN_VEIN) - NUM_BLOCKS_IN_VEIN_VARIATION;

        BlockCoord target = coord;
        for (int blockCount = 0; blockCount < veinSize; blockCount++)
        {
            if (isBlockReplaceable(world, target))
                placeSulfurOre(world, target);

            final ForgeDirection offsetToNext = ForgeDirection.getOrientation(rand.nextInt(6));
            target = target.offset(offsetToNext);

            // Has vein strayed into an unloaded chunk? If so, STOP!
            if (!doesBlockExist(world, target)) return;
        }
    }

    private static boolean doesBlockExist(World world, BlockCoord coord)
    {
        return world.blockExists(coord.getX(), coord.getY(), coord.getZ());
    }

    private static Block getBlock(World world, BlockCoord coord)
    {
        return world.getBlock(coord.getX(), coord.getY(), coord.getZ());
    }

    private static boolean isAirBlock(World world, Block block, BlockCoord coord)
    {
        return block.isAir(world, coord.getX(), coord.getY(), coord.getZ());
    }

    private static boolean isBlockAiriJustAboveLava(World world, BlockCoord coord)
    {
        BlockCoord target = coord;
        for (int i = 0; i < 4; i++)
        {
            if (target.getY() >= 0)
            {
                final Block block = getBlock(world, target);
                if (block.getMaterial().equals(Material.lava))
                    return true;

                if (!isAirBlock(world, block, target))
                {
                    return false;
                }

                target = target.offset(ForgeDirection.DOWN);
            }
        }

        return false;
    }

    private static boolean isBlockLavaNeighbor(World world, BlockCoord coord)
    {
        for (final ForgeDirection offset : BRANCH_DIRECTIONS)
        {
            final BlockCoord target = coord.offset(offset);
            if (doesBlockExist(world, target))
                if (getBlock(world, coord).getMaterial().equals(Material.lava))
                    return true;
        }
        return false;
    }

    private static boolean isBlockReplaceable(World world, BlockCoord coord)
    {
        final Block block = getBlock(world, coord);

        final int x = coord.getX();
        final int y = coord.getY();
        final int z = coord.getZ();

        return block.isReplaceableOreGen(world, x, y, z, Blocks.stone)
                || block.isReplaceableOreGen(world, x, y, z, Blocks.dirt);
    }

    private static boolean isPotentialVein(World world, BlockCoord coord)
    {
        return isBlockLavaNeighbor(world, coord) || isBlockAiriJustAboveLava(world, coord);
    }

    private static void placeSulfurOre(World world, BlockCoord coord)
    {
        world.setBlock(coord.getX(), coord.getY(), coord.getZ(), ModBlock.oreSulfur, 0, 2);
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z)
    {
        if (ModBlock.oreSulfur.isGenEnabled())
        {
            for (int iteration = 0; iteration < NUM_ITERATIONS; iteration++)
            {
                // Pick a "Y"
                // 66% will try to place in underground lava range and 33% will try range for lava lakes
                final int yGen = rand.nextInt(3) < 2 ? rand.nextInt(MAX_HEIGHT + 1) :  rand.nextInt(248) + 8;

                final BlockCoord coordBlock = BlockCoord.of(x + rand.nextInt(16), yGen, z + rand.nextInt(16));
                if (isPotentialVein(world, coordBlock))
                    genOreVein(world, rand, coordBlock);
            }

            RetroGenHandler.markChunk(ChunkCoord.of(x >> 4, z >> 4));
            return true;
        }
        return false;
    }
}
