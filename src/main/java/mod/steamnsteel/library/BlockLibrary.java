package mod.steamnsteel.library;

import mod.steamnsteel.Reference;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class BlockLibrary
{
	@ObjectHolder("remnant_ruin_pillar")
	public static Block remnantRuinPillar;
	@ObjectHolder("concrete")
	public static Block concrete;
	@ObjectHolder("concrete_wet")
	public static Block concreteWet;
}

