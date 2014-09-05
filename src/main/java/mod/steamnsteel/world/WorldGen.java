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

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.library.ModBlocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

public class WorldGen {

	private OreConfiguration sulfurConfiguration;
	private ICustomOreGenerator sulfurGenerator;

	private WorldGen() {
	}

	public static WorldGen INSTANCE;
	public static void init() {
		INSTANCE = new WorldGen();
		INSTANCE.createCustomGenerators();
		INSTANCE.createConfigurations();
		INSTANCE.registerToEventBus();
	}

	private void registerToEventBus() {
		MinecraftForge.EVENT_BUS.register(WorldGen.INSTANCE);
	}

	private void createConfigurations() {
		OreConfiguration sulfurConfig = new OreConfiguration(ModBlocks.SULFUR_ORE, 3, 15, 0, 64);
		sulfurConfiguration = sulfurConfig;
	}

	private void createCustomGenerators() {
		sulfurGenerator = new WorldGenSulfur();
	}

	@SubscribeEvent
	public void onPopulateChunkEvent(DecorateBiomeEvent.Post event) {
		//event.chunkX is actually block space, not chunk space.
		//int chunkX = event.chunkX >> 4;
		//int chunkZ = event.chunkZ >> 4;

		sulfurGenerator.generate(sulfurConfiguration, event.world, event.rand, event.chunkX, event.chunkZ);
	}
}