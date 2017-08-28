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

import net.minecraft.block.material.Material;

@SuppressWarnings({"DeserializableClassInSecureContext", "SerializableClassInSecureContext"})
public enum NiterVeinGeneratorStates
{
    INITIAL
            {
                @Override
                public boolean process(NiterVeinGeneratorStateMachine context)
                {
                    if (!context.stream().hasNext())
                    {
                        context.state(END);
                        return false;
                    }

                    final MaterialWorldCoordPair data = context.stream().next();

                    final Material material = data.getMaterial();
                    if (material.equals(Material.SAND) || material.equals(Material.GROUND))
                        context.state(SEEKING_ROCK);
                    return true;
                }
            },
    SEEKING_ROCK
            {
                @Override
                public boolean process(NiterVeinGeneratorStateMachine context)
                {
                    if (!context.stream().hasNext())
                    {
                        context.state(END);
                        return false;
                    }

                    final MaterialWorldCoordPair data = context.stream().next();

                    if (data.getMaterial().equals(Material.ROCK))
                        context.state(GROW_VEIN);
                    return true;
                }
            },
    GROW_VEIN
            {
                @Override
                public boolean process(NiterVeinGeneratorStateMachine context)
                {
                    context.growVein();
                    context.state(END);
                    return false;
                }
            },
    END;

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean process(NiterVeinGeneratorStateMachine context)
    {
        return false;
    }
}
