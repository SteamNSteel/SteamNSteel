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

package mod.steamnsteel.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import mod.steamnsteel.library.ModBlocks;
import mod.steamnsteel.library.ModItems;
import net.minecraft.item.ItemStack;

@SuppressWarnings("MethodMayBeStatic")
class Smelting
{
    void init()
    {
        final float xp = 0.5F;
        GameRegistry.addSmelting(ModBlocks.COPPER_ORE, new ItemStack(ModItems.COPPER_INGOT), xp);
        GameRegistry.addSmelting(ModBlocks.TIN_ORE, new ItemStack(ModItems.TIN_INGOT), xp);
        GameRegistry.addSmelting(ModBlocks.ZINC_ORE, new ItemStack(ModItems.ZINC_INGOT), xp);
    }
}
