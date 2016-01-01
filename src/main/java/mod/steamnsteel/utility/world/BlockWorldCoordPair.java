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

package mod.steamnsteel.utility.world;

import com.google.common.base.Objects;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

import static com.google.common.base.Preconditions.*;

public class BlockWorldCoordPair
{
    private final Block block;
    private final BlockPos coord;

    public BlockWorldCoordPair(Block block, BlockPos coord)
    {
        this.block = checkNotNull(block);
        this.coord = checkNotNull(coord);
    }

    public static BlockWorldCoordPair of(Block block, BlockPos coord)
    {
        return new BlockWorldCoordPair(block, coord);
    }

    public Block getBlock() { return block; }

    public BlockPos getCoord() { return coord; }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(block, coord);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof BlockWorldCoordPair)) return false;

        final BlockWorldCoordPair that = (BlockWorldCoordPair) o;
        return block.equals(that.block) && coord.equals(that.coord);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("block", block)
                .add("coord", coord)
                .toString();
    }
}
