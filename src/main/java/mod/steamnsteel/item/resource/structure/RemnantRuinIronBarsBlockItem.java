package mod.steamnsteel.item.resource.structure;

import mod.steamnsteel.block.resource.structure.RemnantRuinIronBarsBlock.IronBarsTextures;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by codew on 4/01/2016.
 */
public class RemnantRuinIronBarsBlockItem extends ItemBlock
{
    public RemnantRuinIronBarsBlockItem(Block block)
    {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int meta = itemStack.getItemDamage();
        if (meta > IronBarsTextures.VALUES.length) {
            meta = 0;
        }
        return getUnlocalizedName() + "_" + IronBarsTextures.VALUES[meta].getName();
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
