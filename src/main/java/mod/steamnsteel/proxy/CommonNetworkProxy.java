package mod.steamnsteel.proxy;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.library.Reference;
import mod.steamnsteel.networking.ProjectTableCraftPacket;
import mod.steamnsteel.networking.ProjectTableCraftPacketMessageHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by codew on 17/01/2016.
 */
public class CommonNetworkProxy
{
    private SimpleNetworkWrapper network;

    public SimpleNetworkWrapper getNetwork()
    {
        return network;
    }

    public void init() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        network.registerMessage(ProjectTableCraftPacketMessageHandler.class, ProjectTableCraftPacket.class, 0, Side.SERVER);
    }
}
