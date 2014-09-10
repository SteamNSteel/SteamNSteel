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
import mod.steamnsteel.library.ModBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import java.util.List;

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.CUSTOM;

public enum WorldGen
{
    INSTANCE;

    private static final List<OreGenerator> oreGens = Lists.newArrayList();

    public static void init()
    {
        createOreGenerators();
        register();
    }

    private static void register()
    {
        MinecraftForge.ORE_GEN_BUS.register(INSTANCE);
    }

    private static void createOreGenerators()
    {
        //For reference:
        //       ironConfiguration = new OreConfiguration(Blocks.Iron, 20, 8, 0, 64);

        oreGens.add(new OreGenerator(ModBlock.oreCopper, 20, 6, 0, 64));
        oreGens.add(new OreGenerator(ModBlock.oreTin, 20, 3, 0, 64));
        oreGens.add(new OreGenerator(ModBlock.oreZinc, 20, 6, 0, 64));
	    oreGens.add(new SulfurOreGenerator(ModBlock.oreSulfur, 10, 12, 0, 64));
    }

    @SuppressWarnings("MethodMayBeStatic")
    @SubscribeEvent
    public void OnPostOreGenerated(OreGenEvent.Post event)
    {
        for (final OreGenerator oreGen : oreGens)
            if (TerrainGen.generateOre(event.world, event.rand, oreGen, event.worldX, event.worldZ, CUSTOM))
                oreGen.generate(event.world, event.rand, event.worldX, 0, event.worldZ);
    }
}
