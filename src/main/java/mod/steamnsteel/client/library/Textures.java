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

package mod.steamnsteel.client.library;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.library.Blocks;
import mod.steamnsteel.library.Reference;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings({"EnumeratedConstantNamingConvention", "InnerClassFieldHidesOuterClassField"})
@SideOnly(Side.CLIENT)
public enum Textures
{
    _;
    private static final String FILE_EXTENSION = ".png";

    public enum Armor
    {
        _;
        private static final String LOCATION = "textures/armor/";
    }

    public enum Model
    {
        _;
        private static final String LOCATION = "textures/models/";
        public static final ResourceLocation CUPOLA = getResourceLocation(getTexturePath(LOCATION, Blocks.Names.CUPOLA));
    }

    public enum Gui
    {
        _;
        private static final String LOCATION = "textures/gui/";
    }

    public enum Effect
    {
        _;
        private static final String LOCATION = "textures/effects/";
    }

    private static ResourceLocation getResourceLocation(String modId, String path)
    {
        return new ResourceLocation(modId, path);
    }

    private static ResourceLocation getResourceLocation(String path)
    {
        return getResourceLocation(Reference.MOD_ID.toLowerCase(), path);
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private static String getTexturePath(String location, String name)
    {
        return location + name + FILE_EXTENSION;
    }
}
