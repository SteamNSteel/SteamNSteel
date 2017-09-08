package mod.steamnsteel.registration;

import mod.steamnsteel.Reference;
import mod.steamnsteel.item.ConcreteItemBlock;
import mod.steamnsteel.library.BlockLibrary;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemColored;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public final class ItemRegistration
{
	@SubscribeEvent
	public static void onRegisterItems(Register<Item> event) {
		final Items items = new Items(event.getRegistry());
		items.add(new Item(), Reference.Items.mustyJournal);

		items.add(BlockLibrary.remnantRuinPillar);
		items.add(BlockLibrary.concrete);
		items.add(new ConcreteItemBlock(BlockLibrary.concreteWet), BlockLibrary.concreteWet.getRegistryName() );
	}

	private static class Items
	{
		private final IForgeRegistry<Item> registry;

		Items(IForgeRegistry<Item> registry)
		{
			this.registry = registry;
		}

		void add(Block block)
		{
			final ItemBlock item = new ItemBlock(block);
			add(item, block.getRegistryName());
		}

		void addWithSubtypes(Block block, String... subTypeNames)
		{
			final ItemColored item = new ItemColored(block, true);
			item.setSubtypeNames(subTypeNames);
			add(item, block.getRegistryName());
		}

		void add(Item item, ResourceLocation registryName)
		{
			item
					.setRegistryName(registryName)
					.setUnlocalizedName(registryName.toString())
					.setCreativeTab(Reference.CREATIVE_TAB);
			registry.register(item);
		}

		void addBlockItem(ItemBlock item)
		{
			add(item, item.getBlock().getRegistryName());
		}
	}
}
