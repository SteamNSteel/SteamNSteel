package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.wall.RemnantRuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BottomBandWallFeature extends ProceduralWallFeatureBase
{
    private final ProceduralConnectedTexture texture;

    public BottomBandWallFeature(ProceduralConnectedTexture texture, String name, Layer layer)
    {
        super(name, layer);
        this.texture = texture;
    }

    @Override
    public boolean isFeatureValid(IconRequest request)
    {
        return !texture.isBlockPartOfWallAndUnobstructed(request, TextureDirection.BELOW);
    }

    @Override
    public Collection<FeatureInstance> getFeaturesIn(ChunkCoord chunkCoord)
    {
        List<FeatureInstance> oneInstance = new ArrayList<FeatureInstance>(1);
        oneInstance.add(new FeatureInstance(this, WorldBlockCoord.of(0, 0, 0), 16, 256, 16));
        return oneInstance;
    }

    @Override
    public long getTraits(IconRequest request)
    {
        long properties = 0;
        if (texture.isBlockPartOfWallAndUnobstructed(request, TextureDirection.LEFT, TextureDirection.BELOW))
        {
            properties |= ProceduralConnectedTexture.LEFT;
        }
        if (texture.isBlockPartOfWallAndUnobstructed(request, TextureDirection.RIGHT, TextureDirection.BELOW))
        {
            properties |= ProceduralConnectedTexture.RIGHT;
        }

        //Break up the bases
        if ((getCrownSplitOpportunity(request.getWorldBlockCoord()) & 14) == 0)
        {
            properties |= ProceduralConnectedTexture.LEFT;
        }
        if ((getCrownSplitOpportunity(request.getWorldBlockCoord().offset(request.getRightDirection())) & 14) == 0)
        {
            properties |= ProceduralConnectedTexture.RIGHT;
        }

        properties |= ProceduralConnectedTexture.BOTTOM;
        return properties;
    }

    @Override
    public long getIncompatibleTraits()
    {
        return ProceduralConnectedTexture.TOP | RemnantRuinWallTexture.ALTERNATE;
    }

    private int getCrownSplitOpportunity(WorldBlockCoord worldBlockCoord)
    {
        //I duplicated this because it's currently inconvenient to create a new IconRequest for the offset.
        int x = worldBlockCoord.getX();
        int y = worldBlockCoord.getY();
        int z = worldBlockCoord.getZ();
        Random r = new Random(x * y * z * 31);
        return r.nextInt();
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long traits)
    {
        if (otherLayerFeature instanceof TopBandWallFeature)
        {
            return Behaviour.REPLACES;
        }
        return Behaviour.COEXIST;
    }


}
