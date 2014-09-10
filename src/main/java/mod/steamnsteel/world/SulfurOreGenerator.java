package mod.steamnsteel.world;

import mod.steamnsteel.block.SteamNSteelOreBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.*;

/**
 * Creates a set of sulfur around lava.
 *
 * It does this by mapping out the blocks in a chunk that are close to lava by examining the neighbours of "replaceable"
 * blocks like Stone and dirt. If a stone block has air above it, or has lava below it, it is determined to be a
 * potential starting point for a cluster of sulfur.
 *
 * We score the blocks by a heat rating. The more lava it had adjacent to it, the "Hotter" it is. If a block has lava on
 * one side, but not on the other, the blocks that do not have lava receive a low heat rating, making them still
 * eligible to be replaced with sulfur.
 *
 * Blocks with a higher heat rating are more likely to be starting points, and have sulfur generate there.
 *
 * The sulfur will find a suitable starting point and spread out from that block, processing one block and it's
 * neighbours at a time, in order of their heat rating.
 *
 * When doing the initial search for replaceable blocks, we'll ignore the chunk borders as they ruin determinism.
 */
public class SulfurOreGenerator extends OreGenerator {
	private static final float HEAT_SCORE_CHANCE_DENOMINATOR = 8.0f;
	private static final int DISTANCE_BETWEEN_CLUSTERS = 10;
	private static final int CHUNK_WIDTH = 16;
	private static final int CHUNK_DEPTH = 16;

	private int worldHeight;
	private int worldHeightShift;
	private int depthShift;
	static final int[][] neighbours = new int[26][3];
	static final int[] neighbourOffsets = new int[26];
	private final double logTwo = Math.log(2);
	private int[] potentialStartingPoints;
	private int potentialStartingPointIndex;

	private int[] potentialNeighbourToAdd = new int[26];

	private int[] createdClusters;

	private int startingPointMask;

	public SulfurOreGenerator(SteamNSteelOreBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight) {
		super(block, clusterCount, blocksPerCluster, minHeight, maxHeight);
		depthShift = (int) (Math.log(CHUNK_DEPTH) / logTwo);
		createdClusters = new int[clusterCount];
	}

