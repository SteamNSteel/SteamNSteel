package mod.steamnsteel.world;

import mod.steamnsteel.block.SteamNSteelOreBlock;
import mod.steamnsteel.configuration.Settings;
import mod.steamnsteel.utility.Vector;
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
	public SulfurOreGenerator(SteamNSteelOreBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight)
	{
		super(block, clusterCount, blocksPerCluster, minHeight, maxHeight);
	}

	@Override
	public boolean generate(World world, Random random, int worldX, int unusedY, int worldZ) {
		if (!block.isGenEnabled()) {
			return false;
		}
		try {
			HashMap<Vector<Integer>, GenData> interestingBlocks = new HashMap<Vector<Integer>, GenData>();
			SortedSet<GenData> potentialStartingPoints = new TreeSet<GenData>();

			for (int x = 1; x < 15; ++x) {
				int blockX = worldX + x;
				for (int z = 1; z < 15; ++z) {
					int blockZ = worldZ + z;
					for (int y = minHeight; y < maxHeight; ++y) {
						checkBlock(world, blockX, y, blockZ, interestingBlocks, potentialStartingPoints);
					}
				}
			}

			int blocksChanged = 0;
			double minDistanceBetweenClusters = Math.pow(10, 2);
			List<Vector<Integer>> createdClusters = new LinkedList<Vector<Integer>>();
			for (GenData data : potentialStartingPoints) {
				Vector<Integer> clusterPosition = data.position;
				boolean clusterAllowed = true;
				for (Vector<Integer> otherClusterPosition : createdClusters) {
					double distance = Math.pow(clusterPosition.getX() - otherClusterPosition.getX(), 2) +
							Math.pow(clusterPosition.getY() - otherClusterPosition.getY(), 2) +
							Math.pow(clusterPosition.getZ() - otherClusterPosition.getZ(), 2);

					if (distance < minDistanceBetweenClusters) {
						clusterAllowed = false;
						break;
					}
				}

				float chance = data.heatScore / 8.0f;
				if (clusterAllowed && chance > random.nextFloat()) {
					//Logger.info("Start Point (%d, %d, %d) HeatScore %d", clusterPosition.x, clusterPosition.y, clusterPosition.z, data.heatScore);
					blocksChanged += createCluster(world, random, clusterPosition, interestingBlocks);
					createdClusters.add(clusterPosition);

					if (createdClusters.size() >= this.clusterCount) {
						break;
					}
				}
			}

			//Logger.info("Changed %d blocks to Sulfur", blocksChanged);
			return true;
		} catch (Exception e) {
			Logger.severe("Error generating sulfur: %s", e.toString());
			e.printStackTrace();
		}
		return false;
	}

	private int createCluster(World world, Random random, Vector<Integer> startPosition, Map<Vector<Integer>, GenData> interestingBlocks) {
		int blocks = random.nextInt(blocksPerCluster);
		Queue<GenData> blocksToProcess = new LinkedList<GenData>();
		HashSet<Vector<Integer>> processedBlocks = new HashSet<Vector<Integer>>();
		GenData genData = interestingBlocks.get(startPosition);
		genData.heatScore = Integer.MAX_VALUE;
		blocksToProcess.add(genData);
		int blocksAdded = 0;
		while (!blocksToProcess.isEmpty()) {
			GenData data = blocksToProcess.poll();
			Vector<Integer> pos = data.position;
			float chance = data.heatScore / 8.0f;
			if (chance > random.nextFloat()) {
				world.setBlock(pos.getX(), pos.getY(), pos.getZ(), block, 0, 0);
				processedBlocks.add(pos);
				blocksAdded++;
				for (int[] neighbour : neighbours) {
					Vector<Integer> neighbourPos = new Vector<Integer>(pos.getX() + neighbour[0], pos.getY() + neighbour[1], pos.getZ() + neighbour[2]);
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

	private void checkBlock(World world, int blockX, int blockY, int blockZ, Map<Vector<Integer>, GenData> interestingBlocks, Set<GenData> potentialStartingPoints) {
		int worldX = (blockX) >> 4;
		int worldZ = (blockZ) >> 4;

		Chunk chunk = world.getChunkFromBlockCoords(blockX, blockZ);

		Block worldBlock = chunk.getBlock(blockX & 15, blockY, blockZ & 15);
		Vector<Integer> blockPos = new Vector<Integer>(blockX, blockY, blockZ);

		LinkedList<Vector<Integer>> potentialNeighboursToAdd = new LinkedList<Vector<Integer>>();

		if (worldBlock == Blocks.stone || worldBlock == Blocks.dirt || worldBlock == Blocks.gravel) {
			int heatScore = 0;
			for (int[] neighbour : neighbours) {
				Vector<Integer> neighbourBlockPos = new Vector<Integer>(blockX + neighbour[0], blockY + neighbour[1], blockZ + neighbour[2]);

				//Don't inspect neighbours in chunks that haven't been created - it causes Already Decorating!! exception
				if ((neighbourBlockPos.getX() >> 4 != worldX || neighbourBlockPos.getZ() >> 4 != worldZ)) {
					Logger.warning("wtf, encountered a block that wasn't in this chunk. I thought I'd prevented that.");
					continue;
				}

				Block neighbourBlock = chunk.getBlock(
						neighbourBlockPos.getX() & 15,
						blockY + neighbour[1],
						neighbourBlockPos.getZ() & 15);

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

					data = new GenData(blockPos);
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
						}

						potentialStartingPoints.add(data);
					}
				}


				for (Vector<Integer> neighbour : potentialNeighboursToAdd) {
					data = interestingBlocks.get(neighbour);
					if (data == null) {
						data = new GenData(neighbour);
						interestingBlocks.put(neighbour, data);
						data.heatScore = 1;
					}
				}
			}
		}
	}

	private static class GenData implements Comparable<GenData> {
		public final Vector<Integer> position;
		public int heatScore;

		public GenData(Vector<Integer> position) {
			this.position = position;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			GenData genData = (GenData) o;

			return position.equals(genData.position);

		}

		@Override
		public int hashCode() {
			return position.hashCode();
		}

		@Override
		public int compareTo(GenData genData) {
			if (genData == null) { return 1; }
			int compare = genData.heatScore - this.heatScore;
			if (compare == 0) {
				compare = genData.position.hashCode() - this.position.hashCode();
			}
			return compare;
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
