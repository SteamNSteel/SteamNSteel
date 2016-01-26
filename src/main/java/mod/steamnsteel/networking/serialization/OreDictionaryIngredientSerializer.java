package mod.steamnsteel.networking.serialization;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.api.crafting.ingredient.IIngredientSerializer;
import mod.steamnsteel.api.crafting.ingredient.OreDictionaryIngredient;
import mod.steamnsteel.utility.SteamNSteelException;
import net.minecraft.network.PacketBuffer;

/**
 * Created by codew on 26/01/2016.
 */
public class OreDictionaryIngredientSerializer implements IIngredientSerializer
{
    @Override
    public IIngredient deserialize(PacketBuffer buffer)
    {
        final String oreDictionaryName = buffer.readStringFromBuffer(Integer.MAX_VALUE);
        final int quantity = buffer.readInt();
        return new OreDictionaryIngredient(oreDictionaryName, quantity);
    }

    @Override
    public void serialize(IIngredient ingredient, PacketBuffer buffer)
    {
        if (!(ingredient instanceof OreDictionaryIngredient)) throw new SteamNSteelException("Attempt to deserialize an ingredient that is not an OreDictionaryIngredient");
        final OreDictionaryIngredient oreDictionaryIngredient = (OreDictionaryIngredient) ingredient;

        buffer.writeString(oreDictionaryIngredient.getName())
              .writeInt(oreDictionaryIngredient.getQuantityConsumed());
    }
}
