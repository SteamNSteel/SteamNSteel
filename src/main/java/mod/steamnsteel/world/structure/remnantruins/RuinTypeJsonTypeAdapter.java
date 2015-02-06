package mod.steamnsteel.world.structure.remnantruins;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class RuinTypeJsonTypeAdapter implements JsonDeserializer<RuinType>
{
    public RuinType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        String value = json.getAsString();
        return RuinType.valueOf(value.toUpperCase());
    }
}
