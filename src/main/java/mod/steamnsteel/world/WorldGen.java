package mod.steamnsteel.world;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.library.ModBlocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.CUSTOM;

public final class WorldGen {

	private BasicOreConfiguration copperConfiguration;
	private BasicOreConfiguration tinConfiguration;
	private BasicOreConfiguration zincConfiguration;

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
		//For reference:
		//       ironConfiguration = new OreConfiguration(Blocks.Iron, 20, 8, 0, 64);
		INSTANCE.copperConfiguration = new BasicOreConfiguration(ModBlocks.COPPER_ORE, 20, 6, 0, 64);
		INSTANCE.tinConfiguration = new BasicOreConfiguration(ModBlocks.TIN_ORE, 20, 3, 0, 64);
		INSTANCE.zincConfiguration = new BasicOreConfiguration(ModBlocks.ZINC_ORE, 20, 6, 0, 64);
	}

	@SubscribeEvent
	public void OnPostOreGenerated(OreGenEvent.Post event) {
		generateOre(copperConfiguration, event.world, event.rand, event.worldX, event.worldZ);
		generateOre(tinConfiguration, event.world, event.rand, event.worldX, event.worldZ);
		generateOre(zincConfiguration, event.world, event.rand, event.worldX, event.worldZ);
	}

	public void generateOre(BasicOreConfiguration configuration, World world, Random rand, int worldX, int worldZ) {
		if (TerrainGen.generateOre(world, rand, configuration.worldGenerator, worldX, worldZ, CUSTOM)) {
			for (int clusters = 0; clusters < configuration.clusterCount; ++clusters)
			{
				int x = worldX + rand.nextInt(16);
				int y = rand.nextInt(configuration.maxHeight - configuration.minHeight) + configuration.minHeight;
				int z = worldZ + rand.nextInt(16);
				configuration.worldGenerator.generate(world, rand, x, y, z);
			}
		}
	}

}
