package mod.steamnsteel.world.structure;

import com.google.common.primitives.Doubles;
import com.google.gson.Gson;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.world.WorldGen;
import mod.steamnsteel.world.structure.remnantruins.RuinLevel;
import mod.steamnsteel.world.structure.remnantruins.RuinRing;
import mod.steamnsteel.world.structure.remnantruins.RuinRings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class RemnantRuinsGenerator extends StructureGenerator
{
    private double MinimumRing = 50D;
    private double RingDistance = 100D;
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

        List<RuinRing> validRings = new LinkedList<RuinRing>();

        final Iterator<RuinRing> ruinRingIterator = ruinRings.iterate(world.getSeed());
        while (true)
        {
            RuinRing ruinRing = ruinRingIterator.next();

            if (maxDistance < ruinRing.minRuinRing)
            {
                break;
            }

            if (((minDistance >= ruinRing.minRuinRing && minDistance <= ruinRing.maxRuinRing)) || ((maxDistance >= ruinRing.minRuinRing && maxDistance <= ruinRing.maxRuinRing)))
            {
                validRings.add(ruinRing);
            }
        }

        if (validRings.size() == 0) {
            return null;
        }

        return null;
    }

    private void LoadRuinLevels()
    {
        try
        {
            Gson gson = new Gson();
            final ResourceLocation resourceLocation = new ResourceLocation(TheMod.MOD_ID + ":schematics/RemnantRuins.json");

            final IResource resource;

            resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);

            ruinLevels = gson.fromJson(new InputStreamReader(resource.getInputStream()), RuinLevel[].class);

            for (RuinLevel ruinLevel : ruinLevels)
            {
                ruinLevel.resolveSchematicNames(WorldGen.schematicLoader);
            }

            ruinRings = new RuinRings(ruinLevels, MinimumRing, RingDistance);
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new MinecraftError();
        }

    }
}
