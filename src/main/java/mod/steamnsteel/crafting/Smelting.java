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
import mod.steamnsteel.api.crafting.CraftingManager;
import mod.steamnsteel.api.crafting.IAlloyManager;
import mod.steamnsteel.api.crafting.ingredient.ItemStackIngredient;
import mod.steamnsteel.api.crafting.ingredient.OreDictionaryIngredient;
import mod.steamnsteel.block.resource.storage.BrassBlock;
import mod.steamnsteel.block.resource.storage.CopperBlock;
import mod.steamnsteel.library.ModBlocks;
import mod.steamnsteel.library.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@SuppressWarnings("MethodMayBeStatic")
class Smelting
{
    void init()
    {
        final float xp = 0.5F;
        GameRegistry.addSmelting(ModBlocks.oreCopper, new ItemStack(ModItems.COPPER_INGOT), xp);
        GameRegistry.addSmelting(ModBlocks.oreTin, new ItemStack(ModItems.TIN_INGOT), xp);
        GameRegistry.addSmelting(ModBlocks.oreZinc, new ItemStack(ModItems.ZINC_INGOT), xp);

        final IAlloyManager alloyManager = CraftingManager.alloyManager.get();

        final OreDictionaryIngredient cuIngotIngredient = new OreDictionaryIngredient(ModItems.Names.COPPER_INGOT);
        final OreDictionaryIngredient cuBlockIngredient = new OreDictionaryIngredient(CopperBlock.NAME);

        alloyManager.addAlloy(cuIngotIngredient,
                new OreDictionaryIngredient(ModItems.Names.TIN_INGOT),
                new ItemStack(ModItems.BRONZE_INGOT, 2));
        alloyManager.addAlloy(cuBlockIngredient,
                new OreDictionaryIngredient(ModBlocks.Names.TIN_BLOCK),
                new ItemStack(ModBlocks.blockBronze, 2));

        alloyManager.addAlloy(cuIngotIngredient,
                new OreDictionaryIngredient(ModItems.Names.ZINC_INGOT),
                new ItemStack(ModItems.BRASS_INGOT, 2));
        alloyManager.addAlloy(cuBlockIngredient,
                new OreDictionaryIngredient(ModBlocks.Names.ZINC_BLOCK),
                new ItemStack(ModBlocks.blockBrass, 2));

        alloyManager.addAlloy(new OreDictionaryIngredient("ingotIron"),
                new ItemStackIngredient(new ItemStack(Items.coal, 2)),
                new ItemStack(ModItems.STEEL_INGOT));
        alloyManager.addAlloy(new OreDictionaryIngredient("blockIron"),
                new ItemStackIngredient(new ItemStack(Blocks.coal_block, 2)),
                new ItemStack(ModBlocks.blockSteel));

        alloyManager.addAlloy(new OreDictionaryIngredient(ModItems.Names.BRASS_INGOT),
                new OreDictionaryIngredient(ModItems.Names.STEEL_INGOT),
                new ItemStack(ModItems.PLOTONIUM_INGOT, 2));
        alloyManager.addAlloy(new OreDictionaryIngredient(BrassBlock.NAME),
                new OreDictionaryIngredient(ModBlocks.Names.STEEL_BLOCK),
                new ItemStack(ModBlocks.blockPlotonium, 2));
    }
}
