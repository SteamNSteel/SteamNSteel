package mod.steamnsteel.utility.blockParts;

import com.google.common.base.Objects;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

/**
 * Created by steblo on 31/10/2014.
 */
public class BlockPart
{

    private final String name;
    private final PartSet partSet;
    private AxisAlignedBB boundingBox;
    public final int index;
    private boolean enabledByDefault;
    private Object key;

    public BlockPart(String name, PartSet partSet, int index)
    {
        this.name = name;
        this.partSet = partSet;
        this.index = index;
    }

    public AxisAlignedBB getBoundingBox() { return boundingBox; }

    public void renderBoundingBox(EntityPlayer player, TileEntity tileEntity, float partialTicks)
    {
        double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.4f);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        renderBoundingBox(tileEntity, playerX, playerY, playerZ);

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void renderBoundingBox(TileEntity location, double playerX, double playerY, double playerZ)
    {
        float e = 0.01f;
        AxisAlignedBB drawBoundingBox = AxisAlignedBB.getBoundingBox(
                location.xCoord + boundingBox.minX, location.yCoord + boundingBox.minY, location.zCoord + boundingBox.minZ,
                location.xCoord + boundingBox.maxX, location.yCoord + boundingBox.maxY, location.zCoord + boundingBox.maxZ
        );
        RenderGlobal.drawOutlinedBoundingBox(drawBoundingBox.expand(e, e, e).getOffsetBoundingBox(-playerX, -playerY, -playerZ), -1);
    }

    public boolean isEnabledByDefault()
    {
        return enabledByDefault;
    }

    public Object getKey()
    {
        return key;
    }

    public BlockPart setEnabledByDefault()
    {
        this.enabledByDefault = true;
        return this;
    }

    public BlockPart setKey(Object key)
    {
        this.key = key;
        return this;
    }

    public BlockPart setBoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
    {
        this.boundingBox = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        return this;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("name", this.name)
                .add("index", this.index)
                .add("key", this.key)
                .add("enabledByDefault", this.enabledByDefault)
                .toString();

    }
}