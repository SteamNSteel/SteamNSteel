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

public class NullFeature extends ProceduralWallFeatureBase
{
    public NullFeature(String name, Layer layer)
    {
        super(name, layer);
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
        return 0;
    }

    @Override
    public long getIncompatibleTraits()
    {
        return ProceduralConnectedTexture.TOP | ProceduralConnectedTexture.BOTTOM |
                ProceduralConnectedTexture.LEFT | ProceduralConnectedTexture.RIGHT |
                ProceduralConnectedTexture.FEATURE_EDGE_TOP | ProceduralConnectedTexture.FEATURE_EDGE_BOTTOM |
                ProceduralConnectedTexture.FEATURE_EDGE_LEFT | ProceduralConnectedTexture.FEATURE_EDGE_RIGHT |
                RemnantRuinWallTexture.ALTERNATE;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long traits)
    {
        return Behaviour.REPLACES;
    }
}
