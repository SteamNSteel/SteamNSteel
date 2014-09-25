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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class ProceduralConnectedTexture
{
    protected final int NO_FEATURE = -1;

    protected final int DEFAULT = 0;
    protected final int TOP = 1 << 0;
    protected final int BOTTOM = 1 << 1;
    protected final int LEFT = 1 << 2;
    protected final int RIGHT = 1 << 3;

    protected final int FEATURE_EDGE_TOP = 1<<4;
    protected final int FEATURE_EDGE_BOTTOM = 1<<5;
    protected final int FEATURE_EDGE_LEFT = 1<<6;
    protected final int FEATURE_EDGE_RIGHT = 1<<7;

    private HashMap<Integer, IRuinWallFeature> features;

    protected ProceduralConnectedTexture()
    {
        this.features = getFeatures();
    }

    protected abstract HashMap<Integer,IRuinWallFeature> getFeatures();

    final int MISSING_TEXTURE = Integer.MAX_VALUE;
    private HashMap<Integer, IIcon> icons = new HashMap<Integer, IIcon>();

    public void registerIcons(IIconRegister iconRegister) {
        icons.clear();
        cachedNoiseGens.clear();
        cachedFeatures.clear();

        registerIconsInternal(iconRegister);

        addIcon(MISSING_TEXTURE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-Missing"));
    }

    public abstract void registerIconsInternal(IIconRegister iconRegister);

    protected void addIcon(int fingerprint, IIcon icon) {
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
    HashMap<ChunkCoord, List<Feature>> cachedFeatures = new HashMap<ChunkCoord, List<Feature>>();

    private List<Feature> getChunkFeatures(ChunkCoord chunkCoord)
    {
        List<Feature> features = cachedFeatures.get(chunkCoord);
        if (features != null)
        {
            return features;
        }
        features = new ArrayList<Feature>();

        for (IRuinWallFeature wallFeature : this.features.values()) {
            features.addAll(wallFeature.getFeatureAreasFor(chunkCoord));
        }

        cachedFeatures.put(chunkCoord, features);

        return features;
    }

    protected int getValidFeature(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation) {
        int desiredFeatureId = getFeatureAt(worldBlockCoord);
        if (desiredFeatureId == NO_FEATURE) {
            return NO_FEATURE;
        }
        IRuinWallFeature wallFeature = features.get(desiredFeatureId);
        if (wallFeature.isFeatureValid(blockAccess, worldBlockCoord, orientation, desiredFeatureId)) {
            return desiredFeatureId;
        }
        return NO_FEATURE;
    }


    protected int getFeatureAt(WorldBlockCoord worldBlockCoord)
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
            for (Feature feature : getChunkFeatures(chunkCoord))
            {
                if (x >= feature.getBlockCoord().getX() && x < feature.getBlockCoord().getX() + feature.getWidth())
                {
                    if (z >= feature.getBlockCoord().getZ() && z < feature.getBlockCoord().getZ() + feature.getDepth())
                    {
                        if (y >= feature.getBlockCoord().getY() && y < feature.getBlockCoord().getY() + feature.getHeight())
                        {
                            featureId = feature.getFeatureId();
                            break;
                        }
                    }
                }
            }
            noiseData[index] = featureId;
        }
        return featureId;
    }

    public String describeTextureAt(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
    {
        int blockProperties = getTexturePropertiesForSide(blockAccess, worldBlockCoord, side);
        return describeTextureProperties(blockProperties);

    }


    protected interface IRuinWallFeature {
        boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId);

        Collection<Feature> getFeatureAreasFor(ChunkCoord chunkCoord);
    }

    protected static class Feature
    {
        private final int featureId;
        private final WorldBlockCoord blockCoord;
        private final int width;
        private final int height;
        private final int depth;

        public Feature(int featureId, WorldBlockCoord blockCoord, int width, int height, int depth)
        {

            this.featureId = featureId;
            this.blockCoord = blockCoord;
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        public int getFeatureId()
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
