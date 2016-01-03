package mod.steamnsteel.texturing.api.traitregistry;

import com.google.common.collect.Iterables;
import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.feature.IMultiTraitFeature;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.*;

public class FeatureRegistry implements IFeatureRegistry
{
    private int currentTraitIdBit = 0;
    private int currentLayer = 0;
    Map<Layer, List<IProceduralWallFeature>> featureLayers = new TreeMap<Layer, List<IProceduralWallFeature>>();
    Map<Long, IProceduralWallFeature> features = new Hashtable<Long, IProceduralWallFeature>();
    Map<Long, String> descriptions = new Hashtable<Long, String>();

    HashMap<ChunkCoord, Map<Layer, long[]>> cachedLayerFeatures = new HashMap<ChunkCoord, Map<Layer, long[]>>();
    Map<EnumFacing, List<Layer>> layersEnabledBySide = new HashMap<>();

    public FeatureRegistry()
    {
        for (final EnumFacing value : EnumFacing.VALUES)
        {
            layersEnabledBySide.put(value, new ArrayList<Layer>());
        }
    }

    /**
     * Registers a feature
     *
     * @param feature The Feature
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

        long featureTraitId = 1 << currentTraitIdBit;
        feature.setFeatureTraitId(featureTraitId);

        int bitsToReserve = 1;
        if (feature instanceof IMultiTraitFeature) {
            int sequentialStates = ((IMultiTraitFeature)feature).getSequentialStateCount();
            bitsToReserve = 0;
            while ( (sequentialStates >>= 1) != 0) //http://stackoverflow.com/a/12349615
            {
                bitsToReserve++;
            }

            features.put(featureTraitId, feature);
            for (int i = 0; i <= bitsToReserve; i++)
            {
                long additionalDescription = 1 << currentTraitIdBit;
                descriptions.put(additionalDescription, feature.getName() + "_" + i);
                currentTraitIdBit++;
            }
        } else {
            features.put(featureTraitId, feature);
            descriptions.put(featureTraitId, feature.getName());
            currentTraitIdBit++;
        }


    }

    /**
     * Registers a new layer.
     * The allowRandomization parameter allows you to specify that y values above 16 will be altered so as to not provide a sterile wall
     *
     * @param name               The name of the layer
     * @param allowRandomization true, if the layer should be randomized
     * @return The created layer
     */
    public Layer registerLayer(String name, boolean allowRandomization, EnumFacing... sides)
    {
        if (sides == null || sides.length == 0) {
            sides = EnumFacing.VALUES;
        }

        Layer layer = new Layer(currentLayer, name, allowRandomization);
        for (final EnumFacing side : sides)
        {
            layersEnabledBySide.get(side).add(layer);
        }

        currentLayer++;


        return layer;
    }

    /**
     * Registers a trait
     *
     * @param description description of the trait
     * @return the Id of the trait
     */
    @Override
    public long registerTrait(String description)
    {
        long traitId = 1 << currentTraitIdBit;
        descriptions.put(traitId, description);
        currentTraitIdBit++;
        return traitId;
    }

    /**
     * Requests the feature in place on a given layer at a given world coordinate.
     *
     * @param worldBlockCoord The coordinate of the feature
     * @param layer           The layer the feature appears on
     * @return The feature present at that location on the layer.
     */
    public IProceduralWallFeature getFeatureAt(BlockPos worldBlockCoord, Layer layer)
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
        long featureTraitId = featureMap[index];
        if (featureTraitId == 0)
        {
            int x = localCoord.getX();
            int y = localCoord.getY() & 15;
            int z = localCoord.getZ();

            featureTraitId = -1;
            int offsetAmount = layer.allowRandomization() ? (localCoord.getY() >> 4) : 0;
            for (FeatureInstance feature : getFeaturesIn(chunkCoord, layer))
            {
                final BlockPos featureBlockCoord = feature.getBlockCoord();
                final int featureX = (featureBlockCoord.getX() + offsetAmount) & 15;
                if (x >= featureX && x < featureX + feature.getWidth())
                {
                    final int featureZ = (featureBlockCoord.getZ() + offsetAmount) & 15;
                    if (z >= featureZ && z < featureZ + feature.getDepth())
                    {
                        if (y >= featureBlockCoord.getY() && y < featureBlockCoord.getY() + feature.getHeight())
                        {
                            featureTraitId = feature.getFeature().getFeatureTraitId();
                            break;
                        }
                    }
                }
            }
            featureMap[index] = featureTraitId;
        }
        return features.get(featureTraitId);
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
     * Calculates the final Trait Set for a given SpriteRequest. Part of the Trait Set is already calculated at this
     * point, but it can be overriden by features.
     *
     * @param request           The Icon Request
     * @param currentTraits The preselected traits, such as (LEFT, RIGHT, TOP, BOTTOM) that have already been
     *                          calculated
     * @return the Trait Set.
     */
    public long getTraitSet(SpriteRequest request, long currentTraits)
    {
        Map<IProceduralWallFeature, Long> featuresToApply = new Hashtable<IProceduralWallFeature, Long>() {};

        for (Layer layer : featureLayers.keySet())
        {
            if (!layersEnabledBySide.get(request.getOrientation()).contains(layer)) { continue; }

            IProceduralWallFeature currentLayerFeature = getFeatureAt(request.getWorldBlockCoord(), layer);
            if (currentLayerFeature == null)
            {
                continue;
            }

            if (!currentLayerFeature.isFeatureValid(request))
            {
                continue;
            }

            boolean addFeature = true;
            List<IProceduralWallFeature> featuresToRemove = new LinkedList<IProceduralWallFeature>();
            for (Map.Entry<IProceduralWallFeature, Long> otherLayerFeature : featuresToApply.entrySet())
            {
                Behaviour behaviour = currentLayerFeature.getBehaviourAgainst(otherLayerFeature.getKey(), otherLayerFeature.getValue());
                switch (behaviour)
                {
                    case CANNOT_EXIST:
                        addFeature = false;
                        break;
                    case REPLACES:
                        featuresToRemove.add(otherLayerFeature.getKey());
                        break;
                    case COEXIST:
                        addFeature = true;
                        break;
                }
            }

            for (IProceduralWallFeature featureToRemove : featuresToRemove)
            {
                featuresToApply.remove(featureToRemove);
            }

            if (addFeature)
            {
                long traits = currentLayerFeature.getTraits(request);
                featuresToApply.put(currentLayerFeature, traits);
            }
        }

        long incompatibleTraits = 0;

        for (Map.Entry<IProceduralWallFeature, Long> feature : featuresToApply.entrySet())
        {
            currentTraits |= feature.getKey().getFeatureTraitId();
            currentTraits |= feature.getValue();
            incompatibleTraits |= feature.getKey().getIncompatibleTraits();
        }

        currentTraits &= ~incompatibleTraits;

        return currentTraits;
    }

    /**
     * Describes a Trait Set according to the names of the traits.
     *
     * @param traitSet The Trait Set to describe.
     * @return A textual representation of the Trait Set
     */
    public String describeTraitSet(long traitSet)
    {
        StringBuilder descriptionBuffer = new StringBuilder();
        boolean first = true;
        for (Map.Entry<Long, String> feature : descriptions.entrySet())
        {
            long featureTraitId = feature.getKey();
            if ((traitSet & featureTraitId) == featureTraitId)
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
