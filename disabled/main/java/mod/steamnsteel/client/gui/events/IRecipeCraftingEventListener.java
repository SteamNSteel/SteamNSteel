package mod.steamnsteel.client.gui.events;

import mod.steamnsteel.crafting.projecttable.ProjectTableRecipe;

/**
 * Created by codew on 16/01/2016.
 */
public interface IRecipeCraftingEventListener
{
    void onRecipeCrafting(ProjectTableRecipe recipe);
}
