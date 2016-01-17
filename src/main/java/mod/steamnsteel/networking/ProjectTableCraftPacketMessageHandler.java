package mod.steamnsteel.networking;

import mod.steamnsteel.client.gui.model.ProjectTableRecipe;
import mod.steamnsteel.inventory.Inventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by codew on 17/01/2016.
 */
public class ProjectTableCraftPacketMessageHandler implements IMessageHandler<ProjectTableCraftPacket, IMessage>
{
    @Override
    public IMessage onMessage(ProjectTableCraftPacket message, MessageContext ctx)
    {
        //FIXME: Thread safety.
        InventoryPlayer playerInventory = ctx.getServerHandler().playerEntity.inventory;
        ProjectTableRecipe recipe = message.getRecipe();

        for (final ItemStack itemStack : recipe.getInput())
            {
                playerInventory.clearMatchingItems(itemStack.getItem(), itemStack.getMetadata(), itemStack.stackSize, itemStack.getTagCompound());
                playerInventory.markDirty();
            }

            for (final ItemStack itemStack : recipe.getOutput())
            {
                ItemStack copy = itemStack.copy();

                if (!playerInventory.addItemStackToInventory(copy)) {
                    //FIXME: Throw item on the ground.
                } else {
                    playerInventory.markDirty();
                }
            }

        return null;
    }
}
