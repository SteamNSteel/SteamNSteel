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
import mod.steamnsteel.library.ModItem;
import mod.steamnsteel.utility.crafting.RecipePattern;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import java.util.List;

enum ToolRecipes
{
    INSTANCE;

    private static List<IRecipe> getRecipes()
    {
        final RecipePattern pick = RecipePattern.of("XXX", " # ", " # ");
        final RecipePattern shovel = RecipePattern.of("X", "#", "#");
        final RecipePattern axe = RecipePattern.of("XX", "X#", " #");
        final RecipePattern hoe = RecipePattern.of("XX", " #", " #");
        final RecipePattern sword = RecipePattern.of("X", "X", "#");

        final List<IRecipe> recipes = Lists.newArrayList();

        recipes.add(assembleRecipe(ModItem.pickBronze, pick, ModItem.Names.BRONZE_INGOT));
        recipes.add(assembleRecipe(ModItem.shovelBronze, shovel, ModItem.Names.BRONZE_INGOT));
        recipes.add(assembleRecipe(ModItem.axeBronze, axe, ModItem.Names.BRONZE_INGOT));
        recipes.add(assembleRecipe(ModItem.hoeBronze, hoe, ModItem.Names.BRONZE_INGOT));
        recipes.add(assembleRecipe(ModItem.swordBronze, sword, ModItem.Names.BRONZE_INGOT));

        recipes.add(assembleRecipe(ModItem.pickSteel, pick, ModItem.Names.STEEL_INGOT));
        recipes.add(assembleRecipe(ModItem.shovelSteel, shovel, ModItem.Names.STEEL_INGOT));
        recipes.add(assembleRecipe(ModItem.axeSteel, axe, ModItem.Names.STEEL_INGOT));
        recipes.add(assembleRecipe(ModItem.hoeSteel, hoe, ModItem.Names.STEEL_INGOT));
        recipes.add(assembleRecipe(ModItem.swordSteel, sword, ModItem.Names.STEEL_INGOT));

        return ImmutableList.copyOf(recipes);
    }

    private static IRecipe assembleRecipe(Item result, RecipePattern pattern, String ingot)
    {
        return new ShapedOreRecipe(result, pattern.get(), '#', "stickWood", 'X', ingot);
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
