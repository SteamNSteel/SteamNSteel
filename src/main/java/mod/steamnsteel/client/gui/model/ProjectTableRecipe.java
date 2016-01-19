package mod.steamnsteel.client.gui.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
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
    private ImmutableList<ItemStack> input;
    private ImmutableList<ItemStack> consolidatedInput;
    private String displayName;
    private String renderText;

    public ProjectTableRecipe(Collection<ItemStack> input, String displayName, Collection<ItemStack> output)
    {
        this.input = ImmutableList.copyOf(input);
        this.setDisplayName(displayName);
        this.output = ImmutableList.copyOf(output);
    }

    public ProjectTableRecipe(Collection<ItemStack> input, ItemStack output)
    {
        this.input = ImmutableList.copyOf(input);
        this.setDisplayName(output.getDisplayName());
        this.output = ImmutableList.of(output);
    }

    public ProjectTableRecipe(ItemStack output, ItemStack... input)
    {
        this.input = ImmutableList.copyOf(input);
        this.displayName = output.getDisplayName();
        this.output = ImmutableList.of(output);
    }

    public ImmutableList<ItemStack> getOutput()
    {
        return output;
    }

    public ImmutableList<ItemStack> getInput()
    {
        return input;
    }

    public void setInput(ImmutableList<ItemStack> input)
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
            List<ItemStack> input = Lists.newArrayList();
            for (int i = 0; i < inputItemStackCount; ++i)
            {
                input.add(buf.readItemStackFromBuffer());
            }

            byte outputItemStackCount = buf.readByte();
            List<ItemStack> output = Lists.newArrayList();
            for (int i = 0; i < outputItemStackCount; ++i)
            {
                output.add(buf.readItemStackFromBuffer());
            }

            final String displayName = buf.readStringFromBuffer(255);


            return new ProjectTableRecipe(input, displayName, output);
        } catch (IOException e)
        {
            throw new SteamNSteelException("Unable to deserialize ProjectTableRecipe", e);
        }
    }

    public void writeToBuffer(PacketBuffer buf)
    {
        buf.writeByte(input.size());
        for (final ItemStack itemStack : input)
        {
            buf.writeItemStackToBuffer(itemStack);
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

    public ImmutableList<ItemStack> getConsolidatedInput()
    {
        if (consolidatedInput != null) {
            return consolidatedInput;
        }

        List<ItemStack> usableItems = Lists.newArrayList();
        for (final ItemStack itemStack : input)
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
