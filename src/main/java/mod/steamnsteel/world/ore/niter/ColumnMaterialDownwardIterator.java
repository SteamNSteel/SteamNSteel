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
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.World;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;

@SuppressWarnings("NewExceptionWithoutArguments")
public class ColumnMaterialDownwardIterator implements Iterator<MaterialWorldCoordPair>

{
    private final World world;
    private WorldBlockCoord nextPos;
    private WorldBlockCoord currentCoord = null;

    public ColumnMaterialDownwardIterator(World world, WorldBlockCoord startingPos)
    {
        this.world = world;
        nextPos = startingPos;
    }

    @Override
    public boolean hasNext()
    {
        return nextPos.getY() > 0;
    }

    @Override
    public MaterialWorldCoordPair next()
    {
        if (!hasNext())
            throw new NoSuchElementException();

        final MaterialWorldCoordPair result = MaterialWorldCoordPair.of(nextPos.getBlock(world).getMaterial(), nextPos);
        currentCoord = nextPos;
        nextPos = nextPos.offset(DOWN);
        return result;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    public WorldBlockCoord currentCoord()
    {
        if (currentCoord == null)
            throw new IllegalStateException();
        return currentCoord;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("world", world)
                .add("nextPos", nextPos)
                .add("currentCoord", currentCoord)
                .toString();
    }
}
