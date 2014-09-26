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

import mod.steamnsteel.utility.fsm.Context;
import mod.steamnsteel.utility.fsm.State;
import mod.steamnsteel.utility.world.BlockWorldCoordPair;
import net.minecraft.block.material.Material;

@SuppressWarnings({"DeserializableClassInSecureContext", "SerializableClassInSecureContext"})
public enum RockLayerSeekerStates implements State<BlockWorldCoordPair>
{
    INITIAL
            {
                @Override
                public boolean process(Context<BlockWorldCoordPair> context)
                {
                    if (!context.stream().hasNext())
                    {
                        context.state(FAIL);
                        return false;
                    }

                    final BlockWorldCoordPair data = context.stream().next();

                    final Material material = data.getBlock().getMaterial();
                    if (material.equals(Material.sand) || material.equals(Material.ground))
                        context.state(SEEKING_ROCK);
                    return true;
                }
            }, SEEKING_ROCK
        {
            @Override
            public boolean process(Context<BlockWorldCoordPair> context)
            {
                if (!context.stream().hasNext())
                {
                    context.state(FAIL);
                    return false;
                }

                final BlockWorldCoordPair data = context.stream().next();

                if (data.getBlock().getMaterial().equals(Material.rock))
                {
                    context.state(SUCCESS);

                    ((RockLayerSeekerContext)context).setLevel(data.getCoord().getY());

                    return false;
                }
                return true;
            }
        }, FAIL, SUCCESS;

    @Override
    public boolean process(Context<BlockWorldCoordPair> context)
    {
        return false;
    }
}
