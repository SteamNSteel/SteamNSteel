package mod.steamnsteel.api.crafting;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.entity.player.InventoryPlayer;
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
}
