package mod.steamnsteel.client.gui.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import java.util.Collection;

/**
 * Created by codew on 6/01/2016.
 */
public class ProjectTableRecipe
{
    private ImmutableList<ItemStack> output;
    private ImmutableList<ItemStack> input;
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
}
