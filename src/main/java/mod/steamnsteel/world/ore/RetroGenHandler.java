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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.configuration.Settings;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import java.util.*;

@SuppressWarnings("NonSerializableFieldInSerializableClass")
public enum RetroGenHandler
{
    INSTANCE;
    private static final String RETROGEN_TAG = TheMod.MOD_ID + ':' + "retroGenMarker";
    private static final Set<ChunkCoord> completedChunks = Sets.newHashSet();
    private final List<OreGenerator> retroGens = Lists.newArrayList();
    private final Deque<ChunkCoord> chunksToRetroGen = new ArrayDeque<ChunkCoord>(64);

    private static boolean isChunkEligibleForRetroGen(ChunkDataEvent.Load event)
    {
        return Settings.World.doRetroOreGen()
                && event.world.provider.getDimensionId() == 0
                && event.getData().getString(RETROGEN_TAG).isEmpty();
    }

    public static void register()
    {
        if (INSTANCE.retroGens.isEmpty())
            return;

        FMLCommonHandler.instance().bus().register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public static void markChunk(ChunkCoord coord)
    {
        completedChunks.add(coord);
    }

    private static boolean isTickEligibleForRetroGen(TickEvent.WorldTickEvent event)
    {
        return event.phase == TickEvent.Phase.END || event.side == Side.SERVER;
    }

    public void register(OreGenerator oreGen)
    {
        retroGens.add(oreGen);
    }

    @SuppressWarnings("UnsecureRandomNumberGeneration")
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (isTickEligibleForRetroGen(event))
        {
            if (!chunksToRetroGen.isEmpty())
            {
                final ChunkCoord coord = chunksToRetroGen.pollFirst();
                Logger.info("Regenerating ore in " + coord + '.');

                final World world = event.world;

                if (world.getChunkProvider().chunkExists(coord.getX(), coord.getZ()))
                {
                    final long seed = world.getSeed();
                    final Random rng = new Random(seed);
                    final long xSeed = rng.nextLong() >> 2 + 1L;
                    final long zSeed = rng.nextLong() >> 2 + 1L;
                    final long chunkSeed = (xSeed * coord.getX() + zSeed * coord.getZ()) * seed;
                    rng.setSeed(chunkSeed);

                    for (final OreGenerator oreGen : retroGens)
                    {
                        oreGen.generate(world, rng, new BlockPos(coord.getX() << 4, 0, coord.getZ() << 4));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event)
    {
        if (isChunkEligibleForRetroGen(event))
        {
            final ChunkCoord coord = ChunkCoord.of(event);
            Logger.info("Queueing retro ore gen for " + coord + '.');
            chunksToRetroGen.addLast(coord);
        }
    }

    @SuppressWarnings("MethodMayBeStatic")
    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event)
    {
        final ChunkCoord coord = ChunkCoord.of(event);
        if (completedChunks.contains(coord))
        {
            event.getData().setString(RETROGEN_TAG, "X");
            completedChunks.remove(coord);
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("retroGens", retroGens)
                .add("chunksToRetroGen", chunksToRetroGen)
                .toString();
    }
}
