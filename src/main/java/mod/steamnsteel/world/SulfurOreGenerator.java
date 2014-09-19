package mod.steamnsteel.world;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import mod.steamnsteel.library.ModBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import java.util.*;

/**
 * Creates a set of sulfur around lava.
 * <p/>
 * It does this by mapping out the blocks in a chunk that are close to lava by examining the neighbours of "replaceable"
 * blocks like Stone and dirt. If a stone block has air above it, or has lava below it, it is determined to be a
 * potential starting point for a cluster of sulfur.
 * <p/>
 * We score the blocks by a heat rating. The more lava it had adjacent to it, the "Hotter" it is. If a block has lava on
 * one side, but not on the other, the blocks that do not have lava receive a low heat rating, making them still
 * eligible to be replaced with sulfur.
 * <p/>
 * Blocks with a higher heat rating are more likely to be starting points, and have sulfur generate there.
 * <p/>
 * The sulfur will find a suitable starting point and spread out from that block, processing one block and it's
 * neighbours at a time, in order of their heat rating.
 * <p/>
 * When doing the initial search for replaceable blocks, we'll ignore the chunk borders as they ruin determinism.
 */
public class SulfurOreGenerator extends OreGenerator
{
    private static final float HEAT_SCORE_CHANCE_DENOMINATOR = 8.0f;
    private static final int DISTANCE_BETWEEN_CLUSTERS = 10;
    private static final int CHUNK_WIDTH = 16;
    private static final int CHUNK_DEPTH = 16;
    private static final int[][] neighbours = new int[26][3];
    private static final int[] neighbourOffsets = new int[26];
    private final int depthShift;
    private final double logTwo = StrictMath.log(2);
    private final int[] potentialNeighbourToAdd = new int[26];
    private final int[] createdClusters;
    private final int blocksPerCluster;
    private int worldHeight;
    private int worldHeightShift;
    private int[] potentialStartingPoints = {};
    private int potentialStartingPointIndex;
    private int startingPointMask;

    public SulfurOreGenerator()
    {
        super(ModBlock.oreSulfur, 10, 12, 0, 64);
        blocksPerCluster = 12;
        //noinspection NumericCastThatLosesPrecision
        depthShift = (int) (StrictMath.log(CHUNK_DEPTH) / logTwo);
        createdClusters = new int[10];
    }

