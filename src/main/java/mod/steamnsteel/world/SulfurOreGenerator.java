package mod.steamnsteel.world;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import mod.steamnsteel.block.SteamNSteelOreBlock;
import mod.steamnsteel.utility.log.Logger;
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
 *
 * Crashes in the generation shouldn't happen, but just in case let's not prevent people from playing the game. I highly
 * doubt that a crash would happen in every chunk.
 */
public class SulfurOreGenerator extends OreGenerator {
	private int worldHeight;
	private int worldHeightShift;
	private int depthShift;
	private int[] neighbourOffsets;
	private final double logTwo = Math.log(2);
	private final Set<Map.Entry<Integer, Integer>> potentialStartingPoints = Sets.newTreeSet(new Comparator<Map.Entry<Integer,Integer>>() {

		@Override
		public int compare(Map.Entry<Integer, Integer> entryA, Map.Entry<Integer, Integer> entryB) {
			int comparison = entryB.getValue().compareTo(entryA.getValue());
			if (comparison == 0) {
				return entryB.getKey().compareTo(entryA.getKey());
			}
			return comparison;
		}
	} );

	public SulfurOreGenerator(SteamNSteelOreBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight)
	{
		super(block, clusterCount, blocksPerCluster, minHeight, maxHeight);
		depthShift = (int)(Math.log(16)/ logTwo);
	}

	@Override
	public boolean generate(World world, Random random, int worldX, int unusedY, int worldZ) {
		if (!block.isGenEnabled()) {
			return false;
		}
		long startTime = System.currentTimeMillis();

		try {
			int height = world.getActualHeight();
			if (height >= maxHeight) {
				height = maxHeight;
			}

			//Rounds the world actual height to the nearest base 2 for safety.
			height = (1 << ((int)((Math.log(height - 1)/ logTwo)+1)));

			if (height != worldHeight) {
				worldHeightShift = (int)(Math.log(height)/ logTwo);
				worldHeight = height;
				calculateNeighbourOffsets();

			}

			int[] heatScoreMap = new int[16 * 16 * worldHeight];
			potentialStartingPoints.clear();

			for (int x = 1; x < 15; ++x) {
				int blockX = worldX + x;
				for (int z = 1; z < 15; ++z) {
					int blockZ = worldZ + z;
					for (int y = minHeight; y < maxHeight; ++y) {
						checkBlock(world, blockX, y, blockZ, heatScoreMap);
					}
				}
			}

			//Ordering<Integer> valueComparator = Ordering.natural().onResultOf(Functions.forMap(potentialStartingPoints));

			int blocksChanged = 0;
			double minDistanceBetweenClusters = Math.pow(10, 2);
			List<Integer> createdClusters = new LinkedList<Integer>();
			for (Map.Entry<Integer, Integer> startingPoint : potentialStartingPoints) {
				boolean clusterAllowed = true;
				int clusterPosition = startingPoint.getKey();

				int posX = clusterPosition >> (worldHeightShift + depthShift) & (16-1) ;
				int posY = clusterPosition & (worldHeight-1);
				int posZ = clusterPosition >> worldHeightShift & (16-1);

				for (int otherClusterPosition : createdClusters) {
					int otherPosX = otherClusterPosition >> (worldHeightShift + depthShift) & (16-1) ;
					int otherPosY = otherClusterPosition & (worldHeight-1);
					int otherPosZ = otherClusterPosition >> worldHeightShift & (16-1);

					double distance = Math.pow(posX - otherPosX, 2) +
							Math.pow(posY - otherPosY, 2) +
							Math.pow(posZ - otherPosZ, 2);

					if (distance < minDistanceBetweenClusters) {
						clusterAllowed = false;
						break;
					}
				}

				int heatScore = heatScoreMap[clusterPosition];
				float chance = heatScore / 8.0f;
				if (clusterAllowed && chance > random.nextFloat()) {
					//Logger.info("Start Point (%d, %d, %d) HeatScore %d", clusterPosition.x, clusterPosition.y, clusterPosition.z, data.heatScore);
					blocksChanged += createCluster(world, random, posX, posY, posZ, heatScoreMap);
					createdClusters.add(clusterPosition);

					if (createdClusters.size() >= this.clusterCount) {
						break;
					}
				}
			}

			Logger.info("Changed %d blocks to Sulfur", blocksChanged);
			long stopTime = System.currentTimeMillis();
			long runTime = stopTime - startTime;
			Logger.info("Run time: " + runTime);
			return true;
		} catch (Exception e) {
			Logger.severe("Error generating sulfur: %s", e.toString());
			e.printStackTrace();
		}
		return false;
	}

