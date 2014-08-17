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
import mod.steamnsteel.library.Items;
import mod.steamnsteel.library.Names;
import mod.steamnsteel.utility.crafting.RecipePattern;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import java.util.List;

@SuppressWarnings("MethodMayBeStatic")
class ArmorRecipes
{
    private static List<IRecipe> getRecipes()
    {
        final RecipePattern helmet = RecipePattern.of("XXX", "X X");
        final RecipePattern chestplate = RecipePattern.of("X X", "XXX", "XXX");
        final RecipePattern leggings = RecipePattern.of("XXX", "X X", "X X");
        final RecipePattern boots = RecipePattern.of("X X", "X X");

        final List<IRecipe> recipes = Lists.newArrayList();

        recipes.add(assembleRecipe(Items.BRONZE_HELMET, helmet, Names.Ingots.BRONZE_INGOT));
        recipes.add(assembleRecipe(Items.BRONZE_CHESTPLATE, chestplate, Names.Ingots.BRONZE_INGOT));
        recipes.add(assembleRecipe(Items.BRONZE_LEGGINGS, leggings, Names.Ingots.BRONZE_INGOT));
        recipes.add(assembleRecipe(Items.BRONZE_BOOTS, boots, Names.Ingots.BRONZE_INGOT));

        recipes.add(assembleRecipe(Items.STEEL_HELMET, helmet, Names.Ingots.STEEL_INGOT));
        recipes.add(assembleRecipe(Items.STEEL_CHESTPLATE, chestplate, Names.Ingots.STEEL_INGOT));
        recipes.add(assembleRecipe(Items.STEEL_LEGGINGS, leggings, Names.Ingots.STEEL_INGOT));
        recipes.add(assembleRecipe(Items.STEEL_BOOTS, boots, Names.Ingots.STEEL_INGOT));

        return ImmutableList.copyOf(recipes);
    }

    private static IRecipe assembleRecipe(Item result, RecipePattern pattern, String ingot)
    {
        return new ShapedOreRecipe(result, pattern.get(), 'X', ingot);
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
