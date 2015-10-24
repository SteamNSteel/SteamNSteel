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

import mod.steamnsteel.block.SteamNSteelStorageBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.steamnsteel.api.crafting.CraftingManager;
import mod.steamnsteel.api.crafting.IAlloyManager;
import mod.steamnsteel.api.crafting.ingredient.ItemStackIngredient;
import mod.steamnsteel.api.crafting.ingredient.OreDictionaryIngredient;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

enum Smelting
{
    INSTANCE;

    static void init()
    {
        final float xp = 0.5F;
        GameRegistry.addSmelting(ModBlock.oreCopper, new ItemStack(ModItem.ingotCopper), xp);
        GameRegistry.addSmelting(ModBlock.oreTin, new ItemStack(ModItem.ingotTin), xp);
        GameRegistry.addSmelting(ModBlock.oreZinc, new ItemStack(ModItem.ingotZinc), xp);

        final IAlloyManager alloyManager = CraftingManager.alloyManager.get();

        final OreDictionaryIngredient cuIngotIngredient = new OreDictionaryIngredient(ModItem.Names.COPPER_INGOT);
        final OreDictionaryIngredient cuBlockIngredient = new OreDictionaryIngredient(SteamNSteelStorageBlock.COPPER_BLOCK);

        alloyManager.addAlloy(cuIngotIngredient,
                new OreDictionaryIngredient(ModItem.Names.TIN_INGOT),
                new ItemStack(ModItem.ingotBronze, 2));
        alloyManager.addAlloy(cuBlockIngredient,
                new OreDictionaryIngredient(SteamNSteelStorageBlock.TIN_BLOCK),
                new ItemStack(ModBlock.blockBronze, 2));

        alloyManager.addAlloy(cuIngotIngredient,
                new OreDictionaryIngredient(ModItem.Names.ZINC_INGOT),
                new ItemStack(ModItem.ingotBrass, 2));
        alloyManager.addAlloy(cuBlockIngredient,
                new OreDictionaryIngredient(SteamNSteelStorageBlock.ZINC_BLOCK),
                new ItemStack(ModBlock.blockBrass, 2));

        alloyManager.addAlloy(new OreDictionaryIngredient("ingotIron"),
                new ItemStackIngredient(new ItemStack(Items.coal, 2)),
                new ItemStack(ModItem.ingotSteel));
        alloyManager.addAlloy(new OreDictionaryIngredient("blockIron"),
                new ItemStackIngredient(new ItemStack(Blocks.coal_block, 2)),
                new ItemStack(ModBlock.blockSteel));

        alloyManager.addAlloy(new OreDictionaryIngredient(ModItem.Names.BRASS_INGOT),
                new OreDictionaryIngredient(ModItem.Names.STEEL_INGOT),
                new ItemStack(ModItem.ingotPlotonium, 2));
        alloyManager.addAlloy(new OreDictionaryIngredient(SteamNSteelStorageBlock.BRASS_BLOCK),
                new OreDictionaryIngredient(SteamNSteelStorageBlock.STEEL_BLOCK),
                new ItemStack(ModBlock.blockPlotonium, 2));
    }
}
