package mod.steamnsteel.networking;

import com.google.common.collect.Lists;
import mod.steamnsteel.client.gui.model.ProjectTableRecipe;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import java.util.List;

public class ProjectTableCraftPacketMessageHandler implements IMessageHandler<ProjectTableCraftPacket, IMessage>
{
    @Override
    public IMessage onMessage(final ProjectTableCraftPacket message, final MessageContext ctx)
    {

        final InventoryPlayer playerInventory = ctx.getServerHandler().playerEntity.inventory;
        final ProjectTableRecipe recipe = message.getRecipe();
        final List<ItemStack> compactedInventoryItems = getCompactedInventoryItems(playerInventory);

        boolean canCraft = true;
        for (final ItemStack recipeInput : recipe.getConsolidatedInput())
        {
            boolean itemMatched = false;

            for (final ItemStack playerItem : compactedInventoryItems) {
                if (recipeInput.getItem() == playerItem.getItem() && recipeInput.getMetadata() == playerItem.getMetadata() && ItemStack.areItemStackTagsEqual(recipeInput, playerItem)) {
                    itemMatched = true;
                    if (recipeInput.stackSize > playerItem.stackSize) {
                        canCraft = false;
                    }
                }
            }
            if (!itemMatched) {
                canCraft = false;
            }
        }
        if (!canCraft) {
            return null;
        }

        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {


                for (final ItemStack itemStack : recipe.getInput())
                {
                    playerInventory.clearMatchingItems(itemStack.getItem(), itemStack.getMetadata(), itemStack.stackSize, itemStack.getTagCompound());
                    playerInventory.markDirty();
                }

                for (final ItemStack itemStack : recipe.getOutput())
                {
                    ItemStack copy = itemStack.copy();

                    if (!playerInventory.addItemStackToInventory(copy)) {
                        ctx.getServerHandler().playerEntity.dropPlayerItemWithRandomChoice(copy, true);
                    } else {
                        playerInventory.markDirty();
                    }
                }
            }
        });
        return null; // no response in this case
    }

    private List<ItemStack> getCompactedInventoryItems(InventoryPlayer inventorySlots) {
        List<ItemStack> usableItems = Lists.newArrayList();
        for (final ItemStack itemStack : inventorySlots.mainInventory)
        {
            if (itemStack == null || itemStack.getItem() == null)
            {
                continue;
            }

            boolean itemMatched = false;
            for (final ItemStack existingItemStack : usableItems) {
                if (existingItemStack.getItem() == itemStack.getItem() && existingItemStack.getMetadata() == itemStack.getMetadata() && ItemStack.areItemStackTagsEqual(existingItemStack, itemStack))
                {
                    itemMatched = true;
                    existingItemStack.stackSize += itemStack.stackSize;
                }
            }
            if (!itemMatched) {
                final ItemStack copy = itemStack.copy();
                usableItems.add(copy);
            }
        }
        return usableItems;
    }
}
