package mod.steamnsteel.world;

import mod.steamnsteel.block.SteamNSteelBlock;

class OreConfiguration {
	public final SteamNSteelBlock block;
	public final int clusterCount;
	public final int blocksPerCluster;
	public final int minHeight;
	public final int maxHeight;

	public OreConfiguration(SteamNSteelBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight) {
		this.block = block;
		this.clusterCount = clusterCount;
		this.blocksPerCluster = blocksPerCluster;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;

	}

}
