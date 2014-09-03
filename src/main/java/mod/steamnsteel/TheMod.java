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

package mod.steamnsteel;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mod.steamnsteel.configuration.ConfigurationHandler;
import mod.steamnsteel.crafting.Recipes;
import mod.steamnsteel.library.ModBlocks;
import mod.steamnsteel.library.ModItems;
import mod.steamnsteel.proxy.Proxies;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@SuppressWarnings({"WeakerAccess", "MethodMayBeStatic"})
@Mod(modid = TheMod.MOD_ID, name = TheMod.MOD_NAME, version = TheMod.MOD_VERSION, useMetadata = true, guiFactory = TheMod.MOD_GUI_FACTORY)
public class TheMod
{
    public static final String MOD_ID = "steamnsteel";
    public static final String MOD_NAME = "Steam and Steel";
    public static final String MOD_VERSION = "@MOD_VERSION@";
    public static final String MOD_GUI_FACTORY = "mod.steamnsteel.configuration.client.ModGuiFactory";

    public static final String NETWORK_CHANNEL = MOD_ID.toLowerCase();
    public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ':';

    @SuppressWarnings({"StaticVariableOfConcreteClass", "StaticNonFinalField", "PublicField", "StaticVariableMayNotBeInitialized"})
    @Mod.Instance
    public static TheMod instance;

    @SuppressWarnings("AnonymousInnerClass")
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID.toLowerCase())
    {
        @Override
        public Item getTabIconItem()
        {
            return ModItems.MUSTY_JOURNAL;
        }
    };

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        ModItems.init();

        ModBlocks.init();
    }

    @SuppressWarnings("UnusedParameters")
    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(ConfigurationHandler.INSTANCE);
		MinecraftForge.TERRAIN_GEN_BUS.register(TerrainGenEventHandler.INSTANCE);
        Recipes.init();
        Proxies.render.init();
        ModBlocks.registerTileEntities();
    }

    @SuppressWarnings("UnusedParameters")
    @Mod.EventHandler
    public void onFMLPostInitialization(FMLPostInitializationEvent event)
    {
        // TODO: Handle interaction with other mods, complete your setup based on this.
    }
}
