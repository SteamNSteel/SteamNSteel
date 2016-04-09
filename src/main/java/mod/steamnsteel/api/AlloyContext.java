package mod.steamnsteel.api;

import mod.steamnsteel.api.crafting.*;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.api.crafting.ingredient.ItemStackIngredient;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@SuppressWarnings("ClassHasNoToStringMethod")
class AlloyContext implements ICraftingAlloyFirstIngredient, ICraftingAlloySecondIngredient, ICraftingAlloyResult
{
    private final CraftingManager parent;
    private final IAlloyManager alloyManager;
    private IIngredient firstIngredient;
    private IIngredient secondIngredient;

    AlloyContext(CraftingManager parent, IAlloyManager alloyManager) {
        this.parent = parent;
        this.alloyManager = alloyManager;
    }

    @Override
    public ICraftingAlloySecondIngredient withIngredient(IIngredient ingredient)
    {
        firstIngredient = ingredient;
        return this;
    }

    @Override
    public ICraftingAlloySecondIngredient withIngredient(ItemStack ingredient)
    {
        withIngredient(new ItemStackIngredient(ingredient));
        return this;
    }

    @Override
    public ICraftingAlloySecondIngredient withIngredient(Item item)
    {
        withIngredient(item, 1);
        return this;
    }

    @Override
    public ICraftingAlloySecondIngredient withIngredient(Item item, int amount)
    {
        withIngredient(new ItemStack(item, amount));
        return this;
    }

    @Override
    public ICraftingAlloySecondIngredient withIngredient(Block block)
    {
        withIngredient(block, 1);
        return this;
    }

    @Override
    public ICraftingAlloySecondIngredient withIngredient(Block block, int amount)
    {
        withIngredient(new ItemStack(block, amount));
        return this;
    }

    @Override
    public ICraftingAlloyResult andIngredient(IIngredient ingredient)
    {
        secondIngredient = ingredient;
        return this;
    }

    @Override
    public ICraftingAlloyResult andIngredient(ItemStack ingredient)
    {
        andIngredient(new ItemStackIngredient(ingredient));
        return this;
    }

    @Override
    public ICraftingAlloyResult andIngredient(Item item)
    {
        andIngredient(item, 1);
        return this;
    }

    @Override
    public ICraftingAlloyResult andIngredient(Item item, int amount)
    {
        andIngredient(new ItemStack(item, amount));
        return this;
    }

    @Override
    public ICraftingAlloyResult andIngredient(Block block)
    {
        andIngredient(block, 1);
        return this;
    }

    @Override
    public ICraftingAlloyResult andIngredient(Block block, int amount)
    {
        andIngredient(new ItemStack(block, amount));
        return this;
    }

    @Override
    public ICraftingManager produces(ItemStack result)
    {
        alloyManager.addAlloy(firstIngredient, secondIngredient, result);
        return parent;
    }

    @Override
    public ICraftingManager produces(Item item)
    {
        return produces(item, 1);
    }

    @Override
    public ICraftingManager produces(Item item, int amount)
    {
        return produces(new ItemStack(item, amount));
    }

    @Override
    public ICraftingManager produces(Block block)
    {
        return produces(block, 1);
    }

    @Override
    public ICraftingManager produces(Block block, int amount)
    {
        return produces(new ItemStack(block, amount));
    }
}
