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
import mod.steamnsteel.block.SteamNSteelOreBlock;
import mod.steamnsteel.block.container.PlotoniumChest;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.block.resource.ore.*;
import mod.steamnsteel.block.resource.storage.*;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinFloor;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinPillar;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModBlocks
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    public static final SteamNSteelBlock blockBrass = new BrassBlock();
    public static final SteamNSteelBlock blockBronze = new BronzeBlock();
    public static final SteamNSteelBlock blockCopper = new CopperBlock();
    public static final SteamNSteelBlock blockPlotonium = new PlotoniumBlock();
    public static final SteamNSteelBlock blockSteel = new SteelBlock();
    public static final SteamNSteelBlock blockTin = new TinBlock();
    public static final SteamNSteelBlock blockZinc = new ZincBlock();
    public static final SteamNSteelBlock chestPlotonium = new PlotoniumChest();
    public static final SteamNSteelBlock cupola = new CupolaBlock();
    public static final SteamNSteelOreBlock oreCopper = new CopperOre();
    public static final SteamNSteelBlock oreNiter = new NiterOre();
    public static final SteamNSteelBlock oreSulfur = new SulfurOre();
    public static final SteamNSteelOreBlock oreTin = new TinOre();
    public static final SteamNSteelOreBlock oreZinc = new ZincOre();
    public static final SteamNSteelBlock ruinFloorPlotonium = new PlotoniumRuinFloor();
    public static final SteamNSteelBlock ruinPillarPlotonium = new PlotoniumRuinPillar();
    public static final SteamNSteelBlock ruinWallPlotonium = new PlotoniumRuinWall();

    private ModBlocks()
    {
        throw new AssertionError();
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(CupolaTE.class, getTEName(CupolaBlock.NAME));
    }

    private static String getTEName(String name) { return "tile." + name;}

    public static void init()
    {
        GameRegistry.registerBlock(chestPlotonium, PlotoniumChest.NAME);

        GameRegistry.registerBlock(cupola, CupolaBlock.NAME);

        registerBlockAndOre(oreCopper, CopperOre.NAME);
        registerBlockAndOre(oreNiter, NiterOre.NAME);
        registerBlockAndOre(oreSulfur, SulfurOre.NAME);
        registerBlockAndOre(oreTin, TinOre.NAME);
        registerBlockAndOre(oreZinc, ZincOre.NAME);

        registerBlockAndOre(blockBrass, BrassBlock.NAME);
        registerBlockAndOre(blockBronze, BronzeBlock.NAME);
        registerBlockAndOre(blockCopper, CopperBlock.NAME);
        registerBlockAndOre(blockPlotonium, PlotoniumBlock.NAME);
        registerBlockAndOre(blockSteel, SteelBlock.NAME);
        registerBlockAndOre(blockTin, TinBlock.NAME);
        registerBlockAndOre(blockZinc, ZincBlock.NAME);

        GameRegistry.registerBlock(ruinFloorPlotonium, PlotoniumRuinFloor.NAME);
        GameRegistry.registerBlock(ruinPillarPlotonium, PlotoniumRuinPillar.NAME);
        GameRegistry.registerBlock(ruinWallPlotonium, PlotoniumRuinWall.NAME);
    }

    private static void registerBlockAndOre(Block block, String name)
    {
        GameRegistry.registerBlock(block, name);
        OreDictionary.registerOre(name, block);
    }
}
