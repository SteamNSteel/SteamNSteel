package mod.steamnsteel.texturing;

import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Collection;

public abstract class ProceduralWallFeatureBase implements IProceduralWallFeature
{
    private final String name;
    private final int layer;
    private long featureId;

    protected ProceduralWallFeatureBase(String name, int layer) {
        this.name = name;
        this.layer = layer;
    }

    @Override
    public long getFeatureId()
    {
        return featureId;
    }

    @Override
    public void setFeatureId(long featureId)
    {
        this.featureId = featureId;
    }

    public int getLayer()
    {
        return layer;
    }

    public String getName() {
        return name;
    }
}