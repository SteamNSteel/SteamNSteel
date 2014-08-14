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

package mod.steamnsteel.library;

import cpw.mods.fml.common.registry.GameRegistry;
import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.block.container.PlotoniumChest;
import mod.steamnsteel.block.machine.Cupola;
import mod.steamnsteel.block.resource.ore.*;
import mod.steamnsteel.block.resource.storage.*;
import mod.steamnsteel.block.resource.structure.Concrete;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinFloor;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinPillar;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("UtilityClass")
@GameRegistry.ObjectHolder(Reference.MOD_ID)
public final class Blocks
{
    public static final SteamNSteelBlock BRASS_BLOCK = new BrassBlock();
    public static final SteamNSteelBlock BRONZE_BLOCK = new BronzeBlock();
    public static final SteamNSteelBlock CONCRETE_BLOCK = new Concrete();
    public static final SteamNSteelBlock COPPER_BLOCK = new CopperBlock();
    public static final SteamNSteelBlock COPPER_ORE = new CopperOre();
    public static final SteamNSteelBlock CUPOLA = new Cupola();
    public static final SteamNSteelBlock NITER_ORE = new NiterOre();
    public static final SteamNSteelBlock PLOTONIUM_BLOCK = new PlotoniumBlock();
    public static final SteamNSteelBlock PLOTONIUM_CHEST = new PlotoniumChest();
    public static final SteamNSteelBlock PLOTONIUM_RUIN_FLOOR = new PlotoniumRuinFloor();
    public static final SteamNSteelBlock PLOTONIUM_RUIN_PILLAR = new PlotoniumRuinPillar();
    public static final SteamNSteelBlock PLOTONIUM_RUIN_WALL = new PlotoniumRuinWall();
    public static final SteamNSteelBlock STEEL_BLOCK = new SteelBlock();
    public static final SteamNSteelBlock SULFUR_ORE = new SulfurOre();
    public static final SteamNSteelBlock TIN_BLOCK = new TinBlock();
    public static final SteamNSteelBlock TIN_ORE = new TinOre();
    public static final SteamNSteelBlock ZINC_BLOCK = new ZincBlock();
    public static final SteamNSteelBlock ZINC_ORE = new ZincOre();

    private Blocks()
    {
        throw new AssertionError();
    }

    public static void init()
    {
        GameRegistry.registerBlock(PLOTONIUM_CHEST, Names.Blocks.PLOTONIUM_CHEST);

        GameRegistry.registerBlock(CUPOLA, Names.Blocks.CUPOLA);

        registerBlockAndOre(COPPER_ORE, Names.Blocks.COPPER_ORE);
        registerBlockAndOre(NITER_ORE, Names.Blocks.NITER_ORE);
        registerBlockAndOre(SULFUR_ORE, Names.Blocks.SULFUR_ORE);
        registerBlockAndOre(TIN_ORE, Names.Blocks.TIN_ORE);
        registerBlockAndOre(ZINC_ORE, Names.Blocks.ZINC_ORE);

        registerBlockAndOre(BRASS_BLOCK, Names.Blocks.BRASS_BLOCK);
        registerBlockAndOre(BRONZE_BLOCK, Names.Blocks.BRONZE_BLOCK);
        registerBlockAndOre(COPPER_BLOCK, Names.Blocks.COPPER_BLOCK);
        registerBlockAndOre(PLOTONIUM_BLOCK, Names.Blocks.PLOTONIUM_BLOCK);
        registerBlockAndOre(STEEL_BLOCK, Names.Blocks.STEEL_BLOCK);
        registerBlockAndOre(TIN_BLOCK, Names.Blocks.TIN_BLOCK);
        registerBlockAndOre(ZINC_BLOCK, Names.Blocks.ZINC_BLOCK);

        GameRegistry.registerBlock(CONCRETE_BLOCK, Names.Blocks.CONCRETE_BLOCK);
        GameRegistry.registerBlock(PLOTONIUM_RUIN_FLOOR, Names.Blocks.PLOTONIUM_RUIN_FLOOR);
        GameRegistry.registerBlock(PLOTONIUM_RUIN_PILLAR, Names.Blocks.PLOTONIUM_RUIN_PILLAR);
        GameRegistry.registerBlock(PLOTONIUM_RUIN_WALL, Names.Blocks.PLOTONIUM_RUIN_WALL);
    }

    private static void registerBlockAndOre(Block block, String name)
    {
        GameRegistry.registerBlock(block, name);
        OreDictionary.registerOre(name, block);
    }
}
