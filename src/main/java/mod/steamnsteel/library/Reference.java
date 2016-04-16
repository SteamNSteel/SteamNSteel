package mod.steamnsteel.library;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.machine.CupolaBlock;
import net.minecraft.util.ResourceLocation;

/**
 * Created by codew on 24/10/2015.
 */
public final class Reference {
    private Reference() {}

    public static final String MOD_ID = "steamnsteel";


    public static class ArmourTextures {
        private ArmourTextures() {}

        public static final String TEXTURE_LOCATION = "textures/armor/";

        public static final String BRONZE = "bronze_layer";
        public static final String STEEL = "bronze_layer";
    }

    public static class BlockNames {


        private BlockNames() {}

        public static final String BRASS_BLOCK = "blockBrass";
        public static final String BRONZE_BLOCK = "blockBronze";
        public static final String COPPER_BLOCK = "blockCopper";
        public static final String PLOTONIUM_BLOCK = "blockPlotonium";
        public static final String STEEL_BLOCK = "blockSteel";
        public static final String TIN_BLOCK = "blockTin";
        public static final String ZINC_BLOCK = "blockZinc";

        public static final String STORAGE_BRASS_BLOCK = "storageBrass";
        public static final String STORAGE_BRONZE_BLOCK = "storageBronze";
        public static final String STORAGE_COPPER_BLOCK = "storageCopper";
        public static final String STORAGE_PLOTONIUM_BLOCK = "storagePlotonium";
        public static final String STORAGE_STEEL_BLOCK = "storageSteel";
        public static final String STORAGE_TIN_BLOCK = "storageTin";
        public static final String STORAGE_ZINC_BLOCK = "storageZinc";

        public static final String CUPOLA = "cupola";

        public static final String STRUCTURE_SHAPE = "structureShape";
        public static final String STRUCTURE_SHAPE_LI = "shapeLIBlock";

        public static final String BALL_MILL = "ssBallMill";
        public static final String BLAST_FURNACE = "ssBlastFurnace";
        public static final String BOILER = "ssBoiler";
        public static final String FAN_LARGE = "fanLarge";

        public static final String PIPE = "pipe";
        public static final String PIPE_VALVE = "pipeValve";
        public static final String PIPE_VALVE_REDSTONE = "pipeValveRedstone";
        public static final String PIPE_JUNCTION = "pipeJunction";

        public static final String REMNANT_RUIN_CHEST = "remnantRuinChest";
        public static final String PROJECT_TABLE = "projectTable";

        public static final String REMNANT_RUIN_PILLAR = "remnantRuinPillar";

        public static final String ORE_NITER = "oreNiter";
        public static final String ORE_COPPER = "oreCopper";
        public static final String ORE_SULFUR = "oreSulfur";
        public static final String ORE_TIN = "oreTin";
        public static final String ORE_ZINC = "oreZinc";

        public static final String CONCRETE = "blockConcrete";

        public static final String REMNANT_RUIN_FLOOR = "remnantRuinFloor";
        public static final String REMNANT_RUIN_WALL = "remnantRuinWall";
        public static final String REMNANT_RUIN_IRON_BARS = "remnantRuinIronBars";
    }

    public static class ItemNames
    {
        private ItemNames()
        {
        }

        // Artifacts
        public static final String ANACH_DOODAD = "anachDoodad";
        public static final String MUSTY_JOURNAL = "mustyJournal";
        public static final String PER_GUI_VOX = "perGuiVox";
        public static final String PLOTONIUM_SCRAP = "plotoniumScrap";
        public static final String VOX_BOX = "voxBox";
        // Ingots
        public static final String BRASS_INGOT = "ingotBrass";
        public static final String BRONZE_INGOT = "ingotBronze";
        public static final String COPPER_INGOT = "ingotCopper";
        public static final String PLOTONIUM_INGOT = "ingotPlotonium";
        public static final String STEEL_INGOT = "ingotSteel";
        public static final String TIN_INGOT = "ingotTin";
        public static final String ZINC_INGOT = "ingotZinc";
        // Items
        public static final String NITER = "dustNiter";
        public static final String SULFUR = "dustSulfur";

        // Tools
        public static final String TOOL_AXE_BRONZE = "axeBronze";
        public static final String TOOL_AXE_STEEL = "axeSteel";
        public static final String TOOL_HOE_BRONZE = "hoeBronze";
        public static final String TOOL_HOE_STEEL = "hoeSteel";
        public static final String TOOL_PICKAXE_BRONZE = "pickBronze";
        public static final String TOOL_PICKAXE_STEEL = "pickSteel";
        public static final String TOOL_SHOVEL_BRONZE = "shovelBronze";
        public static final String TOOL_SHOVEL_STEEL = "shovelSteel";
        public static final String TOOL_SWORD_BRONZE = "swordBronze";
        public static final String TOOL_SWORD_STEEL = "swordSteel";
        public static final String TOOL_STRUCTURE_BUILD_STEEL = "structureBuildSteel";

        //Armour
        public static final String ARMOR_BOOTS_BRONZE = "bootsBronze";
        public static final String ARMOR_BOOTS_STEEL = "bootsSteel";
        public static final String ARMOR_CHESTPLATE_BRONZE = "chestplateBronze";
        public static final String ARMOR_CHESTPLATE_STEEL = "chestplateSteel";
        public static final String ARMOR_HELMET_BRONZE = "helmetBronze";
        public static final String ARMOR_HELMET_STEEL = "helmetSteel";
        public static final String ARMOR_LEGGINGS_BRONZE = "leggingsBronze";
        public static final String ARMOR_LEGGINGS_STEEL = "leggingsSteel";
    }

    public static class Textures {
        private Textures() {}

        public static final String CUPOLA_GUI_TEXTURE = BlockNames.CUPOLA;
    }

    public static String containerName(String name)
    {
        return "container." + MOD_ID + ':' + name;
    }
}
