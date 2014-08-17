package mod.steamnsteel.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class SSVoxBoxMessage implements IMessage{



    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class SSVoxBoxMessageHandler implements IMessageHandler<SSVoxBoxMessage, IMessage>{
        @Override
        public IMessage onMessage(SSVoxBoxMessage message, MessageContext ctx) {
            return null;
        }
    }
}
