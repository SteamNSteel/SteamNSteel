package mod.steamnsteel.world;

import com.google.common.collect.Sets;
import mod.steamnsteel.block.SteamNSteelOreBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;
import java.util.Set;

/**
 * Generates Veins of Niter.
 *
 * Pick a random {x,z} in the chunk and check the type of the biome.
 *
 * * If we're in a biome that is HOT, DRY and SANDY, then we search vertically through the {x,z} chosen for where sand
 *      meets sandstone.
 * * If we're in any other biome, we have a lower chance of generating (currently 50%). We search vertically through the
 *      {x,z} for a grass block, then search below that block for where dirt meets stone.
 *
 * If we've found a valid location, we'll create a vein at that location.
 *
 * The Vein generation is a slightly modified version of Minecraft's vanilla oregen in that it uses a slightly wider
 * diameter and generates sandstone together with the niter with a 50% chance.
 */
public class NiterOreGenerator extends OreGenerator {

	private static final float otherBiomeChancePercent = 0.5f;
	private static final float sandstonePaddingChancePercent = 0.5f;

	private final Set<BiomeGenBase> desertLikeBiomes;
	private final int blocksPerCluster;
	//list of blocks that can be replaced by the ore.
	private final Block[] replaceableBlocks;
	private final Block paddingBlock;

