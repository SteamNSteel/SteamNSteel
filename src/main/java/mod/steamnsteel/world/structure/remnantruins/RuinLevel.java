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

    public List<RuinSchematic> getSchematics()
    {
        return schematics;
    }

    @SerializedName("KeyPoints")
    KeyPoint[] keyPoints;
    List<RuinSchematic> schematics;
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
            for (final RuinSchematic schematic : schematics)
            {
                maxX = Math.max(maxX, schematic.maxWidth);
                maxY = Math.max(maxY, schematic.maxLength);
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
            schematics = new ArrayList<RuinSchematic>();
            for (final String name : schematicResourcesNames)
            {
                ResourceLocation location = new ResourceLocation(String.format("%s:schematics/%s.schematic", TheMod.MOD_ID, name));
                schematicLoader.loadSchematic(location);
                SchematicLoader.ISchematicMetadata schematic = schematicLoader.getSchematicMetadata(location);
                schematics.add(new RuinSchematic(location, schematic));
            }
        }
    }


}
