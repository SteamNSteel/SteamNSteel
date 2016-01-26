package mod.steamnsteel.crafting.projecttable;

import com.google.common.collect.Lists;
import mod.steamnsteel.api.crafting.IProjectTableManager;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import java.util.List;

/**
 * Created by codew on 25/01/2016.
 */
public class ProjectTableManager implements IProjectTableManager
{
    private List<ProjectTableRecipe> recipes = Lists.newArrayList();

    @Override
    public void addProjectTableRecipe(Collection<ItemStack> output, String displayName, Collection<IIngredient> ingredients)
    {
        recipes.add(new ProjectTableRecipe(output, ingredients, displayName));
    }

    @Override
    public void addProjectTableRecipeProjectTableRecipe(ItemStack output, Collection<ItemStack> ingredients)
    {

    }

    @Override
    public void ProjectTableRecipe(ItemStack output, ItemStack... ingredients)
    {

    }
}
