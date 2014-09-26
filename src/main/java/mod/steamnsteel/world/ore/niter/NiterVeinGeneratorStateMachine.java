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
import com.google.common.collect.ImmutableSet;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import mod.steamnsteel.world.ore.OreGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import static net.minecraftforge.common.BiomeDictionary.Type.DRY;
import static net.minecraftforge.common.BiomeDictionary.Type.HOT;

public class NiterVeinGeneratorStateMachine
{
    // Average length of vein in blocks (+/- 50%)
    private static final int NUM_BLOCKS_IN_VEIN = 16;
    private static final int NUM_BLOCKS_IN_VEIN_VARIATION = NUM_BLOCKS_IN_VEIN / 2;

    private static final ImmutableSet<Block> TARGET_BLOCKS =
            ImmutableSet.of(Blocks.stone, Blocks.dirt, Blocks.sand, Blocks.sandstone);

    private static final ImmutableSet<BiomeDictionary.Type> PREFERRED_BIOME_TYPES = ImmutableSet.copyOf(EnumSet.of(DRY, HOT));

    private static final float PREF_BIOME_PADDING_CHANCE_PERCENT = 0.25f;
    private static final float OTHER_BIOME_PADDING_CHANCE_PERCENT = 0.5f;

    private static final ImmutableSet<ForgeDirection> DIRECTIONS =
            ImmutableSet.copyOf(EnumSet.of(ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.EAST,
                    ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH));

    private final ColumnMaterialDownwardIterator iterator;
    private final World world;
    private final Random rng;
    private NiterVeinGeneratorStates state = NiterVeinGeneratorStates.INITIAL;

    private NiterVeinGeneratorStateMachine(World world, Random rng, WorldBlockCoord startingPos)
    {
        this.world = world;
        this.rng = rng;
        iterator = new ColumnMaterialDownwardIterator(world, startingPos);
    }

    public static void growVein(World world, Random rng, WorldBlockCoord coord)
    {
        // invoke a state machine to find the rock layer and spawn the vein if possible
        final NiterVeinGeneratorStateMachine context = new NiterVeinGeneratorStateMachine(world, rng, coord);
        //noinspection StatementWithEmptyBody
        while (context.state().process(context)) ;
    }

    private static boolean isPreferredBiome(BiomeGenBase biome)
    {
        return EnumSet.copyOf(Arrays.asList(BiomeDictionary.getTypesForBiome(biome))).containsAll(PREFERRED_BIOME_TYPES);
    }

    public void growVein()
    {
        final int veinSize = NUM_BLOCKS_IN_VEIN + rng.nextInt(NUM_BLOCKS_IN_VEIN) - NUM_BLOCKS_IN_VEIN_VARIATION;

        WorldBlockCoord target = iterator.currentCoord();
        target = WorldBlockCoord.of(target.getX(), Math.max(target.getY() + 1 - rng.nextInt(5), 0), target.getZ());
        for (int blockCount = 0; blockCount < veinSize; blockCount++)
        {
            if (OreGenerator.isBlockReplaceable(world, target, TARGET_BLOCKS))
                placeNiterOre(target);

            final ForgeDirection offsetToNext = ForgeDirection.getOrientation(rng.nextInt(6));
            target = target.offset(offsetToNext);

            // Has vein strayed into an unloaded chunk? If so, STOP!
            if (!target.blockExists(world)) return;
        }
    }

    private void placeNiterOre(WorldBlockCoord target)
    {
        if (OreGenerator.isBlockReplaceable(world, target, TARGET_BLOCKS) && !isBlockLiquidNeighbor(target))
            target.setBlock(world, ModBlock.oreNiter, 0, 2);

        float paddingChancePercent = isPreferredBiome(target.getBiome(world)) ?
                PREF_BIOME_PADDING_CHANCE_PERCENT :
                OTHER_BIOME_PADDING_CHANCE_PERCENT;

        // encrust ore in sandstone (each additional block of crust is rarer)
        final Set<ForgeDirection> directions = EnumSet.copyOf(DIRECTIONS);
        for (int i = 0; i < DIRECTIONS.size(); i++)
        {
            // get random neighbor
            final ForgeDirection offset = ForgeDirection.getOrientation(rng.nextInt(directions.size()));
            directions.remove(offset);
            if (rng.nextFloat() < paddingChancePercent)
            {
                final WorldBlockCoord crustTarget = target.offset(offset);
                if (crustTarget.blockExists(world) && OreGenerator.isBlockReplaceable(world, crustTarget, TARGET_BLOCKS))
                    crustTarget.setBlock(world, Blocks.sandstone, 0, 2);
            }
            paddingChancePercent *= 0.75f;
        }
    }

    private boolean isBlockLiquidNeighbor(WorldBlockCoord coord)
    {
        for (final ForgeDirection offset : DIRECTIONS)
        {
            final WorldBlockCoord target = coord.offset(offset);
            if (target.blockExists(world))
                if (target.getBlock(world).getMaterial().isLiquid())
                    return true;
        }
        return false;
    }

    public ColumnMaterialDownwardIterator stream()
    {
        return iterator;
    }

    NiterVeinGeneratorStates state()
    {
        return state;
    }

    public void state(NiterVeinGeneratorStates state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("iterator", iterator)
                .add("state", state)
                .add("world", world)
                .add("rng", rng)
                .toString();
    }
}
