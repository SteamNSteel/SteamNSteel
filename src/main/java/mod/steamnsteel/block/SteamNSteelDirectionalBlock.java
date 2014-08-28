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

package mod.steamnsteel.block;

import mod.steamnsteel.utility.Orientation;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;

public abstract class SteamNSteelDirectionalBlock extends SteamNSteelBlock
{
    protected SteamNSteelDirectionalBlock(Material material)
    {
        super(material);
    }

    public static Orientation getOrientation(int metadata)
    {
        return Orientation.getdecodedOrientation(metadata);
    }

    @SuppressWarnings("ImplicitNumericConversion")
    public static int getMetadataFromDirection(float direction) {
        return BlockDirectional.getDirection(MathHelper.floor_double(direction * 4.0f / 360.0f + 0.50));
    }
}
