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

import mod.steamnsteel.TheMod;
import mod.steamnsteel.item.SteamNSteelItem;
import mod.steamnsteel.item.armor.*;
import mod.steamnsteel.item.artifact.*;
import mod.steamnsteel.item.resource.Niter;
import mod.steamnsteel.item.resource.Sulfur;
import mod.steamnsteel.item.resource.ingot.*;
import mod.steamnsteel.item.tool.*;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static mod.steamnsteel.library.Material.BRONZE;
import static mod.steamnsteel.library.Material.STEEL;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModItem
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    public static final SteamNSteelItemArmor bootsBronze = new SSArmorBoots(BRONZE);
    public static final SteamNSteelItemArmor chestplateBronze = new SSArmorChest(BRONZE);
    public static final SteamNSteelItemArmor helmetBronze = new SSArmorHelmet(BRONZE);
    public static final SteamNSteelItemArmor leggingsBronze = new SSArmorLegs(BRONZE);

    public static final SteamNSteelItemArmor bootsSteel = new SSArmorBoots(STEEL);
    public static final SteamNSteelItemArmor chestplateSteel = new SSArmorChest(STEEL);
    public static final SteamNSteelItemArmor helmetSteel = new SSArmorHelmet(STEEL);
    public static final SteamNSteelItemArmor leggingsSteel = new SSArmorLegs(STEEL);

    public static final SSToolAxe axeBronze = new SSToolAxe(BRONZE);
    public static final SSToolAxe axeSteel = new SSToolAxe(STEEL);
    public static final SSToolHoe hoeBronze = new SSToolHoe(BRONZE);
    public static final SSToolHoe hoeSteel = new SSToolHoe(STEEL);
    public static final SSToolPickaxe pickBronze = new SSToolPickaxe(BRONZE);
    public static final SSToolPickaxe pickSteel = new SSToolPickaxe(STEEL);
    public static final SSToolShovel shovelBronze = new SSToolShovel(BRONZE);
    public static final SSToolShovel shovelSteel = new SSToolShovel(STEEL);
    public static final SSToolSword swordBronze = new SSToolSword(BRONZE);
    public static final SSToolSword swordSteel = new SSToolSword(STEEL);
    public static final StructureBuild structureBuildSteel = new StructureBuild(STEEL);

    public static final SteamNSteelItem anachDoodad = new AnachDoodad();
    public static final SteamNSteelItem dustNiter = new Niter();
    public static final SteamNSteelItem dustSulfur = new Sulfur();
    public static final SteamNSteelItem ingotBrass = new BrassIngot();
    public static final SteamNSteelItem ingotBronze = new BronzeIngot();
    public static final SteamNSteelItem ingotCopper = new CopperIngot();
    public static final SteamNSteelItem ingotPlotonium = new PlotoniumIngot();
    public static final SteamNSteelItem ingotSteel = new SteelIngot();
    public static final SteamNSteelItem ingotTin = new TinIngot();
    public static final SteamNSteelItem ingotZinc = new ZincIngot();
    public static final SteamNSteelItem mustyJournal = new MustyJournal();
    public static final SteamNSteelItem perGuiVox = new PerGuiVox();
    public static final SteamNSteelItem plotoniumScrap = new PlotoniumScrap();
    public static final SteamNSteelItem voxBox = new VoxBox();

    private ModItem()
    {
        throw new AssertionError();
    }

    public static void init()
    {
        registerArtifacts();
        registerArmor();
        registerIngots();
        registerDusts();
        registerTools();
        registerWeapons();
    }

    private static void registerArmor()
    {
        GameRegistry.registerItem(helmetBronze, helmetBronze.getUndecoratedName());
        GameRegistry.registerItem(chestplateBronze, chestplateBronze.getUndecoratedName());
        GameRegistry.registerItem(leggingsBronze, leggingsBronze.getUndecoratedName());
        GameRegistry.registerItem(bootsBronze, bootsBronze.getUndecoratedName());

        GameRegistry.registerItem(helmetSteel, helmetSteel.getUndecoratedName());
        GameRegistry.registerItem(chestplateSteel, chestplateSteel.getUndecoratedName());
        GameRegistry.registerItem(leggingsSteel, leggingsSteel.getUndecoratedName());
        GameRegistry.registerItem(bootsSteel, bootsSteel.getUndecoratedName());
    }

    private static void registerArtifacts()
    {
        GameRegistry.registerItem(anachDoodad, Names.ANACH_DOODAD);
        GameRegistry.registerItem(mustyJournal, Names.MUSTY_JOURNAL);
        GameRegistry.registerItem(perGuiVox, Names.PER_GUI_VOX);
        GameRegistry.registerItem(plotoniumScrap, Names.PLOTONIUM_SCRAP);
        GameRegistry.registerItem(voxBox, Names.VOX_BOX);
    }

    private static void registerDusts()
    {
        registerItemAndOre(dustNiter, Names.NITER);
        registerItemAndOre(dustSulfur, Names.SULFUR);
    }

    private static void registerIngots()
    {
        registerItemAndOre(ingotBrass, Names.BRASS_INGOT);
        registerItemAndOre(ingotBronze, Names.BRONZE_INGOT);
        registerItemAndOre(ingotCopper, Names.COPPER_INGOT);
        registerItemAndOre(ingotPlotonium, Names.PLOTONIUM_INGOT);
        registerItemAndOre(ingotSteel, Names.STEEL_INGOT);
        registerItemAndOre(ingotTin, Names.TIN_INGOT);
        registerItemAndOre(ingotZinc, Names.ZINC_INGOT);
    }

    private static void registerTools()
    {
        GameRegistry.registerItem(shovelBronze, shovelBronze.getUndecoratedName());
        GameRegistry.registerItem(pickBronze, pickBronze.getUndecoratedName());
        GameRegistry.registerItem(axeBronze, axeBronze.getUndecoratedName());
        GameRegistry.registerItem(hoeBronze, hoeBronze.getUndecoratedName());

        GameRegistry.registerItem(shovelSteel, shovelSteel.getUndecoratedName());
        GameRegistry.registerItem(pickSteel, pickSteel.getUndecoratedName());
        GameRegistry.registerItem(axeSteel, axeSteel.getUndecoratedName());
        GameRegistry.registerItem(hoeSteel, hoeSteel.getUndecoratedName());

        GameRegistry.registerItem(structureBuildSteel, structureBuildSteel.getUndecoratedName());
    }

    private static void registerWeapons()
    {
        GameRegistry.registerItem(swordBronze, swordBronze.getUndecoratedName());
        GameRegistry.registerItem(swordSteel, swordSteel.getUndecoratedName());
    }

    private static void registerItemAndOre(Item item, String name)
    {
        GameRegistry.registerItem(item, name);
        OreDictionary.registerOre(name, item);
    }

    public enum Names
    {
        INSTANCE;

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
    }
}
