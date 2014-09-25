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

package mod.steamnsteel.world.rockseeker;

import com.google.common.base.Objects;
import mod.steamnsteel.utility.fsm.Context;
import mod.steamnsteel.utility.fsm.State;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import mod.steamnsteel.utility.world.BlockWorldCoordPair;
import mod.steamnsteel.utility.world.ColumnDownwardBlockIterator;
import net.minecraft.world.World;
import java.util.Iterator;

public class RockLayerSeekerContext implements Context<BlockWorldCoordPair>
{
    private final ColumnDownwardBlockIterator iterator;
    private State state = RockLayerSeekerStates.INITIAL;
    private int level;

    public RockLayerSeekerContext(World world, WorldBlockCoord startingPos)
    {
        iterator = new ColumnDownwardBlockIterator(world, startingPos);
    }

    @Override
    public Iterator<BlockWorldCoordPair> stream()
    {
        return iterator;
    }

    @Override
    public State state()
    {
        return state;
    }

    @Override
    public void state(State state)
    {
        this.state = state;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("iterator", iterator)
                .add("state", state)
                .add("level", level)
                .toString();
    }
}
