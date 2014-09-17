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
import com.google.common.collect.Sets;
import mod.steamnsteel.block.SteamNSteelOreBlock;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import java.util.Random;
import java.util.Set;

/**
 * Generates Veins of Niter.
 * <p/>
 * Pick a random {x,z} in the chunk and check the type of the biome.
 * <p/>
 * * If we're in a biome that is HOT, DRY and SANDY, then we search vertically through the {x,z} chosen for where sand
 * meets sandstone.
 * * If we're in any other biome, we have a lower chance of generating (currently 50%). We search vertically through the
 * {x,z} for a grass block, then search below that block for where dirt meets stone.
 * <p/>
 * If we've found a valid location, we'll create a vein at that location.
 * <p/>
 * The Vein generation is a slightly modified version of Minecraft's vanilla oregen in that it uses a slightly wider
 * diameter and generates sandstone together with the niter with a 50% chance.
 */
public class NiterOreGenerator extends OreGenerator
{
    private static final float OTHER_BIOME_CHANCE_PERCENT = 0.5f;
    private static final float SANDSTONE_PADDING_CHANCE_PERCENT = 0.5f;

    private final Set<BiomeGenBase> preferredBiomes;
    private final Set<BiomeGenBase> blacklistedBiomes;
    private final int blocksPerCluster;
    //list of blocks that can be replaced by the ore.
    private final Block[] replaceableBlocks;
    private final Block paddingBlock;

    private final double sandstoneDistance;

