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

import mod.steamnsteel.item.SteamNSteelItem;
import mod.steamnsteel.item.armor.SSArmorBoots;
import mod.steamnsteel.item.armor.SSArmorChest;
import mod.steamnsteel.item.armor.SSArmorHelmet;
import mod.steamnsteel.item.armor.SSArmorLegs;
import mod.steamnsteel.item.tool.*;
import mod.steamnsteel.library.Reference.ItemNames;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;

import static mod.steamnsteel.library.Material.BRONZE;
import static mod.steamnsteel.library.Material.STEEL;

@SuppressWarnings({"UtilityClass", "WeakerAccess", "StaticNonFinalField", "StaticVariableMayNotBeInitialized", "PublicField"})
@ObjectHolder(Reference.MOD_ID)
public final class ModItem
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    public static Item bootsBronze;
    public static Item bootsSteel;
    public static Item chestplateBronze;
    public static Item chestplateSteel;
    public static Item helmetBronze;
    public static Item helmetSteel;
    public static Item leggingsBronze;
    public static Item leggingsSteel;

    //public static Item axeBronze;
    //public static Item axeSteel;
    public static Item hoeBronze;
    public static Item hoeSteel;
    public static Item pickBronze;
    public static Item pickSteel;
    public static Item shovelBronze;
    public static Item shovelSteel;
    public static Item swordBronze;
    public static Item swordSteel;
    public static Item structureBuildSteel;

    public static Item dustNiter;
    public static Item dustSulfur;

    public static Item ingotBrass;
    public static Item ingotBronze;
    public static Item ingotCopper;
    public static Item ingotPlotonium;
    public static Item ingotSteel;
    public static Item ingotTin;
    public static Item ingotZinc;

    public static Item anachDoodad;
    public static Item mustyJournal;
    public static Item perGuiVox;
    public static Item plotoniumScrap;
    public static Item voxBox;

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
        bootsBronze = new SSArmorBoots(BRONZE).setRegistryName(ItemNames.ARMOR_BOOTS_BRONZE);
        bootsSteel = new SSArmorBoots(STEEL).setRegistryName(ItemNames.ARMOR_BOOTS_STEEL);
        chestplateBronze = new SSArmorChest(BRONZE).setRegistryName(ItemNames.ARMOR_CHESTPLATE_BRONZE);
        chestplateSteel = new SSArmorChest(STEEL).setRegistryName(ItemNames.ARMOR_CHESTPLATE_STEEL);
        helmetBronze = new SSArmorHelmet(BRONZE).setRegistryName(ItemNames.ARMOR_HELMET_BRONZE);
        helmetSteel = new SSArmorHelmet(STEEL).setRegistryName(ItemNames.ARMOR_HELMET_STEEL);
        leggingsBronze = new SSArmorLegs(BRONZE).setRegistryName(ItemNames.ARMOR_LEGGINGS_BRONZE);
        leggingsSteel = new SSArmorLegs(STEEL).setRegistryName(ItemNames.ARMOR_LEGGINGS_STEEL);

        GameRegistry.register(helmetBronze);
        GameRegistry.register(chestplateBronze);
        GameRegistry.register(leggingsBronze);
        GameRegistry.register(bootsBronze);

        GameRegistry.register(helmetSteel);
        GameRegistry.register(chestplateSteel);
        GameRegistry.register(leggingsSteel);
        GameRegistry.register(bootsSteel);
    }

    private static void registerArtifacts()
    {
        anachDoodad = new SteamNSteelItem().setRegistryName(ItemNames.ANACH_DOODAD);
        mustyJournal = new SteamNSteelItem().setRegistryName(ItemNames.MUSTY_JOURNAL);
        perGuiVox = new SteamNSteelItem().setRegistryName(ItemNames.PER_GUI_VOX);
        plotoniumScrap = new SteamNSteelItem().setRegistryName(ItemNames.PLOTONIUM_SCRAP);
        voxBox = new SteamNSteelItem().setRegistryName(ItemNames.VOX_BOX);

        GameRegistry.register(anachDoodad);
        GameRegistry.register(mustyJournal);
        GameRegistry.register(perGuiVox);
        GameRegistry.register(plotoniumScrap);
        GameRegistry.register(voxBox);
    }

    private static void registerDusts()
    {
        dustNiter = new SteamNSteelItem().setRegistryName(ItemNames.NITER);
        dustSulfur = new SteamNSteelItem().setRegistryName(ItemNames.SULFUR);

        registerItemAndOre(dustNiter);
        registerItemAndOre(dustSulfur);
    }

    private static void registerIngots()
    {
        ingotBrass = new SteamNSteelItem().setRegistryName(ItemNames.BRASS_INGOT);
        ingotBronze = new SteamNSteelItem().setRegistryName(ItemNames.BRONZE_INGOT);
        ingotCopper = new SteamNSteelItem().setRegistryName(ItemNames.COPPER_INGOT);
        ingotPlotonium = new SteamNSteelItem().setRegistryName(ItemNames.PLOTONIUM_INGOT);
        ingotSteel = new SteamNSteelItem().setRegistryName(ItemNames.STEEL_INGOT);
        ingotTin = new SteamNSteelItem().setRegistryName(ItemNames.TIN_INGOT);
        ingotZinc = new SteamNSteelItem().setRegistryName(ItemNames.ZINC_INGOT);

        registerItemAndOre(ingotBrass);
        registerItemAndOre(ingotBronze);
        registerItemAndOre(ingotCopper);
        registerItemAndOre(ingotPlotonium);
        registerItemAndOre(ingotSteel);
        registerItemAndOre(ingotTin);
        registerItemAndOre(ingotZinc);
    }

    private static void registerTools()
    {
        //axeBronze = new SSToolAxe(BRONZE).setRegistryName(ItemNames.TOOL_AXE_BRONZE);
        //axeSteel = new SSToolAxe(STEEL).setRegistryName(ItemNames.TOOL_AXE_STEEL);
        hoeBronze = new SSToolHoe(BRONZE).setRegistryName(ItemNames.TOOL_HOE_BRONZE);
        hoeSteel = new SSToolHoe(STEEL).setRegistryName(ItemNames.TOOL_HOE_STEEL);
        pickBronze = new SSToolPickaxe(BRONZE).setRegistryName(ItemNames.TOOL_PICKAXE_BRONZE);
        pickSteel = new SSToolPickaxe(STEEL).setRegistryName(ItemNames.TOOL_PICKAXE_STEEL);
        shovelBronze = new SSToolShovel(BRONZE).setRegistryName(ItemNames.TOOL_SHOVEL_BRONZE);
        shovelSteel = new SSToolShovel(STEEL).setRegistryName(ItemNames.TOOL_SHOVEL_STEEL);
        structureBuildSteel = new StructureBuild(STEEL).setRegistryName(ItemNames.TOOL_STRUCTURE_BUILD_STEEL);

        GameRegistry.register(shovelBronze);
        GameRegistry.register(pickBronze);
        //GameRegistry.register(axeBronze);
        GameRegistry.register(hoeBronze);

        GameRegistry.register(shovelSteel);
        GameRegistry.register(pickSteel);
        //GameRegistry.register(axeSteel);
        GameRegistry.register(hoeSteel);

        GameRegistry.register(structureBuildSteel);
    }

    private static void registerWeapons()
    {
        swordBronze = new SSToolSword(BRONZE).setRegistryName(ItemNames.TOOL_SWORD_BRONZE);
        swordSteel = new SSToolSword(STEEL).setRegistryName(ItemNames.TOOL_SWORD_STEEL);

        GameRegistry.register(swordBronze);
        GameRegistry.register(swordSteel);
    }

    private static void registerItemAndOre(Item item)
    {
        GameRegistry.register(item);
        OreDictionary.registerOre(item.getRegistryName().getResourcePath(), item);
    }
}