	NiterOreGenerator(SteamNSteelOreBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight) {
		super(block, clusterCount, blocksPerCluster, minHeight, maxHeight);
		this.blocksPerCluster = blocksPerCluster;
		//Find the intersection of DRY, HOT and SANDY biome types
		Set<BiomeGenBase> dryBiomes = Sets.newHashSet(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.DRY));
		Set<BiomeGenBase> hotBiomes = Sets.newHashSet(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.HOT));
		Set<BiomeGenBase> sandyBiomes = Sets.newHashSet(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.SANDY));
		desertLikeBiomes = Sets.intersection(dryBiomes, Sets.intersection(hotBiomes, sandyBiomes));

		replaceableBlocks = new Block[] {
				Blocks.sand,
				Blocks.sandstone,
				Blocks.dirt,
				Blocks.stone
		};

		paddingBlock = Blocks.sandstone;
	}

	@Override
	public boolean generate(World world, Random rand, int worldX, int unused, int worldZ) {
		try {
			if (!block.isGenEnabled()) {
				return false;
			}

			//long startTime = System.currentTimeMillis();

			for (int clusterNum = 0; clusterNum < clusterCount; ++clusterNum) {
				int x = rand.nextInt(16);
				int z = rand.nextInt(16);

				int worldBlockX = x + worldX;
				int worldBlockZ = z + worldZ;

				Chunk chunk = world.getChunkFromBlockCoords(worldX, worldZ);
				BiomeGenBase biomeGenBase = chunk.getBiomeGenForWorldCoords(x, z, world.getWorldChunkManager());

				int blockX = x & 15;
				int blockZ = z & 15;
				//find where to start the search from
				int height = chunk.getHeightValue(blockX, blockZ);
				if (height > maxHeight) {
					height = maxHeight;
				}
				int yHeight = -1;

				//If we're in a desert like biome
				if (desertLikeBiomes.contains(biomeGenBase)) {
					//Use the desert method for finding the generation location
					yHeight = findDesertSeam(chunk, worldBlockX, height, worldBlockZ);
				} else if (rand.nextFloat() < otherBiomeChancePercent) {
					//Otherwise use the alternate method.
					yHeight = findOtherBiomeSeam(chunk, worldBlockX, height, worldBlockZ);
				}

				if (yHeight != -1) {
					//Add a little bit of padding to stop it from generating right on the edges.
					//In general, sandstone tends to only be about 3 blocks thick.
					yHeight -= rand.nextInt(2);

					generateOreVein(world, chunk, rand, x, yHeight, z);
				}
			}

			//long stopTime = System.currentTimeMillis();
			//long runTime = stopTime - startTime;
			//Logger.info("Niter Run time: " + runTime);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private int findDesertSeam(Chunk chunk, int x, int y, int z) {
		boolean foundSand = false;
		int genHeight = -1;
		for (int ySearch = y; ySearch >= minHeight; --ySearch) {
			Block b = chunk.getBlock(x & 15, ySearch, z & 15);
			if (b == Blocks.sand && !foundSand) {
				foundSand = true;
			} else if (foundSand && b == Blocks.sandstone) {
				genHeight = ySearch;
				break;
			}
		}
		return genHeight;
	}

	private int findOtherBiomeSeam(Chunk chunk, int x, int y, int z) {
		boolean foundGrass = false;
		boolean foundDirt = false;
		int genHeight = -1;
		for (int ySearch = y; ySearch >= minHeight; --ySearch) {
			Block b = chunk.getBlock(x & 15, ySearch, z & 15);
			if (b == Blocks.grass && !foundGrass) {
				foundGrass = true;
			} else if (foundGrass && !foundDirt && b == Blocks.dirt) {
				foundDirt = true;
			} else if (foundGrass && foundDirt && b != Blocks.dirt) {
				//found the edge of the dirt/stone line.
				genHeight = ySearch;
				break;
			}
		}
		return genHeight;
	}

	@SuppressWarnings("ConstantConditions")
	public boolean generateOreVein(World world, Chunk chunk, Random random, int worldX, int worldY, int worldZ)
	{
		float direction = random.nextFloat() * (float)Math.PI;
		double maxX = (double)((float)(worldX + 8) + MathHelper.sin(direction) * (float)this.blocksPerCluster / 8.0F);
		double minX = (double)((float)(worldX + 8) - MathHelper.sin(direction) * (float)this.blocksPerCluster / 8.0F);
		double maxZ = (double)((float)(worldZ + 8) + MathHelper.cos(direction) * (float)this.blocksPerCluster / 8.0F);
		double minZ = (double)((float)(worldZ + 8) - MathHelper.cos(direction) * (float)this.blocksPerCluster / 8.0F);
		//neither of these are really the min or max.
		double yBoundsA = (double)(worldY + random.nextInt(3) - 2);
		double yBoundsB = (double)(worldY + random.nextInt(3) - 2);

		for (int blockNum = 0; blockNum <= this.blocksPerCluster; ++blockNum)
		{
			double clusterCentreX = maxX + (minX - maxX) * (double)blockNum / (double)this.blocksPerCluster;
			double clusterCentreY = yBoundsA + (((yBoundsB - yBoundsA) * (double) blockNum) / (double) this.blocksPerCluster);
			double clusterCentreZ = maxZ + (minZ - maxZ) * (double)blockNum / (double)this.blocksPerCluster;
			double d9 = random.nextDouble() * (double)this.blocksPerCluster / 16.0D;
			double clusterSize = (double)(MathHelper.sin((float)blockNum * (float)Math.PI / (float)this.blocksPerCluster) + 1.0F) * d9 + 1.0D;
			int clusterStartX = MathHelper.floor_double(clusterCentreX - clusterSize / 2.0D);
			int clusterStartY = MathHelper.floor_double(clusterCentreY - clusterSize / 2.0D);
			int clusterStartZ = MathHelper.floor_double(clusterCentreZ - clusterSize / 2.0D);
			int clusterEndX = MathHelper.floor_double(clusterCentreX + clusterSize / 2.0D);
			int clusterEndY = MathHelper.floor_double(clusterCentreY + clusterSize / 2.0D);
			int clusterEndZ = MathHelper.floor_double(clusterCentreZ + clusterSize / 2.0D);

			double sandstoneDistance = 1.4D;

			for (int x = clusterStartX; x <= clusterEndX; ++x)
			{
				double d12 = ((double)x + 0.5D - clusterCentreX) / (clusterSize / 2.0D);
				if (d12 * d12 < sandstoneDistance)
				{
					for (int y = clusterStartY; y <= clusterEndY; ++y)
					{
						double d13 = ((double)y + 0.5D - clusterCentreY) / (clusterSize / 2.0D);

						if (d12 * d12 + d13 * d13 < sandstoneDistance)
						{
							for (int z = clusterStartZ; z <= clusterEndZ; ++z)
							{
								double d14 = ((double)z + 0.5D - clusterCentreZ) / (clusterSize / 2.0D);

								Block chunkBlock = chunk.getBlock(x & 15, y, z & 15);

								boolean replaceAllowed = false;
								for (Block replaceableBlock : replaceableBlocks) {
									replaceAllowed |= !replaceAllowed && chunkBlock.isReplaceableOreGen(world, x, y, z, replaceableBlock);
								}

								double distance = d12 * d12 + d13 * d13 + d14 * d14;
								if (distance < sandstoneDistance && replaceAllowed)
								{
									Block block;
									if (random.nextFloat() < sandstonePaddingChancePercent) {
										block = paddingBlock;
									} else {
										block = this.block;
									}

									world.setBlock(x, y, z, block, 0, 2);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}
