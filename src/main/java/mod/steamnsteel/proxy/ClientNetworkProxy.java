package mod.steamnsteel.proxy;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.networking.ProjectTableCraftPacket;
import mod.steamnsteel.networking.ProjectTableCraftPacketMessageHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.core.config.Order;

/**
 * Created by codew on 17/01/2016.
 */
public class ClientNetworkProxy extends CommonNetworkProxy
{
    @Override
    public void init() {
        super.init();
        getNetwork().registerMessage(ProjectTableCraftPacketMessageHandler.class, ProjectTableCraftPacket.class, 0, Side.CLIENT);
    }

}
