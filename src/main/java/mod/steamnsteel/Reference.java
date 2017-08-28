package mod.steamnsteel;

import mod.steamnsteel.library.ItemLibrary;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class Reference
{
	public static final String MOD_ID = "steamnsteel";
	public static final String MOD_NAME = "Steam and Steel";
	public static final String MOD_VERSION = "@MOD_VERSION@";
	//public static final String MOD_GUI_FACTORY = "mod.steamnsteel.configuration.client.ModGuiFactory";

	@SuppressWarnings("AnonymousInnerClass")
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID.toLowerCase())
	{
		public ItemStack cachedStack;

		@Override
		public ItemStack getTabIconItem()
		{

			return (cachedStack != null ? cachedStack : (cachedStack = new ItemStack(ItemLibrary.mustyJournal)));
		}
	};

	public static final class Items
	{
		public static ResourceLocation mustyJournal = resource("musty_journal");
	}

	private Reference() {}

	private static ResourceLocation resource(String resourceName) {
		return new ResourceLocation(MOD_ID, resourceName);
	}
}
