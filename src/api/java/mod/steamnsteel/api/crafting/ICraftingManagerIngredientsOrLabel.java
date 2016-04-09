package mod.steamnsteel.api.crafting;

import net.minecraft.item.ItemStack;
import java.util.Collection;

public interface ICraftingManagerIngredientsOrLabel extends ICraftingManagerIngredients
{
    ICraftingManagerIngredients withLabel(String label);
}