    @Override
    public boolean generate(World world, Random random, int worldX, int unusedY, int worldZ)
    {
        if (!getBlock().isGenEnabled())
        {
            return false;
        }
        //long startTime = System.currentTimeMillis();

        int height = world.getActualHeight();
        final int maxHeight = getMaxHeight();
        if (height >= maxHeight)
        {
            height = maxHeight;
        }

        //Rounds the world actual height to the nearest base 2 for safety.
        //noinspection NumericCastThatLosesPrecision,UnnecessaryExplicitNumericCast
        height = 1 << (int) (StrictMath.log(height - 1) / logTwo + 1);

        //If we are generating in a world with a different height since the last time this was called, we'll need to
        //reallocate our arrays.
        if (height != worldHeight)
        {
            //noinspection NumericCastThatLosesPrecision
            worldHeightShift = (int) (StrictMath.log(height) / logTwo);
            worldHeight = height;
            calculateNeighbourOffsets();
            potentialStartingPoints = new int[CHUNK_WIDTH * CHUNK_DEPTH * worldHeight];
            startingPointMask = (1 << worldHeightShift + depthShift + depthShift) - 1;
        }
        potentialStartingPointIndex = 0;

        //We need this reallocated ever time unfortunately, the 0s matter.
        final int[] heatScoreMap = new int[CHUNK_WIDTH * CHUNK_DEPTH * worldHeight];

        for (int x = 1; x < 15; ++x)
        {
            final int worldBlockX = worldX + x;
            for (int z = 1; z < 15; ++z)
            {
                final int worldBlockZ = worldZ + z;
                for (int y = getMinHeight(); y < maxHeight; ++y)
                {
                    examineBlock(world, worldBlockX, y, worldBlockZ, heatScoreMap);
                }
            }
        }

        //Distance Squared of the desired distance between clusters in blocks.
        final double minDistanceBetweenClusters = StrictMath.pow(DISTANCE_BETWEEN_CLUSTERS, 2);

        //Sort the potential starting points that we've collected.
        Arrays.sort(potentialStartingPoints, 0, potentialStartingPointIndex);
        int createdClusterCount = 0;

        for (final int complexStartingPoint : potentialStartingPoints)
        {
            boolean clusterAllowed = true;
            //Strip off the heat score, we don't need it at this point
            final int startingPoint = complexStartingPoint & startingPointMask;

            //recalculate the XYZ
            final int posX = startingPoint >> worldHeightShift + depthShift & CHUNK_WIDTH - 1;
            final int posY = startingPoint & worldHeight - 1;
            final int posZ = startingPoint >> worldHeightShift & CHUNK_DEPTH - 1;

            //Ensure that we're not too close to another cluster.
            for (int i = 0; i < createdClusterCount && clusterAllowed; i++)
            {
                final int otherClusterPosition = createdClusters[i];
                final int otherPosX = otherClusterPosition >> worldHeightShift + depthShift & CHUNK_WIDTH - 1;
                final int otherPosY = otherClusterPosition & worldHeight - 1;
                final int otherPosZ = otherClusterPosition >> worldHeightShift & CHUNK_DEPTH - 1;

                final double distance = StrictMath.pow(posX - otherPosX, 2) +
                        StrictMath.pow(posY - otherPosY, 2) +
                        StrictMath.pow(posZ - otherPosZ, 2);

                if (distance < minDistanceBetweenClusters)
                {
                    clusterAllowed = false;
                }
            }

            final int heatScore = heatScoreMap[startingPoint];
            final float chance = heatScore / HEAT_SCORE_CHANCE_DENOMINATOR;
            if (clusterAllowed && chance > random.nextFloat())
            {
                //Logger.info("Start Point (%d, %d, %d) HeatScore %d", posX, posY, posZ, heatScore);
                createCluster(world, random, worldX, worldZ, posX, posY, posZ, heatScoreMap);
                createdClusters[createdClusterCount] = startingPoint;
                createdClusterCount++;

                if (createdClusterCount >= getClusterCount())
                {
                    break;
                }
            }
        }

        //long stopTime = System.currentTimeMillis();
        //long runTime = stopTime - startTime;
        //Logger.info("Run time: " + runTime);
        return true;
    }

    /**
     * Calculates an array that can be used to navigate a 1D array as if it were actually 3D.
     */
    private void calculateNeighbourOffsets()
    {
        int pos = 0;
        final int xOffset = CHUNK_DEPTH * worldHeight;
        final int yOffset = 1;
        final int zOffset = worldHeight;
        for (int z = -zOffset; z <= zOffset; z += zOffset)
        {
            for (int y = -yOffset; y <= yOffset; y += yOffset)
            {
                for (int x = -xOffset; x <= xOffset; x += xOffset)
                {
                    if (x == 0 && y == 0 && z == 0)
                    {
                        continue;
                    }
                    neighbourOffsets[pos] = x + y + z;
                    pos++;
                }
            }
        }
    }