    NiterOreGenerator(SteamNSteelOreBlock block, int clusterCount, int blocksPerCluster, int minHeight, int maxHeight)
    {
        super(block, clusterCount, blocksPerCluster, minHeight, maxHeight);
        this.blocksPerCluster = blocksPerCluster;
        //Find the intersection of DRY, HOT and SANDY biome types
        final Set<BiomeGenBase> dryBiomes = Sets.newHashSet(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.DRY));
        final Set<BiomeGenBase> hotBiomes = Sets.newHashSet(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.HOT));
        //Set<BiomeGenBase> sandyBiomes = Sets.newHashSet(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.SANDY));
        preferredBiomes = Sets.intersection(dryBiomes, hotBiomes);
        blacklistedBiomes = Sets.newHashSet(BiomeDictionary.getBiomesForType(BiomeDictionary.Type.WET));

        replaceableBlocks = new Block[]{
                Blocks.sand,
                Blocks.sandstone,
                Blocks.dirt,
                Blocks.stone
        };

        paddingBlock = Blocks.sandstone;
        sandstoneDistance = 1.4D;
    }

    @Override
    public boolean generate(World world, Random rand, int worldX, int unused, int worldZ)
    {
        //noinspection OverlyBroadCatchBlock
        try
        {
            if (!block.isGenEnabled())
            {
                return false;
            }

            //long startTime = System.currentTimeMillis();

            for (int clusterNum = 0; clusterNum < clusterCount; ++clusterNum)
            {
                final int x = rand.nextInt(16);
                final int z = rand.nextInt(16);

                final int worldBlockX = x + worldX;
                final int worldBlockZ = z + worldZ;

                final Chunk chunk = world.getChunkFromBlockCoords(worldX, worldZ);
                final BiomeGenBase biomeGenBase = chunk.getBiomeGenForWorldCoords(x, z, world.getWorldChunkManager());

                final int blockX = x & 15;
                final int blockZ = z & 15;
                //find where to start the search from
                int height = chunk.getHeightValue(blockX, blockZ);
                if (height > maxHeight)
                {
                    height = maxHeight;
                }

                //Don't create ores in the blacklisted biomes.
                if (blacklistedBiomes.contains(biomeGenBase))
                {
                    continue;
                }
                //If we're in a desert like biome
                int yHeight = -1;
                if (preferredBiomes.contains(biomeGenBase))
                {
                    //Use the desert method for finding the generation location
                    yHeight = findDesertSeam(chunk, worldBlockX, height, worldBlockZ);
                } else if (rand.nextFloat() < OTHER_BIOME_CHANCE_PERCENT)
                {
                    //Otherwise use the alternate method.
                    yHeight = findOtherBiomeSeam(chunk, worldBlockX, height, worldBlockZ);
                }

                if (yHeight != -1)
                {
                    //Add a little bit of padding to stop it from generating right on the edges.
                    //In general, sandstone tends to only be about 3 blocks thick.
                    yHeight -= rand.nextInt(2);

                    generateOreVein(world, chunk, rand, x, yHeight, z);
                }
            }

            //long stopTime = System.currentTimeMillis();
            //long runTime = stopTime - startTime;
            //Logger.info("Niter Run time: " + runTime);

            return true;
        } catch (final Exception ignored)
        {
            Logger.warning("Error generating Niter in chunk at x=%d z=%d.", worldX, worldZ);
        }
        return false;
    }

    @SuppressWarnings("ObjectEquality")
    private int findDesertSeam(Chunk chunk, int x, int y, int z)
    {
        boolean foundSand = false;
        int genHeight = -1;
        for (int ySearch = y; ySearch >= minHeight; --ySearch)
        {
            final Block test = chunk.getBlock(x & 15, ySearch, z & 15);
            if (test == Blocks.sand && !foundSand)
            {
                foundSand = true;
            } else if (foundSand && test == Blocks.sandstone)
            {
                genHeight = ySearch;
                break;
            }
        }
        return genHeight;
    }

    @SuppressWarnings("ObjectEquality")
    private int findOtherBiomeSeam(Chunk chunk, int x, int y, int z)
    {
        boolean foundGrass = false;
        boolean foundDirt = false;
        int genHeight = -1;
        for (int ySearch = y; ySearch >= minHeight; --ySearch)
        {
            final Block test = chunk.getBlock(x & 15, ySearch, z & 15);
            if (test == Blocks.grass && !foundGrass)
            {
                foundGrass = true;
            } else if (foundGrass && !foundDirt && test == Blocks.dirt)
            {
                foundDirt = true;
            } else if (foundGrass && foundDirt && test != Blocks.dirt)
            {
                //found the edge of the dirt/stone line.
                genHeight = ySearch;
                break;
            }
        }
        return genHeight;
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private void generateOreVein(World world, Chunk chunk, Random random, int worldX, int worldY, int worldZ)
    {
        final float direction = random.nextFloat() * (float) Math.PI;
        final double maxX = worldX + 8 + MathHelper.sin(direction) * blocksPerCluster / 8.0F;
        final double minX = worldX + 8 - MathHelper.sin(direction) * blocksPerCluster / 8.0F;
        final double maxZ = worldZ + 8 + MathHelper.cos(direction) * blocksPerCluster / 8.0F;
        final double minZ = worldZ + 8 - MathHelper.cos(direction) * blocksPerCluster / 8.0F;
        //neither of these are really the min or max.
        final double yBoundsA = worldY + random.nextInt(3) - 2;
        final double yBoundsB = worldY + random.nextInt(3) - 2;

        for (int blockNum = 0; blockNum <= blocksPerCluster; ++blockNum)
        {
            final double clusterCentreX = maxX + (minX - maxX) * blockNum / blocksPerCluster;
            final double clusterCentreY = yBoundsA + (yBoundsB - yBoundsA) * blockNum / blocksPerCluster;
            final double clusterCentreZ = maxZ + (minZ - maxZ) * blockNum / blocksPerCluster;
            final double d9 = random.nextDouble() * blocksPerCluster / 16.0D;
            final double clusterSize = (MathHelper.sin(blockNum * (float) Math.PI / blocksPerCluster) + 1.0F) * d9 + 1.0D;
            final int clusterStartX = MathHelper.floor_double(clusterCentreX - clusterSize / 2.0D);
            final int clusterStartY = MathHelper.floor_double(clusterCentreY - clusterSize / 2.0D);
            final int clusterStartZ = MathHelper.floor_double(clusterCentreZ - clusterSize / 2.0D);
            final int clusterEndX = MathHelper.floor_double(clusterCentreX + clusterSize / 2.0D);
            final int clusterEndY = MathHelper.floor_double(clusterCentreY + clusterSize / 2.0D);
            final int clusterEndZ = MathHelper.floor_double(clusterCentreZ + clusterSize / 2.0D);

            for (int x = clusterStartX; x <= clusterEndX; ++x)
            {
                final double d12 = (x + 0.5D - clusterCentreX) / (clusterSize / 2.0D);

                if (d12 * d12 < sandstoneDistance)
                {
                    for (int y = clusterStartY; y <= clusterEndY; ++y)
                    {
                        final double d13 = (y + 0.5D - clusterCentreY) / (clusterSize / 2.0D);

                        if (d12 * d12 + d13 * d13 < sandstoneDistance)
                        {
                            for (int z = clusterStartZ; z <= clusterEndZ; ++z)
                            {
                                final Block chunkBlock = chunk.getBlock(x & 15, y, z & 15);

                                boolean replaceAllowed = false;
                                for (final Block replaceableBlock : replaceableBlocks)
                                {
                                    replaceAllowed |= !replaceAllowed && chunkBlock.isReplaceableOreGen(world, x, y, z, replaceableBlock);
                                }

                                final double d14 = (z + 0.5D - clusterCentreZ) / (clusterSize / 2.0D);
                                final double distance = d12 * d12 + d13 * d13 + d14 * d14;
                                if (distance < sandstoneDistance && replaceAllowed)
                                {
                                    final Block blockToPlace = random.nextFloat() < SANDSTONE_PADDING_CHANCE_PERCENT
                                            ? paddingBlock : block;
                                    world.setBlock(x, y, z, blockToPlace, 0, 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("preferredBiomes", preferredBiomes)
                .add("blacklistedBiomes", blacklistedBiomes)
                .add("blocksPerCluster", blocksPerCluster)
                .add("replaceableBlocks", replaceableBlocks)
                .add("paddingBlock", paddingBlock)
                .add("sandstoneDistance", sandstoneDistance)
                .toString();
    }
}
