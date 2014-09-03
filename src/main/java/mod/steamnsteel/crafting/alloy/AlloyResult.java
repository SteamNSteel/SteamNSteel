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
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import mod.steamnsteel.api.crafting.IAlloyResult;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AlloyResult implements IAlloyResult
{
    public static final AlloyResult EMPTY = new AlloyResult(null, 0, 0);

    @Nullable
    private final Item item;
    private final int meta;
    private final int quantity;
    private final int consumedA;
    private final int consumedB;

    AlloyResult(ItemStack itemStack, int consumedA, int consumedB)
    {
        //noinspection AssignmentToNull
        item = itemStack == null ? null : itemStack.getItem();
        meta = itemStack == null ? 0 : itemStack.getItemDamage();
        quantity = itemStack == null ? 0 : itemStack.stackSize;
        this.consumedA = consumedA;
        this.consumedB = consumedB;
    }

    @NotNull
    @Override
    public Optional<ItemStack> getItemStack()
    {
        if (item == null)
            return Optional.absent();

        return Optional.of(new ItemStack(item, quantity, meta));
    }

    @Override
    public int getConsumedA() { return consumedA; }

    @Override
    public int getConsumedB() { return consumedB; }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AlloyResult that = (AlloyResult) o;

        if (consumedA != that.consumedA || consumedB != that.consumedB) return false;
        return meta == that.meta && quantity == that.quantity && item == that.item;
    }

    @Override
    public int hashCode()
    {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + meta;
        result = 31 * result + quantity;
        result = 31 * result + consumedA;
        result = 31 * result + consumedB;
        return result;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("item", item)
                .add("meta", meta)
                .add("quantity", quantity)
                .add("consumedA", consumedA)
                .add("consumedB", consumedB)
                .toString();
    }
}
