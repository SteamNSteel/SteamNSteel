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

import akka.event.Logging;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.library.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class TerrainGenEventHandler {
	public static final TerrainGenEventHandler INSTANCE = new TerrainGenEventHandler();
	private TerrainGenEventHandler() {}

	@SubscribeEvent
	public void onPopulateChunkEvent(PopulateChunkEvent.Populate event) {
		if (event.type == PopulateChunkEvent.Populate.EventType.LAVA && !event.hasVillageGenerated && event.rand.nextInt(8) == 0) {
			//Stolen from ChunkProviderGenerate
			int blockX = event.chunkX * 16;
			int blockZ = event.chunkZ * 16;
			Random rand = event.rand;
			int x = blockX + rand.nextInt(16) + 8;
			int y = rand.nextInt(rand.nextInt(248) + 8);
			int z = blockZ + rand.nextInt(16) + 8;

			if (y < 63 || rand.nextInt(10) == 0)
			{
				World world = event.world;
				(new WorldGenLakes(Blocks.lava)).generate(world, rand, x, y, z);
				calculateSulphurEdges(world, rand, x, y, z);
			}
			event.setResult(Event.Result.DENY);
		}
	}

	static final int[][] neighbours = {
			{0, 0, 1},
			{0, 1, 0},
			{1, 0, 0},
			{0, 0, -1},
			{0, -1, 0},
			{-1, 0, 0}
	};

	private void calculateSulphurEdges(World world, Random rand, int worldX, int worldY, int worldZ) {
		for (int x = 0; x < 16; ++x)
		{
			int blockX = worldX + x;
			for (int z = 0; z < 16; ++z)
			{
				int blockZ = worldZ + z;
				for (int y = 0; y < 8; ++y)
				{
					int blockY = worldY + y;
					if (world.getBlock(blockX, blockY, blockZ) == Blocks.stone)
					{
						for (int[] neighbour : neighbours)
						{
							if (world.getBlock(blockX + neighbour[0], blockY + neighbour[1], blockZ + neighbour[2]) == Blocks.lava)
							{
								FMLLog.log(TheMod.MOD_ID, Level.INFO, "sulfur @ (%d, %d, %d)", blockX, blockY, blockZ);
										world.setBlock(blockX, blockY, blockZ, ModBlocks.SULFUR_ORE);
								break;
							}
						}
					}
				}
			}
		}
	}
}
