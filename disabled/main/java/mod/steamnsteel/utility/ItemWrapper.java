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

package mod.steamnsteel.utility;

import com.google.common.base.Objects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.google.common.base.Preconditions.*;

public class ItemWrapper
{
    private final Item item;
    private final int meta;

    private ItemWrapper(Item item, int meta)
    {
        this.item = checkNotNull(item);
        this.meta = meta;
    }

    public ItemWrapper(ItemStack itemStack)
    {
        this(checkNotNull(itemStack).getItem(), itemStack.getItemDamage());
    }

    public ItemStack getStack()
    {
        return new ItemStack(item, 1, meta);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemWrapper that = (ItemWrapper) o;
        return meta == that.meta && item == that.item;
    }

    @Override
    public int hashCode() { return 31 * item.hashCode() + meta; }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("item", item)
                .add("meta", meta)
                .toString();
    }
}