    private void createCluster(World world, Random random, int worldBlockX, int worldBlockZ, int chunkBlockX, int chunkBlockY, int chunkBlockZ, int[] interestingBlocks)
    {
        final int maxBlocksInThisCluster = random.nextInt(blocksPerCluster);
        final Queue<Integer> blocksToProcess = new LinkedList<Integer>();
        final Set<Integer> processedBlocks = Sets.newHashSet();

        final int startPosition = CHUNK_DEPTH * worldHeight * chunkBlockX + worldHeight * chunkBlockZ + chunkBlockY;

        //Ensure that the chance of a block spawning here is 100%
        interestingBlocks[startPosition] = Integer.MAX_VALUE;
        blocksToProcess.add(startPosition);
        int blocksAdded = 0;

        while (blocksAdded < maxBlocksInThisCluster && !blocksToProcess.isEmpty())
        {
            //Get the next block from the queue
            final int blockIndex = blocksToProcess.poll();
            final float chance = interestingBlocks[blockIndex] / 8.0f;
            if (chance > random.nextFloat())
            {
                //Convert the block index back to it's XYZ components
                final int posX = blockIndex >> worldHeightShift + depthShift & CHUNK_WIDTH - 1;
                final int posY = blockIndex & worldHeight - 1;
                final int posZ = blockIndex >> worldHeightShift & CHUNK_DEPTH - 1;

                //Change the block to sulfur
                world.setBlock(posX + worldBlockX, posY, posZ + worldBlockZ, getBlock(), 0, 0);
                //Mark it as processed so we don't end up in loops.
                processedBlocks.add(blockIndex);
                blocksAdded++;

                //For any neighbour that has a calculated heat score, give it a chance to be part of the cluster.
                for (int i = 0; i < neighbours.length; i++)
                {
                    final int[] neighbour = neighbours[i];
                    //But do not cross the boundary into other chunks.
                    if (posX + neighbour[0] < 0 || posX + neighbour[0] > CHUNK_WIDTH - 1)
                    {
                        continue;
                    }
                    if (posY + neighbour[1] < 0 || posY + neighbour[1] >= getMaxHeight())
                    {
                        continue;
                    }
                    if (posZ + neighbour[2] < 0 || posZ + neighbour[2] > CHUNK_DEPTH - 1)
                    {
                        continue;
                    }

                    final int neighbourPos = blockIndex + neighbourOffsets[i];
                    final int neighbourHeatScore = interestingBlocks[neighbourPos];

                    if (neighbourHeatScore != 0 && !processedBlocks.contains(neighbourPos))
                    {
                        blocksToProcess.add(neighbourPos);
                    }
                }
            }
        }
    }

