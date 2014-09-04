/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.api.crafting;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.item.ItemStack;

/**
 * The IAlloyManager is implemented to submit recipes to Steam and Steel machines.
 *
 * @author Scott Killen
 * @version 1.0
 * @since 0.1
 */
public interface IAlloyManager
{
    /**
     * Add an alloy recipe
     *
     * @param ingredient1 An instance of IIngredient
     * @param ingredient2 An instance of IIngredient
     * @param output      The ItemStack instance that is produced by the recipe
     */
    void addAlloy(@NotNull IIngredient ingredient1, @NotNull IIngredient ingredient2, @NotNull ItemStack output);

    /**
     * Retrieves the result produced by the two ingredients. Ore Dictionary compatible. If a result is not found with
     * the ingredients in the order passed, they ingredients will be swapped and the retrieval performed again.
     *
     * @param ingredient1 An ItemStack used as an alloy ingredient
     * @param ingredient2 An ItemStack used as an alloy ingredient
     * @return The resulting {@link mod.steamnsteel.api.crafting.IAlloyResult}, or an empty instance of
     * {@link mod.steamnsteel.api.crafting.IAlloyResult} if no result is found.
     */
    @NotNull
    IAlloyResult getCupolaResult(@Nullable ItemStack ingredient1, @Nullable ItemStack ingredient2);
}
