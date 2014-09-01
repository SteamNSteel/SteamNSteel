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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.sun.istack.internal.Nullable;
import mod.steamnsteel.api.crafting.IAlloyResult;
import net.minecraft.item.ItemStack;

public class AlloyResult implements IAlloyResult
{
    public static final AlloyResult EMPTY = new AlloyResult(null, 0, 0);

    @Nullable
    private final ItemStack itemStack;
    private final int consumedFirst;
    private final int consumedSecond;

    AlloyResult(ItemStack itemStack, int consumedFirst, int consumedSecond)
    {
        this.itemStack = itemStack;
        this.consumedFirst = consumedFirst;
        this.consumedSecond = consumedSecond;
    }

    @Override
    public Optional<ItemStack> getItemStack() { return Optional.fromNullable(itemStack); }

    @Override
    public int getConsumedFirst() { return consumedFirst; }

    @Override
    public int getConsumedSecond() { return consumedSecond; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AlloyResult that = (AlloyResult) o;
        return consumedFirst == that.consumedFirst && consumedSecond == that.consumedSecond &&
                (itemStack != null ? itemStack.equals(that.itemStack) : that.itemStack == null);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(itemStack, consumedFirst, consumedSecond);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("itemStack", itemStack)
                .add("consumedFirst", consumedFirst)
                .add("consumedSecond", consumedSecond)
                .toString();
    }
}
