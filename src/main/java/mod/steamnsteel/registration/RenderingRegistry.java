package mod.steamnsteel.registration;

import com.google.common.base.Preconditions;
import mod.steamnsteel.Reference;
import mod.steamnsteel.client.model.PillarMultiModel;
import mod.steamnsteel.client.model.SNSMultiModel;
import mod.steamnsteel.library.ItemLibrary;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public final class RenderingRegistry
{
	private static final SNSMultiModel[] multiModels = {
			new PillarMultiModel()
	};

	@SubscribeEvent
	public static void onModelRegistryReady(ModelRegistryEvent event)
	{
		OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);

		setItemModel(ItemLibrary.mustyJournal);
		setItemModel(ItemLibrary.remnantRuinPillar);
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event)
	{
		for (final SNSMultiModel multiModel : multiModels)
		{
			multiModel.loadModel(event);
		}
	}


	private static void setItemModel(Item item)
	{
		final ResourceLocation registryName = item.getRegistryName();
		Preconditions.checkNotNull(registryName);

		final boolean isSimpleItem = !item.getHasSubtypes();
		if (isSimpleItem)
		{
			ModelLoader.setCustomModelResourceLocation(
					item,
					0,
					new ModelResourceLocation(registryName, "inventory")
			);
		} else
		{
			final NonNullList<ItemStack> subTypes = NonNullList.create();

			item.getSubItems(Reference.CREATIVE_TAB, subTypes);

			ModelLoader.setCustomMeshDefinition(item, stack ->
			{
				final ResourceLocation itemRegistryName = stack.getItem().getRegistryName();
				Preconditions.checkNotNull(itemRegistryName);
				return new ModelResourceLocation(itemRegistryName, getInventoryString(stack));
			});

			for (final ItemStack subType : subTypes) {
				ModelBakery.registerItemVariants(item, new ModelResourceLocation(registryName, getInventoryString(subType)));
			}
		}
	}

	private static String getInventoryString(ItemStack stack)
	{
		final StringBuilder builder = new StringBuilder(128);
		builder.append("inventory");
		final NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null)
		{
			for (final String key : nbt.getKeySet())
			{
				final NBTBase tag = nbt.getTag(key);
				if (tag instanceof NBTTagString)
				{
					builder.append(',');
					builder.append(key);
					builder.append('=');
					builder.append(((NBTTagString) tag).getString());
				}
			}
		}
		return builder.toString();
	}
}
