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

package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.TheMod;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("UtilityClass")
abstract class SteamNSteelModel
{
    private static final String MODEL_LOCATION = "models/";
    private static final String MODEL_FILE_EXTENSION = ".obj";

    static ResourceLocation getResourceLocation(String path)
    {
        return new ResourceLocation(TheMod.MOD_ID.toLowerCase(), path);
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    protected static String getModelPath(String name)
    {
        return MODEL_LOCATION + name + MODEL_FILE_EXTENSION;
    }
}
