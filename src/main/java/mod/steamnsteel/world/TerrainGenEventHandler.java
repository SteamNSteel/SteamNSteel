/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.world;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.library.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import org.apache.logging.log4j.Level;

public class TerrainGenEventHandler {
	public static final TerrainGenEventHandler INSTANCE = new TerrainGenEventHandler();

	private TerrainGenEventHandler() {
	}

	@SubscribeEvent
	public void onPopulateChunkEvent(DecorateBiomeEvent.Post event) {
		World world = event.world;
		//event.chunkX is actually block space, not chunk space.
		int chunkX = event.chunkX >> 4;
		int chunkZ = event.chunkZ >> 4;

		int minBlocksInCluster = 5;
		int maxBlocksInCluster = 15;
		int chanceToStartCluster = 4;

		if (maxBlocksInCluster == 0 || chanceToStartCluster == 0) {
			return;
		}

		int maxCreatedBlocks = event.rand.nextInt(maxBlocksInCluster - minBlocksInCluster) + minBlocksInCluster;
		int blocksCreated = 0;
		boolean creatingBlob = false;

		for (int x = 0; x < 16; ++x) {
			int blockX = event.chunkX + x;
			for (int z = 0; z < 16; ++z) {
				int blockZ = event.chunkZ + z;
				for (int y = 0; y < 48; ++y) {
					int blockY = y;

					Block worldBlock = world.getBlock(blockX, blockY, blockZ);
					if (worldBlock == Blocks.stone || worldBlock == Blocks.dirt) {
						boolean lavaFound = false;
						boolean sulfurFound = false;

						for (int[] neighbour : neighbours) {
							int neighbourChunkX = (blockX + neighbour[0]) >> 4;
							int neighbourChunkZ = (blockZ + neighbour[2]) >> 4;

							//Don't inspect neighbours in chunks that haven't been created - causes Already Decorating!! exception
							if ((neighbourChunkX != chunkX || neighbourChunkZ != chunkZ) && !world.getChunkProvider().chunkExists(neighbourChunkX, neighbourChunkZ)) {
								continue;
							}

							Block neighbourBlock = world.getBlock(blockX + neighbour[0], blockY + neighbour[1], blockZ + neighbour[2]);

							if (neighbourBlock == Blocks.lava) {
								lavaFound = true;
							} else if (neighbourBlock == ModBlocks.SULFUR_ORE) {
								sulfurFound = true;
							}

							//If we find a lava block, there is a chance we'll use it as the start generating from here.
							if (!creatingBlob && lavaFound && event.rand.nextInt(chanceToStartCluster) == 0) {
								Block blockAbove = world.getBlock(blockX, blockY + 1, blockZ);
								if (blockAbove == Blocks.air) {
									world.setBlock(blockX, blockY, blockZ, ModBlocks.SULFUR_ORE, 0, 0);
									creatingBlob = true;
									blocksCreated++;
									break;
								}
							} else
							//Create a blob by detecting neighbour sulfur blocks once we've started creating a blob
							//Make the blob a bit more interesting, definately place a block if there is sulfur nearby
							//maybe place a block if there isn't
							if (creatingBlob == true && sulfurFound && (lavaFound || event.rand.nextInt(16) == 0)) {
								world.setBlock(blockX, blockY, blockZ, ModBlocks.SULFUR_ORE, 0, 0);
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