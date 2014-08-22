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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public enum Models
{
    _;
    private static final String LOCATION = "models/";
    private static final String FILE_EXTENSION = ".obj";

    public static final ResourceLocation CUPOLA = getResourceLocation(getModelPath(Blocks.Names.CUPOLA));

    private static ResourceLocation getResourceLocation(String modId, String path)
    {
        return new ResourceLocation(modId, path);
    }

    private static ResourceLocation getResourceLocation(String path)
    {
        return getResourceLocation(Constants.MOD_ID.toLowerCase(), path);
    }

    private static String getModelPath(String name)
    {
        return LOCATION + name + FILE_EXTENSION;
    }
}
