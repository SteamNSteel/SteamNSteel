package mod.steamnsteel.texturing;

import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class FeatureRegistry implements IFeatureRegistry
{
    private int featureMask = 0;
    private int currentBit = 0;
    Map<Integer, List<IProceduralWallFeature>> featureLayers = new TreeMap<Integer, List<IProceduralWallFeature>>();
    Map<Long, IProceduralWallFeature> features = new Hashtable<Long, IProceduralWallFeature>();
    Map<Long, String> descriptions = new Hashtable<Long, String>();

    HashMap<ChunkCoord, Map<Integer, long[]>> cachedLayerFeatures = new HashMap<ChunkCoord, Map<Integer, long[]>>();
    //HashMap<ChunkCoord, List<FeatureInstance>> cachedFeatures = new HashMap<ChunkCoord, List<FeatureInstance>>();

    @Override
    public void registerFeature(IProceduralWallFeature feature)
    {
        List<IProceduralWallFeature> featureList = featureLayers.get(feature.getLayer());
        if (featureList == null)
        {
            featureList = new ArrayList<IProceduralWallFeature>();
            featureLayers.put(feature.getLayer(), featureList);
        }
        featureList.add(feature);

        long featureId = 1 << currentBit;
        featureMask &= featureId;
        feature.setFeatureId(featureId);
        features.put(featureId, feature);
        descriptions.put(featureId, feature.getName());
        currentBit++;
    }

    @Override
    public long registerFeatureProperty(String description)
    {
        long featurePropertyId = 1 << currentBit;
        descriptions.put(featurePropertyId, description);
        currentBit++;
        return featurePropertyId;
    }

    public IProceduralWallFeature getFeatureAt(WorldBlockCoord worldBlockCoord, int layer)
    {
        final ChunkCoord chunkCoord = ChunkCoord.of(worldBlockCoord);
        Map<Integer, long[]> layerFeatures = cachedLayerFeatures.get(chunkCoord);
        if (layerFeatures == null)
        {
            layerFeatures = new Hashtable<Integer, long[]>();
            cachedLayerFeatures.put(chunkCoord, layerFeatures);
        }
        long[] featureMap = layerFeatures.get(layer);
        if (featureMap == null)
        {
            featureMap = new long[16 * 256 * 16];
            layerFeatures.put(layer, featureMap);
        }

        ChunkBlockCoord localCoord = ChunkBlockCoord.of(worldBlockCoord);
        final int index = localCoord.getY() | localCoord.getZ() << 8 | localCoord.getX() << 12;
        long featureId = featureMap[index];
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
                //final int featureX = (featureBlockCoord.getX() + offsetAmount) & 15;
                final int featureX = (featureBlockCoord.getX()) & 15;
                if (x >= featureX && x < featureX + feature.getWidth())
                {
                    //final int featureZ = (featureBlockCoord.getZ() + offsetAmount) & 15;
                    final int featureZ = (featureBlockCoord.getZ()) & 15;
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
            featureMap[index] = featureId;
        }
        return features.get(featureId);
    }

    private List<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord, int layer)
    {
        List<FeatureInstance> featureInstances = new LinkedList<FeatureInstance>();
        for (IProceduralWallFeature feature : featureLayers.get(layer))
        {

            final Collection<FeatureInstance> layerFeatureInstances = feature.getFeatureAreasFor(chunkCoord);
            if (layerFeatureInstances != null)
            {
                featureInstances.addAll(layerFeatureInstances);
            }
        }

        return featureInstances;
    }

    public long getFeatureBits(TextureContext context, long currentProperties)
    {
        List<IProceduralWallFeature> featureList = new LinkedList<IProceduralWallFeature>();

        for (int layer : featureLayers.keySet())
        {
            IProceduralWallFeature currentLayerFeature = getFeatureAt(context.getWorldBlockCoord(), layer);
            if (currentLayerFeature == null)
            {
                continue;
            }

            if (!currentLayerFeature.isFeatureValid(context))
            {
                continue;
            }

            boolean add = true;
            List<IProceduralWallFeature> removeList = new LinkedList<IProceduralWallFeature>();
            for (IProceduralWallFeature otherLayerFeature : featureList)
            {
                Behaviour behaviour = currentLayerFeature.getBehaviourAgainst(otherLayerFeature);
                switch (behaviour)
                {
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

            for (IProceduralWallFeature removeFeature : removeList)
            {
                featureList.remove(removeFeature);
            }

            if (add)
            {
                featureList.add(currentLayerFeature);
            }
        }

        long featureBits = currentProperties;

        for (IProceduralWallFeature feature : featureList)
        {
            featureBits = feature.getSubProperties(context, featureBits);
        }

        return featureBits;
    }

    public String describeSide(long features)
    {
        StringBuffer descriptionBuffer = new StringBuffer();
        boolean first = true;
        for (Map.Entry<Long, String> feature : descriptions.entrySet())
        {
            long featureMask = feature.getKey();
            if ((features & featureMask) == featureMask)
            {
                if (!first)
                {
                    descriptionBuffer.append(", ");
                }

                descriptionBuffer.append(feature.getValue());
                first = false;
            }

        }
        return descriptionBuffer.toString();
    }
}
