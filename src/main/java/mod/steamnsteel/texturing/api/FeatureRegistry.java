package mod.steamnsteel.texturing.api;

import com.google.common.collect.Iterables;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.*;

public class FeatureRegistry implements IFeatureRegistry
{
    private int currentBit = 0;
    private int currentLayer = 0;
    Map<Layer, List<IProceduralWallFeature>> featureLayers = new TreeMap<Layer, List<IProceduralWallFeature>>();
    Map<Long, IProceduralWallFeature> features = new Hashtable<Long, IProceduralWallFeature>();
    Map<Long, String> descriptions = new Hashtable<Long, String>();

    HashMap<ChunkCoord, Map<Layer, long[]>> cachedLayerFeatures = new HashMap<ChunkCoord, Map<Layer, long[]>>();

    /**
     * Registers a feature
     *
     * @param feature
     */
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
        feature.setFeatureId(featureId);
        features.put(featureId, feature);
        descriptions.put(featureId, feature.getName());
        currentBit++;
    }

    /**
     * Registers a new layer
     *
     * @param name               The name of the layer
     * @param allowRandomization true, if the layer should be randomized
     * @return The created layer
     */
    public Layer registerLayer(String name, boolean allowRandomization)
    {
        Layer layer = new Layer(currentLayer, name, allowRandomization);
        currentLayer++;
        return layer;
    }

    /**
     * Registers a feature property, that is, a condition that affects the design of a feature.
     *
     * @param description
     * @return
     */
    @Override
    public long registerFeatureProperty(String description)
    {
        long featurePropertyId = 1 << currentBit;
        descriptions.put(featurePropertyId, description);
        currentBit++;
        return featurePropertyId;
    }

    /**
     * Requests the feature in place on a given layer at a given world coordinate.
     *
     * @param worldBlockCoord The coordinate of the feature
     * @param layer           The layer the feature appears on
     * @return The feature present at that location on the layer.
     */
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
            featureMap[index] = featureId;
        }
        return features.get(featureId);
    }

    private Iterable<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord, Layer layer)
    {
        Iterable<FeatureInstance> featureInstances = new LinkedList<FeatureInstance>();

        for (IProceduralWallFeature feature : featureLayers.get(layer))
        {
            final Iterable<FeatureInstance> layerFeatureInstances = feature.getFeatureInstancesFor(chunkCoord);
            if (layerFeatureInstances != null)
            {
                featureInstances = Iterables.concat(featureInstances, layerFeatureInstances);
            }
        }

        return featureInstances;
    }

    /**
     * Calculates the final condition for a given TextureContext. Part of the condition is already calculated at this
     * point, but it can be overriden by features.
     *
     * @param context           The Texture Condition
     * @param currentProperties The preselected conditions, such as (LEFT, RIGHT, TOP, BOTTOM) that are already
     *                          calculated
     * @return the condition.
     */
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

        long incompatibleBits = 0;

        for (Map.Entry<IProceduralWallFeature, Long> featureBit : featureList.entrySet())
        {
            currentProperties |= featureBit.getKey().getFeatureId();
            currentProperties |= featureBit.getValue();
            incompatibleBits |= featureBit.getKey().getIncompatibleProperties();
        }

        currentProperties &= ~incompatibleBits;

        return currentProperties;
    }

    /**
     * Describes a condition according to the names of features.
     *
     * @param features The condition to describe.
     * @return A textual representation of the condition
     */
    public String describeSide(long features)
    {
        StringBuilder descriptionBuffer = new StringBuilder();
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
