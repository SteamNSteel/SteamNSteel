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

package mod.steamnsteel.world.ore;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.world.ore.niter.NiterVeinGeneratorStateMachine;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;

import static net.minecraftforge.common.BiomeDictionary.Type.WET;

/**
 * Generates Veins of Niter.
 * <p/>
 * Pick a random {x,z} in the chunk and check the type of the biome.
 * <p/>
 * * If we're in a biome that is HOT, DRY and SANDY, then we search vertically through the {x,z} chosen for where sand
 * meets sandstone. * If we're in any other biome, we have a lower chance of generating (currently 50%). We search
 * vertically through the {x,z} for a grass block, then search below that block for where dirt meets stone.
 * <p/>
 * If we've found a valid location, we'll create a vein at that location.
 * <p/>
 * The Vein generation is a slightly modified version of Minecraft's vanilla oregen in that it uses a slightly wider
 * diameter and generates sandstone together with the niter with a 50% chance.
 */
public class NiterOreGenerator extends OreGenerator
{
    public NiterOreGenerator() {
        super(ModBlock.oreNiter);
    }

    private static final ImmutableSet<BiomeDictionary.Type> BLACKLISTED_BIOME_TYPES = ImmutableSet.copyOf(EnumSet.of(WET));

    private static final int CLUSTER_COUNT = 2;
    private static final int MAX_HEIGHT = 70;

    private static boolean isQualifiedBiome(BiomeGenBase biome)
    {
        return Sets.intersection(EnumSet.copyOf(Arrays.asList(BiomeDictionary.getTypesForBiome(biome))),
                BLACKLISTED_BIOME_TYPES).isEmpty();
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        if (!ModBlock.oreNiter.isGenEnabled())
        {
            return false;
        }

        for (int i = 0; i < CLUSTER_COUNT; i++)
        {
            final BlockPos coord = pos.add(rand.nextInt(16), 0, rand.nextInt(16));

            // world getBiome method is safer than chunk version (does not throw exceptions for unloaded chunks)
            // also, block columns are assigned biomes

            final BiomeGenBase biome = world.getBiomeGenForCoords(coord);

            if (isQualifiedBiome(biome))
            {
                final BlockPos startingSearchPos = new BlockPos(
                        coord.getX(),
                        Math.min(world.getHeight(coord).getY(), MAX_HEIGHT),
                        coord.getZ());

                NiterVeinGeneratorStateMachine.growVein(world, rand, startingSearchPos);
            }
        }

        RetroGenHandler.markChunk(ChunkCoord.of(pos));
        return true;
    }
}
