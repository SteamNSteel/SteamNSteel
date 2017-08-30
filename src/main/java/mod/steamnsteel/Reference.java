package mod.steamnsteel;

import mod.steamnsteel.library.ItemLibrary;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("UtilityClass")
public final class Reference
{
	public static final String MOD_ID = "steamnsteel";
	static final String MOD_NAME = "Steam and Steel";
	static final String MOD_VERSION = "@MOD_VERSION@";
	//public static final String MOD_GUI_FACTORY = "mod.steamnsteel.configuration.client.ModGuiFactory";

	@SuppressWarnings("AnonymousInnerClass")
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID.toLowerCase())
	{
		private ItemStack cachedStack = null;

		@Override
		public ItemStack getTabIconItem()
		{
			if (cachedStack == null)
			{
				cachedStack = new ItemStack(ItemLibrary.mustyJournal);
			}
			return cachedStack;
		}
	};

	public static final class Items
	{
		public static final ResourceLocation mustyJournal = resource("musty_journal");

		private Items() {}
	}

	public static final class Blocks
	{
		public static final ResourceLocation remnantRuinPillar = resource("remnant_ruin_pillar");

		private Blocks() {}
	}

	public static final class BlockProperties
	{
		public static final PropertyDirection HORIZONTAL_FACING = BlockHorizontal.FACING;

		public static final PropertyDirection FACING = BlockDirectional.FACING;

		public static final PropertyBool CONNECT_TOP = PropertyBool.create("connect_top");
		public static final PropertyBool CONNECT_BOTTOM = PropertyBool.create("connect_bottom");
	}

	private Reference() {}

	private static ResourceLocation resource(final String resourceName) {
		return new ResourceLocation(MOD_ID, resourceName);
	}
}
