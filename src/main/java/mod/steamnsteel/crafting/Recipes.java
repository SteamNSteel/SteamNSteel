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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import cpw.mods.fml.common.registry.GameRegistry;
import mod.steamnsteel.library.Blocks;
import mod.steamnsteel.library.Items;
import mod.steamnsteel.utility.crafting.RecipePattern;
import net.minecraft.item.ItemStack;

@SuppressWarnings("UtilityClass")
public final class Recipes
{

    public static final float SMELTING_XP = 0.5F;

    private Recipes()
    {
        throw new AssertionError();
    }

    public static void init()
    {
        initFurnaceRecipes();
        initToolRecipes();
    }

    private static void initToolRecipes()
    {
        for (final Table.Cell<String, RecipePattern, ItemStack> cell : getToolProductTable().cellSet())
        {
            final ItemStack product = cell.getValue();
            final RecipePattern recipe = cell.getColumnKey();
            final String ingot = cell.getRowKey();
            GameRegistry.addRecipe(product, recipe.pattern(), '#', "stickWood", 'X', ingot);
        }
    }

    private static Table<String, RecipePattern, ItemStack> getToolProductTable()
    {
        final RecipePattern pick = RecipePattern.of("XXX", " # ", " # ");
        final RecipePattern shovel = RecipePattern.of("X", "#", "#");
        final RecipePattern axe = RecipePattern.of("XX", "X#", " #");
        final RecipePattern hoe = RecipePattern.of("XX", " #", " #");

        final Table<String, RecipePattern, ItemStack> productTable = HashBasedTable.create();

        productTable.put("bronzeIngot", pick, new ItemStack(Items.BRONZE_PICKAXE));
        productTable.put("bronzeIngot", shovel, new ItemStack(Items.BRONZE_SHOVEL));
        productTable.put("bronzeIngot", axe, new ItemStack(Items.BRONZE_AXE));
        productTable.put("bronzeIngot", hoe, new ItemStack(Items.BRONZE_HOE));

        productTable.put("steelIngot", pick, new ItemStack(Items.STEEL_PICKAXE));
        productTable.put("steelIngot", shovel, new ItemStack(Items.STEEL_SHOVEL));
        productTable.put("steelIngot", axe, new ItemStack(Items.STEEL_AXE));
        productTable.put("steelIngot", hoe, new ItemStack(Items.STEEL_HOE));

        return ImmutableTable.copyOf(productTable);
    }

    private static void initFurnaceRecipes()
    {
        GameRegistry.addSmelting(Blocks.COPPER_ORE, new ItemStack(Items.COPPER_INGOT), SMELTING_XP);
        GameRegistry.addSmelting(Blocks.TIN_ORE, new ItemStack(Items.TIN_INGOT), SMELTING_XP);
        GameRegistry.addSmelting(Blocks.ZINC_ORE, new ItemStack(Items.ZINC_INGOT), SMELTING_XP);
    }
}
