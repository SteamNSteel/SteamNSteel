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

import mod.steamnsteel.block.container.RemnantRuinChestBlock;
import mod.steamnsteel.block.machine.*;
import mod.steamnsteel.block.resource.structure.RemnantRuinFloorBlock;
import mod.steamnsteel.block.resource.structure.RemnantRuinIronBarsBlock;
import mod.steamnsteel.block.resource.structure.RemnantRuinPillarBlock;
import mod.steamnsteel.block.resource.structure.RemnantRuinWallBlock;
import mod.steamnsteel.block.utility.CraftingStationBlock;
import mod.steamnsteel.item.resource.structure.RemnantRuinIronBarsBlockItem;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.*;
import mod.steamnsteel.block.resource.ore.*;
import mod.steamnsteel.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModBlock
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    //TODO: Resurrect Blocks

    public static final SteamNSteelBlock blockBrass = new SteamNSteelStorageBlock(SteamNSteelStorageBlock.BRASS_BLOCK);
    public static final SteamNSteelBlock blockBronze = new SteamNSteelStorageBlock(SteamNSteelStorageBlock.BRONZE_BLOCK);
    public static final SteamNSteelBlock blockCopper = new SteamNSteelStorageBlock(SteamNSteelStorageBlock.COPPER_BLOCK);
    public static final SteamNSteelBlock blockPlotonium = new SteamNSteelStorageBlock(SteamNSteelStorageBlock.PLOTONIUM_BLOCK);
    public static final SteamNSteelBlock blockSteel = new SteamNSteelStorageBlock(SteamNSteelStorageBlock.STEEL_BLOCK);
    public static final SteamNSteelBlock blockTin = new SteamNSteelStorageBlock(SteamNSteelStorageBlock.TIN_BLOCK);
    public static final SteamNSteelBlock blockZinc = new SteamNSteelStorageBlock(SteamNSteelStorageBlock.ZINC_BLOCK);

    public static final SteamNSteelBlock cupola = new CupolaBlock();
    public static final SteamNSteelBlock fanLarge = new FanLargeBlock();

    public static final SteamNSteelBlock pipe = new PipeBlock();
    public static final SteamNSteelBlock pipeValve = new PipeValveBlock();
    public static final SteamNSteelBlock pipeValveRedstone = new PipeRedstoneValveBlock();
    public static final SteamNSteelBlock pipeJunction = new PipeJunctionBlock();

    public static final SteamNSteelBlock remnantRuinChest = new RemnantRuinChestBlock();
    public static final SteamNSteelBlock craftingStation = new CraftingStationBlock();
    public static final SteamNSteelBlock remnantRuinPillar = new RemnantRuinPillarBlock();

    public static final SteamNSteelOreBlock oreNiter = new NiterOre();
    public static final SteamNSteelOreBlock oreCopper = new CopperOre();
    public static final SteamNSteelOreBlock oreSulfur = new SulfurOre();
    public static final SteamNSteelOreBlock oreTin = new TinOre();
    public static final SteamNSteelOreBlock oreZinc = new ZincOre();


    public static final SteamNSteelBlock remnantRuinFloor = new RemnantRuinFloorBlock();
    public static final SteamNSteelBlock remnantRuinWall = new RemnantRuinWallBlock();
    public static final RemnantRuinIronBarsBlock remnantRuinIronBars = new RemnantRuinIronBarsBlock();
    /*
    public static final SteamNSteelPaneBlock remnantRuinIronBars = new RemnantRuinIronBarsBlock();
    */

    private ModBlock()
    {
        throw new AssertionError();
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(RemnantRuinChestTE.class, getTEName(RemnantRuinChestBlock.NAME));
        GameRegistry.registerTileEntity(CupolaTE.class, getTEName(CupolaBlock.NAME));
        GameRegistry.registerTileEntity(LargeFanTE.class, getTEName(FanLargeBlock.NAME));
        GameRegistry.registerTileEntity(PipeTE.class, getTEName(PipeBlock.NAME));
        GameRegistry.registerTileEntity(PipeValveTE.class, getTEName(PipeValveBlock.NAME));
        GameRegistry.registerTileEntity(PipeRedstoneValveTE.class, getTEName(PipeRedstoneValveBlock.NAME));
        GameRegistry.registerTileEntity(PipeJunctionTE.class, getTEName(PipeJunctionBlock.NAME));
        GameRegistry.registerTileEntity(RemnantRuinPillarTE.class, getTEName(RemnantRuinPillarBlock.NAME));
    }

    private static String getTEName(String name) { return "tile." + name;}

    public static void init()
    {
        GameRegistry.registerBlock(remnantRuinChest, RemnantRuinChestBlock.NAME);
        GameRegistry.registerBlock(craftingStation, CraftingStationBlock.NAME);

        GameRegistry.registerBlock(cupola, CupolaBlock.NAME);
        GameRegistry.registerBlock(fanLarge, FanLargeBlock.NAME);

        GameRegistry.registerBlock(pipe, PipeBlock.NAME);
        GameRegistry.registerBlock(pipeValve, PipeValveBlock.NAME);
        GameRegistry.registerBlock(pipeValveRedstone, PipeRedstoneValveBlock.NAME);
        GameRegistry.registerBlock(pipeJunction, PipeJunctionBlock.NAME);

        registerBlockAndOre(oreNiter, NiterOre.NAME);
        registerBlockAndOre(oreCopper, CopperOre.NAME);
        registerBlockAndOre(oreSulfur, SulfurOre.NAME);
        registerBlockAndOre(oreTin, TinOre.NAME);
        registerBlockAndOre(oreZinc, ZincOre.NAME);

        registerBlockAndOre(blockBrass, SteamNSteelStorageBlock.BRASS_BLOCK);
        registerBlockAndOre(blockBronze, SteamNSteelStorageBlock.BRONZE_BLOCK);
        registerBlockAndOre(blockCopper, SteamNSteelStorageBlock.COPPER_BLOCK);
        registerBlockAndOre(blockPlotonium, SteamNSteelStorageBlock.PLOTONIUM_BLOCK);
        registerBlockAndOre(blockSteel, SteamNSteelStorageBlock.STEEL_BLOCK);
        registerBlockAndOre(blockTin, SteamNSteelStorageBlock.TIN_BLOCK);
        registerBlockAndOre(blockZinc, SteamNSteelStorageBlock.ZINC_BLOCK);

        GameRegistry.registerBlock(remnantRuinPillar, RemnantRuinPillarBlock.NAME);

        GameRegistry.registerBlock(remnantRuinFloor, RemnantRuinFloorBlock.NAME);
        GameRegistry.registerBlock(remnantRuinWall, RemnantRuinWallBlock.NAME);
        GameRegistry.registerBlock(remnantRuinIronBars, RemnantRuinIronBarsBlockItem.class, RemnantRuinIronBarsBlock.NAME);

        //Compat
        TileEntity.addMapping(RemnantRuinChestTE.class, "tile.chestPlotonium");
    }

    private static void registerBlockAndOre(Block block, String name)
    {
        GameRegistry.registerBlock(block, name);
        OreDictionary.registerOre(name, block);
    }

    public static void remapMissingMappings(List<FMLMissingMappingsEvent.MissingMapping> missingMappings)
    {
        /*
        //These mappings are temporary and are only really present to prevent Rorax' worlds from being destroyed.
        for (FMLMissingMappingsEvent.MissingMapping missingMapping : missingMappings) {
            if (missingMapping.name.equals(TheMod.MOD_ID + ":ruinWallPlotonium")) {
                remapBlock(missingMapping, remnantRuinWall);
            } else if (missingMapping.name.equals(TheMod.MOD_ID + ":blockSteelFloor")) {
                remapBlock(missingMapping, remnantRuinFloor);
            } else if (missingMapping.name.equals(TheMod.MOD_ID + ":ruinPillarPlotonium")) {
                remapBlock(missingMapping, remnantRuinPillar);
            } else if (missingMapping.name.equals(TheMod.MOD_ID + ":chestPlotonium")) {
                remapBlock(missingMapping, remnantRuinChest);
            } else if (missingMapping.name.equals(TheMod.MOD_ID + ":ironBarsRust")) {
                remapBlock(missingMapping, remnantRuinIronBars);
            }/* else if (missingMapping.name.equals(TheMod.MOD_ID + ":ironBarsMoss")) {
                remapBlock(missingMapping, remnantRuinIronBars);
            } else if (missingMapping.name.equals(TheMod.MOD_ID + ":ironBarsMossRust")) {
                remapBlock(missingMapping, remnantRuinIronBars);
            }

        }*/
    }

    private static void remapBlock(FMLMissingMappingsEvent.MissingMapping missingMapping, Block block)
    {
        if (missingMapping.type == GameRegistry.Type.BLOCK)
        {
            missingMapping.remap(block);
        } else {
            missingMapping.remap(Item.getItemFromBlock(block));
        }
    }
}
