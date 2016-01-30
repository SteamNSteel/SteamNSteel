package mod.steamnsteel.library;

import mod.steamnsteel.api.SteamNSteelInitializedEvent;
import mod.steamnsteel.api.crafting.ICraftingManager;
import mod.steamnsteel.api.crafting.ingredient.ItemStackIngredient;
import mod.steamnsteel.api.crafting.ingredient.OreDictionaryIngredient;
import mod.steamnsteel.networking.serialization.ItemStackIngredientSerializer;
import mod.steamnsteel.networking.serialization.OreDictionaryIngredientSerializer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public enum ModCrafting
{
    INSTANCE;

    @SubscribeEvent
    public void onSteamNSteelInitialized(SteamNSteelInitializedEvent event) {
        final ICraftingManager craftingManager = event.getCraftingManager();
        craftingManager
                .registerInventorySerializer(OreDictionaryIngredient.class, new OreDictionaryIngredientSerializer())
                .registerInventorySerializer(ItemStackIngredient.class, new ItemStackIngredientSerializer())
                .addProjectTableVanillaRecipe(new ItemStack(ModBlock.blockSteel, 1), new ItemStack(ModItem.ingotSteel, 15))
                .addProjectTableVanillaRecipe(new ItemStack(Items.diamond, 10), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64))
                .addProjectTableVanillaRecipe(new ItemStack(Items.gold_nugget, 1), new ItemStack(Blocks.gold_block, 64), new ItemStack(Blocks.gold_ore, 64), new ItemStack(Blocks.beacon, 64), new ItemStack(Blocks.brown_mushroom_block, 64))
                .addProjectTableVanillaRecipe(new ItemStack(Items.diamond, 1), new ItemStack(Blocks.dirt, 64), new ItemStack(Blocks.dirt, 64))
                .addProjectTableRecipe(new ItemStack(Blocks.anvil, 1), new OreDictionaryIngredient("plankWood", 64))
        ;
    }
}
