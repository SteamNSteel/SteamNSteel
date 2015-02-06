package mod.steamnsteel.world.structure.remnantruins;

import com.google.gson.annotations.SerializedName;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.world.SchematicLoader;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Dimension;
import java.util.ArrayList;
import java.util.List;

public class RuinLevel
{

    public RuinLevel()
    {
        ruinType = RuinType.COMPOSITE;
    }

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

    @SerializedName("RuinType")
    RuinType ruinType;

    public Dimension getMaxRuinSize()
    {
        if (ruinType == RuinType.SINGLE && maxRuinSize == null) {
            int maxX = 0;
            int maxY = 0;
            for (final Schematic schematic : schematics)
            {
                maxX = Math.max(maxX, schematic.schematicMetadata.getWidth());
                maxY = Math.max(maxY, schematic.schematicMetadata.getLength());
            }
            maxRuinSize = new Dimension(maxX, maxY);
        }

        return maxRuinSize;
    }

    @SerializedName("MaxRuinSize")
    Dimension maxRuinSize;

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
