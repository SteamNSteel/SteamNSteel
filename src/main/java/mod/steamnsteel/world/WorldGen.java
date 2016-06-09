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
import mod.steamnsteel.configuration.Settings;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.world.ore.NiterOreGenerator;
import mod.steamnsteel.world.ore.OreGenerator;
import mod.steamnsteel.world.ore.RetroGenHandler;
import mod.steamnsteel.world.ore.SulfurOreGenerator;
import mod.steamnsteel.world.structure.RemnantRuinsGenerator;
import mod.steamnsteel.world.structure.StructureChunkGenerator;
import mod.steamnsteel.world.structure.StructureGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import java.util.Optional;

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.CUSTOM;

public enum WorldGen
{
    INSTANCE;

    private static final List<OreGenerator> oreGens = Lists.newArrayList();
    private static final List<StructureGenerator> structureGens = Lists.newArrayList();

    public static SchematicLoader schematicLoader = new SchematicLoader();

    public static void init()
    {
        createOreGenerators();
        register();
        RetroGenHandler.register();
    }

    private static void register() {
        MinecraftForge.ORE_GEN_BUS.register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    private static void createOreGenerators()
    {
        //For reference:
        //       ironConfiguration = new OreConfiguration(Blocks.Iron, 20, 8, 0, 64);
        final OreGenerator copperGen = new OreGenerator(ModBlock.oreCopper, 20, 6, 64);
        oreGens.add(copperGen);

        final NiterOreGenerator niterGen = new NiterOreGenerator();
        oreGens.add(niterGen);

        final SulfurOreGenerator sulfurGen = new SulfurOreGenerator();
        oreGens.add(sulfurGen);

        final OreGenerator tinGen = new OreGenerator(ModBlock.oreTin, 20, 3, 64);
        oreGens.add(tinGen);

        final OreGenerator zincGen = new OreGenerator(ModBlock.oreZinc, 20, 6, 64);
        oreGens.add(zincGen);

        if (Settings.World.doRetroOreGen())
        {
            RetroGenHandler.INSTANCE.register(copperGen);
            RetroGenHandler.INSTANCE.register(niterGen);
            RetroGenHandler.INSTANCE.register(sulfurGen);
            RetroGenHandler.INSTANCE.register(tinGen);
            RetroGenHandler.INSTANCE.register(zincGen);
        }
    }

    @SubscribeEvent
    public void OnWorldStarted(Load worldLoadEvent) {
        //required as different Worlds (from consecutive loads) may have different IDs
        schematicLoader = new SchematicLoader();
        schematicLoader.addSetBlockEventListener(event -> {
            final IBlockState schematicBlock = event.getBlockState();
            if (schematicBlock.getBlock() == Blocks.VINE) {
                final IBlockState worldBlock = event.world.getBlockState(event.worldCoord);
                if (!worldBlock.getBlock().isAir(worldBlock, event.world, event.schematicCoord)) {
                    event.cancelSetBlock();
                }
            }
        });
        structureGens.clear();
        final RemnantRuinsGenerator ruinsGenerator = new RemnantRuinsGenerator();
        structureGens.add(ruinsGenerator);
    }



    @SubscribeEvent
    @SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
    public void OnPostOreGenerated(OreGenEvent.Post event)
    {
        for (final OreGenerator oreGen : oreGens)
            if (TerrainGen.generateOre(event.getWorld(), event.getRand(), oreGen, event.getPos(), CUSTOM))
                oreGen.generate(event.getWorld(), event.getRand(), event.getPos());
    }

    @SubscribeEvent
    @SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
    public void OnPostPopulateChunkEvent(PopulateChunkEvent.Post event) {
        if (event.isHasVillageGenerated()) {
            return;
        }
        for (final StructureGenerator structureGen : structureGens) {
            final Optional<StructureChunkGenerator> structureToGenerate = structureGen.getStructureChunkToGenerate(event.getWorld(), event.getChunkX(), event.getChunkZ());
            structureToGenerate.ifPresent(StructureChunkGenerator::generate);
        }
    }
}
