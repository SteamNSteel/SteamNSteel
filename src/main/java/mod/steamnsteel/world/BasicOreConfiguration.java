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

import mod.steamnsteel.block.SteamNSteelBlock;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

class BasicOreConfiguration extends OreConfiguration
{
    public final WorldGenerator worldGenerator;

    BasicOreConfiguration(SteamNSteelBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight)
    {
        super(block, clusterCount, blocksPerCluster, minHeight, maxHeight);
        worldGenerator = new WorldGenMinable(block, blocksPerCluster);
    }
}