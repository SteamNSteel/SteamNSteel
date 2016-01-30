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

package mod.steamnsteel.api;

import com.google.common.collect.Lists;
import mod.steamnsteel.api.crafting.IAlloyManager;
import mod.steamnsteel.api.crafting.ICraftingManager;
import mod.steamnsteel.api.crafting.IProjectTableManager;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.api.crafting.ingredient.IIngredientSerializer;
import mod.steamnsteel.api.crafting.ingredient.ItemStackIngredient;
import mod.steamnsteel.crafting.alloy.AlloyManager;
import mod.steamnsteel.crafting.projecttable.ProjectTableManager;
import mod.steamnsteel.networking.SerializationRegistry;
import net.minecraft.item.ItemStack;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The class that accesses the singleton crafting managers..
 *
 * @author Scott Killen
 * @version 1.0
 * @since 0.1
 */
@SuppressWarnings({"StaticNonFinalField", "PublicField", "NonConstantFieldWithUpperCaseName", "NonSerializableFieldInSerializableClass"})
public enum CraftingManager implements ICraftingManager
{
    INSTANCE;

    /**
     * The singleton IAlloyManager implementation. If the API is present without the mod, alloyManager.isPresent()
     * returns <code>false</code>.
     */
    public final IAlloyManager alloyManager = AlloyManager.INSTANCE;

    public final IProjectTableManager projectTableManager = ProjectTableManager.INSTANCE;

    public ICraftingManager registerInventorySerializer(Class<? extends IIngredient> ingredientClass, IIngredientSerializer serializer) {
        SerializationRegistry.INSTANCE.addSerializer(ingredientClass, serializer);
        return this;
    }

    public ICraftingManager addProjectTableRecipe(ItemStack output, IIngredient... input) {
        projectTableManager.addProjectTableRecipe(output, input);
        return this;
    }

    public ICraftingManager addProjectTableRecipe(ItemStack output, Collection<IIngredient> input) {
        projectTableManager.addProjectTableRecipe(output, input);
        return this;
    }

    public ICraftingManager addProjectTableRecipe(Collection<ItemStack> output, String displayName, Collection<IIngredient> input) {
        projectTableManager.addProjectTableRecipe(output, displayName, input);
        return this;
    }

    @Override
    public ICraftingManager addProjectTableVanillaRecipe(ItemStack output, ItemStack... input)
    {
        addProjectTableRecipe(output, wrapItemStacks(Arrays.asList(input)));
        return this;
    }

    @Override
    public ICraftingManager addProjectTableVanillaRecipe(ItemStack output, Collection<ItemStack> input)
    {
        addProjectTableRecipe(output, wrapItemStacks(input));
        return this;
    }

    @Override
    public ICraftingManager addProjectTableVanillaRecipe(Collection<ItemStack> output, String displayName, Collection<ItemStack> input)
    {
        addProjectTableRecipe(output, displayName, wrapItemStacks(input));
        return this;
    }

    protected List<IIngredient> wrapItemStacks(Collection<ItemStack> input)
    {
        List<IIngredient> transformedIngredients = Lists.newArrayList();
        for (final ItemStack itemStack : input)
        {
            transformedIngredients.add(new ItemStackIngredient(itemStack));
        }
        return transformedIngredients;
    }
}
