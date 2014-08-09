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

@SuppressWarnings("InnerClassFieldHidesOuterClassField")
public enum Names
{
    INSTANCE;
    public enum Armor
    {
        INSTANCE;
        public static final String BRONZE_BOOTS = "bootsBronze";
        public static final String BRONZE_CHESTPLATE = "chestplateBronze";
        public static final String BRONZE_HELMET = "helmetBronze";
        public static final String BRONZE_LEGGINGS = "leggingsBronze";
        public static final String STEEL_BOOTS = "bootsSteel";
        public static final String STEEL_CHESTPLATE = "chestplateSteel";
        public static final String STEEL_HELMET = "helmetSteel";
        public static final String STEEL_LEGGINGS = "leggingsSteel";
    }

    public enum Artifacts
    {
        INSTANCE;
        public static final String ANACH_DOODAD = "anachDoodad";
        public static final String MUSTY_JOURNAL = "mustyJournal";
        public static final String PER_GUI_VOX = "perGuiVox";
        public static final String PLOTONIUM_SCRAP = "plotoniumScrap";
        public static final String VOX_BOX = "voxBox";
    }

    public enum Ingots
    {
        INSTANCE;
        public static final String BRASS_INGOT = "ingotBrass";
        public static final String BRONZE_INGOT = "ingotBronze";
        public static final String COPPER_INGOT = "ingotCopper";
        public static final String PLOTONIUM_INGOT = "ingotPlotonium";
        public static final String STEEL_INGOT = "ingotSteel";
        public static final String TIN_INGOT = "ingotTin";
    }

    public enum Items
    {
        INSTANCE;
        public static final String NITER = "niter";
        public static final String SULFUR = "sulfur";
    }

    public enum Tools
    {
        INSTANCE;
        public static final String BRONZE_AXE = "axeBronze";
        public static final String BRONZE_HOE = "hoeBronze";
        public static final String BRONZE_PICKAXE = "pickBronze";
        public static final String BRONZE_SHOVEL = "shovelBronze";
        public static final String STEEL_AXE = "axeSteel";
        public static final String STEEL_HOE = "hoeSteel";
        public static final String STEEL_PICKAXE = "pickSteel";
        public static final String STEEL_SHOVEL = "shovelSteel";
    }

    public enum Weapons
    {
        INSTANCE;
        public static final String BRONZE_SWORD = "swordBronze";
        public static final String STEEL_SWORD = "swordSteel";
    }
}
