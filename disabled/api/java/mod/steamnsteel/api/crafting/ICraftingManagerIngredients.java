package mod.steamnsteel.api.crafting;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.Collection;

/**
 * Created by codew on 9/04/2016.
 */
public interface ICraftingManagerIngredients
{
    ICraftingManagerIngredientOrResult withIngredient(Item item);
    ICraftingManagerIngredientOrResult withIngredient(Item item, int amount);
    ICraftingManagerIngredientOrResult withIngredient(Block block);
    ICraftingManagerIngredientOrResult withIngredient(Block block, int amount);
    ICraftingManagerIngredientOrResult withIngredient(IIngredient ingredient);
    ICraftingManagerIngredientOrResult withIngredient(ItemStack ingredient);
    ICraftingManagerIngredientOrResult withIngredients(ItemStack... ingredients);
    ICraftingManagerIngredientOrResult withIngredients(IIngredient... ingredient);
}
