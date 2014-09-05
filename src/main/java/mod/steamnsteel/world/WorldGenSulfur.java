package mod.steamnsteel.world;

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.library.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenSulfur implements ICustomOreGenerator {
	@Override
	public void generate(OreConfiguration configuration, World world, Random random, int worldX, int worldZ) {
		int maxCreatedBlocks = random.nextInt(configuration.blocksPerCluster);
		int blocksCreated = 0;
		boolean creatingBlob = false;

		for (int x = 0; x < 16; ++x) {
			int blockX = worldX + x;
			for (int z = 0; z < 16; ++z) {
				int blockZ = worldZ + z;
				for (int y = configuration.minHeight; y < configuration.maxHeight; ++y) {
					int blockY = y;

					Block worldBlock = world.getBlock(blockX, blockY, blockZ);
					if (worldBlock == Blocks.stone || worldBlock == Blocks.dirt) {
						boolean lavaFound = false;
						boolean sulfurFound = false;

						for (int[] neighbour : neighbours) {
							int neighbourChunkX = (blockX + neighbour[0]) >> 4;
							int neighbourChunkZ = (blockZ + neighbour[2]) >> 4;

							//Don't inspect neighbours in chunks that haven't been created - causes Already Decorating!! exception
							if ((neighbourChunkX != worldX || neighbourChunkZ != worldZ) && !world.getChunkProvider().chunkExists(neighbourChunkX, neighbourChunkZ)) {
								continue;
							}

							Block neighbourBlock = world.getBlock(blockX + neighbour[0], blockY + neighbour[1], blockZ + neighbour[2]);

							if (neighbourBlock == Blocks.lava) {
								lavaFound = true;
							} else if (neighbourBlock == configuration.block) {
								sulfurFound = true;
							}

							//If we find a lava block, there is a chance we'll use it as the start generating from here.
							if (!creatingBlob && lavaFound && random.nextInt(configuration.clusterCount) == 0) {
								Block blockAbove = world.getBlock(blockX, blockY + 1, blockZ);
								if (blockAbove == Blocks.air) {
									world.setBlock(blockX, blockY, blockZ, configuration.block, 0, 0);
									creatingBlob = true;
									blocksCreated++;
									break;
								}
							} else
								//Create a blob by detecting neighbour sulfur blocks once we've started creating a blob
								//Make the blob a bit more interesting, definately place a block if there is sulfur nearby
								//maybe place a block if there isn't
								if (creatingBlob == true && sulfurFound && (lavaFound || random.nextInt(16) == 0)) {
									world.setBlock(blockX, blockY, blockZ, configuration.block, 0, 0);
									blocksCreated++;
									break;
								}

							if (blocksCreated == maxCreatedBlocks) {
								//FMLLog.log(TheMod.MOD_ID, Level.INFO, "sulfur @ (/tp %d %d %d) - %d blocks", blockX, blockY + 2, blockZ, blocksCreated);
								return;
							}
						}
					}
				}
			}
		}
	}


	static final int[][] neighbours = {
			{0, 0, 1},
			{1, 0, 0},
			{0, 0, -1},
			{-1, 0, 0},
			{0, 1, 0},
			{0, -1, 0}
	};
}
