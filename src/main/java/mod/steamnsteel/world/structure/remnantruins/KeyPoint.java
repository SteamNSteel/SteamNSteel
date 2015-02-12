package mod.steamnsteel.world.structure.remnantruins;

import com.google.gson.annotations.SerializedName;

public class KeyPoint
{
    @SerializedName("CurveProfile")
    double curveProfile;
    @SerializedName("Probability")
    double probability;

    public double getCurveProfile()
    {
        return curveProfile;
    }

    public long getDistanceFromSpawn()
    {
        return distanceFromSpawn;
    }

    public double getProbability()
    {
        return probability;
    }

    @SerializedName("DistanceFromSpawn")
    long distanceFromSpawn;
}
