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

package mod.steamnsteel.utility.position;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.tuple.ImmutableTriple;

/**
 * Coordinates in terms of position within a chunk
 */
public class ChunkBlockCoord implements Comparable<ChunkBlockCoord>
{
    private final ImmutableTriple<Byte, Integer, Byte> data;

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private ChunkBlockCoord(int x, int y, int z)
    {
        Preconditions.checkArgument(x >= 0 && x < 16);
        Preconditions.checkArgument(z >= 0 && z < 16);
        data = ImmutableTriple.of((byte) x, y, (byte) z);
    }

    public static ChunkBlockCoord of(int x, int y, int z) { return new ChunkBlockCoord(x, y, z); }

    public static ChunkBlockCoord of(BlockPos coord)
    {
        return new ChunkBlockCoord(coord.getX() & 15, coord.getY(), coord.getZ() & 15);
    }

    public int getX() { return data.left; }

    public int getY() { return data.middle; }

    public int getZ() { return data.right; }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(data.left, data.middle, data.right);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ChunkBlockCoord that = (ChunkBlockCoord) o;
        return data.left.equals(that.data.left)
                && data.middle.equals(that.data.middle)
                && data.right.equals(that.data.right);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("X", data.left)
                .add("Y", data.middle)
                .add("Z", data.right)
                .toString();
    }

    @Override
    public int compareTo(ChunkBlockCoord o)
    {
        if (data.left.equals(o.data.left)) return data.middle.equals(o.data.middle)
                ? data.right.compareTo(o.data.right)
                : data.middle.compareTo(o.data.middle);

        else return data.left.compareTo(o.data.left);
    }
}
