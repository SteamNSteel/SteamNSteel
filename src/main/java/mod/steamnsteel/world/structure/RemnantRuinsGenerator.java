package mod.steamnsteel.world.structure;

import com.google.common.primitives.Doubles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.world.WorldGen;
import mod.steamnsteel.world.structure.remnantruins.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class RemnantRuinsGenerator extends StructureGenerator
{
    private double MinimumRing = 50D;
    private double RingDistance = 100D;
    private double DistanceBetweenRuins = 500D;
    private RuinRings ruinRings;
    public RemnantRuinsGenerator()
    {
        LoadRuinLevels();

    }

    private RuinLevel[] ruinLevels;

    @Override
    public StructureChunkGenerator getStructureChunkToGenerate(World world, int chunkX, int chunkZ)
    {
        double[] distancesToCheck = {
                Math.sqrt(Math.pow(chunkX * 16, 2) + Math.pow(chunkZ * 16, 2)),
                Math.sqrt(Math.pow(chunkX * 16 + 15, 2) + Math.pow(chunkZ * 16, 2)),
                Math.sqrt(Math.pow(chunkX * 16 + 15, 2) + Math.pow(chunkZ * 16 + 15, 2)),
                Math.sqrt(Math.pow(chunkX * 16, 2) + Math.pow(chunkZ * 16 + 15, 2)),
        };

        double minDistance = Doubles.min(distancesToCheck);
        double maxDistance = Doubles.max(distancesToCheck);

        final Iterator<RuinRing> ruinRingIterator = ruinRings.iterate(world.getSeed());
        RuinRing ruinRing;
        do
        {
            ruinRing = ruinRingIterator.next();
            //Logger.info("Checking ring size: %f (maxDistance: %f)", ruinRing.ringSize, maxDistance);
            /*if (maxDistance < ruinRing.minRuinRing)
            {
                break;
            }*/

            if (((minDistance >= ruinRing.minRuinRing && minDistance <= ruinRing.maxRuinRing)) || ((maxDistance >= ruinRing.minRuinRing && maxDistance <= ruinRing.maxRuinRing)))
            {
                //Logger.info("Ring is potentially valid.");
                Rectangle chunkRect = new Rectangle(new Point(chunkX << 4, chunkZ << 4), new Dimension(16, 16));
                Ruin ruin = ruinRing.GetIntersectingRuin(chunkRect);

                if (ruin != null)
                {
                    //Logger.info("Found a ruin!");
                    Rectangle intersection = new Rectangle(ruin.location, new Dimension(16, 16));
                    return new StructureChunkGenerator(world, chunkX, chunkZ, ruin.schematic, intersection);
                }
            }
        } while (ruinRing.minRuinRing < maxDistance);

        return null;
    }

    private void LoadRuinLevels()
    {
        try
        {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Dimension.class, new DimensionJsonTypeAdapter())
                    .registerTypeAdapter(RuinType.class, new RuinTypeJsonTypeAdapter())
                    .create();
            final ResourceLocation resourceLocation = new ResourceLocation(TheMod.MOD_ID + ":schematics/RemnantRuins.json");

            final IResource resource;

            resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);

            ruinLevels = gson.fromJson(new InputStreamReader(resource.getInputStream()), RuinLevel[].class);

            for (RuinLevel ruinLevel : ruinLevels)
            {
                ruinLevel.resolveSchematicNames(WorldGen.schematicLoader);
            }

            ruinRings = new RuinRings(ruinLevels, MinimumRing, RingDistance, DistanceBetweenRuins);
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new MinecraftError();
        }

    }
}
