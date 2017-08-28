package mod.steamnsteel.networking.serialization;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.api.crafting.ingredient.IIngredientSerializer;
import mod.steamnsteel.api.crafting.ingredient.ItemStackIngredient;
import mod.steamnsteel.networking.PacketBufferExtensions;
import mod.steamnsteel.utility.SteamNSteelException;
import net.minecraft.network.PacketBuffer;
import java.io.IOException;

/**
 * Created by codew on 26/01/2016.
 */
public class ItemStackIngredientSerializer implements IIngredientSerializer
{
    @Override
    public IIngredient deserialize(PacketBuffer buffer)
    {
        try
        {
            return new ItemStackIngredient(PacketBufferExtensions.readLargeItemStackFromBuffer(buffer));
        } catch (IOException e)
        {
            throw new SteamNSteelException(e);
        }
    }

    @Override
    public void serialize(IIngredient ingredient, PacketBuffer buffer)
    {
        if (!(ingredient instanceof ItemStackIngredient)) throw new SteamNSteelException("Attempt to deserialize an ingredient that is not an ItemStackIngredient");
        final ItemStackIngredient itemStackIngredient = (ItemStackIngredient) ingredient;
        PacketBufferExtensions.writeLargeItemStackToBuffer(buffer, itemStackIngredient.getItemStack());
    }
}
