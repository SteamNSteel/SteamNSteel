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

package mod.steamnsteel.world;

import com.google.common.base.Objects;
import mod.steamnsteel.block.SteamNSteelOreBlock;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import java.util.Random;

class OreGenerator extends WorldGenMinable
{
    private final SteamNSteelOreBlock block;
    private final int clusterCount;
    private final int minHeight;
    private final int maxHeight;

    SteamNSteelOreBlock getBlock()
    {
        return block;
    }

    int getClusterCount()
    {
        return clusterCount;
    }

    int getMaxHeight()
    {
        return maxHeight;
    }

    int getMinHeight()
    {
        return minHeight;
    }

    OreGenerator(SteamNSteelOreBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight)
    {
        super(block, blocksPerCluster);

        this.block = block;
        this.clusterCount = clusterCount;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public void generate(World world, Random rng, ChunkCoord coord)
    {
        generate(world, rng, coord.getX() << 4, 0, coord.getZ() << 4);
    }

    @Override
    public boolean generate(World world, Random rand, int worldX, int unused, int worldZ)
    {
        if (block.isGenEnabled())
        {
            for (int cluster = 0; cluster < clusterCount; cluster++)
            {
                final int x = worldX + rand.nextInt(16);
                final int y = rand.nextInt(maxHeight - minHeight) + minHeight;
                final int z = worldZ + rand.nextInt(16);
                super.generate(world, rand, x, y, z);
            }

            RetroGenHandler.markChunk(new ChunkCoord(worldX >>4, worldZ >> 4));
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
