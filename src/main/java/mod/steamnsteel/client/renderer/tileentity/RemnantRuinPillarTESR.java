package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.resource.structure.RemnantRuinPillarBlock;
import mod.steamnsteel.client.renderer.model.RemnantRuinPillarModel;
import mod.steamnsteel.utility.Orientation;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class RemnantRuinPillarTESR extends SteamNSteelTESR
{
    public static ResourceLocation TEXTURE = getResourceLocation(RemnantRuinPillarBlock.NAME);
    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1.0f, 1.0f, 1.0f);
    private static final ImmutableTriple<Float, Float, Float> OFFSET = ImmutableTriple.of(0.5f, 0.5f, 0.5f);
    public RemnantRuinPillarTESR() {}

    private final RemnantRuinPillarModel model = new RemnantRuinPillarModel();


    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double xPos, double yPos, double zPos, float p_147500_8_)
    {
        int x = tileEntity.xCoord;
        int y = tileEntity.yCoord;
        int z = tileEntity.zCoord;

        World world = tileEntity.getWorldObj();
        Block block = world.getBlock(x, y, z);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        // Open Render buffer
        GL11.glPushMatrix();

        // Orient the model to match the placement
        final int metadata = world.getBlockMetadata(x, y, z);
        final Orientation orientation = Orientation.getdecodedOrientation(metadata);
        final float angleFromOrientation = getAngleFromOrientation(orientation);

        // Inherent adjustments to model
        GL11.glScalef(SCALE.left, SCALE.middle, SCALE.right);
        GL11.glTranslatef(OFFSET.left, OFFSET.middle, OFFSET.right);
        GL11.glTranslated(xPos, yPos, zPos);
        GL11.glRotatef(angleFromOrientation, 0.0F, 1.0F, 0.0F);

        model.renderPillar();

        if (y > 0)
        {
            if ((world.getBlock(x, y - 1, z) != block || world.getBlockMetadata(x, y - 1, z) != metadata))
            {
                model.renderBottomCap();
            }
        }

        if (y < world.getHeight() - 2)
        {
            if ((world.getBlock(x, y + 1, z) != block || world.getBlockMetadata(x, y + 1, z) != metadata))
            {
                model.renderTopCap();
            }
        }
        GL11.glPopMatrix();

        return ;
    }

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

}
