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
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import mod.steamnsteel.utility.crafting.RecipePattern;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import java.util.List;

enum IngotRecipes
{
    INSTANCE;

    private static List<IRecipe> getRecipes()
    {
        final List<IRecipe> recipes = Lists.newArrayList();

        recipes.add(assembleRecipe(ModBlock.blockBrass, ModItem.Names.BRASS_INGOT));
        recipes.add(assembleRecipe(ModBlock.blockBronze, ModItem.Names.BRONZE_INGOT));
        recipes.add(assembleRecipe(ModBlock.blockCopper, ModItem.Names.COPPER_INGOT));
        recipes.add(assembleRecipe(ModBlock.blockPlotonium, ModItem.Names.PLOTONIUM_INGOT));
        recipes.add(assembleRecipe(ModBlock.blockSteel, ModItem.Names.STEEL_INGOT));
        recipes.add(assembleRecipe(ModBlock.blockTin, ModItem.Names.TIN_INGOT));
        recipes.add(assembleRecipe(ModBlock.blockZinc, ModItem.Names.ZINC_INGOT));

        return ImmutableList.copyOf(recipes);
    }

    private static IRecipe assembleRecipe(Block result, String ingot)
    {
        final RecipePattern pattern = RecipePattern.of("###", "###", "###");
        return new ShapedOreRecipe(result, pattern.get(), '#', ingot);
    }

    static void init()
    {
        final List<IRecipe> recipes = getRecipes();
        for (final IRecipe recipe : recipes)
        {
            GameRegistry.addRecipe(recipe);
        }
    }
}
