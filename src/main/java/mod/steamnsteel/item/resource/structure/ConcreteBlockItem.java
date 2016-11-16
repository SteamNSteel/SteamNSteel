package mod.steamnsteel.item.resource.structure;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * <p>
 * Info about this class goes HERE!
 * </p>
 *
 * @author Bret 'Horfius' Dusseault
 * @version ${VERSION}
 */
public class ConcreteBlockItem extends ItemBlock {

    public static final String NAME_WET = "item_wet";
    public static final String NAME_DRY = "item_dry";

    public ConcreteBlockItem(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        if(meta > 5) meta = 5;

        return super.getUnlocalizedName() + '_' + (meta == 5 ? NAME_DRY : NAME_WET);
    }

    @Override
    public int getMetadata(int damage) {
        return damage == 5 ? 5 : 0;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 5));
    }
}
