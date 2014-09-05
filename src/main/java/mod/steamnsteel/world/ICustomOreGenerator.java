package mod.steamnsteel.world;

import net.minecraft.world.World;

import java.util.Random;

public interface ICustomOreGenerator {
	void generate(OreConfiguration configuration, World world, Random random, int worldX, int worldZ);
}
