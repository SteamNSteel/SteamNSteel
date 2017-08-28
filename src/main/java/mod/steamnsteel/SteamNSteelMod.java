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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@SuppressWarnings({"WeakerAccess", "MethodMayBeStatic"})
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, useMetadata = true)//, guiFactory = Reference.MOD_GUI_FACTORY)
public class SteamNSteelMod
{
	@SuppressWarnings("UnusedParameters")
	@Mod.EventHandler
	public void onFMLInitialization(FMLInitializationEvent event)
	{

	}

	@SuppressWarnings("UnusedParameters")
	@Mod.EventHandler
	public void onFMLPostInitialization(FMLPostInitializationEvent event)
	{
		//SteamNSteelInitializedEvent initializedEvent = new SteamNSteelInitializedEvent(CraftingManager.INSTANCE);
		//MinecraftForge.EVENT_BUS.post(initializedEvent);
	}
}
