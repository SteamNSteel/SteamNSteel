package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.container.PlotoniumChest;
import mod.steamnsteel.tileentity.PlotoniumChestTE;
import mod.steamnsteel.utility.Orientation;
import net.minecraft.client.model.ModelChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class PlotoniumChestTESR extends SteamNSteelTESR
{
    public static final ResourceLocation TEXTURE = getResourceLocation(PlotoniumChest.NAME);
    public static final ModelChest vanillaChest = new ModelChest();

    private static float getAngleFromOrientation(Orientation orientation)
    {
        switch (orientation)
        {
            case SOUTH:
                return 180.0f;
            case WEST:
                return 90.0f;
            case NORTH:
                return 0.0f;
            default:
                return 270.0f;
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        if (tileEntity instanceof PlotoniumChestTE)
        {
            final PlotoniumChestTE te = (PlotoniumChestTE) tileEntity;

            // Open Render buffer
            GL11.glPushMatrix();

            // Position Renderer
            GL11.glTranslatef((float) x, (float) y+1F, (float) z+1F);

            renderPlotoniumChest(te, tick);

            // Close Render Buffer
            GL11.glPopMatrix();
        }
    }

    private void renderPlotoniumChest(PlotoniumChestTE te, float tick)
    {
        final int x = te.xCoord;
        final int y = te.yCoord;
        final int z = te.zCoord;
        final World world = te.getWorldObj();

        GL11.glPushMatrix();

        // Position Renderer
        this.bindTexture(TEXTURE);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(1.0F, -1.0F, -1.0F);  //flip & rotate
        GL11.glTranslatef(0.5F, 0.5F, 0.5F); //translate block pos around fromBLK ORG

        final int metadata = world.getBlockMetadata(x, y, z);
        final Orientation orientation = Orientation.getdecodedOrientation(metadata);
        GL11.glRotatef(getAngleFromOrientation(orientation), 0.0F, -1.0F, 0.0F);

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F); //translate BLK ORG to block pos

        //lid angle.
        float adjLDAngle = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * tick;
        adjLDAngle = 1.0F - adjLDAngle;
        adjLDAngle = 1.0F - adjLDAngle * adjLDAngle * adjLDAngle;
        vanillaChest.chestLid.rotateAngleX = -(adjLDAngle * (float) Math.PI / 2.0F);

        vanillaChest.renderAll();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
