package mod.steamnsteel.utility.blockParts;

import mod.steamnsteel.utility.world.WorldRaytraceIterator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class BlockHighlightEventListener
{

    private BlockHighlightEventListener() {}

    private static BlockHighlightEventListener ourInstance = new BlockHighlightEventListener();

    public static BlockHighlightEventListener getInstance()
    {
        return ourInstance;
    }

    @SubscribeEvent
    public void onBlockHighlightEvent(DrawBlockHighlightEvent highlightEvent)
    {
        MovingObjectPosition target = highlightEvent.target;
        if (target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            final Minecraft minecraft = Minecraft.getMinecraft();
            EntityPlayer player = highlightEvent.player;
            double blockReachDistance = (double) minecraft.playerController.getBlockReachDistance();
            World world = highlightEvent.player.worldObj;

            final float partialTicks = highlightEvent.partialTicks;
            Vec3 vec3 = player.getPosition(partialTicks);
            Vec3 vec31 = player.getLook(partialTicks);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);

            WorldRaytraceIterator worldRaytraceIterator = new WorldRaytraceIterator(world, vec3, vec32);
            while (worldRaytraceIterator.hasNext())
            {
                MovingObjectPosition mop = worldRaytraceIterator.next();
                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    TileEntity tileEntity = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                    if (tileEntity instanceof ITileEntityWithParts)
                    {
                        ITileEntityWithParts tileEntityWithParts = (ITileEntityWithParts) tileEntity;

                        Vec3 targetBlockVec = Vec3.createVectorHelper(mop.blockX, mop.blockY, mop.blockZ);
                        Vec3 vec = targetBlockVec.subtract(mop.hitVec);
                        Vec3 lookVec = player.getLookVec();

                        BlockPartConfiguration partConfiguration = tileEntityWithParts.getBlockPartConfiguration();
                        Iterable<BlockPart> parts = partConfiguration.getBlockPartsIntersecting(tileEntity, vec, lookVec);
                        for (BlockPart part : parts)
                        {
                            part.renderBoundingBox(highlightEvent.player, tileEntity, highlightEvent.partialTicks);
                        }
                        highlightEvent.setCanceled(true);
                        break;
                    }
                }
            }
        }
    }

    public void drawSelectionBox(World world, EntityPlayer p_72731_1_, MovingObjectPosition p_72731_2_, int p_72731_3_, float p_72731_4_)
    {
        if (p_72731_3_ == 0 && p_72731_2_.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            float f1 = 0.002F;
            Block block = world.getBlock(p_72731_2_.blockX, p_72731_2_.blockY, p_72731_2_.blockZ);

            //if (block.getMaterial() != Material.air)
            //{
            block.setBlockBoundsBasedOnState(world, p_72731_2_.blockX, p_72731_2_.blockY, p_72731_2_.blockZ);
            double d0 = p_72731_1_.lastTickPosX + (p_72731_1_.posX - p_72731_1_.lastTickPosX) * (double) p_72731_4_;
            double d1 = p_72731_1_.lastTickPosY + (p_72731_1_.posY - p_72731_1_.lastTickPosY) * (double) p_72731_4_;
            double d2 = p_72731_1_.lastTickPosZ + (p_72731_1_.posZ - p_72731_1_.lastTickPosZ) * (double) p_72731_4_;
            RenderGlobal.drawOutlinedBoundingBox(block.getSelectedBoundingBoxFromPool(world, p_72731_2_.blockX, p_72731_2_.blockY, p_72731_2_.blockZ).expand((double) f1, (double) f1, (double) f1).getOffsetBoundingBox(-d0, -d1, -d2), -1);
            //}

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }
}
