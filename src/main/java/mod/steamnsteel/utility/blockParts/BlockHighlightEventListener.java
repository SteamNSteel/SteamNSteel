package mod.steamnsteel.utility.blockParts;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

/**
 * Created by steblo on 31/10/2014.
 */
public class BlockHighlightEventListener {

    private BlockHighlightEventListener() {}

    private static BlockHighlightEventListener ourInstance = new BlockHighlightEventListener();
    public static BlockHighlightEventListener getInstance() {
        return ourInstance;
    }

    @SubscribeEvent
    public void onBlockHighlightEvent(DrawBlockHighlightEvent highlightEvent) {
        MovingObjectPosition target = highlightEvent.target;
        if (target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            EntityPlayer player = highlightEvent.player;
            World world = highlightEvent.player.worldObj;
            TileEntity tileEntity = world.getTileEntity(target.blockX, target.blockY, target.blockZ);
            if (tileEntity instanceof ITileEntityWithParts) {
                ITileEntityWithParts tileEntityWithParts = (ITileEntityWithParts)tileEntity;
                Vec3 targetBlockVec = Vec3.createVectorHelper(target.blockX, target.blockY, target.blockZ);
                Vec3 vec = targetBlockVec.subtract(target.hitVec);
                Vec3 lookVec = player.getLookVec();

                //Should this be taking into account the player???

                BlockPartConfiguration partConfiguration = tileEntityWithParts.getBlockPartConfiguration();
                Iterable<BlockPart> parts = partConfiguration.getBlockPartsIntersecting(tileEntity, vec, lookVec);
                for(BlockPart part : parts) {
                    part.renderBoundingBox(highlightEvent.player, tileEntity, highlightEvent.partialTicks);
                }
                highlightEvent.setCanceled(true);
            }
        }
    }
}
