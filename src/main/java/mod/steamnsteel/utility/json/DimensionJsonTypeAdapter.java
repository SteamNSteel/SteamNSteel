package mod.steamnsteel.utility.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.lwjgl.util.Dimension;
import java.lang.reflect.Type;

public class DimensionJsonTypeAdapter implements JsonDeserializer<Dimension>
{
    public Dimension deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        String value = json.getAsString();
        String[] dimensions = value.split(",");
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);
        return new Dimension(width, height);
        //return new Dimension(json.getAsJsonPrimitive().getAsString());
    }
}

