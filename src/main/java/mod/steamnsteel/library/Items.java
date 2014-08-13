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

@SuppressWarnings("UtilityClass")
@GameRegistry.ObjectHolder(Reference.MOD_ID)
public final class Items
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

    private Items()
    {
        throw new AssertionError();
    }

    public static void init()
    {
        GameRegistry.registerItem(ANACH_DOODAD, Names.Artifacts.ANACH_DOODAD);
        GameRegistry.registerItem(MUSTY_JOURNAL, Names.Artifacts.MUSTY_JOURNAL);
        GameRegistry.registerItem(PER_GUI_VOX, Names.Artifacts.PER_GUI_VOX);
        GameRegistry.registerItem(PLOTONIUM_SCRAP, Names.Artifacts.PLOTONIUM_SCRAP);
        GameRegistry.registerItem(VOX_BOX, Names.Artifacts.VOX_BOX);

        GameRegistry.registerItem(BRONZE_BOOTS, Names.Armor.BRONZE_BOOTS);
        GameRegistry.registerItem(BRONZE_CHESTPLATE, Names.Armor.BRONZE_CHESTPLATE);
        GameRegistry.registerItem(BRONZE_HELMET, Names.Armor.BRONZE_HELMET);
        GameRegistry.registerItem(BRONZE_LEGGINGS, Names.Armor.BRONZE_LEGGINGS);

        GameRegistry.registerItem(BRONZE_AXE, Names.Tools.BRONZE_AXE);
        GameRegistry.registerItem(BRONZE_HOE, Names.Tools.BRONZE_HOE);
        GameRegistry.registerItem(BRONZE_PICKAXE, Names.Tools.BRONZE_PICKAXE);
        GameRegistry.registerItem(BRONZE_SHOVEL, Names.Tools.BRONZE_SHOVEL);

        GameRegistry.registerItem(BRASS_INGOT, Names.Ingots.BRASS_INGOT);
        GameRegistry.registerItem(BRONZE_INGOT, Names.Ingots.BRONZE_INGOT);
        GameRegistry.registerItem(COPPER_INGOT, Names.Ingots.COPPER_INGOT);
        GameRegistry.registerItem(PLOTONIUM_INGOT, Names.Ingots.PLOTONIUM_INGOT);
        GameRegistry.registerItem(STEEL_INGOT, Names.Ingots.STEEL_INGOT);
        GameRegistry.registerItem(TIN_INGOT, Names.Ingots.TIN_INGOT);
        GameRegistry.registerItem(ZINC_INGOT, Names.Ingots.ZINC_INGOT);

        GameRegistry.registerItem(NITER, Names.Items.NITER);
        GameRegistry.registerItem(SULFUR, Names.Items.SULFUR);

        GameRegistry.registerItem(STEEL_BOOTS, Names.Armor.STEEL_BOOTS);
        GameRegistry.registerItem(STEEL_CHESTPLATE, Names.Armor.STEEL_CHESTPLATE);
        GameRegistry.registerItem(STEEL_HELMET, Names.Armor.STEEL_HELMET);
        GameRegistry.registerItem(STEEL_LEGGINGS, Names.Armor.STEEL_LEGGINGS);

        GameRegistry.registerItem(STEEL_AXE, Names.Tools.STEEL_AXE);
        GameRegistry.registerItem(STEEL_HOE, Names.Tools.STEEL_HOE);
        GameRegistry.registerItem(STEEL_PICKAXE, Names.Tools.STEEL_PICKAXE);
        GameRegistry.registerItem(STEEL_SHOVEL, Names.Tools.STEEL_SHOVEL);

        GameRegistry.registerItem(BRONZE_SWORD, Names.Weapons.BRONZE_SWORD);
        GameRegistry.registerItem(STEEL_SWORD, Names.Weapons.STEEL_SWORD);
    }

}
