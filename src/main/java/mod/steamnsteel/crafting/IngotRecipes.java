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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameRegistry;
import mod.steamnsteel.library.ModBlocks;
import mod.steamnsteel.library.ModItems;
import mod.steamnsteel.utility.crafting.RecipePattern;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import java.util.List;

@SuppressWarnings("MethodMayBeStatic")
class IngotRecipes
{
    private static List<IRecipe> getRecipes()
    {
        final List<IRecipe> recipes = Lists.newArrayList();

        recipes.add(assembleRecipe(ModBlocks.BRASS_BLOCK, ModItems.Names.BRASS_INGOT));
        recipes.add(assembleRecipe(ModBlocks.BRONZE_BLOCK, ModItems.Names.BRONZE_INGOT));
        recipes.add(assembleRecipe(ModBlocks.COPPER_BLOCK, ModItems.Names.COPPER_INGOT));
        recipes.add(assembleRecipe(ModBlocks.PLOTONIUM_BLOCK, ModItems.Names.PLOTONIUM_INGOT));
        recipes.add(assembleRecipe(ModBlocks.STEEL_BLOCK, ModItems.Names.STEEL_INGOT));
        recipes.add(assembleRecipe(ModBlocks.TIN_BLOCK, ModItems.Names.TIN_INGOT));
        recipes.add(assembleRecipe(ModBlocks.ZINC_BLOCK, ModItems.Names.ZINC_INGOT));

        return ImmutableList.copyOf(recipes);
    }

    private static IRecipe assembleRecipe(Block result, String ingot)
    {
        final RecipePattern pattern = RecipePattern.of("###", "###", "###");
        return new ShapedOreRecipe(result, pattern.get(), '#', ingot);
    }

    void init()
    {
        final List<IRecipe> recipes = getRecipes();
        for (final IRecipe recipe : recipes)
        {
            GameRegistry.addRecipe(recipe);
        }
    }
}
