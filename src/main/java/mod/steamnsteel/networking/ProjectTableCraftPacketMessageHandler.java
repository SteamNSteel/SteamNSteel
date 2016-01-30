package mod.steamnsteel.networking;

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

public class ProjectTableCraftPacketMessageHandler implements IMessageHandler<ProjectTableCraftPacket, IMessage>
{
    @Override
    public IMessage onMessage(final ProjectTableCraftPacket message, final MessageContext ctx)
    {
        final InventoryPlayer playerInventory = ctx.getServerHandler().playerEntity.inventory;
        final ProjectTableRecipe recipe = message.getRecipe();

        final boolean canCraft = ProjectTableManager.INSTANCE.canCraftRecipe(recipe, playerInventory);
        if (!canCraft) {
            return new ProjectTableCraftResultPacket(recipe, false);
        }


        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                for (final IIngredient ingredient : recipe.getInput())
                {
                    int quantityToConsume = ingredient.getQuantityConsumed();
                    for (final ItemStack itemStack : ingredient.getItemStacks())
                    {
                        quantityToConsume -= playerInventory.clearMatchingItems(itemStack.getItem(), itemStack.getMetadata(), quantityToConsume, itemStack.getTagCompound());
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
                        ctx.getServerHandler().playerEntity.dropPlayerItemWithRandomChoice(copy, true);
                    }
                    else
                    {
                        playerInventory.markDirty();
                    }
                }
            }
        });

        return null; // no response in this case
    }
}
