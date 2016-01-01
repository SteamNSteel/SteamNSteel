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

package mod.steamnsteel.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.DefaultOverlayHandler;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.client.gui.CupolaGui;

@SuppressWarnings("WeakerAccess")
public class NEISteamNSteelConfig implements IConfigureNEI
{

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    @Override
    public void loadConfig()
    {
        API.registerRecipeHandler(new CupolaRecipeHandler());
        API.registerUsageHandler(new CupolaRecipeHandler());
        //FIXME: Reenable with the cupola
        API.registerGuiOverlay(CupolaGui.class, TheMod.MOD_ID + CupolaBlock.NAME, 5, 11);
        API.registerGuiOverlayHandler(CupolaGui.class, new DefaultOverlayHandler(5, 11), TheMod.MOD_ID + CupolaBlock.NAME);
    }

    @Override
    public String getName()
    {
        return TheMod.MOD_NAME;
    }

    @Override
    public String getVersion()
    {
        return TheMod.MOD_VERSION;
    }
}