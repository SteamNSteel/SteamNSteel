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
import mod.steamnsteel.utility.SteamNSteelException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.world.ChunkEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class ChunkCoord implements Comparable<ChunkCoord>
{
    private final ImmutablePair<Integer, Integer> data;

    private ChunkCoord(int x, int z) { data = ImmutablePair.of(x, z); }

    private ChunkCoord(ChunkEvent event)
    {
        this(event.getChunk().xPosition, event.getChunk().zPosition);
    }

    public static ChunkCoord of(int x, int z) { return new ChunkCoord(x, z); }

    public static ChunkCoord of(BlockPos coord)
    {
        return new ChunkCoord(coord.getX() >> 4, coord.getZ() >> 4);
    }

    public static ChunkCoord of(ChunkEvent event) { return new ChunkCoord(event); }

    public boolean exists(World world) {
        final IChunkProvider chunkProvider = world.getChunkProvider();
        if (!(chunkProvider instanceof ChunkProviderServer)) {
            throw new SteamNSteelException("attempt to use exists on the client chunk provider");
        }
        return ((ChunkProviderServer)chunkProvider).chunkExists(data.getLeft(), data.getRight());
    }

    public boolean containsWorldCoord(BlockPos coord)
    {
        return equals(of(coord));
    }

    public int getX() { return data.left; }

    public int getZ() { return data.right; }

    public BlockPos localToWorldCoords(ChunkBlockCoord coord)
    {
        return new BlockPos((data.left << 4) + coord.getX(), coord.getY(), (data.right << 4) + coord.getZ());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(data.left, data.right);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ChunkCoord that = (ChunkCoord) o;
        return data.left.equals(that.data.left) && data.right.equals(that.data.right);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("X", data.left)
                .add("Z", data.right)
                .toString();
    }

    @Override
    public int compareTo(ChunkCoord o)
    {
        return data.left.equals(o.data.left)
                ? data.right.compareTo(o.data.right)
                : data.left.compareTo(o.data.left);
    }
}
