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
import mod.steamnsteel.block.SteamNSteelStorageBlock;
import mod.steamnsteel.block.container.PlotoniumChest;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.block.machine.PipeBlock;
import mod.steamnsteel.block.machine.PipeJunctionBlock;
import mod.steamnsteel.block.resource.ore.*;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinFloor;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinPillar;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.tileentity.CupolaTE;
import mod.steamnsteel.tileentity.PipeJunctionTE;
import mod.steamnsteel.tileentity.PipeTE;
import mod.steamnsteel.tileentity.PlotoniumChestTE;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

import static mod.steamnsteel.block.SteamNSteelStorageBlock.*;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModBlock
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    public static final SteamNSteelBlock blockBrass = new SteamNSteelStorageBlock(BRASS_BLOCK);
    public static final SteamNSteelBlock blockBronze = new SteamNSteelStorageBlock(BRONZE_BLOCK);
    public static final SteamNSteelBlock blockCopper = new SteamNSteelStorageBlock(COPPER_BLOCK);
    public static final SteamNSteelBlock blockPlotonium = new SteamNSteelStorageBlock(PLOTONIUM_BLOCK);
    public static final SteamNSteelBlock blockSteel = new SteamNSteelStorageBlock(STEEL_BLOCK);
    public static final SteamNSteelBlock blockTin = new SteamNSteelStorageBlock(TIN_BLOCK);
    public static final SteamNSteelBlock blockZinc = new SteamNSteelStorageBlock(ZINC_BLOCK);

    public static final SteamNSteelBlock chestPlotonium = new PlotoniumChest();
    public static final SteamNSteelBlock cupola = new CupolaBlock();
    public static final SteamNSteelBlock pipe = new PipeBlock();
    public static final SteamNSteelBlock pipeJunction = new PipeJunctionBlock();

    public static final SteamNSteelOreBlock oreCopper = new CopperOre();
    public static final SteamNSteelOreBlock oreNiter = new NiterOre();
    public static final SteamNSteelOreBlock oreSulfur = new SulfurOre();
    public static final SteamNSteelOreBlock oreTin = new TinOre();
    public static final SteamNSteelOreBlock oreZinc = new ZincOre();
    public static final SteamNSteelBlock ruinFloorPlotonium = new PlotoniumRuinFloor();
    public static final SteamNSteelBlock ruinPillarPlotonium = new PlotoniumRuinPillar();
    public static final SteamNSteelBlock ruinWallPlotonium = new PlotoniumRuinWall();



    private ModBlock()
    {
        throw new AssertionError();
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(CupolaTE.class, getTEName(CupolaBlock.NAME));
        GameRegistry.registerTileEntity(PlotoniumChestTE.class, getTEName(PlotoniumChest.NAME));
        GameRegistry.registerTileEntity(PipeTE.class, getTEName(PipeBlock.NAME));
        GameRegistry.registerTileEntity(PipeJunctionTE.class, getTEName(PipeJunctionBlock.NAME));

    }

    private static String getTEName(String name) { return "tile." + name;}

    public static void init()
    {
        GameRegistry.registerBlock(chestPlotonium, PlotoniumChest.NAME);
        GameRegistry.registerBlock(cupola, CupolaBlock.NAME);
        GameRegistry.registerBlock(pipe, PipeBlock.NAME);
        GameRegistry.registerBlock(pipeJunction, PipeJunctionBlock.NAME);

        registerBlockAndOre(oreCopper, CopperOre.NAME);
        registerBlockAndOre(oreNiter, NiterOre.NAME);
        registerBlockAndOre(oreSulfur, SulfurOre.NAME);
        registerBlockAndOre(oreTin, TinOre.NAME);
        registerBlockAndOre(oreZinc, ZincOre.NAME);

        registerBlockAndOre(blockBrass, BRASS_BLOCK);
        registerBlockAndOre(blockBronze, BRONZE_BLOCK);
        registerBlockAndOre(blockCopper, COPPER_BLOCK);
        registerBlockAndOre(blockPlotonium, PLOTONIUM_BLOCK);
        registerBlockAndOre(blockSteel, STEEL_BLOCK);
        registerBlockAndOre(blockTin, TIN_BLOCK);
        registerBlockAndOre(blockZinc, ZINC_BLOCK);

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
