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

@SuppressWarnings("UtilityClass")
public final class ModItems
{
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

    public static void init()
    {
        GameRegistry.registerItem(BRONZE_BOOTS, "bootsBronze");
        GameRegistry.registerItem(BRONZE_CHESTPLATE, "chestplateBronze");
        GameRegistry.registerItem(BRONZE_HELMET, "helmetBronze");
        GameRegistry.registerItem(BRONZE_LEGGINGS, "leggingsBronze");

        GameRegistry.registerItem(STEEL_BOOTS, "bootsSteel");
        GameRegistry.registerItem(STEEL_CHESTPLATE, "chestplateSteel");
        GameRegistry.registerItem(STEEL_HELMET, "helmetSteel");
        GameRegistry.registerItem(STEEL_LEGGINGS, "leggingsSteel");

        GameRegistry.registerItem(ANACH_DOODAD, "anachDoodad");
        GameRegistry.registerItem(MUSTY_JOURNAL, "mustyJournal");
        GameRegistry.registerItem(PER_GUI_VOX, "perGuiVox");
        GameRegistry.registerItem(PLOTONIUM_SCRAP, "plotoniumScrap");
        GameRegistry.registerItem(VOX_BOX, "voxBox");

        GameRegistry.registerItem(NITER, "niter");
        GameRegistry.registerItem(SULFUR, "sulfur");

        GameRegistry.registerItem(BRASS_INGOT, "ingotBrass");
        GameRegistry.registerItem(BRONZE_INGOT, "ingotBronze");
        GameRegistry.registerItem(COPPER_INGOT, "ingotCopper");
        GameRegistry.registerItem(PLOTONIUM_INGOT, "ingotPlotonium");
        GameRegistry.registerItem(STEEL_INGOT, "ingotSteel");
        GameRegistry.registerItem(TIN_INGOT, "ingotTin");

        GameRegistry.registerItem(BRONZE_AXE, "axeBronze");
        GameRegistry.registerItem(BRONZE_HOE, "hoeBronze");
        GameRegistry.registerItem(BRONZE_PICKAXE, "pickBronze");
        GameRegistry.registerItem(BRONZE_SHOVEL, "shovelBronze");
        GameRegistry.registerItem(BRONZE_SWORD, "swordBronze");

        GameRegistry.registerItem(STEEL_AXE, "axeSteel");
        GameRegistry.registerItem(STEEL_HOE, "hoeSteel");
        GameRegistry.registerItem(STEEL_PICKAXE, "pickSteel");
        GameRegistry.registerItem(STEEL_SHOVEL, "shovelSteel");
        GameRegistry.registerItem(STEEL_SWORD, "swordSteel");
    }

    // PRIVATE //

    /**
     * The caller references the constants using <tt>Reference.EMPTY_STRING</tt>,
     * and so on. Thus, the caller should be prevented from constructing objects of
     * this class, by declaring this private constructor.
     */
    private ModItems()
    {
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }

}
