package mod.steamnsteel.library;

import mod.steamnsteel.api.SteamNSteelInitializedEvent;
import mod.steamnsteel.api.crafting.ICraftingManager;
import mod.steamnsteel.api.crafting.ingredient.ItemStackIngredient;
import mod.steamnsteel.api.crafting.ingredient.OreDictionaryIngredient;
import mod.steamnsteel.library.Reference.BlockNames;
import mod.steamnsteel.library.Reference.ItemNames;
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
        final OreDictionaryIngredient cuIngotIngredient = new OreDictionaryIngredient(ItemNames.COPPER_INGOT);
        final OreDictionaryIngredient cuBlockIngredient = new OreDictionaryIngredient(BlockNames.COPPER_BLOCK);

        final ICraftingManager craftingManager = event.getCraftingManager();
        craftingManager
                .registerInventorySerializer(OreDictionaryIngredient.class, new OreDictionaryIngredientSerializer())
                .registerInventorySerializer(ItemStackIngredient.class, new ItemStackIngredientSerializer())

                .addProjectTableRecipe()
                    .withIngredient(ModItem.ingotSteel, 15)
                    .crafts(ModBlock.blockSteel, 1)

                .addProjectTableRecipe()
                    .withIngredient(Blocks.dirt, 64 * 3)
                    .crafts(Items.diamond, 10)

                .addProjectTableRecipe()
                    .withIngredient(Blocks.gold_block, 64)
                    .andIngredient(Blocks.gold_ore, 64)
                    .andIngredient(Blocks.beacon, 64)
                    .andIngredient(Blocks.brown_mushroom_block, 64)
                    .crafts(Items.gold_nugget)

                .addProjectTableRecipe()
                    .withIngredient(Blocks.dirt, 2 * 64)
                    .crafts(Items.diamond)

                .addProjectTableRecipe()
                    .withIngredient(new OreDictionaryIngredient("plankWood", 64))
                    .crafts(Blocks.anvil)

                .addProjectTableRecipe()
                    .withIngredient(ModBlock.blockSteel, 2)
                    .crafts(Blocks.anvil)

                .addAlloy()
                    .withIngredient(cuIngotIngredient)
                    .andIngredient(new OreDictionaryIngredient(ItemNames.TIN_INGOT))
                    .produces(ModItem.ingotBronze, 2)

                .addAlloy()
                    .withIngredient(cuBlockIngredient)
                    .andIngredient(new OreDictionaryIngredient(BlockNames.TIN_BLOCK))
                    .produces(ModBlock.blockBronze, 2)

                .addAlloy()
                    .withIngredient(cuIngotIngredient)
                    .andIngredient(new OreDictionaryIngredient(ItemNames.ZINC_INGOT))
                    .produces(ModItem.ingotBrass, 2)

                .addAlloy()
                    .withIngredient(cuBlockIngredient)
                    .andIngredient(new OreDictionaryIngredient(BlockNames.ZINC_BLOCK))
                    .produces(ModBlock.blockBrass, 2)

                .addAlloy()
                    .withIngredient(new OreDictionaryIngredient("ingotIron"))
                    .andIngredient(Items.coal, 2)
                    .produces(ModItem.ingotSteel)

                .addAlloy()
                    .withIngredient(new OreDictionaryIngredient("blockIron"))
                    .andIngredient(Blocks.coal_block, 2)
                    .produces(ModBlock.blockSteel)

                .addAlloy()
                    .withIngredient(new OreDictionaryIngredient(ItemNames.BRASS_INGOT))
                    .andIngredient(new OreDictionaryIngredient(ItemNames.STEEL_INGOT))
                    .produces(ModItem.ingotPlotonium, 2)

                .addAlloy()
                    .withIngredient(new OreDictionaryIngredient(BlockNames.BRASS_BLOCK))
                    .andIngredient(new OreDictionaryIngredient(BlockNames.STEEL_BLOCK))
                    .produces(ModBlock.blockPlotonium, 2)
        ;
    }
}
