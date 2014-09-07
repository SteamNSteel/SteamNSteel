package mod.steamnsteel.world;

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
public class WorldGenSulfur implements ICustomOreGenerator {
	@Override
	public void generate(OreConfiguration configuration, World world, Random random, int worldX, int worldZ) {
		try {
			int maxCreatedBlocks = random.nextInt(configuration.blocksPerCluster);
			int blocksCreated = 0;

			HashMap<BlockPos, GenData> interestingBlocks = new HashMap<BlockPos, GenData>();
			SortedSet<GenData> potentialStartingPoints = new TreeSet<GenData>(new GenDataComparator());

			for (int x = 1; x < 15; ++x) {
				int blockX = worldX + x;
				for (int z = 1; z < 15; ++z) {
					int blockZ = worldZ + z;
					for (int y = configuration.minHeight; y < configuration.maxHeight; ++y) {
						int blockY = y;
						checkBlock(world, blockX, blockY, blockZ, interestingBlocks, potentialStartingPoints);
					}
				}
			}

			int blocksChanged = 0;
			for (Map.Entry<BlockPos, GenData> x : interestingBlocks.entrySet()) {
				BlockPos pos = x.getKey();
				GenData data = x.getValue();
				if (data.type == BlockType.REPLACABLE) {
					world.setBlock(pos.x, pos.y, pos.z, configuration.block, 0, 2);
					blocksChanged++;
					Logger.info("(%d, %d, %d) HeatScore %d", pos.x, pos.y, pos.z, data.heatScore);
				}
			}

			for (GenData data : potentialStartingPoints) {
				BlockPos pos = data.position;
				Logger.info("Start Point (%d, %d, %d) HeatScore %d", pos.x, pos.y, pos.z, data.heatScore);
			}

			Logger.info("Changed %d blocks to Sulfur", blocksChanged);
		} catch (Exception e) {
			Logger.severe("Error generating sulfur: %s", e.toString());
			e.printStackTrace();
		}
	}

	private void checkBlock(World world, int blockX, int blockY, int blockZ, Map<BlockPos, GenData> interestingBlocks, Set<GenData> potentialStartingPoints) {
		int worldX = (blockX) >> 4;
		int worldZ = (blockZ) >> 4;

		Chunk thisChunk = world.getChunkFromBlockCoords(blockX, blockZ);

		Block worldBlock = thisChunk.getBlock(blockX & 15, blockY, blockZ & 15);
		BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);

		LinkedList<BlockPos> potentialNeighboursToAdd = new LinkedList<BlockPos>();

		if (worldBlock == Blocks.stone || worldBlock == Blocks.dirt || worldBlock == Blocks.gravel) {
			int heatScore = 0;
			for (int[] neighbour : neighbours) {
				Chunk checkChunk = thisChunk;
				int neighbourChunkX = (blockX + neighbour[0]) >> 4;
				int neighbourChunkZ = (blockZ + neighbour[2]) >> 4;

				int neighbourBlockX =(blockX + neighbour[0]) & 15;
				int neighbourBlockZ =(blockZ + neighbour[2]) & 15;

				//Don't inspect neighbours in chunks that haven't been created - it causes Already Decorating!! exception
				if ((neighbourChunkX != worldX || neighbourChunkZ != worldZ)) {
					Logger.warning("wtf, encountered a block that wasn't in this chunk. I thought I'd prevented that.");
					continue;
					/*if (!world.getChunkProvider().chunkExists(neighbourChunkX, neighbourChunkZ)) {

					}
					checkChunk = world.getChunkFromChunkCoords(neighbourChunkX, neighbourChunkZ);*/
				}

				Block neighbourBlock = checkChunk.getBlock(
						neighbourBlockX,
						blockY + neighbour[1],
						neighbourBlockZ);

				BlockPos neighbourBlockPos = new BlockPos(blockX + neighbour[0], blockY + neighbour[1], blockZ + neighbour[2]);

				//If we've found a block neighbouring Lava, we've found a block of interest we'll want to track.
				if (neighbourBlock == Blocks.lava || neighbourBlock == Blocks.flowing_lava) {
					heatScore += 2;

					//Keeping track of the lava blocks will allow me to quickly tell me that this is a block that shouldn't be replaced.
					if (!interestingBlocks.containsKey(neighbourBlockPos)) {
						interestingBlocks.put(neighbourBlockPos, new GenData(neighbourBlockPos, BlockType.LAVA, 16));
					}
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
				for (BlockPos neighbour : potentialNeighboursToAdd) {
					data = interestingBlocks.get(neighbour);
					if (data == null) {
						data = new GenData(neighbour, BlockType.REPLACABLE);
						interestingBlocks.put(neighbour, data);
						data.heatScore = 1;
					}
				}

				Block blockAbove = thisChunk.getBlock(blockX&15, blockY + 1, blockZ&15);
				if (blockAbove == Blocks.air) {
					potentialStartingPoints.add(data);
				} else if (blockY > 0) {
					Block blockBelow = thisChunk.getBlock(blockX&15, blockY - 1, blockZ&15);
					if (blockAbove == Blocks.lava || blockAbove == Blocks.flowing_lava) {
						potentialStartingPoints.add(data);
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
