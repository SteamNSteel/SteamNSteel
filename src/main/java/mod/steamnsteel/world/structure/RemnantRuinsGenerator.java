package mod.steamnsteel.world.structure;

import com.google.common.primitives.Doubles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.library.Reference;
import mod.steamnsteel.utility.SteamNSteelException;
import mod.steamnsteel.utility.json.DimensionJsonTypeAdapter;
import mod.steamnsteel.utility.json.RuinTypeJsonTypeAdapter;
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

public class RemnantRuinsGenerator extends StructureGenerator
{
    private double MinimumRing = 50D;
    private double RingDistance = 100D;
    private double DistanceBetweenRuins = 500D;
    final Dimension chunkSize = new Dimension(16, 16);

    private RuinRings ruinRings;
    public RemnantRuinsGenerator()
    {
        LoadRuinLevels();

    }

    private RuinLevel[] ruinLevels;

    @Override
    public Optional<StructureChunkGenerator> getStructureChunkToGenerate(World world, int chunkX, int chunkZ)
    {
        //Check which of the four corners of the chunk are closest/furtherst from spawn.
        double[] distancesToCheck = {
                Math.sqrt(Math.pow(chunkX * 16, 2) + Math.pow(chunkZ * 16, 2)),
                Math.sqrt(Math.pow(chunkX * 16 + 15, 2) + Math.pow(chunkZ * 16, 2)),
                Math.sqrt(Math.pow(chunkX * 16 + 15, 2) + Math.pow(chunkZ * 16 + 15, 2)),
                Math.sqrt(Math.pow(chunkX * 16, 2) + Math.pow(chunkZ * 16 + 15, 2)),
        };

        //
        final double minDistance = Doubles.min(distancesToCheck);
        final double maxDistance = Doubles.max(distancesToCheck);

        final Iterator<RuinRing> ruinRingIterator = ruinRings.iterate(world.getSeed());
        RuinRing ruinRing;
        do
        {
            ruinRing = ruinRingIterator.next();

            //If the chunk's minimumDistance or maximumDistance is within the ring's band

            if (((minDistance >= ruinRing.minRuinRing && minDistance <= ruinRing.maxRuinRing)) || ((maxDistance >= ruinRing.minRuinRing && maxDistance <= ruinRing.maxRuinRing)))
            {

                final Rectangle chunkRect = new Rectangle(new Point(chunkX << 4, chunkZ << 4), chunkSize);
                //Check if this chunk occurs in any ruins
                final Optional<Ruin> ruin = ruinRing.GetIntersectingRuin(chunkRect);

                if (ruin.isPresent())
                {
                    final Ruin r = ruin.get();
                    //If we've found a ruin, return a StructureChunkGenerator for it.
                    final Rectangle ruinLocation = new Rectangle(r.location, chunkSize);
                    return Optional.of(new StructureChunkGenerator(world, chunkX, chunkZ, r, ruinLocation));
                }
            }
        } while (ruinRing.minRuinRing < maxDistance);

        return Optional.empty();
    }

    private void LoadRuinLevels()
    {
        try
        {
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Dimension.class, new DimensionJsonTypeAdapter())
                    .registerTypeAdapter(RuinType.class, new RuinTypeJsonTypeAdapter())
                    .create();
            final ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID + ":schematics/remnant_ruins.json");

            final IResource resource;

            resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);

            ruinLevels = gson.fromJson(new InputStreamReader(resource.getInputStream()), RuinLevel[].class);

            for (final RuinLevel ruinLevel : ruinLevels)
            {
                ruinLevel.resolveSchematicNames(WorldGen.schematicLoader);
            }

            ruinRings = new RuinRings(ruinLevels, MinimumRing, RingDistance, DistanceBetweenRuins);
        } catch (final IOException e)
        {
            throw new SteamNSteelException("Unable to load Schematics for ruin generation", e);
        }

    }
}
