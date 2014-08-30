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

package mod.steamnsteel.utility;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockDirectional;
import java.util.Map;

public enum Orientation
{
    SOUTH(0x0),
    WEST(0x1),
    NORTH(0x2),
    EAST(0x3);

    // Reverse-lookup map for getting an orientation from the metadata code
    private static final ImmutableMap<Integer, Orientation> LOOKUP;

    static
    {
        final Map<Integer, Orientation> lookup = Maps.newHashMapWithExpectedSize(Orientation.values().length);
        for (final Orientation o : Orientation.values())
            lookup.put(o.flag, o);
        LOOKUP = ImmutableMap.copyOf(lookup);
    }

    private final int flag;

    Orientation(int flag)
    {
        this.flag = flag;
    }

    public static Orientation getdecodedOrientation(int encoded)
    {
        return LOOKUP.get(BlockDirectional.getDirection(encoded));
    }

    public int encode()
    {
        return flag;
    }
}
