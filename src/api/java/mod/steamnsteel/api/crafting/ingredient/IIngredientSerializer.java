package mod.steamnsteel.api.crafting.ingredient;

import net.minecraft.network.PacketBuffer;

public interface IIngredientSerializer
{
    /**
     * Deserializes a packet buffer to a custom IIngredient implementation
     * @param buffer The buffer containing the Ingredient bytes
     * @return The IIngredient.
     */
    IIngredient deserialize(PacketBuffer buffer);

    /**
     * Serializes an IIngredient to packet buffer
     * @param ingredient The ingredient to send
     * @param buffer The buffer to store it in.
     */
    void serialize(IIngredient ingredient, PacketBuffer buffer);
}
