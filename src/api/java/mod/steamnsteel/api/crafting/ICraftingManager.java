package mod.steamnsteel.api.crafting;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.api.crafting.ingredient.IIngredientSerializer;

/**
 * Created by codew on 26/01/2016.
 */
public interface ICraftingManager
{
    ICraftingManager registerInventorySerializer(Class<? extends IIngredient> inventoryClass, IIngredientSerializer serializer);
    ICraftingManagerIngredientsOrLabel addProjectTableRecipe();
    ICraftingAlloyFirstIngredient addAlloy();
}

