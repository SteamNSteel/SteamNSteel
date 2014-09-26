package mod.steamnsteel.texturing;

import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Collection;

public interface IProceduralWallFeature
{
    boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId);

    Collection<ProceduralConnectedTexture.FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord);

    int getFeatureId();

    boolean canIntersect(IProceduralWallFeature feature);
}
