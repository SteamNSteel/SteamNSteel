package mod.steamnsteel.world;

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.block.SteamNSteelOreBlock;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenMinable;
import scala.actors.LinkedQueue;

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
	public SulfurOreGenerator(SteamNSteelOreBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight)
	{
		super(block, clusterCount, blocksPerCluster, minHeight, maxHeight);
	}

	@Override
	public boolean generate(World world, Random random, int worldX, int unusedY, int worldZ) {
		try {
			HashMap<BlockPos, GenData> interestingBlocks = new HashMap<BlockPos, GenData>();
			SortedSet<GenData> potentialStartingPoints = new TreeSet<GenData>(new GenDataComparator());

			for (int x = 1; x < 15; ++x) {
				int blockX = worldX + x;
				for (int z = 1; z < 15; ++z) {
					int blockZ = worldZ + z;
					for (int y = minHeight; y < maxHeight; ++y) {
						int blockY = y;
						checkBlock(world, blockX, blockY, blockZ, interestingBlocks, potentialStartingPoints);
					}
				}
			}

			int blocksChanged = 0;
			double minDistanceBetweenClusters = Math.pow(20, 2);
			List<BlockPos> createdClusters = new LinkedList<BlockPos>();
			for (GenData data : potentialStartingPoints) {
				BlockPos clusterPosition = data.position;
				boolean clusterAllowed = true;
				for (BlockPos otherClusterPosition : createdClusters) {
					double distance = Math.pow(clusterPosition.x - otherClusterPosition.x, 2) +
							Math.pow(clusterPosition.y - otherClusterPosition.y, 2) +
							Math.pow(clusterPosition.z - otherClusterPosition.y, 2);

					if (distance < minDistanceBetweenClusters) {
						clusterAllowed = false;
						break;
					}
				}

				float chance = data.heatScore / 8.0f;
				if (clusterAllowed && chance > random.nextFloat()) {

					//TODO Check radius to other clusters
					Logger.info("Start Point (%d, %d, %d) HeatScore %d", clusterPosition.x, clusterPosition.y, clusterPosition.z, data.heatScore);
					blocksChanged += createCluster(world, random, clusterPosition, interestingBlocks);
					createdClusters.add(clusterPosition);
				}
			}

			Logger.info("Changed %d blocks to Sulfur", blocksChanged);
			return true;
		} catch (Exception e) {
			Logger.severe("Error generating sulfur: %s", e.toString());
			e.printStackTrace();
		}
		return false;
	}

	private int createCluster(World world, Random random, BlockPos startPosition, Map<BlockPos, GenData> interestingBlocks) {
		int blocks = random.nextInt(blocksPerCluster);
		Queue<GenData> blocksToProcess = new LinkedList<GenData>();
		HashSet<BlockPos> processedBlocks = new HashSet<BlockPos>();
		GenData genData = interestingBlocks.get(startPosition);
		if (genData == null) {
			Logger.warning("wtf, the sulfur gen starting block wasn't interesting?");
		}
		genData.heatScore = Integer.MAX_VALUE;
		blocksToProcess.add(genData);
		int blocksAdded = 0;
		while (!blocksToProcess.isEmpty()) {
			GenData data = blocksToProcess.poll();
			BlockPos pos = data.position;
			float chance = data.heatScore / 8.0f;
			if (chance > random.nextFloat()) {
				world.setBlock(pos.x, pos.y, pos.z, block, 0, 0);
				processedBlocks.add(pos);
				blocksAdded++;
				for (int[] neighbour : neighbours) {
					BlockPos neighbourPos = new BlockPos(pos.x + neighbour[0], pos.y + neighbour[1], pos.z + neighbour[2]);
					GenData neighbourData = interestingBlocks.get(neighbourPos);
					if (neighbourData != null && !processedBlocks.contains(neighbourPos)) {
						blocksToProcess.add(neighbourData);
					}
				}

				if (blocksAdded >= blocks) {
					return blocksAdded;
				}
			}
		}
		return blocksAdded;
	}

	private void checkBlock(World world, int blockX, int blockY, int blockZ, Map<BlockPos, GenData> interestingBlocks, Set<GenData> potentialStartingPoints) {
		int worldX = (blockX) >> 4;
		int worldZ = (blockZ) >> 4;

		Chunk chunk = world.getChunkFromBlockCoords(blockX, blockZ);

		Block worldBlock = chunk.getBlock(blockX & 15, blockY, blockZ & 15);
		BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);

		LinkedList<BlockPos> potentialNeighboursToAdd = new LinkedList<BlockPos>();

		if (worldBlock == Blocks.stone || worldBlock == Blocks.dirt || worldBlock == Blocks.gravel) {
			int heatScore = 0;
			for (int[] neighbour : neighbours) {
				BlockPos neighbourBlockPos = new BlockPos(blockX + neighbour[0], blockY + neighbour[1], blockZ + neighbour[2]);

				//Don't inspect neighbours in chunks that haven't been created - it causes Already Decorating!! exception
				if ((neighbourBlockPos.x >> 4 != worldX || neighbourBlockPos.z >> 4 != worldZ)) {
					Logger.warning("wtf, encountered a block that wasn't in this chunk. I thought I'd prevented that.");
					continue;
				}

				Block neighbourBlock = chunk.getBlock(
						neighbourBlockPos.x & 15,
						blockY + neighbour[1],
						neighbourBlockPos.z & 15);

				//If we've found a block neighbouring Lava, increase this block's heat score
				if (neighbourBlock == Blocks.lava || neighbourBlock == Blocks.flowing_lava) {
					heatScore += 2;
				} else if (neighbourBlock == Blocks.stone || neighbourBlock == Blocks.dirt || neighbourBlock == Blocks.gravel) {
					potentialNeighboursToAdd.add(neighbourBlockPos);
				}
			}

			if (heatScore > 0) {
				GenData data = interestingBlocks.get(blockPos);
				if (data == null) {

					data = new GenData(blockPos, BlockType.REPLACABLE);
					interestingBlocks.put(blockPos, data);
				}
				data.heatScore = heatScore;

				//A heat score of 2 or higher means that we're adjacent to lava.
				Block blockAbove = chunk.getBlock(blockX & 15, blockY + 1, blockZ & 15);
				if (blockAbove == Blocks.air) {
					potentialStartingPoints.add(data);
				} else if (blockY > 0) {
					Block blockBelow = chunk.getBlock(blockX & 15, blockY - 1, blockZ & 15);
					if (blockBelow == Blocks.lava || blockBelow == Blocks.flowing_lava) {
						//the heat score of these blocks tends to be abnormally high, usually because they are
						//potentially surrounded by more blocks, they're also harder to see, so we'll limit the heat
						//score in these instances.
						if (data.heatScore > 5) {
							data.heatScore = 5;
							//interestingBlocks.put(blockPos, data);
						}

						potentialStartingPoints.add(data);
					}
				}


				for (BlockPos neighbour : potentialNeighboursToAdd) {
					data = interestingBlocks.get(neighbour);
					if (data == null) {
						data = new GenData(neighbour, BlockType.REPLACABLE);
						interestingBlocks.put(neighbour, data);
						data.heatScore = 1;
					}
				}
			}
		}
	}

	private enum BlockType {
		REPLACABLE,
		LAVA
	}

	private static class GenData {
		public final BlockPos position;
		public final BlockType type;
		public int heatScore;

		public GenData(BlockPos position, BlockType type) {
			this.position = position;
			this.type = type;
		}

		public GenData(BlockPos position, BlockType type, int heatScore) {
			this(position, type);
			this.heatScore = heatScore;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			GenData genData = (GenData) o;

			if (!position.equals(genData.position)) return false;

			return true;
		}

		@Override
		public int hashCode() {
			return position.hashCode();
		}
	}

	private static class BlockPos {
		public final int x;
		public final int y;
		public final int z;

		public BlockPos(int x, int y, int z) {

			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			BlockPos blockPos = (BlockPos) o;

			if (x != blockPos.x) return false;
			if (y != blockPos.y) return false;
			if (z != blockPos.z) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			result = 31 * result + z;
			return result;
		}
	}

	static final int[][] neighbours = new int[26][4];

	static {
		int pos = 0;
		for (int z = -1; z <= 1; ++z) {
			for (int y = -1; y <= 1; ++y) {
				for (int x = -1; x <= 1; ++x) {
					int importance = 3;
					if (x == 0) {
						importance--;
					}
					if (y == 0) {
						importance--;
					}
					if (z == 0) {
						importance--;
					}
					if (importance > 0) {
						neighbours[pos][0] = x;
						neighbours[pos][1] = y;
						neighbours[pos][2] = z;
						neighbours[pos][3] = importance;

						++pos;
					}
				}
			}
		}
	}


	private static class GenDataComparator implements Comparator<GenData> {
		@Override
		public int compare(GenData genData, GenData genData2) {
			int compare = genData2.heatScore - genData.heatScore;
			if (compare == 0) {
				compare = genData2.position.hashCode() - genData.position.hashCode();
			}
			return compare;
		}
	}
}
