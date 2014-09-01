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

import com.google.common.base.Optional;
import com.sun.istack.internal.NotNull;
import net.minecraft.item.ItemStack;

/**
 * The IAlloyResult is implemented by Results from {@link mod.steamnsteel.api.crafting.IAlloyManager}.
 *
 * @author Scott Killen
 * @version 1.0
 * @since 0.1
 */
public interface IAlloyResult
{
    /**
     * Returns the ItemStack resulting from the submitted ingredients. The stackSize is significant. If the result is
     * empty, getItemStack().isPresent() will return false;
     *
     * @return The ItemStack resulting from the submitted ingredients.
     */
    @NotNull
    Optional<ItemStack> getItemStack();

    /**
     * Returns the amount consumed for the first ingredient.
     *
     * @return The amount consumed for the first ingredient.
     * @see IAlloyManager#getCupolaResult(net.minecraft.item.ItemStack, net.minecraft.item.ItemStack)
     */
    int getConsumedFirst();

    /**
     * Returns the amount consumed for the second ingredient.
     *
     * @return The amount consumed for the second ingredient.
     * @see IAlloyManager#getCupolaResult(net.minecraft.item.ItemStack, net.minecraft.item.ItemStack)
     */
    int getConsumedSecond();
}
