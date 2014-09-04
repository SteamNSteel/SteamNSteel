package mod.steamnsteel.world;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.library.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.CUSTOM;

public final class WorldGen {

	private OreConfiguration copperConfiguration;
	private OreConfiguration tinConfiguration;
	private OreConfiguration zincConfiguration;

	public static WorldGen INSTANCE;

	public static void init() {
		INSTANCE = new WorldGen();
		INSTANCE.createConfigurations();
		INSTANCE.registerToEventBus();
	}

	private void registerToEventBus() {
		MinecraftForge.ORE_GEN_BUS.register(INSTANCE);
	}

	private void createConfigurations() {
		//TODO:  read these from configuration
		INSTANCE.copperConfiguration = new OreConfiguration(ModBlocks.COPPER_ORE, 20, 8, 0, 64);
		INSTANCE.tinConfiguration = new OreConfiguration(ModBlocks.TIN_ORE, 20, 8, 0, 64);
		INSTANCE.zincConfiguration = new OreConfiguration(ModBlocks.ZINC_ORE, 20, 8, 0, 64);
	}

	@SubscribeEvent
	public void OnPostOreGenerated(OreGenEvent.Post event) {
		generateOre(copperConfiguration, event.world, event.rand, event.worldX, event.worldZ);
		generateOre(tinConfiguration, event.world, event.rand, event.worldX, event.worldZ);
		generateOre(zincConfiguration, event.world, event.rand, event.worldX, event.worldZ);
	}

	public void generateOre(OreConfiguration configuration, World world, Random rand, int worldX, int worldZ) {
		if (TerrainGen.generateOre(world, rand, configuration.worldGenMinable, worldX, worldZ, CUSTOM)) {
			for (int clusters = 0; clusters < configuration.clusterCount; ++clusters)
			{
				int x = worldX + rand.nextInt(16);
				int y = rand.nextInt(configuration.maxHeight - configuration.minHeight) + configuration.minHeight;
				int z = worldZ + rand.nextInt(16);
				configuration.worldGenMinable.generate(world, rand, x, y, z);
			}
		}
	}

	private static class OreConfiguration {
		public final SteamNSteelBlock block;
		public final int clusterCount;
		public final int minHeight;
		public final int maxHeight;
		public final WorldGenMinable worldGenMinable;

		public OreConfiguration(SteamNSteelBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight) {
			this.block = block;
			this.clusterCount = clusterCount;
			this.minHeight = minHeight;
			this.maxHeight = maxHeight;
			worldGenMinable = new WorldGenMinable(block, blocksPerCluster);
		}
	}
}
