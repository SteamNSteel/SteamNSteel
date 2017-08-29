package mod.steamnsteel.registration;

import mod.steamnsteel.Reference;
import mod.steamnsteel.block.aesthetic.RemnantRuinPillarBlock;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public final class BlockRegistration
{
	@SubscribeEvent
	public static void onRegisterBlocks(Register<Block> event) {
		Blocks blocks = new Blocks(event.getRegistry());

		blocks.add(new RemnantRuinPillarBlock(), Reference.Blocks.remnantRuinPillar);

	}

	private static class Blocks
	{
		private final IForgeRegistry<Block> registry;

		Blocks(IForgeRegistry<Block> registry)
		{
			this.registry = registry;
		}

		<B extends Block> void add(B block, ResourceLocation registryName)
		{
			block.setRegistryName(registryName)
					.setUnlocalizedName(registryName.toString())
					.setCreativeTab(Reference.CREATIVE_TAB);

			registry.register(block);
		}

		@SuppressWarnings("MethodMayBeStatic")
		public void add(Class<? extends TileEntity> tileEntityClass, ResourceLocation id)
		{
			GameRegistry.registerTileEntity(tileEntityClass, "tile." + id);
		}
	}
}
