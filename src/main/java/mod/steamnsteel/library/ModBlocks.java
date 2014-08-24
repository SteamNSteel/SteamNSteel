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
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.block.container.PlotoniumChest;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.block.resource.ore.*;
import mod.steamnsteel.block.resource.storage.*;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinFloor;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinPillar;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.item.block.CupolaItem;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModBlocks
{
    @SuppressWarnings("InnerClassFieldHidesOuterClassField")
    public enum Names
    {
        INSTANCE;
        public static final String BRASS_BLOCK = "blockBrass";
        public static final String BRONZE_BLOCK = "blockBronze";
        public static final String COPPER_BLOCK = "blockCopper";
        public static final String COPPER_ORE = "oreCopper";
        public static final String CUPOLA = "cupola";
        public static final String NITER_ORE = "oreNiter";
        public static final String PLOTONIUM_BLOCK = "blockPlotonium";
        public static final String PLOTONIUM_CHEST = "chestPlotonium";
        public static final String PLOTONIUM_RUIN_FLOOR = "ruinFloorPlotonium";
        public static final String PLOTONIUM_RUIN_PILLAR = "ruinPillarPlotonium";
        public static final String PLOTONIUM_RUIN_WALL = "ruinWallPlotonium";
        public static final String STEEL_BLOCK = "blockSteel";
        public static final String SULFUR_ORE = "oreSulfur";
        public static final String TIN_BLOCK = "blockTin";
        public static final String TIN_ORE = "oreTin";
        public static final String ZINC_BLOCK = "blockZinc";
        public static final String ZINC_ORE = "oreZinc";
    }

    public static final SteamNSteelBlock BRASS_BLOCK = new BrassBlock();
    public static final SteamNSteelBlock BRONZE_BLOCK = new BronzeBlock();
    public static final SteamNSteelBlock COPPER_BLOCK = new CopperBlock();
    public static final SteamNSteelBlock COPPER_ORE = new CopperOre();
    public static final SteamNSteelBlock CUPOLA = new CupolaBlock();
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

    private ModBlocks()
    {
        throw new AssertionError();
    }

    public static void init()
    {
        GameRegistry.registerBlock(PLOTONIUM_CHEST, Names.PLOTONIUM_CHEST);

        GameRegistry.registerBlock(CUPOLA, CupolaItem.class, Names.CUPOLA);

        registerBlockAndOre(COPPER_ORE, Names.COPPER_ORE);
        registerBlockAndOre(NITER_ORE, Names.NITER_ORE);
        registerBlockAndOre(SULFUR_ORE, Names.SULFUR_ORE);
        registerBlockAndOre(TIN_ORE, Names.TIN_ORE);
        registerBlockAndOre(ZINC_ORE, Names.ZINC_ORE);

        registerBlockAndOre(BRASS_BLOCK, Names.BRASS_BLOCK);
        registerBlockAndOre(BRONZE_BLOCK, Names.BRONZE_BLOCK);
        registerBlockAndOre(COPPER_BLOCK, Names.COPPER_BLOCK);
        registerBlockAndOre(PLOTONIUM_BLOCK, Names.PLOTONIUM_BLOCK);
        registerBlockAndOre(STEEL_BLOCK, Names.STEEL_BLOCK);
        registerBlockAndOre(TIN_BLOCK, Names.TIN_BLOCK);
        registerBlockAndOre(ZINC_BLOCK, Names.ZINC_BLOCK);

        GameRegistry.registerBlock(PLOTONIUM_RUIN_FLOOR, Names.PLOTONIUM_RUIN_FLOOR);
        GameRegistry.registerBlock(PLOTONIUM_RUIN_PILLAR, Names.PLOTONIUM_RUIN_PILLAR);
        GameRegistry.registerBlock(PLOTONIUM_RUIN_WALL, Names.PLOTONIUM_RUIN_WALL);
    }

    private static void registerBlockAndOre(Block block, String name)
    {
        GameRegistry.registerBlock(block, name);
        OreDictionary.registerOre(name, block);
    }
}
