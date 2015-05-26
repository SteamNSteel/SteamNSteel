package mod.steamnsteel.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import mod.steamnsteel.TheMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class VoxBoxPacket implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<VoxBoxPacket, VoxBoxPacket> {

        @Override
        public VoxBoxPacket onMessage(VoxBoxPacket message, MessageContext ctx) {
            EntityPlayerMP playerMP = ctx.getServerHandler().playerEntity;
            playerMP.openGui(TheMod.instance, 1, playerMP.worldObj,
                    (int) playerMP.posX, (int) playerMP.posY, (int) playerMP.posZ);
            return message;
        }
    }
}