	@Override
	public boolean generate(World world, Random random, int worldX, int unusedY, int worldZ) {
		if (!block.isGenEnabled()) {
			return false;
		}
		//long startTime = System.currentTimeMillis();

		int height = world.getActualHeight();
		if (height >= maxHeight) {
			height = maxHeight;
		}

		//Rounds the world actual height to the nearest base 2 for safety.
		height = (1 << ((int) ((Math.log(height - 1) / logTwo) + 1)));

		//If we are generating in a world with a different height since the last time this was called, we'll need to
		//reallocate our arrays.
		if (height != worldHeight) {
			worldHeightShift = (int) (Math.log(height) / logTwo);
			worldHeight = height;
			calculateNeighbourOffsets();
			potentialStartingPoints = new int[CHUNK_WIDTH * CHUNK_DEPTH * worldHeight];
			startingPointMask = (1 << (worldHeightShift + depthShift + depthShift)) - 1;
		}
		potentialStartingPointIndex = 0;

		//We need this reallocated ever time unfortunately, the 0s matter.
		int[] heatScoreMap = new int[CHUNK_WIDTH * CHUNK_DEPTH * worldHeight];

		for (int x = 1; x < 15; ++x) {
			int worldBlockX = worldX + x;
			for (int z = 1; z < 15; ++z) {
				int worldBlockZ = worldZ + z;
				for (int y = minHeight; y < maxHeight; ++y) {
					checkBlock(world, worldBlockX, y, worldBlockZ, heatScoreMap);
				}
			}
		}

		//Distance Squared of the desired distance between clusters in blocks.
		double minDistanceBetweenClusters = Math.pow(DISTANCE_BETWEEN_CLUSTERS, 2);

		//Sort the potential starting points that we've collected.
		Arrays.sort(potentialStartingPoints, 0, potentialStartingPointIndex);
		int createdClusterCount = 0;

		for (int complexStartingPoint : potentialStartingPoints) {
			boolean clusterAllowed = true;
			//Strip off the heat score, we don't need it at this point
			int startingPoint = complexStartingPoint & startingPointMask;

			//recalculate the XYZ
			int posX = startingPoint >> (worldHeightShift + depthShift) & (CHUNK_WIDTH - 1);
			int posY = startingPoint & (worldHeight - 1);
			int posZ = startingPoint >> worldHeightShift & (CHUNK_DEPTH - 1);

			//Ensure that we're not too close to another cluster.
			for (int i = 0; i < createdClusterCount; i++) {
				int otherClusterPosition = createdClusters[i];
				int otherPosX = otherClusterPosition >> (worldHeightShift + depthShift) & (CHUNK_WIDTH - 1);
				int otherPosY = otherClusterPosition & (worldHeight - 1);
				int otherPosZ = otherClusterPosition >> worldHeightShift & (CHUNK_DEPTH - 1);

				double distance = Math.pow(posX - otherPosX, 2) +
						Math.pow(posY - otherPosY, 2) +
						Math.pow(posZ - otherPosZ, 2);

				if (distance < minDistanceBetweenClusters) {
					clusterAllowed = false;
					break;
				}
			}


			int heatScore = heatScoreMap[startingPoint];
			float chance = heatScore / HEAT_SCORE_CHANCE_DENOMINATOR;
			if (clusterAllowed && chance > random.nextFloat()) {
				//Logger.info("Start Point (%d, %d, %d) HeatScore %d", posX, posY, posZ, heatScore);
				createCluster(world, random, worldX, worldZ, posX, posY, posZ, heatScoreMap);
				createdClusters[createdClusterCount++] = startingPoint;

				if (createdClusterCount >= this.clusterCount) {
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
	private void calculateNeighbourOffsets() {
		int pos = 0;
		final int xOffset = CHUNK_DEPTH * worldHeight;
		final int yOffset = 1;
		final int zOffset = worldHeight;
		for (int z = -zOffset; z <= zOffset; z += zOffset) {
			for (int y = -yOffset; y <= yOffset; y += yOffset) {
				for (int x = -xOffset; x <= xOffset; x += xOffset) {
					if (x == 0 && y == 0 && z == 0) {
						continue;
					}
					neighbourOffsets[pos++] = x + y + z;
				}
			}
		}
	}

	private int createCluster(World world, Random random, int worldBlockX, int worldBlockZ, int chunkBlockX, int chunkBlockY, int chunkBlockZ, int[] interestingBlocks) {
		final int maxBlocksInThisCluster = random.nextInt(blocksPerCluster);
		Queue<Integer> blocksToProcess = new LinkedList<Integer>();
		HashSet<Integer> processedBlocks = new HashSet<Integer>();

		int startPosition = CHUNK_DEPTH * worldHeight * chunkBlockX + worldHeight * chunkBlockZ + chunkBlockY;

		//Ensure that the chance of a block spawning here is 100%
		interestingBlocks[startPosition] = Integer.MAX_VALUE;
		blocksToProcess.add(startPosition);
		int blocksAdded = 0;

		while (blocksAdded < maxBlocksInThisCluster && !blocksToProcess.isEmpty()) {
			//Get the next block from the queue
			int blockIndex = blocksToProcess.poll();
			float chance = interestingBlocks[blockIndex] / 8.0f;
			if (chance > random.nextFloat()) {
				//Convert the block index back to it's XYZ components
				int posX = blockIndex >> (worldHeightShift + depthShift) & (CHUNK_WIDTH - 1);
				int posY = blockIndex & (worldHeight - 1);
				int posZ = blockIndex >> worldHeightShift & (CHUNK_DEPTH - 1);

				//Change the block to sulfur
				world.setBlock(posX + worldBlockX, posY, posZ + worldBlockZ, block, 0, 0);
				//Mark it as processed so we don't end up in loops.
				processedBlocks.add(blockIndex);
				blocksAdded++;

				//For any neighbour that has a calculated heat score, give it a chance to be part of the cluster.
				for (int i = 0; i < neighbours.length; i++) {
					int[] neighbour = neighbours[i];
					//But do not cross the boundary into other chunks.
					if (posX + neighbour[0] < 0 || posX + neighbour[0] > (CHUNK_WIDTH - 1)) {
						continue;
					}
					if (posY + neighbour[1] < 0 || posY + neighbour[1] >= this.maxHeight) {
						continue;
					}
					if (posZ + neighbour[2] < 0 || posZ + neighbour[2] > (CHUNK_DEPTH - 1)) {
						continue;
					}

					int neighbourPos = blockIndex + neighbourOffsets[i];
					int neighbourHeatScore = interestingBlocks[neighbourPos];

					if (neighbourHeatScore != 0 && !processedBlocks.contains(neighbourPos)) {
						blocksToProcess.add(neighbourPos);
					}
				}
			}
		}
		return blocksAdded;
	}

	private void checkBlock(World world, int worldBlockX, int worldBlockY, int worldBlockZ, int[] heatScoreMap) {
		final int worldX = (worldBlockX) >> 4;
		final int worldZ = (worldBlockZ) >> 4;
		int potentialNeighbourToAddCount = 0;

		//grab the chunk. We don't need the extra things that world provides
		Chunk chunk = world.getChunkFromBlockCoords(worldBlockX, worldBlockZ);

		int chunkBlockX = worldBlockX & (CHUNK_WIDTH - 1);
		int chunkBlockZ = worldBlockZ & (CHUNK_DEPTH - 1);
		Block worldBlock = chunk.getBlock(chunkBlockX, worldBlockY, chunkBlockZ);

		int blockIndex = CHUNK_DEPTH * worldHeight * chunkBlockX + worldHeight * chunkBlockZ + worldBlockY;

		if (worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.stone) ||
				worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.dirt)) {
			int heatScore = 0;
			//Calculate the heat score, based on it's neighbours
			for (int i = 0; i < neighbours.length; i++) {
				int[] neighbour = neighbours[i];
				int neighbourIndexOffset = neighbourOffsets[i];
				int neighbourIndex = blockIndex + neighbourIndexOffset;
				int neighbourWorldX = worldBlockX + neighbour[0];
				int neighbourWorldY = worldBlockY + neighbour[1];
				int neighbourWorldZ = worldBlockZ + neighbour[2];

				if (neighbourWorldY < 0 || neighbourWorldY >= this.maxHeight) {
					continue;
				}

				//Don't inspect neighbours in chunks that haven't been created - it causes Already Decorating!! exception
				if ((neighbourWorldX >> 4 != worldX || neighbourWorldZ >> 4 != worldZ)) {
					//Logger.warning("wtf, encountered a block that wasn't in this chunk. I thought I'd prevented that.");
					continue;
				}

				Block neighbourBlock = chunk.getBlock(
						neighbourWorldX & (CHUNK_WIDTH - 1),
						neighbourWorldY,
						neighbourWorldZ & (CHUNK_DEPTH - 1));

				//If we've found a block neighbouring Lava, increase this block's heat score
				if (neighbourBlock == Blocks.lava || neighbourBlock == Blocks.flowing_lava) {
					heatScore += 2;
					//Otherwise, if the neighbour is replacable, it's eligible to be replaced as part of a cluster, assuming
					//that the current block is deemed to be an appropriate start location.
				} else if (worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.stone) ||
						worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.dirt)) {
					potentialNeighbourToAdd[potentialNeighbourToAddCount++] = neighbourIndex;
				}
			}

			//If we've found a block with a heat score
			//A heat score of 2 or higher means that we're adjacent to lava.
			if (heatScore > 0) {
				//stash the heat score in the heat map
				heatScoreMap[blockIndex] = heatScore;
				Block blockAbove = chunk.getBlock(chunkBlockX, worldBlockY + 1, chunkBlockZ);
				//heatScore should never be higher than 54, so we can pack that into 6 bits (64)
				boolean addPotentialStartingPoint = false;
				if (blockAbove == Blocks.air) {
					addPotentialStartingPoint = true;
				} else if (worldBlockY > 0) {
					//We'll also allow starting points to be blocks that are above lava.
					Block blockBelow = chunk.getBlock(chunkBlockX, worldBlockY - 1, chunkBlockZ);
					if (blockBelow == Blocks.lava || blockBelow == Blocks.flowing_lava) {
						//The heat score of these blocks tends to be abnormally high, usually because they are
						//potentially surrounded by more blocks, they're also harder to see, so we'll limit the heat
						//score in these instances.
						if (heatScore > 5) {
							heatScoreMap[blockIndex] = 5;
						}
						addPotentialStartingPoint = true;
					}
				}
				if (addPotentialStartingPoint) {
					//We can avoid using a SortedSet by bit-packing the heat score with the blockIndex.
					//By packing the heat into the higher order bits allows us to sort on them, but for that to work
					//we need to order the heat descending, we can pack it into 6 bits if we subtract it from 64
					//Then we just need to mask out the heat score.
					int encodedStartPoint = blockIndex | ((64 - heatScoreMap[blockIndex]) << (worldHeightShift + depthShift + depthShift));
					//Logger.info("Adding potential starting point (%d, %d, %d) index %d, heat score of %d, encoded=%d", chunkBlockX, worldBlockY, chunkBlockZ, blockIndex, heatScoreMap[blockIndex], encodedStartPoint);
					potentialStartingPoints[potentialStartingPointIndex++] = encodedStartPoint;
				}

				//Add all the potential neighbours to the heat map with a heat value of 1 if they don't already have a
				//higher value.
				for (int i = 0; i < potentialNeighbourToAddCount; i++) {
					int neighbour = potentialNeighbourToAdd[i];
					int neighbourHeat = heatScoreMap[neighbour];
					if (neighbourHeat == 0) {
						heatScoreMap[neighbour] = 1;
					}
				}
			}
		}
	}

	static {
		int pos = 0;
		for (int z = -1; z <= 1; ++z) {
			for (int y = -1; y <= 1; ++y) {
				for (int x = -1; x <= 1; ++x) {
					if (x == 0 && y == 0 && z == 0) {
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
}
