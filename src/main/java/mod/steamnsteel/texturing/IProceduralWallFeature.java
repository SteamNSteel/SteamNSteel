package mod.steamnsteel.texturing;

import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Collection;

public interface IProceduralWallFeature
{
    Collection<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord);

    long getFeatureId();

    boolean canIntersect(IProceduralWallFeature feature);

    long getSubProperties(TextureContext context, long currentProperties);

    void setFeatureId(long featureId);

    String getName();

    int getLayer();

    Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature);

    boolean isFeatureValid(TextureContext context);
}
