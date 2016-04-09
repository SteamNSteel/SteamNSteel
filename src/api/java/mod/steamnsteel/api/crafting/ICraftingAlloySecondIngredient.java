package mod.steamnsteel.api.crafting;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by codew on 10/04/2016.
 */
public interface ICraftingAlloySecondIngredient
{
    ICraftingAlloyResult andIngredient(IIngredient ingredient);
    ICraftingAlloyResult andIngredient(ItemStack ingredient);
    ICraftingAlloyResult andIngredient(Item item);
    ICraftingAlloyResult andIngredient(Item item, int amount);
    ICraftingAlloyResult andIngredient(Block block);
    ICraftingAlloyResult andIngredient(Block block, int amount);
}
