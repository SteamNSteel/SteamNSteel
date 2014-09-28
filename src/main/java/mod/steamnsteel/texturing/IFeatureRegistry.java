package mod.steamnsteel.texturing;

public interface IFeatureRegistry
{
    void registerFeature(IProceduralWallFeature feature);

    long registerFeatureProperty(String description);
}
