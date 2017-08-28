package mod.steamnsteel.networking;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.crafting.projecttable.ProjectTableManager;
import mod.steamnsteel.crafting.projecttable.ProjectTableRecipe;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.oredict.OreDictionary;

public class ProjectTableCraftPacketMessageHandler implements IMessageHandler<ProjectTableCraftPacket, IMessage>
{
    @Override
    public IMessage onMessage(final ProjectTableCraftPacket message, final MessageContext ctx)
    {
        final InventoryPlayer playerInventory = ctx.getServerHandler().playerEntity.inventory;
        final ProjectTableRecipe recipe = message.getRecipe();

        final boolean canCraft = ProjectTableManager.INSTANCE.canCraftRecipe(recipe, playerInventory);
        if (!canCraft) {
            return null;
        }

        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        mainThread.addScheduledTask(() -> {
            for (final IIngredient ingredient : recipe.getInput())
            {
                int quantityToConsume = ingredient.getQuantityConsumed();
                final ImmutableList<ItemStack> itemStacks = ingredient.getItemStacks();
                for (final ItemStack itemStack : itemStacks)
                {
                    int metadata = itemStack.getMetadata();
                    metadata = metadata == OreDictionary.WILDCARD_VALUE ? -1 : metadata;
                    quantityToConsume -= playerInventory.clearMatchingItems(itemStack.getItem(), metadata, quantityToConsume, itemStack.getTagCompound());
                    playerInventory.markDirty();
                    if (quantityToConsume <= 0) {
                        break;
                    }
                }
            }

            for (final ItemStack itemStack : recipe.getOutput())
            {
                final ItemStack copy = itemStack.copy();

                if (!playerInventory.addItemStackToInventory(copy))
                {
                    ctx.getServerHandler().playerEntity.dropItem(copy, true);
                }
                else
                {
                    playerInventory.markDirty();
                }
            }
        });

        return null; // no response in this case
    }
}
