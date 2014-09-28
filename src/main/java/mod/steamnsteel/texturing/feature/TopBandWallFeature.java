package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TopBandWallFeature extends ProceduralWallFeatureBase
{
    private final ProceduralConnectedTexture texture;

    public TopBandWallFeature(ProceduralConnectedTexture texture, String name, int layer) {

        super(name, layer);
        this.texture = texture;
    }

    @Override
    public boolean isFeatureValid(TextureContext context)
    {
        return !context.getNearbyBlock(TextureDirection.ABOVE).getMaterial().isOpaque();
    }

    @Override
    public Collection<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        List<FeatureInstance> oneInstance = new ArrayList<FeatureInstance>(1);
        oneInstance.add(new FeatureInstance(this, WorldBlockCoord.of(0,0,0), 16, 256, 16));
        return oneInstance;
    }

    @Override
    public boolean canIntersect(IProceduralWallFeature feature)
    {
        return false;
    }

    /*@Override
    public long getSubProperties(TextureContext context, long currentProperties)
    {
        return getFeatureId();
    }*/

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature)
    {
        if (otherLayerFeature instanceof  BottomBandWallFeature) {
            return Behaviour.CANNOT_EXIST;
        }
        return Behaviour.COEXIST;
    }
}
