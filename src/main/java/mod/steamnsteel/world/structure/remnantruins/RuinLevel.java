package mod.steamnsteel.world.structure.remnantruins;

import com.google.gson.annotations.SerializedName;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.world.SchematicLoader;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class RuinLevel
{
    public KeyPoint[] getKeyPoints()
    {
        return keyPoints;
    }

    public String getLevelName()
    {
        return levelName;
    }

    public List<Schematic> getSchematics()
    {
        return schematics;
    }

    @SerializedName("KeyPoints")
    KeyPoint[] keyPoints;
    List<Schematic> schematics;
    @SerializedName("Schematics")
    List<String> schematicResourcesNames;

    @SerializedName("LevelName")
    String levelName;

    public Point2D.Double getMaxRuinSize()
    {
        return maxRuinSize;
    }

    @SerializedName("MaxRuinSize")
    Point2D.Double maxRuinSize;

    public void resolveSchematicNames(SchematicLoader schematicLoader)
    {
        if (schematicResourcesNames != null) {
            schematics = new ArrayList<Schematic>();
            for (final String name : schematicResourcesNames)
            {
                ResourceLocation location = new ResourceLocation(String.format("%s:schematics/%s.schematic", TheMod.MOD_ID, name));
                SchematicLoader.ISchematicWorldMetadata schematic = schematicLoader.loadSchematic(location);
                schematics.add(new Schematic(location, schematic));
            }
        }
    }


}
