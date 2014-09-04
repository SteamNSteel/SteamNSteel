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

package mod.steamnsteel.crafting.alloy;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import mod.steamnsteel.api.crafting.IAlloyManager;
import mod.steamnsteel.api.crafting.IAlloyResult;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.utility.ItemWrapper;
import net.minecraft.item.ItemStack;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public enum AlloyManager implements IAlloyManager
{
    INSTANCE;

    private static final Table<ItemWrapper, ItemWrapper, IAlloyResult> alloys = HashBasedTable.create();

    @SuppressWarnings("MethodWithMultipleLoops")
    @Override
    public void addAlloy(IIngredient ingredientA, IIngredient ingredientB, ItemStack output)
    {
        final AlloyResult result = new AlloyResult(checkNotNull(output),
                checkNotNull(ingredientA).getQuantityConsumed(),
                checkNotNull(ingredientB).getQuantityConsumed());

        final List<ItemStack> itemStacksA = ImmutableList.copyOf(ingredientA.getItemStacks());
        final List<ItemStack> itemStacksB = ImmutableList.copyOf(ingredientB.getItemStacks());

        for (final ItemStack row : itemStacksA)
            for (final ItemStack column : itemStacksB)
                //noinspection ObjectAllocationInLoop
                alloys.put(new ItemWrapper(row), new ItemWrapper(column), result);
    }

    @Override
    public IAlloyResult getCupolaResult(ItemStack itemStackLeft, ItemStack itemStackRight)
    {
        if (itemStackLeft == null || itemStackRight == null)
            return AlloyResult.EMPTY;

        final ItemWrapper rowKey = new ItemWrapper(itemStackLeft);
        final ItemWrapper columnKey = new ItemWrapper(itemStackRight);

        final IAlloyResult result = alloys.get(rowKey, columnKey);

        if (result != null) return result;

        final IAlloyResult reversedResult = alloys.get(columnKey, rowKey);
        if (reversedResult != null)
            return new AlloyResult(reversedResult.getItemStack().orNull(),
                    reversedResult.getConsumedB(),
                    reversedResult.getConsumedA());

        return AlloyResult.EMPTY;
    }
}
