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
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
    @NotNull
    private final ItemStack itemStack;
    private final int quantityConsumed;

    /**
     * Class constructor specifying an ItemStack. The quantity consumed is deduced from the stackSize of the ItemStack.
     *
     * @param itemStack The ore name to use as an ingredient. This must not be <code>null</code> nor an empty stack. A
     *                  reference to itemStack is <i>not</i> maintained.
     */
    public ItemStackIngredient(@NotNull ItemStack itemStack)
    {
        checkArgument(checkNotNull(itemStack).stackSize > 0);
        checkNotNull(itemStack.getItem());
        this.itemStack = itemStack.copy();
        quantityConsumed = itemStack.stackSize;
    }

    /**
     * Determine whether an ItemStack matches this ingredient
     *
     * @param itemStack The ItemStack to check for a match with this ingredient.
     * @return <CODE>true</CODE> if itemStack is a match, <CODE>false</CODE> otherwise.
     */
    @Override
    public boolean isMatch(@Nullable ItemStack itemStack)
    {
        return OreDictionary.itemMatches(this.itemStack, itemStack, false);
    }

    /**
     * Return the quantity of this ingredient to be consumed on a successful use.
     *
     * @return The quantity of this ingredient to be consumed on a successful use.
     */
    @Override
    public int getQuantityConsumed()
    {
        return quantityConsumed;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemStackIngredient that = (ItemStackIngredient) o;

        return itemStack != null ? OreDictionary.itemMatches(itemStack, that.itemStack, true) && quantityConsumed == that.quantityConsumed : that.itemStack == null;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(itemStack.getItem(), itemStack.getItemDamage(), quantityConsumed);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("itemStack", itemStack)
                .add("quantityConsumed", quantityConsumed)
                .toString();
    }
}