	private void calculateNeighbourOffsets() {
		neighbourOffsets = new int[26];
		int pos = 0;
		final int xOffset = 16*worldHeight;
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

	private int createCluster(World world, Random random, int chunkBlockX, int chunkBlockY, int chunkBlockZ, int[] interestingBlocks) {
		final int blocks = random.nextInt(blocksPerCluster);
		Queue<Integer> blocksToProcess = new LinkedList<Integer>();
		HashSet<Integer> processedBlocks = new HashSet<Integer>();

		//16 = chunk depth
		int startPosition = 16*worldHeight*chunkBlockX + worldHeight*chunkBlockZ + chunkBlockY;

		//We WILL be starting here.
		interestingBlocks[startPosition] = Integer.MAX_VALUE;
		blocksToProcess.add(startPosition);
		int blocksAdded = 0;
		while (!blocksToProcess.isEmpty()) {
			int blockPos = blocksToProcess.poll();
			float chance = interestingBlocks[blockPos] / 8.0f;
			if (chance > random.nextFloat()) {
				//TODO: Convert pos back to x,y,z
				int posX = blockPos >> (worldHeightShift + depthShift) & (16-1) ;
				int posY = blockPos & (worldHeight-1);
				int posZ = blockPos >> worldHeightShift & (16-1);

				world.setBlock(posX, posY, posZ, block, 0, 0);
				processedBlocks.add(blockPos);
				blocksAdded++;
				for (int i = 0; i < neighbours.length; i++) {
					int[] neighbour = neighbours[i];
					if (posX + neighbour[0] < 0 || posX + neighbour[0] > 15) {
						continue;
					}
					if (posY + neighbour[1] < 0 || posY + neighbour[1] >= this.maxHeight) {
						continue;
					}
					if (posZ + neighbour[2] < 0 || posZ + neighbour[2] > 15) {
						continue;
					}

					int neighbourPos = blockPos + neighbourOffsets[i];
					int neighbourHeatScore = interestingBlocks[neighbourPos];

					if (neighbourHeatScore != 0 && !processedBlocks.contains(neighbourPos)) {
						blocksToProcess.add(neighbourPos);
					}
				}

				if (blocksAdded >= blocks) {
					return blocksAdded;
				}
			}
		}
		return blocksAdded;
	}

	private void checkBlock(World world, int worldBlockX, int worldBlockY, int worldBlockZ, int[] heatScoreMap) {
		final int worldX = (worldBlockX) >> 4;
		final int worldZ = (worldBlockZ) >> 4;

		//final int worldHeight = world.getActualHeight();
		Chunk chunk = world.getChunkFromBlockCoords(worldBlockX, worldBlockZ);

		int chunkBlockX = worldBlockX & 15;
		int chunkBlockZ = worldBlockZ & 15;
		Block worldBlock = chunk.getBlock(chunkBlockX, worldBlockY, chunkBlockZ);

		//16 = chunk depth
		int blockIndex = 16*worldHeight* chunkBlockX + worldHeight* chunkBlockZ + worldBlockY;

		LinkedList<Integer> potentialNeighboursToAdd = new LinkedList<Integer>();

		if (worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.stone) ||
				worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.dirt)) {
			int heatScore = 0;
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
					Logger.warning("wtf, encountered a block that wasn't in this chunk. I thought I'd prevented that.");
					continue;
				}

				Block neighbourBlock = chunk.getBlock(
						neighbourWorldX & 15,
						neighbourWorldY,
						neighbourWorldZ & 15);

				//If we've found a block neighbouring Lava, increase this block's heat score
				if (neighbourBlock == Blocks.lava || neighbourBlock == Blocks.flowing_lava) {
					heatScore += 2;
				} else if (worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.stone) ||
						worldBlock.isReplaceableOreGen(world, worldBlockX, worldBlockY, worldBlockZ, Blocks.dirt)) {
					potentialNeighboursToAdd.add(neighbourIndex);
				}
			}

			if (heatScore > 0) {
				heatScoreMap[blockIndex] = heatScore;

				//A heat score of 2 or higher means that we're adjacent to lava.
				Block blockAbove = chunk.getBlock(chunkBlockX, worldBlockY + 1, chunkBlockZ);
				if (blockAbove == Blocks.air) {
					potentialStartingPoints.add(Maps.immutableEntry(blockIndex, heatScore));
				} else if (worldBlockY > 0) {
					Block blockBelow = chunk.getBlock(chunkBlockX, worldBlockY - 1, chunkBlockZ);
					if (blockBelow == Blocks.lava || blockBelow == Blocks.flowing_lava) {
						//the heat score of these blocks tends to be abnormally high, usually because they are
						//potentially surrounded by more blocks, they're also harder to see, so we'll limit the heat
						//score in these instances.
						if (heatScore > 5) {
							heatScoreMap[blockIndex] = 5;
						}
						potentialStartingPoints.add(Maps.immutableEntry(blockIndex, heatScore));
					}
				}

				for (int neighbour : potentialNeighboursToAdd) {
					int neighbourHeat = heatScoreMap[neighbour];
					if (neighbourHeat == 0) {
						heatScoreMap[neighbour] = 1;
					}
				}
			}
		}
	}

	static final int[][] neighbours = new int[26][3];

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
