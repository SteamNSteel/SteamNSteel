package mod.steamnsteel.api.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by codew on 10/04/2016.
 */
public interface ICraftingAlloyResult
{
    ICraftingManager produces(ItemStack result);
    ICraftingManager produces(Item item);
    ICraftingManager produces(Item item, int amount);
    ICraftingManager produces(Block block);
    ICraftingManager produces(Block block, int amount);
}
