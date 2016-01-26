package mod.steamnsteel.api.crafting;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.item.ItemStack;
import java.util.Collection;

/**
 * Created by codew on 25/01/2016.
 */
public interface IProjectTableManager
{
    /**
     * Add a project Table Recipe
     *
     * @param output      The ItemStack instance that is produced by the recipe
     * @param displayName The name to be localized on the Project Table
     * @param ingredients An instance of IIngredient
     */
    void addProjectTableRecipe(Collection<ItemStack> output, String displayName, Collection<IIngredient> ingredients);

    /**
     * Add a project Table Recipe, the display name will be inferred from the output.
     *
     * @param ingredients An instance of IIngredient
     * @param output      The ItemStack instance that is produced by the recipe
     */
    void addProjectTableRecipeProjectTableRecipe(ItemStack output, Collection<ItemStack> ingredients);

    /**
     * Add a project Table Recipe, the display name will be inferred from the output.
     *
     * @param output      The ItemStack instance that is produced by the recipe
     * @param ingredients An instance of IIngredient
     */
    void ProjectTableRecipe(ItemStack output, ItemStack... ingredients);
}
