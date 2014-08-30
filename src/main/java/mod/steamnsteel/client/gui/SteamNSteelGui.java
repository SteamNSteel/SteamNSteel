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

package mod.steamnsteel.client.gui;

import mod.steamnsteel.TheMod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class SteamNSteelGui extends GuiContainer
{
    protected static final int TEXT_COLOR = 4210752;
    protected static final String VANILLA_INVENTORY = "container.inventory";
    private static final String LOCATION = "textures/gui/";
    private static final String FILE_EXTENSION = ".png";

    public SteamNSteelGui(Container container)
    {
        super(container);
    }

    protected static ResourceLocation getResourceLocation(String path)
    {
        return getResourceLocation(TheMod.MOD_ID.toLowerCase(), LOCATION + path + FILE_EXTENSION);
    }

    private static ResourceLocation getResourceLocation(String modID, String path)
    {
        return new ResourceLocation(modID, path);
    }
}
