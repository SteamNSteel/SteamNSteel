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

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.api.crafting.IAlloyResult;
import mod.steamnsteel.block.machine.CupolaBlock;
import mod.steamnsteel.client.gui.CupolaGui;
import mod.steamnsteel.crafting.alloy.AlloyManager;
import mod.steamnsteel.utility.ItemWrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.google.common.collect.Table.Cell;

public class CupolaRecipeHandler extends TemplateRecipeHandler
{
	class CachedCupolaRecipe extends CachedRecipe
	{
		PositionedStack result;
		List<PositionedStack> inputs;
		
		CachedCupolaRecipe(ItemWrapper input1, ItemWrapper input2, ItemStack result)
		{
			inputs = new ArrayList<PositionedStack>();
			inputs.add(new PositionedStack(input1.getStack(), 20, 6));
			inputs.add(new PositionedStack(input2.getStack(), 54, 6));
			
			this.result = new PositionedStack(result, 37, 42);
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			return inputs;
		}

		@Override
		public PositionedStack getResult()
		{
			return result;
		}
	}

	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(76, 24, 24, 16), getOverlayIdentifier()));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return CupolaGui.class;
	}

	@Override
	public String getRecipeName()
	{
		return StatCollector.translateToLocal("container." + TheMod.MOD_ID + ":" + CupolaBlock.NAME);
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals(getOverlayIdentifier()))
		{
			for (Cell<ItemWrapper, ItemWrapper, IAlloyResult> cell : AlloyManager.alloys.cellSet())
			{
				CachedCupolaRecipe recipe = new CachedCupolaRecipe(cell.getRowKey(), cell.getColumnKey(), cell.getValue().getItemStack().get());
				arecipes.add(recipe);
			}
		} else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (Cell<ItemWrapper, ItemWrapper, IAlloyResult> cell : AlloyManager.alloys.cellSet())
		{
			if (areStacksTheSame(result, cell.getValue().getItemStack().get()))
			{
				CachedCupolaRecipe recipe = new CachedCupolaRecipe(cell.getRowKey(), cell.getColumnKey(), cell.getValue().getItemStack().get());
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for (Cell<ItemWrapper, ItemWrapper, IAlloyResult> cell : AlloyManager.alloys.cellSet())
		{
			if (areStacksTheSame(ingredient, cell.getRowKey().getStack()) || areStacksTheSame(ingredient, cell.getColumnKey().getStack()))
			{
				CachedCupolaRecipe recipe = new CachedCupolaRecipe(cell.getRowKey(), cell.getColumnKey(), cell.getValue().getItemStack().get());
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
		return TheMod.MOD_ID + CupolaBlock.NAME;
	}

	@Override
	public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
	{
		return RecipeInfo.hasDefaultOverlay(gui, getOverlayIdentifier()) || RecipeInfo.hasOverlayHandler(gui, getOverlayIdentifier());
	}

	@Override
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe)
	{
		IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, getOverlayIdentifier());
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

	private boolean areStacksTheSame(ItemStack stack1, ItemStack stack2)
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

	private boolean isWildcard(int meta)
	{
		return meta == OreDictionary.WILDCARD_VALUE;
	}
}