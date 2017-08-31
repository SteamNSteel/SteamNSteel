package mod.steamnsteel.library;

import mod.steamnsteel.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ItemLibrary
{
	@ObjectHolder("musty_journal")
	public static Item mustyJournal;

	@ObjectHolder("remnant_ruin_pillar")
	public static ItemBlock remnantRuinPillar;

	@ObjectHolder("concrete")
	public static ItemBlock concrete;
	@ObjectHolder("concrete_wet")
	public static ItemBlock concreteWet;
}
