package mod.steamnsteel.crafting.projecttable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.api.crafting.ingredient.IIngredientSerializer;
import mod.steamnsteel.networking.SerializationRegistry;
import mod.steamnsteel.utility.SteamNSteelException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by codew on 6/01/2016.
 */
public class ProjectTableRecipe
{
    private ImmutableList<ItemStack> output;
    private ImmutableList<IIngredient> input;
    private ImmutableList<ItemStack> consolidatedInput;
    private String displayName;
    private String renderText;

    public ProjectTableRecipe(Collection<ItemStack> output, Collection<IIngredient> input, String displayName)
    {
        this.input = ImmutableList.copyOf(input);
        this.setDisplayName(displayName);
        this.output = ImmutableList.copyOf(output);
    }

    public ProjectTableRecipe(ItemStack output, Collection<IIngredient> input)
    {
        this.input = ImmutableList.copyOf(input);
        this.setDisplayName(output.getDisplayName());
        this.output = ImmutableList.of(output);
    }

    public ProjectTableRecipe(ItemStack output, IIngredient... input)
    {
        this.input = ImmutableList.copyOf(input);
        this.displayName = output.getDisplayName();
        this.output = ImmutableList.of(output);
    }

    public ImmutableList<ItemStack> getOutput()
    {
        return output;
    }

    public ImmutableList<IIngredient> getInput()
    {
        return input;
    }

    public void setInput(ImmutableList<IIngredient> input)
    {
        this.input = input;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getRenderText()
    {
        return renderText;
    }

    public void setRenderText(String renderText)
    {
        this.renderText = renderText;
    }

    public static ProjectTableRecipe readFromBuffer(PacketBuffer buf)
    {
        try
        {
            byte inputItemStackCount = buf.readByte();
            List<IIngredient> input = Lists.newArrayList();
            for (int i = 0; i < inputItemStackCount; ++i)
            {
                input.add(readIngredient(buf));
            }

            byte outputItemStackCount = buf.readByte();
            List<ItemStack> output = Lists.newArrayList();
            for (int i = 0; i < outputItemStackCount; ++i)
            {
                output.add(buf.readItemStackFromBuffer());
            }

            final String displayName = buf.readStringFromBuffer(255);

            return new ProjectTableRecipe(output, input, displayName);
        } catch (IOException e)
        {
            throw new SteamNSteelException("Unable to deserialize ProjectTableRecipe", e);
        }
    }

    private static IIngredient readIngredient(PacketBuffer buf) {
        final String ingredientType = buf.readStringFromBuffer(Integer.MAX_VALUE);
        final IIngredientSerializer serializer = SerializationRegistry.INSTANCE.getSerializer(ingredientType);
        if (serializer == null) {
            throw new SteamNSteelException("Unknown Ingredient serializer: " + ingredientType);
        }
        return serializer.deserialize(buf);
    }

    public void writeToBuffer(PacketBuffer buf)
    {
        buf.writeByte(input.size());
        for (final IIngredient itemStack : input)
        {
            writeIngredient(itemStack, buf);
        }
        buf.writeByte(output.size());
        for (final ItemStack itemStack : output)
        {
            buf.writeItemStackToBuffer(itemStack);
        }
        if (displayName.length() > 255) {
            displayName = displayName.substring(0, 255);
        }
        buf.writeString(displayName);
    }

    private void writeIngredient(IIngredient ingredient, PacketBuffer buf)
    {
        final String name = ingredient.getClass().getName();
        buf.writeString(name);
        final IIngredientSerializer serializer = SerializationRegistry.INSTANCE.getSerializer(ingredient.getClass().getName());
        if (serializer == null) {
            throw new SteamNSteelException("Unknown Ingredient serializer: " + serializer);
        }
        serializer.serialize(ingredient, buf);
    }

    public ImmutableList<ItemStack> getConsolidatedInput()
    {
        if (consolidatedInput != null) {
            return consolidatedInput;
        }

        List<ItemStack> usableItems = Lists.newArrayList();
        for (final IIngredient itemStack : input)
        {
            if (itemStack == null || itemStack.getItem() == null)
            {
                continue;
            }

            boolean itemMatched = false;
            for (final ItemStack existingItemStack : usableItems) {
                if (itemStack.getItem() == existingItemStack.getItem() && itemStack.getMetadata() == existingItemStack.getMetadata() && ItemStack.areItemStackTagsEqual(itemStack, existingItemStack))
                {
                    itemMatched = true;
                    existingItemStack.stackSize += itemStack.stackSize;
                }
            }
            if (!itemMatched) {
                final ItemStack copy = itemStack.copy();
                usableItems.add(copy);
            }
        }
        consolidatedInput = ImmutableList.copyOf(usableItems);
        return consolidatedInput;
    }
}
