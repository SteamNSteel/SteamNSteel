package mod.steamnsteel.texturing;

import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class FeatureRegistry implements IFeatureRegistry
{
    private int featureMask = 0;
    private int currentBit = 0;
    Map<Integer, List<IProceduralWallFeature>> featureLayers = new TreeMap<Integer, List<IProceduralWallFeature>>();
    Map<Integer, IProceduralWallFeature> features = new Hashtable<Integer, IProceduralWallFeature>();
    Map<Long, String> descriptions = new Hashtable<Long, String>();

    HashMap<ChunkCoord, long[]> cachedNoiseGens = new HashMap<ChunkCoord, long[]>();
    HashMap<ChunkCoord, List<FeatureInstance>> cachedFeatures = new HashMap<ChunkCoord, List<FeatureInstance>>();

    public IProceduralWallFeature getFeatureBy(int featureId)
    {
        return null;
    }

    public int getFeatureMask()
    {
        return featureMask;
    }

    @Override
    public void registerFeature(IProceduralWallFeature feature) {
        List<IProceduralWallFeature> featureList = featureLayers.get(feature.getLayer());
        if (featureList == null) {
            featureList = new ArrayList<IProceduralWallFeature>();
            featureLayers.put(feature.getLayer(), featureList);
        }
        featureList.add(feature);

        long featureId = 1 << currentBit;
        featureMask &= featureId;
        feature.setFeatureId(featureId);
        descriptions.put(featureId, feature.getName());
        currentBit++;
    }

    @Override
    public long registerFeatureProperty(String description) {
        long featurePropertyId = 1 << currentBit;
        descriptions.put(featurePropertyId, description);
        return featurePropertyId;
    }

    public IProceduralWallFeature getFeatureAt(WorldBlockCoord worldBlockCoord, int layer)
    {
        final ChunkCoord chunkCoord = ChunkCoord.of(worldBlockCoord);
        long[] noiseData = cachedNoiseGens.get(chunkCoord);
        if (noiseData == null)
        {
            noiseData = new long[16 * 256 * 16];
            cachedNoiseGens.put(chunkCoord, noiseData);
        }
        ChunkBlockCoord localCoord = ChunkBlockCoord.of(worldBlockCoord);
        final int index = localCoord.getY() | localCoord.getZ() << 8 | localCoord.getX() << 12;
        long featureId = noiseData[index];
        if (featureId == 0)
        {
            int x = localCoord.getX();
            int y = localCoord.getY() & 15;
            int z = localCoord.getZ();

            featureId = -1;
            int offsetAmount = localCoord.getY() >> 4;
            for (FeatureInstance feature : getFeaturesIn(chunkCoord, layer))
            {
                final WorldBlockCoord featureBlockCoord = feature.getBlockCoord();
                final int featureX = (featureBlockCoord.getX() + offsetAmount) & 15;
                if (x >= featureX && x < featureX + feature.getWidth())
                {
                    final int featureZ = (featureBlockCoord.getZ() + offsetAmount) & 15;
                    if (z >= featureZ && z < featureZ + feature.getDepth())
                    {
                        if (y >= featureBlockCoord.getY() && y < featureBlockCoord.getY() + feature.getHeight())
                        {
                            featureId = feature.getFeature().getFeatureId();
                            break;
                        }
                    }
                }
            }
            noiseData[index] = featureId;
        }
        return features.get(featureId);
    }

    private List<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord, int layer)
    {
        List<FeatureInstance> featureInstances = new LinkedList<FeatureInstance>();
        for (IProceduralWallFeature feature : featureLayers.get(layer)) {

            final Collection<FeatureInstance> layerFeatureInstances = feature.getFeatureAreasFor(chunkCoord);
            featureInstances.addAll(layerFeatureInstances);
        }

        return featureInstances;
    }

    public int getFeatureBits(TextureContext context)
    {
        List<IProceduralWallFeature> featureList = new LinkedList<IProceduralWallFeature>();

        for (int layer : featureLayers.keySet()) {
            IProceduralWallFeature currentLayerFeature = getFeatureAt(context.getWorldBlockCoord(), layer);
            if (currentLayerFeature == null) {
                continue;
            }
            boolean add = true;
            List<IProceduralWallFeature> removeList = new LinkedList<IProceduralWallFeature>();
            for (IProceduralWallFeature otherLayerFeature : featureList) {
                Behaviour behaviour = currentLayerFeature.getBehaviourAgainst(otherLayerFeature);
                switch (behaviour) {
                    case CANNOT_EXIST:
                        add = false;
                        break;
                    case REPLACES:
                        removeList.add(otherLayerFeature);
                        break;
                    case COEXIST:
                        add = true;
                        break;
                }
            }

            for (IProceduralWallFeature removeFeature : removeList) {
                featureList.remove(removeFeature);
            }

            if (add) {
                featureList.add(currentLayerFeature);
            }
        }

        return 0;
    }

    public String describeSide(long features) {
        StringBuffer descriptionBuffer = new StringBuffer();
        boolean first = true;
        for (Map.Entry<Long, String> feature : descriptions.entrySet())
        {
            if (!first) {
                descriptionBuffer.append(", ");
            }

            long featureMask = feature.getKey();
            if ((features & featureMask) == featureMask) {
                descriptionBuffer.append(feature.getValue());
            }
            first = false;
        }
        return descriptionBuffer.toString();
    }
}
