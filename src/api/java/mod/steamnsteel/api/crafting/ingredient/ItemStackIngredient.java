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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

import static com.google.common.base.Preconditions.*;

/**
 * An implementation of IIngredient that allows ItemStack instances to be submitted as ingredients.
 *
 * @author Scott Killen
 * @version 1.0
 * @see mod.steamnsteel.api.crafting.ingredient.IIngredient
 * @since 0.1
 */
public class ItemStackIngredient implements IIngredient
{
    private final ItemStack itemStack;

    /**
     * Class constructor specifying an ItemStack. The quantity consumed is deduced from the stackSize of the ItemStack.
     *
     * @param itemStack The ore name to use as an ingredient. This must not be <code>null</code> nor an empty stack. A
     *                  reference to itemStack is <i>not</i> maintained.
     */
    public ItemStackIngredient(ItemStack itemStack)
    {
        checkArgument(checkNotNull(itemStack).func_190916_E() > 0);
        checkNotNull(itemStack.getItem());
        this.itemStack = itemStack.copy();
    }

    /**
     * Returns a list of ItemStack aliases for this ingredient.
     *
     * @return A  list of ItemStack aliases for this ingredient.
     */
    @Override
    public ImmutableList<ItemStack> getItemStacks()
    {
        return ImmutableList.of(itemStack);
    }

    /**
     * Return the quantity of this ingredient to be consumed on a successful use.
     *
     * @return The quantity of this ingredient to be consumed on a successful use.
     */
    @Override
    public int getQuantityConsumed()
    {
        return itemStack.func_190916_E();
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("itemStack", itemStack)
                .add("quantityConsumed", itemStack.func_190916_E())
                .toString();
    }

    public ItemStack getItemStack()
    {
        return itemStack.copy();
    }
}
