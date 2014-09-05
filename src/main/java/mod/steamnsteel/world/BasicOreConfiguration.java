package mod.steamnsteel.world;

import mod.steamnsteel.block.SteamNSteelBlock;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

class BasicOreConfiguration extends OreConfiguration {

	public final WorldGenerator worldGenerator;

	public BasicOreConfiguration(SteamNSteelBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight) {
		super(block, clusterCount, blocksPerCluster, minHeight, maxHeight);
		worldGenerator = new WorldGenMinable(block, blocksPerCluster);
	}
}