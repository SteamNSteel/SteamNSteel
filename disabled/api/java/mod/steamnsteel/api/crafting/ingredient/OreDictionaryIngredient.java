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
import net.minecraftforge.oredict.OreDictionary;

import static com.google.common.base.Preconditions.*;

/**
 * An implementation of IIngredient that allows OreDictionary ore names to be submitted as ingredients.
 *
 * @author Scott Killen
 * @version 1.0
 * @see mod.steamnsteel.api.crafting.ingredient.IIngredient
 * @since 0.1
 */
public class OreDictionaryIngredient implements IIngredient
{
    private final String name;
    private final int quantityConsumed;

    /**
     * Class constructor specifying an ore name. A quantity consumed of 1 is assumed.
     *
     * @param name The ore name to use as an ingredient. Must not be <code>null</code> nor empty.
     */
    public OreDictionaryIngredient(String name)
    {
        this(name, 1);
    }

    /**
     * Class constructor specifying an ore name and quantity consumed.
     *
     * @param name             The ore name to use as an ingredient.
     * @param quantityConsumed The quantity consumed upon successful crafting
     */
    public OreDictionaryIngredient(String name, int quantityConsumed)
    {
        checkArgument(!checkNotNull(name).isEmpty());
        this.name = name;
        this.quantityConsumed = quantityConsumed;
    }

    /**
     * Returns a list of ItemStack aliases for this ingredient.
     *
     * @return A  list of ItemStack aliases for this ingredient.
     */
    @Override
    public ImmutableList<ItemStack> getItemStacks()
    {
        return ImmutableList.copyOf(OreDictionary.getOres(name));
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
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("name", name)
                .add("quantityConsumed", quantityConsumed)
                .toString();
    }

    public String getName()
    {
        return name;
    }
}
