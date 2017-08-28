package mod.steamnsteel.block.resource.structure;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import mod.steamnsteel.TheMod;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RemnantRuinIronBarsBlock extends BlockPane
{
    public static final PropertyIronBarTextures IronBarsType = PropertyIronBarTextures.create("type", Arrays.asList(IronBarsTextures.VALUES));

    public RemnantRuinIronBarsBlock()
    {
        super(Material.IRON, true);
        setHardness(5.0F);
        setResistance(10.0F);
        setSoundType(SoundType.METAL);
        setCreativeTab(TheMod.CREATIVE_TAB);

        setDefaultState(
                blockState.getBaseState()
                        .withProperty(NORTH, false)
                        .withProperty(EAST, false)
                        .withProperty(SOUTH, false)
                        .withProperty(WEST, false)
                        .withProperty(IronBarsType, IronBarsTextures.RUSTED)
        );
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < IronBarsTextures.VALUES.length; ++i)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, IronBarsType);
    }

    @Override
    public String getUnlocalizedName()
    {
        return "tile." + getRegistryName();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(IronBarsType).ordinal();
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        return super.getStateFromMeta(meta)
                .withProperty(IronBarsType, IronBarsTextures.VALUES[meta & 15]);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    public enum IronBarsTextures implements IStringSerializable
    {
        RUSTED("rusted"),
        MOSSY("mossy"),
        RUSTED_MOSSY("rustedmossy");

        public static final IronBarsTextures[] VALUES = {
                RUSTED,
                MOSSY,
                RUSTED_MOSSY
        };

        private final String name;

        IronBarsTextures(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return name;
        }
    }

    public static class PropertyIronBarTextures extends PropertyEnum<IronBarsTextures>
    {
        protected PropertyIronBarTextures(String name, Collection<IronBarsTextures> values)
        {
            super(name, IronBarsTextures.class, values);
        }

        public static PropertyIronBarTextures create(String name)
        {
            /**
             * Create a new PropertyDirection with all directions that match the given Predicate
             */
            return create(name, Predicates.<IronBarsTextures>alwaysTrue());
        }

        /**
         * Create a new PropertyDirection with all directions that match the given Predicate
         */
        public static PropertyIronBarTextures create(String name, Predicate<IronBarsTextures> filter)
        {
            /**
             * Create a new PropertyDirection for the given direction values
             */
            return create(name, Collections2.filter(Lists.newArrayList(IronBarsTextures.VALUES), filter));
        }

        /**
         * Create a new PropertyDirection for the given direction values
         */
        public static PropertyIronBarTextures create(String name, Collection<IronBarsTextures> values)
        {
            return new PropertyIronBarTextures(name, values);
        }
    }
}
