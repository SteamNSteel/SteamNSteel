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

package mod.steamnsteel.api.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

/**
 * The IIngredient is implemented to submit recipes to Steam and Steel machines.
 *
 * @author Scott Killen
 * @version 1.0
 * @see mod.steamnsteel.api.crafting.ingredient.ItemStackIngredient
 * @see mod.steamnsteel.api.crafting.ingredient.OreDictionaryIngredient
 * @since 0.1
 */
public interface IIngredient
{
    /**
     * Returns a list of ItemStack aliases for this ingredient.
     *
     * @return A  list of ItemStack aliases for this ingredient.
     */
    ImmutableList<ItemStack> getItemStacks();

    /**
     * Returns the quantity of this ingredient to be consumed on a successful use.
     *
     * @return The quantity of this ingredient to be consumed on a successful use.
     */
    int getQuantityConsumed();
}
