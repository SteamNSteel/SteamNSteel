package mod.steamnsteel.api.crafting;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by codew on 10/04/2016.
 */
public interface ICraftingAlloyFirstIngredient
{
    ICraftingAlloySecondIngredient withIngredient(IIngredient ingredient);
    ICraftingAlloySecondIngredient withIngredient(ItemStack ingredient);
    ICraftingAlloySecondIngredient withIngredient(Item item);
    ICraftingAlloySecondIngredient withIngredient(Item item, int amount);
    ICraftingAlloySecondIngredient withIngredient(Block block);
    ICraftingAlloySecondIngredient withIngredient(Block block, int amount);
}
