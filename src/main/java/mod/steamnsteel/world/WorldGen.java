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

import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.configuration.Settings;
import mod.steamnsteel.library.ModBlocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import java.util.List;
import java.util.Random;

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.CUSTOM;

public enum WorldGen
{
    INSTANCE;

    private static final List<BasicOreConfiguration> oreGens = Lists.newArrayList();

    public static void init()
    {
        createConfigurations();
        registerToEventBus();
    }

    private static void registerToEventBus()
    {
        MinecraftForge.ORE_GEN_BUS.register(INSTANCE);
    }

    public static void syncConfig()
    {
        createConfigurations();
    }

    private static void createConfigurations()
    {
        //For reference:
        //       ironConfiguration = new OreConfiguration(Blocks.Iron, 20, 8, 0, 64);

        oreGens.clear();

        if (Settings.World.isCopperGenerated())
            oreGens.add(new BasicOreConfiguration(ModBlocks.COPPER_ORE, 20, 6, 0, 64));

        if (Settings.World.isTinGenerated())
            oreGens.add(new BasicOreConfiguration(ModBlocks.TIN_ORE, 20, 3, 0, 64));

        if (Settings.World.isZincGenerated())
            oreGens.add(new BasicOreConfiguration(ModBlocks.ZINC_ORE, 20, 6, 0, 64));
    }

    @SubscribeEvent
    public void OnPostOreGenerated(OreGenEvent.Post event)
    {
        for (final BasicOreConfiguration oreGen : oreGens)
            generateOre(oreGen, event.world, event.rand, event.worldX, event.worldZ);
    }

    public void generateOre(BasicOreConfiguration configuration, World world, Random rand, int worldX, int worldZ)
    {
        if (TerrainGen.generateOre(world, rand, configuration.worldGenerator, worldX, worldZ, CUSTOM))
        {
            for (int clusters = 0; clusters < configuration.clusterCount; ++clusters)
            {
                final int x = worldX + rand.nextInt(16);
                final int y = rand.nextInt(configuration.maxHeight - configuration.minHeight) + configuration.minHeight;
                final int z = worldZ + rand.nextInt(16);
                configuration.worldGenerator.generate(world, rand, x, y, z);
            }
        }
    }

}
