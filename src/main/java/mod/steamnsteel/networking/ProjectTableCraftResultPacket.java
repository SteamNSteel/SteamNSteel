package mod.steamnsteel.networking;

import io.netty.buffer.ByteBuf;
import mod.steamnsteel.crafting.projecttable.ProjectTableRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectTableCraftResultPacket implements IMessage
{
    private ProjectTableRecipe recipe;
    private boolean succeeded;

    public ProjectTableCraftResultPacket()
    {
    }

    @SideOnly(Side.CLIENT)
    public ProjectTableCraftResultPacket(ProjectTableRecipe recipe, boolean succeeded)
    {
        this.recipe = recipe;
        this.succeeded = succeeded;
    }

    public ProjectTableRecipe getRecipe() {
        return recipe;
    }

    public boolean hasSucceeded()
    {
        return succeeded;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {

        final PacketBuffer packetBuffer = new PacketBuffer(buf);
        recipe = ProjectTableRecipe.readFromBuffer(packetBuffer);
        succeeded = packetBuffer.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {

        final PacketBuffer packetBuffer = new PacketBuffer(buf);
        recipe.writeToBuffer(packetBuffer);
        packetBuffer.writeBoolean(succeeded);
    }
}
