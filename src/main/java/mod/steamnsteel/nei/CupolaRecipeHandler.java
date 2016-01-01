/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.nei;

import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table.Cell;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.api.crafting.IAlloyResult;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.client.gui.CupolaGui;
import mod.steamnsteel.crafting.alloy.AlloyManager;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import mod.steamnsteel.utility.ItemWrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import java.awt.*;
import java.util.List;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class CupolaRecipeHandler extends TemplateRecipeHandler
{
    private static final List<ItemStack> burnable = Lists.newArrayList();
    private static final Set<Item> hiddenBurnable = Sets.newHashSet();
    private static final int TICKS = 24;

    private static void getHiddenFuelTypes()
    {
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.brown_mushroom));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.daylight_detector));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.acacia_fence));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.birch_fence));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.dark_oak_fence));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.jungle_fence));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.oak_fence));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.spruce_fence));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.acacia_fence_gate));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.birch_fence_gate));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.dark_oak_fence_gate));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.jungle_fence_gate));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.oak_fence_gate));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.spruce_fence_gate));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.jukebox));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.noteblock));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.red_mushroom));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.standing_sign));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.trapdoor));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.trapped_chest));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.wall_sign));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.acacia_door));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.birch_door));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.dark_oak_door));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.jungle_door));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.oak_door));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.spruce_door));
        hiddenBurnable.add(Item.getItemFromBlock(Blocks.wooden_pressure_plate));
    }

    private static void getFuelTypes()
    {
        for (final ItemStack item : ItemList.items)
            if (!hiddenBurnable.contains(item.getItem()))
                if (TileEntityFurnace.getItemBurnTime(item) > 0)
                    burnable.add(item);
    }

    @SuppressWarnings("ObjectEquality")
    private static boolean areStacksTheSame(ItemStack stack1, ItemStack stack2)
    {
        if (stack1 == null || stack2 == null)
        {
            return false;
        }

        if (stack1.getItem() == stack2.getItem())
        {
            if (stack1.getItemDamage() == stack2.getItemDamage() || isWildcard(stack1.getItemDamage()) || isWildcard(stack2.getItemDamage()))
            {
                if (stack1.hasTagCompound() && stack2.hasTagCompound())
                {
                    return stack1.getTagCompound().equals(stack2.getTagCompound());
                }
                return stack1.hasTagCompound() == stack2.hasTagCompound();
            }
        }
        return false;
    }

    private static boolean isWildcard(int meta)
    {
        return meta == OreDictionary.WILDCARD_VALUE;
    }

    @Override
    public TemplateRecipeHandler newInstance()
    {
        if (hiddenBurnable.isEmpty())
            getHiddenFuelTypes();

        if (burnable.isEmpty())
            getFuelTypes();

        return super.newInstance();
    }

    @Override
    public void loadTransferRects()
    {
        transferRects.add(new RecipeTransferRect(new Rectangle(76, 24, 24, 16), getOverlayIdentifier()));
    }

    @Override
    public void drawExtras(int recipe)
    {
        drawProgressBar(38, 25, 176, 0, 14, 14, TICKS, 7);
        drawProgressBar(74, 23, 176, 14, 24, 16, TICKS, 0);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass()
    {
        return CupolaGui.class;
    }

    @Override
    public String getRecipeName()
    {
        return StatCollector.translateToLocal(SteamNSteelTE.containerName(CupolaBlock.NAME));
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals(getOverlayIdentifier()))
        {
            for (final Cell<ItemWrapper, ItemWrapper, IAlloyResult> cell : AlloyManager.getAlloys().cellSet())
            {
                final CachedCupolaRecipe recipe = new CachedCupolaRecipe(cell.getRowKey(), cell.getColumnKey(), cell.getValue().getItemStack().get());
                arecipes.add(recipe);
            }
        } else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (final Cell<ItemWrapper, ItemWrapper, IAlloyResult> cell : AlloyManager.getAlloys().cellSet())
        {
            if (areStacksTheSame(result, cell.getValue().getItemStack().get()))
            {
                final CachedCupolaRecipe recipe = new CachedCupolaRecipe(cell.getRowKey(), cell.getColumnKey(), cell.getValue().getItemStack().get());
                arecipes.add(recipe);
            }
        }
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        for (final Cell<ItemWrapper, ItemWrapper, IAlloyResult> cell : AlloyManager.getAlloys().cellSet())
        {
            if (areStacksTheSame(ingredient, cell.getRowKey().getStack()) || areStacksTheSame(ingredient, cell.getColumnKey().getStack()))
            {
                final CachedCupolaRecipe recipe = new CachedCupolaRecipe(cell.getRowKey(), cell.getColumnKey(), cell.getValue().getItemStack().get());
                arecipes.add(recipe);
            }
        }
    }

    @Override
    public String getGuiTexture()
    {
        return TheMod.MOD_ID + ":textures/gui/" + CupolaBlock.NAME + ".png";
    }

    @Override
    public String getOverlayIdentifier()
    {
        //noinspection StringConcatenationMissingWhitespace
        return TheMod.MOD_ID + CupolaBlock.NAME;
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
    {
        return RecipeInfo.hasDefaultOverlay(gui, getOverlayIdentifier()) || RecipeInfo.hasOverlayHandler(gui, getOverlayIdentifier());
    }

    @SuppressWarnings("ReturnOfNull")
    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe)
    {
        final IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, getOverlayIdentifier());
        if (positioner == null)
        {
            return null;
        }
        return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe)
    {
        return RecipeInfo.getOverlayHandler(gui, getOverlayIdentifier());
    }

    @SuppressWarnings("NonStaticInnerClassInSecureContext")
    private class CachedCupolaRecipe extends CachedRecipe
    {
        private final PositionedStack result;
        private final List<PositionedStack> inputs;

        CachedCupolaRecipe(ItemWrapper input1, ItemWrapper input2, ItemStack result)
        {
            inputs = Lists.newArrayList();
            inputs.add(new PositionedStack(input1.getStack(), 20, 6));  //Left
            inputs.add(new PositionedStack(input2.getStack(), 54, 6));  //Right

            this.result = new PositionedStack(result, 111, 23);    //Out
        }

        @Override
        public List<PositionedStack> getIngredients()
        {
            return ImmutableList.copyOf(inputs);
        }

        @Override
        public PositionedStack getResult()
        {
            return result;
        }

        @Override
        public PositionedStack getOtherStack()
        {        //Fuel
            return new PositionedStack(burnable.get(cycleticks / TICKS % burnable.size()), 37, 42);
        }

        @Override
        public String toString()
        {
            return Objects.toStringHelper(this)
                    .add("result", result)
                    .add("inputs", inputs)
                    .toString();
        }
    }
}