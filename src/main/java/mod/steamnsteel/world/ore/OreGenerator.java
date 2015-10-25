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

import com.google.common.base.Objects;
import mod.steamnsteel.block.SteamNSteelOreBlock;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import java.util.Random;

public class OreGenerator extends WorldGenMinable
{
    private final SteamNSteelOreBlock block;
    private final int clusterCount;
    private final int minHeight;
    private final int maxHeight;

    protected OreGenerator(SteamNSteelOreBlock block) {
        this(block, 0, 0, 0);
    }

    public OreGenerator(SteamNSteelOreBlock block, int clusterCount, int blocksPerCluster, int maxHeight)
    {
        super(block.getDefaultState(), blocksPerCluster);

        this.block = block;
        this.clusterCount = clusterCount;
        minHeight = 0;
        this.maxHeight = maxHeight;
    }

    public static boolean isBlockReplaceable(World world, BlockPos pos, Iterable<IBlockState> targetBlockStates)
    {
        final Block block = world.getBlockState(pos).getBlock();

        for (final IBlockState target : targetBlockStates)
        {
            if (block.isReplaceableOreGen(world, pos, BlockHelper.forBlock(target.getBlock()))) return true;
        }

        return false;
    }

    /*public void generate(World world, Random rng, ChunkCoord coord)
    {
        generate(world, rng, coord.getX() << 4, 0, coord.getZ() << 4);
    }*/

    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        if (block.isGenEnabled())
        {
            int worldX = pos.getX();
            int worldZ = pos.getZ();

            for (int cluster = 0; cluster < clusterCount; cluster++)
            {
                final int x = worldX + rand.nextInt(16);
                final int y = rand.nextInt(maxHeight - minHeight) + minHeight;
                final int z = worldZ + rand.nextInt(16);
                super.generate(world, rand, pos);
            }

            RetroGenHandler.markChunk(ChunkCoord.of(worldX >> 4, worldZ >> 4));
        }

        return true;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("block", block)
                .add("clusterCount", clusterCount)
                .add("minHeight", minHeight)
                .add("maxHeight", maxHeight)
                .toString();
    }
}
