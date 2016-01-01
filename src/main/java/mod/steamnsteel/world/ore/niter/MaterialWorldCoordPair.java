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

package mod.steamnsteel.world.ore.niter;

import com.google.common.base.Objects;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;

import static com.google.common.base.Preconditions.*;

public class MaterialWorldCoordPair
{
    private final Material material;
    private final BlockPos coord;

    private MaterialWorldCoordPair(Material material, BlockPos coord)
    {
        this.material = checkNotNull(material);
        this.coord = checkNotNull(coord);
    }

    public static MaterialWorldCoordPair of(Material material, BlockPos coord)
    {
        return new MaterialWorldCoordPair(material, coord);
    }

    public Material getMaterial() { return material; }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(material, coord);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof MaterialWorldCoordPair)) return false;

        final MaterialWorldCoordPair that = (MaterialWorldCoordPair) o;
        return material.equals(that.material) && coord.equals(that.coord);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("material", material)
                .add("coord", coord)
                .toString();
    }
}
