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

import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.utility.crafting.RecipePattern;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

@SuppressWarnings("UtilityClass")
public final class Recipes
{
    private Recipes()
    {
        throw new AssertionError();
    }

    public static void init()
    {
        Smelting.init();
        ToolRecipes.init();
        IngotRecipes.init();
        ArmorRecipes.init();

        addCupolaRecipe();
    }

    private static void addCupolaRecipe()
    {
        final RecipePattern pattern = RecipePattern.of("#X#", "X X", "#X#");

        final IRecipe recipe = new ShapedOreRecipe(ModBlock.cupola, pattern.get(), '#', "ingotIron", 'X', Blocks.brick_block);
        GameRegistry.addRecipe(recipe);


    }
}
