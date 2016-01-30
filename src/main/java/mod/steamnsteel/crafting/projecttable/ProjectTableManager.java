package mod.steamnsteel.crafting.projecttable;

import com.google.common.collect.Lists;
import mod.steamnsteel.api.crafting.IProjectTableManager;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import scala.collection.generic.CanBuildFrom;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by codew on 25/01/2016.
 */
public enum ProjectTableManager implements IProjectTableManager
{
    INSTANCE;

    private List<ProjectTableRecipe> recipes = Lists.newArrayList();

    @Override
    public void addProjectTableRecipe(Collection<ItemStack> output, String displayName, Collection<IIngredient> ingredients)
    {
        recipes.add(new ProjectTableRecipe(output, displayName, ingredients));
    }

    @Override
    public void addProjectTableRecipe(ItemStack output, Collection<IIngredient> ingredients)
    {
        recipes.add(new ProjectTableRecipe(Lists.newArrayList(output), output.getDisplayName(), ingredients));
    }

    @Override
    public void addProjectTableRecipe(ItemStack output, IIngredient... ingredients)
    {
        recipes.add(new ProjectTableRecipe(Lists.newArrayList(output), output.getDisplayName(), Arrays.asList(ingredients)));
    }

    @Override
    public boolean canCraftRecipe(ProjectTableRecipe recipe, InventoryPlayer playerInventory)
    {
        final List<ItemStack> compactedInventoryItems = getCompactedInventoryItems(playerInventory);

        boolean canCraft = true;
        for (final IIngredient recipeIngredient : recipe.getInput())
        {
            boolean itemMatched = false;
            int itemsAvailable = 0;
            for (final ItemStack recipeInput : recipeIngredient.getItemStacks())
            {
                for (final ItemStack playerItem : compactedInventoryItems) {
                    if (recipeInput.getItem() == playerItem.getItem() && recipeInput.getMetadata() == playerItem.getMetadata() && ItemStack.areItemStackTagsEqual(recipeInput, playerItem)) {
                        itemMatched = true;
                        itemsAvailable += playerItem.stackSize;
                    }
                }
            }

            if (itemsAvailable < recipeIngredient.getQuantityConsumed() || !itemMatched) {
                return false;
            }
        }

        return true;
    }

    private List<ItemStack> getCompactedInventoryItems(InventoryPlayer inventorySlots) {
        List<ItemStack> usableItems = Lists.newArrayList();
        for (final ItemStack itemStack : inventorySlots.mainInventory)
        {
            if (itemStack == null || itemStack.getItem() == null)
            {
                continue;
            }

            boolean itemMatched = false;
            for (final ItemStack existingItemStack : usableItems) {
                if (existingItemStack.getItem() == itemStack.getItem() && existingItemStack.getMetadata() == itemStack.getMetadata() && ItemStack.areItemStackTagsEqual(existingItemStack, itemStack))
                {
                    itemMatched = true;
                    existingItemStack.stackSize += itemStack.stackSize;
                }
            }
            if (!itemMatched) {
                final ItemStack copy = itemStack.copy();
                usableItems.add(copy);
            }
        }
        return usableItems;
    }

    public Collection<ProjectTableRecipe> getRecipes()
    {
        return recipes;
    }


}
