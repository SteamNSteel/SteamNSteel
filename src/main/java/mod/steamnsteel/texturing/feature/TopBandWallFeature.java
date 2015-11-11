package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.api.*;
import mod.steamnsteel.texturing.wall.RemnantRuinWallTexture;
import mod.steamnsteel.utility.position.ChunkCoord;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class TopBandWallFeature extends ProceduralWallFeatureBase
{
    private final ProceduralConnectedTexture texture;

    public TopBandWallFeature(ProceduralConnectedTexture texture, String name, Layer layer)
    {

        super(name, layer);
        this.texture = texture;
    }

    @Override
    public boolean isFeatureValid(SpriteRequest request)
    {
        return !texture.isBlockPartOfWallAndUnobstructed(request, TextureDirection.ABOVE);
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
        long properties = 0;
        if (texture.isBlockPartOfWallAndUnobstructed(request, TextureDirection.LEFT, TextureDirection.ABOVE))
        {
            properties |= ProceduralConnectedTexture.LEFT;
        }
        if (texture.isBlockPartOfWallAndUnobstructed(request, TextureDirection.RIGHT, TextureDirection.ABOVE))
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

        properties |= ProceduralConnectedTexture.TOP;

        return properties;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature, long traits)
    {
        if (otherLayerFeature instanceof BottomBandWallFeature)
        {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }

    @Override
    public long getIncompatibleTraits()
    {
        return RemnantRuinWallTexture.ALTERNATE;
    }

    private int getCrownSplitOpportunity(BlockPos worldBlockCoord)
    {
        int x = worldBlockCoord.getX();
        int y = worldBlockCoord.getY();
        int z = worldBlockCoord.getZ();
        Random r = new Random(x * y * z * 31);
        return r.nextInt();
    }
}
