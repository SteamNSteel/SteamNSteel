package mod.steamnsteel.texturing;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ProceduralConnectedTexture
{
    protected final int NO_FEATURE = -1;

    protected final int DEFAULT = 0;
    protected final int TOP = 1 << 0;
    protected final int BOTTOM = 1 << 1;
    protected final int LEFT = 1 << 2;
    protected final int RIGHT = 1 << 3;

    public static final int FEATURE_EDGE_TOP = 1 << 4;
    public static final int FEATURE_EDGE_BOTTOM = 1 << 5;
    public static final int FEATURE_EDGE_LEFT = 1 << 6;
    public static final int FEATURE_EDGE_RIGHT = 1 << 7;

    private HashMap<Integer, IProceduralWallFeature> features;

    protected ProceduralConnectedTexture()
    {
        this.features = getFeatures();
    }

    protected abstract HashMap<Integer, IProceduralWallFeature> getFeatures();

    final int MISSING_TEXTURE = Integer.MAX_VALUE;
    private HashMap<Integer, IIcon> icons = new HashMap<Integer, IIcon>();

    public void registerIcons(IIconRegister iconRegister)
    {
        icons.clear();
        cachedNoiseGens.clear();
        cachedFeatures.clear();

        final Map<String, Integer[]> iconMap = getIconMap();

        for (Map.Entry<String, Integer[]> set : iconMap.entrySet())
        {
            for (Integer i : set.getValue())
            {
                addIcon(i, iconRegister.registerIcon(TheMod.MOD_ID + ":" + set.getKey()));
            }
        }
        addIcon(MISSING_TEXTURE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-Missing"));
    }

    protected abstract Map<String, Integer[]> getIconMap();

    protected void addIcon(int fingerprint, IIcon icon)
    {
        icons.put(fingerprint, icon);
    }

    public IIcon getIconForSide(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
    {
        int blockProperties = getTexturePropertiesForSide(blockAccess, worldBlockCoord, side);

        if (!icons.containsKey(blockProperties))
        {
            String blockPropertiesDescription = describeTextureProperties(blockProperties);

            Logger.warning("Unknown texture: %d (%s) - %s @ (%s) - %d", blockProperties, Integer.toBinaryString(blockProperties), blockPropertiesDescription, worldBlockCoord, side);

            blockProperties = MISSING_TEXTURE;
        }
        return icons.get(blockProperties);
    }

    protected abstract String describeTextureProperties(int blockProperties);

    protected abstract int getTexturePropertiesForSide(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side);

    HashMap<ChunkCoord, int[]> cachedNoiseGens = new HashMap<ChunkCoord, int[]>();
    HashMap<ChunkCoord, List<FeatureInstance>> cachedFeatures = new HashMap<ChunkCoord, List<FeatureInstance>>();

    private List<FeatureInstance> getChunkFeatures(ChunkCoord chunkCoord)
    {
        List<FeatureInstance> features = cachedFeatures.get(chunkCoord);
        if (features != null)
        {
            return features;
        }
        features = new ArrayList<FeatureInstance>();

        for (IProceduralWallFeature wallFeature : this.features.values())
        {

            for (FeatureInstance feature : wallFeature.getFeatureAreasFor(chunkCoord))
            {
                final IProceduralWallFeature ruinWallFeature = this.features.get(feature.featureId);
                final WorldBlockCoord blockCoord = feature.getBlockCoord();
                boolean addFeature = true;
                for (FeatureInstance otherFeature : features)
                {

                    final WorldBlockCoord otherFeatureBlockCoord = otherFeature.getBlockCoord();
                    if (blockCoord.getX() + feature.getWidth() >= otherFeatureBlockCoord.getX() &&
                            blockCoord.getX() <= otherFeatureBlockCoord.getX() + otherFeature.getWidth() &&
                            blockCoord.getY() + feature.getHeight() >= otherFeatureBlockCoord.getY() &&
                            blockCoord.getY() <= otherFeatureBlockCoord.getY() + otherFeature.getHeight() &&
                            blockCoord.getZ() + feature.getDepth() >= otherFeatureBlockCoord.getZ() &&
                            blockCoord.getZ() <= otherFeatureBlockCoord.getZ() + otherFeature.getDepth()
                            )
                    {
                        if (!feature.getFeature().canIntersect(otherFeature.getFeature()))
                        {
                            addFeature = false;
                            break;
                        }
                    }
                }
                if (addFeature)
                {
                    features.add(feature);
                }
            }

            features.addAll(wallFeature.getFeatureAreasFor(chunkCoord));
        }

        cachedFeatures.put(chunkCoord, features);

        return features;
    }

    public IProceduralWallFeature getValidFeature(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        IProceduralWallFeature desiredFeature = getFeatureAt(worldBlockCoord);
        if (desiredFeature == null)
        {
            return null;
        }
        if (desiredFeature.isFeatureValid(blockAccess, worldBlockCoord, orientation))
        {
            return desiredFeature;
        }
        return null;
    }


    public IProceduralWallFeature getFeatureAt(WorldBlockCoord worldBlockCoord)
    {
        final ChunkCoord chunkCoord = ChunkCoord.of(worldBlockCoord);
        int[] noiseData = cachedNoiseGens.get(chunkCoord);
        if (noiseData == null)
        {
            noiseData = new int[16 * 256 * 16];
            cachedNoiseGens.put(chunkCoord, noiseData);
        }
        ChunkBlockCoord localCoord = ChunkBlockCoord.of(worldBlockCoord);
        final int index = localCoord.getY() | localCoord.getZ() << 8 | localCoord.getX() << 12;
        int featureId = noiseData[index];
        if (featureId == 0)
        {
            int x = localCoord.getX();
            int y = localCoord.getY() & 15;
            int z = localCoord.getZ();
            //-1 denotes processed, but needs special handling elsewhere then.
            featureId = -1;
            int offsetAmount = localCoord.getY() >> 4;
            for (FeatureInstance feature : getChunkFeatures(chunkCoord))
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
        return this.features.get(featureId);
    }

    public String describeTextureAt(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
    {
        int blockProperties = getTexturePropertiesForSide(blockAccess, worldBlockCoord, side);
        return describeTextureProperties(blockProperties);

    }


    public static class FeatureInstance
    {
        private final IProceduralWallFeature featureId;
        private final WorldBlockCoord blockCoord;
        private final int width;
        private final int height;
        private final int depth;

        public FeatureInstance(IProceduralWallFeature featureId, WorldBlockCoord blockCoord, int width, int height, int depth)
        {

            this.featureId = featureId;
            this.blockCoord = blockCoord;
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        public IProceduralWallFeature getFeature()
        {
            return featureId;
        }

        public WorldBlockCoord getBlockCoord()
        {
            return blockCoord;
        }

        public int getWidth()
        {
            return width;
        }

        public int getHeight()
        {
            return height;
        }

        public int getDepth()
        {
            return depth;
        }
    }
}
