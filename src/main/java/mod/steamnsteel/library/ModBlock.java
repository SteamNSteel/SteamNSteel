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

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import com.foudroyantfactotum.tool.structure.item.StructureBlockItem;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.*;
import mod.steamnsteel.block.container.RemnantRuinChestBlock;
import mod.steamnsteel.block.machine.*;
import mod.steamnsteel.block.machine.structure.FanLargeStructure;
import mod.steamnsteel.block.machine.structure.SSBallMillStructure;
import mod.steamnsteel.block.machine.structure.SSBlastFurnaceStructure;
import mod.steamnsteel.block.machine.structure.SSBoilerStructure;
import mod.steamnsteel.block.resource.ore.*;
import mod.steamnsteel.block.resource.structure.*;
import mod.steamnsteel.item.resource.structure.ConcreteBlockItem;
import mod.steamnsteel.block.resource.structure.RemnantRuinFloorBlock;
import mod.steamnsteel.block.resource.structure.RemnantRuinIronBarsBlock;
import mod.steamnsteel.block.resource.structure.RemnantRuinPillarBlock;
import mod.steamnsteel.block.resource.structure.RemnantRuinWallBlock;
import mod.steamnsteel.block.utility.ProjectTableBlock;
import mod.steamnsteel.item.resource.structure.RemnantRuinIronBarsBlockItem;
import mod.steamnsteel.library.Reference.BlockNames;
import mod.steamnsteel.tileentity.*;
import mod.steamnsteel.tileentity.structure.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
//@ObjectHolder(TheMod.MOD_ID)
public final class ModBlock
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *

    public static Block blockBrass;
    public static Block blockBronze;
    public static Block blockCopper;
    public static Block blockPlotonium;
    public static Block blockSteel;
    public static Block blockTin;
    public static Block blockZinc;
    public static Block blockBrassStorage;
    public static Block blockBronzeStorage;
    public static Block blockCopperStorage;
    public static Block blockPlotoniumStorage;
    public static Block blockSteelStorage;
    public static Block blockTinStorage;
    public static Block blockZincStorage;

    public static Block cupola;

    public static SteamNSteelStructureShapeBlock structureShape;
    public static SteamNSteelStructureShapeBlock shapeLI;
    public static SteamNSteelStructureBlock ssBallMill;
    public static SteamNSteelStructureBlock ssBlastFurnace;
    public static SteamNSteelStructureBlock ssBoiler;
    public static SteamNSteelStructureBlock fanLarge;

    public static Block pipe;
    public static Block pipeValve;
    public static Block pipeValveRedstone;
    public static Block pipeJunction;

    public static Block remnantRuinChest;
    public static Block projectTable;
    public static Block remnantRuinPillar;

    public static SteamNSteelOreBlock oreNiter;
    public static SteamNSteelOreBlock oreCopper;
    public static SteamNSteelOreBlock oreSulfur;
    public static SteamNSteelOreBlock oreTin;
    public static SteamNSteelOreBlock oreZinc;

    public static Block blockConcrete;


    public static Block remnantRuinFloor;
    public static Block remnantRuinWall;
    public static Block remnantRuinIronBars;

    private ModBlock()
    {
        throw new AssertionError();
    }

    public static void createBlocks() {
        blockBrass = new SteamNSteelStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.BRASS_BLOCK);
        blockBronze = new SteamNSteelStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.BRONZE_BLOCK);
        blockCopper = new SteamNSteelStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.COPPER_BLOCK);
        blockPlotonium = new SteamNSteelStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.PLOTONIUM_BLOCK);
        blockSteel = new SteamNSteelStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.STEEL_BLOCK);
        blockTin = new SteamNSteelStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.TIN_BLOCK);
        blockZinc = new SteamNSteelStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.ZINC_BLOCK);
        blockBrassStorage = new SteamNSteelDirectionalStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.STORAGE_BRASS_BLOCK);
        blockBronzeStorage = new SteamNSteelDirectionalStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.STORAGE_BRONZE_BLOCK);
        blockCopperStorage = new SteamNSteelDirectionalStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.STORAGE_COPPER_BLOCK);
        blockPlotoniumStorage = new SteamNSteelDirectionalStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.STORAGE_PLOTONIUM_BLOCK);
        blockSteelStorage = new SteamNSteelDirectionalStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.STORAGE_STEEL_BLOCK);
        blockTinStorage = new SteamNSteelDirectionalStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.STORAGE_TIN_BLOCK);
        blockZincStorage = new SteamNSteelDirectionalStorageBlock().setRegistryName(Reference.MOD_ID, BlockNames.STORAGE_ZINC_BLOCK);

        cupola = new CupolaBlock().setRegistryName(Reference.MOD_ID, BlockNames.CUPOLA);

        structureShape = (SteamNSteelStructureShapeBlock)new SteamNSteelStructureShapeBlock().setRegistryName(Reference.MOD_ID, BlockNames.STRUCTURE_SHAPE);
        shapeLI = (SteamNSteelStructureShapeBlock)new ShapeLIBlock().setRegistryName(Reference.MOD_ID, BlockNames.STRUCTURE_SHAPE_LI);
        ssBallMill = (SteamNSteelStructureBlock)new SSBallMillStructure().setRegistryName(Reference.MOD_ID, BlockNames.BALL_MILL);
        ssBlastFurnace = (SteamNSteelStructureBlock)new SSBlastFurnaceStructure().setRegistryName(Reference.MOD_ID, BlockNames.BLAST_FURNACE);
        ssBoiler = (SteamNSteelStructureBlock)new SSBoilerStructure().setRegistryName(Reference.MOD_ID, BlockNames.BOILER);
        fanLarge = (SteamNSteelStructureBlock)new FanLargeStructure().setRegistryName(Reference.MOD_ID, BlockNames.FAN_LARGE);

        pipe = new PipeBlock().setRegistryName(Reference.MOD_ID, BlockNames.PIPE);
        pipeValve = new PipeValveBlock().setRegistryName(Reference.MOD_ID, BlockNames.PIPE_VALVE);
        pipeValveRedstone = new PipeRedstoneValveBlock().setRegistryName(Reference.MOD_ID, BlockNames.PIPE_VALVE_REDSTONE);
        pipeJunction = new PipeJunctionBlock().setRegistryName(Reference.MOD_ID, BlockNames.PIPE_JUNCTION);

        remnantRuinChest = new RemnantRuinChestBlock().setRegistryName(Reference.MOD_ID, BlockNames.REMNANT_RUIN_CHEST);
        projectTable = new ProjectTableBlock().setRegistryName(Reference.MOD_ID, BlockNames.PROJECT_TABLE);
        remnantRuinPillar = new RemnantRuinPillarBlock().setRegistryName(Reference.MOD_ID, BlockNames.REMNANT_RUIN_PILLAR);

        oreNiter = (SteamNSteelOreBlock)new NiterOre().setRegistryName(Reference.MOD_ID, BlockNames.ORE_NITER);
        oreCopper = (SteamNSteelOreBlock)new CopperOre().setRegistryName(Reference.MOD_ID, BlockNames.ORE_COPPER);
        oreSulfur = (SteamNSteelOreBlock)new SulfurOre().setRegistryName(Reference.MOD_ID, BlockNames.ORE_SULFUR);
        oreTin = (SteamNSteelOreBlock)new TinOre().setRegistryName(Reference.MOD_ID, BlockNames.ORE_TIN);
        oreZinc = (SteamNSteelOreBlock)new ZincOre().setRegistryName(Reference.MOD_ID, BlockNames.ORE_ZINC);

        blockConcrete = new ConcreteBlock().setRegistryName(Reference.MOD_ID, BlockNames.CONCRETE);


        remnantRuinFloor = new RemnantRuinFloorBlock().setRegistryName(Reference.MOD_ID, BlockNames.REMNANT_RUIN_FLOOR);
        remnantRuinWall = new RemnantRuinWallBlock().setRegistryName(Reference.MOD_ID, BlockNames.REMNANT_RUIN_WALL);
        remnantRuinIronBars = new RemnantRuinIronBarsBlock().setRegistryName(Reference.MOD_ID, BlockNames.REMNANT_RUIN_IRON_BARS);
    }
    
    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(RemnantRuinChestTE.class, getTEName(BlockNames.REMNANT_RUIN_CHEST));
        GameRegistry.registerTileEntity(CupolaTE.class, getTEName(BlockNames.CUPOLA));
        GameRegistry.registerTileEntity(SteamNSteelStructureShapeTE.class, getTEName(BlockNames.STRUCTURE_SHAPE));
        GameRegistry.registerTileEntity(ShapeLITE.class, getTEName(BlockNames.STRUCTURE_SHAPE_LI));
        GameRegistry.registerTileEntity(BallMillTE.class, getTEName(BlockNames.BALL_MILL));
        GameRegistry.registerTileEntity(LargeFanTE.class, getTEName(BlockNames.FAN_LARGE));
        GameRegistry.registerTileEntity(BlastFurnaceTE.class, getTEName(BlockNames.BLAST_FURNACE));
        GameRegistry.registerTileEntity(BoilerTE.class, getTEName(BlockNames.BOILER));
        GameRegistry.registerTileEntity(PipeTE.class, getTEName(BlockNames.PIPE));
        GameRegistry.registerTileEntity(PipeValveTE.class, getTEName(BlockNames.PIPE_VALVE));
        GameRegistry.registerTileEntity(PipeRedstoneValveTE.class, getTEName(BlockNames.PIPE_VALVE_REDSTONE));
        GameRegistry.registerTileEntity(PipeJunctionTE.class, getTEName(BlockNames.PIPE_JUNCTION));
        GameRegistry.registerTileEntity(RemnantRuinPillarTE.class, getTEName(BlockNames.REMNANT_RUIN_PILLAR));
    }

    private static String getTEName(String name) { return "tile." + name;}

    public static void init()
    {
        createBlocks();

        registerBlockAndItem(remnantRuinChest);
        registerBlockAndItem(projectTable);

        registerBlockAndItem(cupola);

        registerBlock(structureShape);
        registerBlock(shapeLI);
        registerStructure(ssBallMill, shapeLI, BlockNames.BALL_MILL);
        registerStructure(ssBlastFurnace, shapeLI, BlockNames.BLAST_FURNACE);
        registerStructure(ssBoiler, shapeLI, BlockNames.BOILER);
        registerStructure(fanLarge, structureShape, BlockNames.FAN_LARGE);

        registerBlockAndItem(pipe);
        registerBlockAndItem(pipeValve);
        registerBlockAndItem(pipeValveRedstone);
        registerBlockAndItem(pipeJunction);

        registerBlockAndOre(oreNiter, BlockNames.ORE_NITER);
        registerBlockAndOre(oreCopper, BlockNames.ORE_COPPER);
        registerBlockAndOre(oreSulfur, BlockNames.ORE_SULFUR);
        registerBlockAndOre(oreTin, BlockNames.ORE_TIN);
        registerBlockAndOre(oreZinc, BlockNames.ZINC_BLOCK);

        registerBlockAndOre(blockBrass, BlockNames.BRASS_BLOCK);
        registerBlockAndOre(blockBronze, BlockNames.BRONZE_BLOCK);
        registerBlockAndOre(blockCopper, BlockNames.COPPER_BLOCK);
        registerBlockAndOre(blockPlotonium, BlockNames.PLOTONIUM_BLOCK);
        registerBlockAndOre(blockSteel, BlockNames.STEEL_BLOCK);
        registerBlockAndOre(blockTin, BlockNames.TIN_BLOCK);
        registerBlockAndOre(blockZinc, BlockNames.ZINC_BLOCK);
        registerBlockAndOre(blockBrassStorage, BlockNames.STORAGE_BRASS_BLOCK);
        registerBlockAndOre(blockBronzeStorage, BlockNames.STORAGE_BRONZE_BLOCK);
        registerBlockAndOre(blockCopperStorage, BlockNames.STORAGE_COPPER_BLOCK);
        registerBlockAndOre(blockPlotoniumStorage, BlockNames.STORAGE_PLOTONIUM_BLOCK);
        registerBlockAndOre(blockSteelStorage, BlockNames.STORAGE_STEEL_BLOCK);
        registerBlockAndOre(blockTinStorage, BlockNames.STORAGE_TIN_BLOCK);
        registerBlockAndOre(blockZincStorage, BlockNames.STORAGE_ZINC_BLOCK);

        registerBlockAndItem(remnantRuinPillar);

        registerBlockAndItem(remnantRuinFloor);
        registerBlockAndItem(remnantRuinWall);
        registerBlock(remnantRuinIronBars);
        registerItem(new RemnantRuinIronBarsBlockItem(remnantRuinIronBars).setRegistryName(BlockNames.REMNANT_RUIN_IRON_BARS));
        registerBlock(blockConcrete);
        registerItem(new ConcreteBlockItem(blockConcrete).setRegistryName(BlockNames.CONCRETE));

        TileEntity.addMapping(RemnantRuinChestTE.class, "tile.chestPlotonium");
    }

    private static void registerBlock(Block block)
    {
        GameRegistry.register(block);
    }

    private static void registerItem(Item item) {
        GameRegistry.register(item);
    }

    private static void registerBlockAndItem(Block block)
    {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    private static void registerBlockAndOre(Block block, String name)
    {
        registerBlockAndItem(block);
        OreDictionary.registerOre(name, block);
    }

    private static void registerStructure(SteamNSteelStructureBlock structure, SteamNSteelStructureShapeBlock shape, String name)
    {
        GameRegistry.register(structure);
        GameRegistry.register(new StructureBlockItem(structure).setRegistryName(structure.getRegistryName()));
        StructureRegistry.registerStructureForLoad(structure, shape);
    }

    public static void remapMissingMappings(List<MissingMapping> missingMappings)
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

    private static void remapBlock(MissingMapping missingMapping, Block block)
    {
        if (missingMapping.type == Type.BLOCK)
        {
            missingMapping.remap(block);
        } else {
            missingMapping.remap(Item.getItemFromBlock(block));
        }
    }
}
