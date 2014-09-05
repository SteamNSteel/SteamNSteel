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

package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.TheMod;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

abstract class SteamNSteelTESR extends TileEntitySpecialRenderer
{
    private static final String TEXTURE_FILE_EXTENSION = ".png";
    public static final String TEXTURE_LOCATION = "textures/models/";

    static ResourceLocation getResourceLocation(String name)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), getTexturePath(name));
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static String getTexturePath(String name)
    {
        return TEXTURE_LOCATION + name + TEXTURE_FILE_EXTENSION;
    }
}
