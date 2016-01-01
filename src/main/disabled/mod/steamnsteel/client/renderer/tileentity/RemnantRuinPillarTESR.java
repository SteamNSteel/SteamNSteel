package mod.steamnsteel.client.renderer.tileentity;

import mod.steamnsteel.block.resource.structure.RemnantRuinPillarBlock;
import mod.steamnsteel.client.renderer.model.RemnantRuinPillarModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
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
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float tick, int whatDoesThisDo)
    {
        BlockPos pos = new BlockPos(posX, posY, posZ);
        World world = tileEntity.getWorld();
        final IBlockState blockState = world.getBlockState(pos);
        final int blockMeta = blockState.getBlock().getMetaFromState(blockState);
        Block block = blockState.getBlock();

        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        // Open Render buffer
        GL11.glPushMatrix();

        // Orient the model to match the placement
        //final IBlockState metadata = world.getBlockMetadata(pos);
        final Orientation orientation = Orientation.getdecodedOrientation(blockState);
        final float angleFromOrientation = getAngleFromOrientation(orientation);

        // Inherent adjustments to model
        GL11.glScalef(SCALE.left, SCALE.middle, SCALE.right);
        GL11.glTranslatef(OFFSET.left, OFFSET.middle, OFFSET.right);
        GL11.glTranslated(posX, posY, posZ);
        GL11.glRotatef(angleFromOrientation, 0.0F, 1.0F, 0.0F);

        model.renderPillar();

        if (posY > 0)
        {
            final IBlockState blockStateBeneath = world.getBlockState(pos.down());
            final int blockMetaBeneath = blockStateBeneath.getBlock().getMetaFromState(blockStateBeneath);
            if ((blockStateBeneath.getBlock() != block || blockMetaBeneath != blockMeta))
            {
                model.renderBottomCap();
            }
        }

        if (posY < world.getHeight() - 2)
        {
            final IBlockState blockStateAbove = world.getBlockState(pos.up());
            final int blockMetaAbove = blockStateAbove.getBlock().getMetaFromState(blockStateAbove);
            if ((blockStateAbove.getBlock() != block || blockMetaAbove != blockMeta))
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
