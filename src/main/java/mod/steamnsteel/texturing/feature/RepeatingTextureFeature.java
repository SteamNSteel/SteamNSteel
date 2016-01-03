package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.api.traitspriteregistry.ISpriteDefinitionStart;
import mod.steamnsteel.texturing.wall.RemnantRuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RepeatingTextureFeature extends ProceduralWallFeatureBase implements IMultiTraitFeature
{
    private final int width;
    private final int height;

    public RepeatingTextureFeature(String name, Layer layer, int width, int height)
    {
        super(name, layer);
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isFeatureValid(SpriteRequest request)
    {
        return true;
    }

    @Override
    public Collection<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord)
    {
        List<FeatureInstance> oneInstance = new ArrayList<FeatureInstance>(1);
        oneInstance.add(new FeatureInstance(this, new BlockPos(0, 0, 0), 16, 256, 16));
        return oneInstance;
    }

    @Override
    public long getTraits(SpriteRequest request)
    {
        int xPos = request.getWorldBlockCoord().getX() % width;
        if (xPos < 0) xPos += width;
        int zPos = request.getWorldBlockCoord().getZ() % height;
        if (zPos < 0) zPos += height;

        final int index = zPos * width + xPos;

        int bitsToReserve = 1;
        long sequentialStateCount = getFeatureTraitId();
        while ( (sequentialStateCount >>= 1) != 0) //http://stackoverflow.com/a/12349615
        {
            bitsToReserve++;
        }

        return getFeatureTraitId() | (index << bitsToReserve);
    }

    @Override
    public long getIncompatibleTraits()
    {
        return ProceduralConnectedTexture.TOP | ProceduralConnectedTexture.BOTTOM |
                ProceduralConnectedTexture.LEFT | ProceduralConnectedTexture.RIGHT |
                ProceduralConnectedTexture.FEATURE_EDGE_TOP | ProceduralConnectedTexture.FEATURE_EDGE_BOTTOM |
                ProceduralConnectedTexture.FEATURE_EDGE_LEFT | ProceduralConnectedTexture.FEATURE_EDGE_RIGHT;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long traits)
    {
        return Behaviour.REPLACES;
    }

    @Override
    public int getSequentialStateCount()
    {
        return width * height;
    }

    @Override
    public void registerTextures(ISpriteDefinitionStart textureDefinitions, ResourceLocation baseResourceLocation)
    {
        long featureTraitId = getFeatureTraitId();
        int bitsToReserve = 1;
        long sequentialStateCount = getFeatureTraitId();
        while ( (sequentialStateCount >>= 1) != 0) //http://stackoverflow.com/a/12349615
        {
            bitsToReserve++;
        }

        for (int x = 0; x < width; x++)
        {
            for (int z = 0; z < height; z++)
            {
                textureDefinitions.useSpriteNamed(
                        new ResourceLocation(
                                baseResourceLocation.getResourceDomain(),
                                String.format("%s_%dx%d", baseResourceLocation.getResourcePath(), x + 1, z + 1)
                        )
                ).forTraitSet(featureTraitId | ((z * width + x) << bitsToReserve));
            }
        }
    }
}
