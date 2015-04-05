package mod.steamnsteel.blockitem;

import mod.steamnsteel.block.resource.structure.RemnantRuinIronBarsBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class RemnantRuinIronBarsItemBlock extends ItemBlockWithMetadata
{
    public RemnantRuinIronBarsItemBlock(Block block)
    {
        super(block, block);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int meta = itemStack.getItemDamage();
        if (meta > RemnantRuinIronBarsBlock.NAMES.length) {
            meta = 0;
        }
        return getUnlocalizedName() + "_" + RemnantRuinIronBarsBlock.NAMES[meta];
    }
}
