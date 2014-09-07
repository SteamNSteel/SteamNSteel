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
import mod.steamnsteel.item.SteamNSteelItem;
import mod.steamnsteel.item.armor.*;
import mod.steamnsteel.item.artifact.*;
import mod.steamnsteel.item.resource.Niter;
import mod.steamnsteel.item.resource.Sulfur;
import mod.steamnsteel.item.resource.ingot.*;
import mod.steamnsteel.item.tool.*;
import mod.steamnsteel.item.weapon.BronzeSword;
import mod.steamnsteel.item.weapon.SteelSword;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModItems
{
    public static final SteamNSteelItem bootsBronze = new BronzeBoots();
    public static final SteamNSteelItem chestplateBronze = new BronzeChestplate();
    public static final SteamNSteelItem helmetBronze = new BronzeHelmet();
    public static final SteamNSteelItem leggingsBronze = new BronzeLeggings();
    public static final SteamNSteelItem bootsSteel = new SteelBoots();
    public static final SteamNSteelItem chestplateSteel = new SteelChestplate();
    public static final SteamNSteelItem helmetSteel = new SteelHelmet();
    public static final SteamNSteelItem leggingsSteel = new SteelLeggings();
    public static final SteamNSteelItem anachDoodad = new AnachDoodad();
    public static final SteamNSteelItem mustyJournal = new MustyJournal();
    public static final SteamNSteelItem perGuiVox = new PerGuiVox();
    public static final SteamNSteelItem plotoniumScrap = new PlotoniumScrap();
    public static final SteamNSteelItem voxBox = new VoxBox();
    public static final SteamNSteelItem dustNiter = new Niter();
    public static final SteamNSteelItem dustSulfur = new Sulfur();
    public static final SteamNSteelItem ingotBrass = new BrassIngot();
    public static final SteamNSteelItem ingotBronze = new BronzeIngot();
    public static final SteamNSteelItem ingotCopper = new CopperIngot();
    public static final SteamNSteelItem ingotPlotonium = new PlotoniumIngot();
    public static final SteamNSteelItem ingotSteel = new SteelIngot();
    public static final SteamNSteelItem ingotTin = new TinIngot();
    public static final SteamNSteelItem ingotZinc = new ZincIngot();
    public static final SteamNSteelItem axeBronze = new BronzeAxe();
    public static final SteamNSteelItem hoeBronze = new BronzeHoe();
    public static final SteamNSteelItem pickBronze = new BronzePickaxe();
    public static final SteamNSteelItem shovelBronze = new BronzeShovel();
    public static final SteamNSteelItem swordBronze = new BronzeSword();
    public static final SteamNSteelItem axeSteel = new SteelAxe();
    public static final SteamNSteelItem hoeSteel = new SteelHoe();
    public static final SteamNSteelItem pickSteel = new SteelPickaxe();
    public static final SteamNSteelItem shovelSteel = new SteelShovel();
    public static final SteamNSteelItem swordSteel = new SteelSword();

    private ModItems()
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
        GameRegistry.registerItem(bootsBronze, Names.BRONZE_BOOTS);
        GameRegistry.registerItem(chestplateBronze, Names.BRONZE_CHESTPLATE);
        GameRegistry.registerItem(helmetBronze, Names.BRONZE_HELMET);
        GameRegistry.registerItem(leggingsBronze, Names.BRONZE_LEGGINGS);

        GameRegistry.registerItem(bootsSteel, Names.STEEL_BOOTS);
        GameRegistry.registerItem(chestplateSteel, Names.STEEL_CHESTPLATE);
        GameRegistry.registerItem(helmetSteel, Names.STEEL_HELMET);
        GameRegistry.registerItem(leggingsSteel, Names.STEEL_LEGGINGS);
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
        GameRegistry.registerItem(axeBronze, Names.BRONZE_AXE);
        GameRegistry.registerItem(hoeBronze, Names.BRONZE_HOE);
        GameRegistry.registerItem(pickBronze, Names.BRONZE_PICKAXE);
        GameRegistry.registerItem(shovelBronze, Names.BRONZE_SHOVEL);

        GameRegistry.registerItem(axeSteel, Names.STEEL_AXE);
        GameRegistry.registerItem(hoeSteel, Names.STEEL_HOE);
        GameRegistry.registerItem(pickSteel, Names.STEEL_PICKAXE);
        GameRegistry.registerItem(shovelSteel, Names.STEEL_SHOVEL);
    }

    private static void registerWeapons()
    {
        GameRegistry.registerItem(swordBronze, Names.BRONZE_SWORD);
        GameRegistry.registerItem(swordSteel, Names.STEEL_SWORD);
    }

    private static void registerItemAndOre(Item item, String name)
    {
        GameRegistry.registerItem(item, name);
        OreDictionary.registerOre(name, item);
    }

    public enum Names
    {
        INSTANCE;
        // Armor
        public static final String BRONZE_BOOTS = "bootsBronze";
        public static final String BRONZE_CHESTPLATE = "chestplateBronze";
        public static final String BRONZE_HELMET = "helmetBronze";
        public static final String BRONZE_LEGGINGS = "leggingsBronze";
        public static final String STEEL_BOOTS = "bootsSteel";
        public static final String STEEL_CHESTPLATE = "chestplateSteel";
        public static final String STEEL_HELMET = "helmetSteel";
        public static final String STEEL_LEGGINGS = "leggingsSteel";
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
        public static final String BRONZE_AXE = "axeBronze";
        public static final String BRONZE_HOE = "hoeBronze";
        public static final String BRONZE_PICKAXE = "pickBronze";
        public static final String BRONZE_SHOVEL = "shovelBronze";
        public static final String STEEL_AXE = "axeSteel";
        public static final String STEEL_HOE = "hoeSteel";
        public static final String STEEL_PICKAXE = "pickSteel";
        public static final String STEEL_SHOVEL = "shovelSteel";
        // Weapons
        public static final String BRONZE_SWORD = "swordBronze";
        public static final String STEEL_SWORD = "swordSteel";
    }
}
