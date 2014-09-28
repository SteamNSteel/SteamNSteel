package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Collection;

public class BottomBandWallFeature extends ProceduralWallFeatureBase
{
    private final ProceduralConnectedTexture texture;

    public BottomBandWallFeature(ProceduralConnectedTexture texture, String name, int layer)
    {
        super(name, layer);
        this.texture = texture;
    }

    @Override
    public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        return false;
    }

    @Override
    public Collection<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        return null;
    }

    @Override
    public boolean canIntersect(IProceduralWallFeature feature)
    {
        return false;
    }

    @Override
    public long getSubProperties(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation)
    {
        return 0;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature)
    {
        return null;
    }
}
