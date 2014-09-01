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

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import mod.steamnsteel.api.crafting.IAlloyManager;
import mod.steamnsteel.api.crafting.IAlloyResult;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import net.minecraft.item.ItemStack;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

public enum AlloyManager implements IAlloyManager
{
    INSTANCE;

    private static final Table<IIngredient, IIngredient, ItemStack> alloys = HashBasedTable.create();

    private static Optional<IIngredient> findMatchingIngredient(Set<IIngredient> ingredients, ItemStack itemStack)
    {
        for (final IIngredient ingredient : ingredients)
        {
            if (ingredient.isMatch(itemStack))
                return Optional.of(ingredient);
        }

        return Optional.absent();
    }

    @Override
    public void addAlloy(@NotNull IIngredient ingredient1, @NotNull IIngredient ingredient2, @NotNull ItemStack output)
    {
        checkNotNull(ingredient1, "Input may not be null.");
        checkNotNull(ingredient2, "Input may not be null.");
        checkNotNull(output, "output may not be null.");

        alloys.put(ingredient1, ingredient2, output);
    }

    @Override
    @NotNull
    public IAlloyResult getCupolaResult(@Nullable ItemStack itemStackLeft, @Nullable ItemStack itemStackRight)
    {
        final Map<IIngredient, Map<IIngredient, ItemStack>> rowMap = alloys.rowMap();

        boolean reverse = false;
        Optional<IIngredient> row = findMatchingIngredient(rowMap.keySet(), itemStackLeft);
        if (!row.isPresent())
        {
            reverse = true;
            row = findMatchingIngredient(rowMap.keySet(), itemStackRight);
        }
        if (row.isPresent())
        {
            final Optional<IIngredient> column = findMatchingIngredient(alloys.row(row.get()).keySet(), reverse ? itemStackLeft : itemStackRight);
            if (column.isPresent())
                return new AlloyResult(alloys.get(row, column),
                        reverse ? column.get().getQuantityConsumed() : row.get().getQuantityConsumed(),
                        reverse ? row.get().getQuantityConsumed() : column.get().getQuantityConsumed());
        }
        return AlloyResult.EMPTY;
    }
}
