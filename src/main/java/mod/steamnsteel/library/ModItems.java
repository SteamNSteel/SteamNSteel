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

@SuppressWarnings("UtilityClass")
@GameRegistry.ObjectHolder(Constants.MOD_ID)
public final class ModItems
{
    @SuppressWarnings("InnerClassFieldHidesOuterClassField")
    public enum Names
    {
        _;
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

    public static final SteamNSteelItem BRONZE_BOOTS = new BronzeBoots();
    public static final SteamNSteelItem BRONZE_CHESTPLATE = new BronzeChestplate();
    public static final SteamNSteelItem BRONZE_HELMET = new BronzeHelmet();
    public static final SteamNSteelItem BRONZE_LEGGINGS = new BronzeLeggings();

    public static final SteamNSteelItem STEEL_BOOTS = new SteelBoots();
    public static final SteamNSteelItem STEEL_CHESTPLATE = new SteelChestplate();
    public static final SteamNSteelItem STEEL_HELMET = new SteelHelmet();
    public static final SteamNSteelItem STEEL_LEGGINGS = new SteelLeggings();

    public static final SteamNSteelItem ANACH_DOODAD = new AnachDoodad();
    public static final SteamNSteelItem MUSTY_JOURNAL = new MustyJournal();
    public static final SteamNSteelItem PER_GUI_VOX = new PerGuiVox();
    public static final SteamNSteelItem PLOTONIUM_SCRAP = new PlotoniumScrap();
    public static final SteamNSteelItem VOX_BOX = new VoxBox();

    public static final SteamNSteelItem NITER = new Niter();
    public static final SteamNSteelItem SULFUR = new Sulfur();

    public static final SteamNSteelItem BRASS_INGOT = new BrassIngot();
    public static final SteamNSteelItem BRONZE_INGOT = new BronzeIngot();
    public static final SteamNSteelItem COPPER_INGOT = new CopperIngot();
    public static final SteamNSteelItem PLOTONIUM_INGOT = new PlotoniumIngot();
    public static final SteamNSteelItem STEEL_INGOT = new SteelIngot();
    public static final SteamNSteelItem TIN_INGOT = new TinIngot();
    public static final SteamNSteelItem ZINC_INGOT = new ZincIngot();

    public static final SteamNSteelItem BRONZE_AXE = new BronzeAxe();
    public static final SteamNSteelItem BRONZE_HOE = new BronzeHoe();
    public static final SteamNSteelItem BRONZE_PICKAXE = new BronzePickaxe();
    public static final SteamNSteelItem BRONZE_SHOVEL = new BronzeShovel();
    public static final SteamNSteelItem BRONZE_SWORD = new BronzeSword();

    public static final SteamNSteelItem STEEL_AXE = new SteelAxe();
    public static final SteamNSteelItem STEEL_HOE = new SteelHoe();
    public static final SteamNSteelItem STEEL_PICKAXE = new SteelPickaxe();
    public static final SteamNSteelItem STEEL_SHOVEL = new SteelShovel();
    public static final SteamNSteelItem STEEL_SWORD = new SteelSword();

    private ModItems()
    {
        throw new AssertionError();
    }

    public static void init()
    {
        GameRegistry.registerItem(ANACH_DOODAD, Names.ANACH_DOODAD);
        GameRegistry.registerItem(MUSTY_JOURNAL, Names.MUSTY_JOURNAL);
        GameRegistry.registerItem(PER_GUI_VOX, Names.PER_GUI_VOX);
        GameRegistry.registerItem(PLOTONIUM_SCRAP, Names.PLOTONIUM_SCRAP);
        GameRegistry.registerItem(VOX_BOX, Names.VOX_BOX);

        GameRegistry.registerItem(BRONZE_BOOTS, Names.BRONZE_BOOTS);
        GameRegistry.registerItem(BRONZE_CHESTPLATE, Names.BRONZE_CHESTPLATE);
        GameRegistry.registerItem(BRONZE_HELMET, Names.BRONZE_HELMET);
        GameRegistry.registerItem(BRONZE_LEGGINGS, Names.BRONZE_LEGGINGS);

        GameRegistry.registerItem(BRONZE_AXE, Names.BRONZE_AXE);
        GameRegistry.registerItem(BRONZE_HOE, Names.BRONZE_HOE);
        GameRegistry.registerItem(BRONZE_PICKAXE, Names.BRONZE_PICKAXE);
        GameRegistry.registerItem(BRONZE_SHOVEL, Names.BRONZE_SHOVEL);

        registerItemAndOre(BRASS_INGOT, Names.BRASS_INGOT);
        registerItemAndOre(BRONZE_INGOT, Names.BRONZE_INGOT);
        registerItemAndOre(COPPER_INGOT, Names.COPPER_INGOT);
        registerItemAndOre(PLOTONIUM_INGOT, Names.PLOTONIUM_INGOT);
        registerItemAndOre(STEEL_INGOT, Names.STEEL_INGOT);
        registerItemAndOre(TIN_INGOT, Names.TIN_INGOT);
        registerItemAndOre(ZINC_INGOT, Names.ZINC_INGOT);

        registerItemAndOre(NITER, Names.NITER);
        registerItemAndOre(SULFUR, Names.SULFUR);

        GameRegistry.registerItem(STEEL_BOOTS, Names.STEEL_BOOTS);
        GameRegistry.registerItem(STEEL_CHESTPLATE, Names.STEEL_CHESTPLATE);
        GameRegistry.registerItem(STEEL_HELMET, Names.STEEL_HELMET);
        GameRegistry.registerItem(STEEL_LEGGINGS, Names.STEEL_LEGGINGS);

        GameRegistry.registerItem(STEEL_AXE, Names.STEEL_AXE);
        GameRegistry.registerItem(STEEL_HOE, Names.STEEL_HOE);
        GameRegistry.registerItem(STEEL_PICKAXE, Names.STEEL_PICKAXE);
        GameRegistry.registerItem(STEEL_SHOVEL, Names.STEEL_SHOVEL);

        GameRegistry.registerItem(BRONZE_SWORD, Names.BRONZE_SWORD);
        GameRegistry.registerItem(STEEL_SWORD, Names.STEEL_SWORD);
    }

    private static void registerItemAndOre(Item item, String name)
    {
        GameRegistry.registerItem(item, name);
        OreDictionary.registerOre(name, item);
    }
}
