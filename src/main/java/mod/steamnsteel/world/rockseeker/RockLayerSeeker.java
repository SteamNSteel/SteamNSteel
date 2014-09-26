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

import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.World;

public enum RockLayerSeeker
{
    ;

    @SuppressWarnings({"unchecked", "StatementWithEmptyBody", "NewExceptionWithoutArguments"})
    public static int findRockLayer(World world, WorldBlockCoord coord)
    {
        final RockLayerSeekerContext context = new RockLayerSeekerContext(world, coord);
        while (context.state().process(context)) ;
        if (context.state() != RockLayerSeekerStates.SUCCESS)
            throw new IndexOutOfBoundsException();
        return context.getLevel();
    }
}