    @SuppressWarnings("ObjectEquality")
    private void examineBlock(World world, int worldBlockX, int worldBlockY, int worldBlockZ, int[] heatScoreMap)
    {
        final int worldX = worldBlockX >> 4;
        final int worldZ = worldBlockZ >> 4;

        //grab the chunk. We don't need the extra things that world provides
        final Chunk chunk = world.getChunkFromBlockCoords(worldBlockX, worldBlockZ);

        final int chunkBlockX = worldBlockX & CHUNK_WIDTH - 1;
        final int chunkBlockZ = worldBlockZ & CHUNK_DEPTH - 1;
        final Block worldBlock = chunk.getBlock(chunkBlockX, worldBlockY, chunkBlockZ);

        final int blockIndex = CHUNK_DEPTH * worldHeight * chunkBlockX + worldHeight * chunkBlockZ + worldBlockY;

        if (worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.stone) ||
                worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.dirt))
        {
            int heatScore = 0;
            //Calculate the heat score, based on it's neighbours
            int potentialNeighbourToAddCount = 0;
            for (int i = 0; i < neighbours.length; i++)
            {
                final int[] neighbour = neighbours[i];
                final int neighbourIndexOffset = neighbourOffsets[i];
                final int neighbourIndex = blockIndex + neighbourIndexOffset;
                final int neighbourWorldX = worldBlockX + neighbour[0];
                final int neighbourWorldY = worldBlockY + neighbour[1];
                final int neighbourWorldZ = worldBlockZ + neighbour[2];

                if (neighbourWorldY < 0 || neighbourWorldY >= getMaxHeight())
                {
                    continue;
                }

                //Don't inspect neighbours in chunks that haven't been created - it causes Already Decorating!! exception
                if (neighbourWorldX >> 4 != worldX || neighbourWorldZ >> 4 != worldZ)
                {
                    //Logger.warning("wtf, encountered a block that wasn't in this chunk. I thought I'd prevented that.");
                    continue;
                }

                final Block neighbourBlock = chunk.getBlock(
                        neighbourWorldX & CHUNK_WIDTH - 1,
                        neighbourWorldY,
                        neighbourWorldZ & CHUNK_DEPTH - 1);

                //If we've found a block neighbouring Lava, increase this block's heat score
                if (neighbourBlock == Blocks.lava || neighbourBlock == Blocks.flowing_lava)
                {
                    heatScore += 2;
                    //Otherwise, if the neighbour is replaceable, it's eligible to be replaced as part of a cluster, assuming
                    //that the current block is deemed to be an appropriate start location.
                } else if (worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.stone) ||
                        worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.dirt))
                {
                    potentialNeighbourToAdd[potentialNeighbourToAddCount] = neighbourIndex;
                    potentialNeighbourToAddCount++;
                }
            }

            //If we've found a block with a heat score
            //A heat score of 2 or higher means that we're adjacent to lava.
            if (heatScore > 0)
            {
                //stash the heat score in the heat map
                heatScoreMap[blockIndex] = heatScore;
                final Block blockAbove = chunk.getBlock(chunkBlockX, worldBlockY + 1, chunkBlockZ);
                //heatScore should never be higher than 54, so we can pack that into 6 bits (64)
                boolean addPotentialStartingPoint = false;
                if (blockAbove == Blocks.air)
                {
                    addPotentialStartingPoint = true;
                } else if (worldBlockY > 0)
                {
                    //We'll also allow starting points to be blocks that are above lava.
                    final Block blockBelow = chunk.getBlock(chunkBlockX, worldBlockY - 1, chunkBlockZ);
                    if (blockBelow == Blocks.lava || blockBelow == Blocks.flowing_lava)
                    {
                        //The heat score of these blocks tends to be abnormally high, usually because they are
                        //potentially surrounded by more blocks, they're also harder to see, so we'll limit the heat
                        //score in these instances.
                        if (heatScore > 5)
                        {
                            heatScoreMap[blockIndex] = 5;
                        }
                        addPotentialStartingPoint = true;
                    }
                }
                if (addPotentialStartingPoint)
                {
                    //We can avoid using a SortedSet by bit-packing the heat score with the blockIndex.
                    //By packing the heat into the higher order bits allows us to sort on them, but for that to work
                    //we need to order the heat descending, we can pack it into 6 bits if we subtract it from 64
                    //Then we just need to mask out the heat score.
                    final int encodedStartPoint = blockIndex | 64 - heatScoreMap[blockIndex] << worldHeightShift + depthShift + depthShift;
                    //Logger.info("Adding potential starting point (%d, %d, %d) index %d, heat score of %d, encoded=%d", chunkBlockX, worldBlockY, chunkBlockZ, blockIndex, heatScoreMap[blockIndex], encodedStartPoint);
                    potentialStartingPoints[potentialStartingPointIndex] = encodedStartPoint;
                    potentialStartingPointIndex++;
                }

                //Add all the potential neighbours to the heat map with a heat value of 1 if they don't already have a
                //higher value.
                for (int i = 0; i < potentialNeighbourToAddCount; i++)
                {
                    final int neighbour = potentialNeighbourToAdd[i];
                    final int neighbourHeat = heatScoreMap[neighbour];
                    if (neighbourHeat == 0)
                    {
                        heatScoreMap[neighbour] = 1;
                    }
                }
            }
        }
    }

    static
    {
        int pos = 0;
        for (int z = -1; z <= 1; ++z)
        {
            for (int y = -1; y <= 1; ++y)
            {
                for (int x = -1; x <= 1; ++x)
                {
                    if (x == 0 && y == 0 && z == 0)
                    {
                        continue;
                    }

                    neighbours[pos][0] = x;
                    neighbours[pos][1] = y;
                    neighbours[pos][2] = z;

                    ++pos;
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("worldHeight", worldHeight)
                .add("worldHeightShift", worldHeightShift)
                .add("depthShift", depthShift)
                .add("logTwo", logTwo)
                .add("potentialStartingPoints", potentialStartingPoints)
                .add("potentialStartingPointIndex", potentialStartingPointIndex)
                .add("potentialNeighbourToAdd", potentialNeighbourToAdd)
                .add("createdClusters", createdClusters)
                .add("startingPointMask", startingPointMask)
                .add("blocksPerCluster", blocksPerCluster)
                .toString();
    }
}
