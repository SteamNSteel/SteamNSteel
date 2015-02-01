package mod.steamnsteel.block.resource.structure;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelPaneBlock;
import mod.steamnsteel.client.renderer.block.SteamNSteelPaneRenderer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import java.util.List;

public class RemnantRuinIronBarsBlock extends SteamNSteelPaneBlock
{
    public static final String NAME = "remnantRuinIronBars";
    public static final String[] NAMES = {"Rusted", "Mossy", "RustedMossy" };

    public RemnantRuinIronBarsBlock()
    {
        super(NAME, Material.iron);

        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setBlockName(NAME);
    }

    private static final IIcon[] iconsSide = new IIcon[NAMES.length];
    private static final IIcon[] iconsTopAndBottom = new IIcon[NAMES.length];

    @SideOnly(Side.CLIENT)
    public IIcon func_149735_b(int side, int metadata)
    {
        return iconsSide[metadata % iconsSide.length];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getTopAndBottomIcon(int metadata)
    {
        if (metadata > NAMES.length) {
            metadata = 0;
        }
        return iconsTopAndBottom[metadata];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        if (metadata > NAMES.length) {
            metadata = 0;
        }
        return this.func_149735_b(side, metadata);
    }

    public int damageDropped(int metadata)
    {
        return metadata;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < iconsSide.length; ++i)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 0;
    }

    @Override
    public int getRenderType()
    {
        return SteamNSteelPaneRenderer.INSTANCE.getRenderId();
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegistry)
    {
        super.registerBlockIcons(iconRegistry);

        for (int i = 0; i < NAMES.length; ++i)
        {
            iconsSide[i] = iconRegistry.registerIcon(TheMod.MOD_ID + ":" + "remnantRuinIronBars/" + NAMES[i]);
            iconsTopAndBottom[i] = iconRegistry.registerIcon(TheMod.MOD_ID + ":" + "remnantRuinIronBars/" + NAMES[i]);
        }
    }
}
