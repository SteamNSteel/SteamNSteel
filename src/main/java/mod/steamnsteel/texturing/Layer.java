package mod.steamnsteel.texturing;

public class Layer implements Comparable<Layer>

{
    private final int layerId;
    private final String name;
    private final boolean allowRandomization;

    public Layer(int layerId, String name, boolean allowRandomization) {
        this.layerId = layerId;
        this.name = name;
        this.allowRandomization = allowRandomization;
    }

    public int getLayerId()
    {
        return layerId;
    }

    public String getName()
    {
        return name;
    }

    public boolean allowRandomization()
    {
        return allowRandomization;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Layer layer = (Layer) o;

        if (layerId != layer.layerId) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return layerId;
    }

    @Override
    public int compareTo(Layer layer)
    {
        return Integer.compare(getLayerId(), layer.getLayerId());
    }
}
