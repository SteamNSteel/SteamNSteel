package mod.steamnsteel.api.crafting;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.Collection;

public interface ICraftingManagerIngredientOrResult extends ICraftingManagerResult
{
    ICraftingManagerIngredientOrResult andIngredient(Item item);
    ICraftingManagerIngredientOrResult andIngredient(Item item, int amount);
    ICraftingManagerIngredientOrResult andIngredient(Block block);
    ICraftingManagerIngredientOrResult andIngredient(Block block, int amount);
    ICraftingManagerIngredientOrResult andIngredient(IIngredient ingredient);
    ICraftingManagerIngredientOrResult andIngredient(ItemStack ingredient);
    ICraftingManagerIngredientOrResult andIngredients(ItemStack... ingredients);
    ICraftingManagerIngredientOrResult andIngredients(IIngredient... ingredients);
}
