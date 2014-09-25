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
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.ImmutableTriple;

/**
 * Coordinates in terms of position within the world
 */
public class WorldBlockCoord implements Comparable<WorldBlockCoord>
{
    private final ImmutableTriple<Integer, Integer, Integer> data;

    private WorldBlockCoord(int x, int y, int z) { data = ImmutableTriple.of(x, y, z); }

    public static WorldBlockCoord of(int x, int y, int z) { return new WorldBlockCoord(x, y, z); }

    public int getX() { return data.left; }

    public int getY() { return data.middle; }

    public int getZ() { return data.right; }

    public Block getBlock(IBlockAccess world)
    {
        return world.getBlock(data.left, data.middle, data.right);
    }

    public TileEntity getTileEntity(World world)
    {
        return world.getTileEntity(data.left, data.middle, data.right);
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean blockExists(World world)
    {
        return world.blockExists(data.left, data.middle, data.right);
    }

    public WorldBlockCoord offset(ForgeDirection direction)
    {
        return new WorldBlockCoord(data.left + direction.offsetX, data.middle + direction.offsetY,
                data.right + direction.offsetZ);
    }

    public boolean isAirBlock(World world)
    {
        return world.isAirBlock(data.left, data.middle, data.right);
    }

    public void setBlock(World world, Block block, int metadata, int flags)
    {
        world.setBlock(data.left, data.middle, data.right, block, metadata, flags);
    }

    public void setBlock(World world, Block block)
    {
        world.setBlock(data.left, data.middle, data.right, block);
    }

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

        final WorldBlockCoord that = (WorldBlockCoord) o;
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
    public int compareTo(WorldBlockCoord o)
    {
        if (data.left.equals(o.data.left)) return data.middle.equals(o.data.middle)
                ? data.right.compareTo(o.data.right)
                : data.middle.compareTo(o.data.middle);

        else return data.left.compareTo(o.data.left);
    }

}
