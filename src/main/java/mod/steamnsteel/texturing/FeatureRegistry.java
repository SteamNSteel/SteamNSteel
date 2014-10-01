package mod.steamnsteel.texturing;

import com.google.common.collect.Iterables;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class FeatureRegistry implements IFeatureRegistry
{
    private int featureMask = 0;
    private int currentBit = 0;
    private int currentLayer = 0;
    Map<Layer, List<IProceduralWallFeature>> featureLayers = new TreeMap<Layer, List<IProceduralWallFeature>>();
    Map<Long, IProceduralWallFeature> features = new Hashtable<Long, IProceduralWallFeature>();
    Map<Long, String> descriptions = new Hashtable<Long, String>();

    HashMap<ChunkCoord, Map<Layer, long[]>> cachedLayerFeatures = new HashMap<ChunkCoord, Map<Layer, long[]>>();

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

    public Layer registerLayer(String name, boolean allowRandomization) {
        Layer layer = new Layer(currentLayer, name, allowRandomization);
        currentLayer++;
        return layer;
    }

    @Override
    public long registerFeatureProperty(String description)
    {
        long featurePropertyId = 1 << currentBit;
        descriptions.put(featurePropertyId, description);
        currentBit++;
        return featurePropertyId;
    }

    public IProceduralWallFeature getFeatureAt(WorldBlockCoord worldBlockCoord, Layer layer)
    {
        final ChunkCoord chunkCoord = ChunkCoord.of(worldBlockCoord);
        Map<Layer, long[]> layerFeatures = cachedLayerFeatures.get(chunkCoord);
        if (layerFeatures == null)
        {
            layerFeatures = new Hashtable<Layer, long[]>();
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
            int offsetAmount = layer.allowRandomization() ? (localCoord.getY() >> 4) : 0;
            for (FeatureInstance feature : getFeaturesIn(chunkCoord, layer))
            {
                final WorldBlockCoord featureBlockCoord = feature.getBlockCoord();
                final int featureX = (featureBlockCoord.getX() + offsetAmount);//& 15;
                //final int featureX = (featureBlockCoord.getX());
                if (x >= featureX && x < featureX + feature.getWidth())
                {
                    final int featureZ = (featureBlockCoord.getZ() + offsetAmount);// & 15;
                    //final int featureZ = (featureBlockCoord.getZ());
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

    private Iterable<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord, Layer layer)
    {
        Iterable<FeatureInstance> featureInstances = new LinkedList<FeatureInstance>();

        for (IProceduralWallFeature feature : featureLayers.get(layer))
        {
            final Iterable<FeatureInstance> layerFeatureInstances = feature.getFeatureAreasFor(chunkCoord);
            if (layerFeatureInstances != null)
            {
                featureInstances = Iterables.concat(featureInstances, layerFeatureInstances);
            }
        }

        return featureInstances;
    }

    public long getFeatureBits(TextureContext context, long currentProperties)
    {
        Map<IProceduralWallFeature, Long> featureList = new Hashtable<IProceduralWallFeature, Long>() {};

        for (Layer layer : featureLayers.keySet())
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
            for (Map.Entry<IProceduralWallFeature, Long> otherLayerFeature : featureList.entrySet())
            {
                Behaviour behaviour = currentLayerFeature.getBehaviourAgainst(otherLayerFeature.getKey(), otherLayerFeature.getValue());
                switch (behaviour)
                {
                    case CANNOT_EXIST:
                        add = false;
                        break;
                    case REPLACES:
                        removeList.add(otherLayerFeature.getKey());
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
                long featureBits = currentLayerFeature.getSubProperties(context);
                featureList.put(currentLayerFeature, featureBits);
            }
        }

        //long featureBits = currentProperties;

        for (Map.Entry<IProceduralWallFeature, Long> featureBit : featureList.entrySet())
        {
            currentProperties |= featureBit.getKey().getFeatureId();
            currentProperties |= featureBit.getValue();
            currentProperties &= ~featureBit.getKey().getIncompatibleProperties();
        }

        return currentProperties;
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
