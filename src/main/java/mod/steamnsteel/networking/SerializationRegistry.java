package mod.steamnsteel.networking;

import com.google.common.collect.Maps;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.api.crafting.ingredient.IIngredientSerializer;
import java.util.Map;

public enum SerializationRegistry
{
    INSTANCE;

    private Map<String, IIngredientSerializer> ingredientSerializers = Maps.newHashMap();


    public void addSerializer(Class<? extends IIngredient> ingredientClass, IIngredientSerializer serializer)
    {
        ingredientSerializers.put(ingredientClass.getName(), serializer);
    }

    public IIngredientSerializer getSerializer(String name)
    {
        return ingredientSerializers.get(name);
    }
}
