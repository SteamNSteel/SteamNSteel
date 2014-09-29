package mod.steamnsteel.texturing;

public abstract class ProceduralWallFeatureBase implements IProceduralWallFeature
{
    private final String name;
    private final int layer;
    private long featureId;

    protected ProceduralWallFeatureBase(String name, int layer)
    {
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

    public String getName()
    {
        return name;
    }

    @Override
    public long getSubProperties(TextureContext context)
    {
        return 0;
    }

    @Override
    public long getIncompatibleProperties()
    {
        return 0;
    }
}
