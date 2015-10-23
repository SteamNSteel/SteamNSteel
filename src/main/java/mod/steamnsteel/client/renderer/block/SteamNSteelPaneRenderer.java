package mod.steamnsteel.client.renderer.block;

import mod.steamnsteel.block.resource.structure.RemnantRuinIronBarsBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class SteamNSteelPaneRenderer implements ISimpleBlockRenderingHandler
{
    private final int id;

    public static SteamNSteelPaneRenderer INSTANCE = new SteamNSteelPaneRenderer();

    private SteamNSteelPaneRenderer()
    {
        this.id = RenderingRegistry.getNextAvailableRenderId();

    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, BlockPos pos, Block block, int modelId, RenderBlocks renderer)
    {
        return renderBlockPane((RemnantRuinIronBarsBlock)block, pos, renderer);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return id;
    }

    public boolean renderBlockPane(RemnantRuinIronBarsBlock block, BlockPos pos, RenderBlocks renderer)
    {
        int worldHeight = renderer.blockAccess.getHeight();
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, pos));
        int colourMultiplier = block.colorMultiplier(renderer.blockAccess, pos);
        float red = (float)(colourMultiplier >> 16 & 255) / 255.0F;
        float green = (float)(colourMultiplier >> 8 & 255) / 255.0F;
        float blue = (float)(colourMultiplier & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float adjustedRed = (red * 30.0F + green * 59.0F + blue * 11.0F) / 100.0F;
            float adjustedGreen = (red * 30.0F + green * 70.0F) / 100.0F;
            float adjustedBlue = (red * 30.0F + blue * 70.0F) / 100.0F;
            red = adjustedRed;
            green = adjustedGreen;
            blue = adjustedBlue;
        }

        tessellator.setColorOpaque_F(red, green, blue);
        IIcon sideIcon;
        IIcon topBottomIcon;

        if (renderer.hasOverrideBlockTexture())
        {
            sideIcon = renderer.overrideBlockTexture;
            topBottomIcon = renderer.overrideBlockTexture;
        }
        else
        {
            int metadata = renderer.blockAccess.getBlockMetadata(pos);
            sideIcon = renderer.getBlockIconFromSideAndMetadata(block, 0, metadata);
            topBottomIcon = block.getTopAndBottomIcon(metadata);
        }

        double sideTexUMin = (double)sideIcon.getMinU();
        double sideTexUMid = (double)sideIcon.getInterpolatedU(8.0D);
        double sideTexUMax = (double)sideIcon.getMaxU();
        double sideTexVMin = (double)sideIcon.getMinV();
        double sideTexVMax = (double)sideIcon.getMaxV();
        double topBottomTexUMidMinus1 = (double)topBottomIcon.getInterpolatedU(7.0D);
        double topBottomTexUMidPlus1 = (double)topBottomIcon.getInterpolatedU(9.0D);
        double topBottomTexVMin = (double)topBottomIcon.getMinV();
        double topBottomTexVMid = (double)topBottomIcon.getInterpolatedV(8.0D);
        double topBottomTexVMax = (double)topBottomIcon.getMaxV();
        double blockXMin = (double)x;
        double blockXMid = (double)x + 0.5D;
        double blockXMax = (double)(x + 1);
        double blockZMin = (double)z;
        double blockZMid = (double)z + 0.5D;
        double blockZMax = (double)(z + 1);
        double blockXMidMinus1px = (double)x + 0.5D - 0.0625D;
        double blockXMidPlus1px = (double)x + 0.5D + 0.0625D;
        double blockZMidMinus1px = (double)z + 0.5D - 0.0625D;
        double blockZMidPlus1px = (double)z + 0.5D + 0.0625D;
        boolean shouldConnectZMinus  = block.canPaneConnectTo(renderer.blockAccess, pos - 1, NORTH);
        boolean shouldConnectZPlus = block.canPaneConnectTo(renderer.blockAccess, pos + 1, SOUTH);
        boolean shouldConnectXMinus = block.canPaneConnectTo(renderer.blockAccess, x - 1, y, z, WEST);
        boolean shouldConnectXPlus = block.canPaneConnectTo(renderer.blockAccess, x + 1, y, z, EAST);
        boolean shouldConnectAbove = block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1);
        boolean shouldConnectBelow = block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0);
        double offset = 0.01D;
        double halfOffset = 0.005D;

        if ((!shouldConnectXMinus || !shouldConnectXPlus) && (shouldConnectXMinus || shouldConnectXPlus || shouldConnectZMinus || shouldConnectZPlus))
        {
            if (shouldConnectXMinus && !shouldConnectXPlus)
            {
                tessellator.addVertexWithUV(blockXMin, (double)(y + 1), blockZMid, sideTexUMin, sideTexVMin);
                tessellator.addVertexWithUV(blockXMin, (double)(y + 0), blockZMid, sideTexUMin, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMid, sideTexUMid, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMid, sideTexUMid, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMid, sideTexUMin, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMid, sideTexUMin, sideTexVMax);
                tessellator.addVertexWithUV(blockXMin, (double)(y + 0), blockZMid, sideTexUMid, sideTexVMax);
                tessellator.addVertexWithUV(blockXMin, (double)(y + 1), blockZMid, sideTexUMid, sideTexVMin);

                if (!shouldConnectZPlus && !shouldConnectZMinus)
                {
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMidPlus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMidPlus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMidMinus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMidMinus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                }

                if (shouldConnectAbove || y < worldHeight - 1 && renderer.blockAccess.isAirBlock(x - 1, y + 1, z))
                {
                    tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                }

                if (shouldConnectBelow || y > 1 && renderer.blockAccess.isAirBlock(x - 1, y - 1, z))
                {
                    tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                }
            }
            else if (!shouldConnectXMinus && shouldConnectXPlus)
            {
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMid, sideTexUMid, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMid, sideTexUMid, sideTexVMax);
                tessellator.addVertexWithUV(blockXMax, (double)(y + 0), blockZMid, sideTexUMax, sideTexVMax);
                tessellator.addVertexWithUV(blockXMax, (double)(y + 1), blockZMid, sideTexUMax, sideTexVMin);
                tessellator.addVertexWithUV(blockXMax, (double)(y + 1), blockZMid, sideTexUMid, sideTexVMin);
                tessellator.addVertexWithUV(blockXMax, (double)(y + 0), blockZMid, sideTexUMid, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMid, sideTexUMax, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMid, sideTexUMax, sideTexVMin);

                if (!shouldConnectZPlus && !shouldConnectZMinus)
                {
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMidPlus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMidPlus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMidMinus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMidMinus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                }

                if (shouldConnectAbove || y < worldHeight - 1 && renderer.blockAccess.isAirBlock(x + 1, y + 1, z))
                {
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                }

                if (shouldConnectBelow || y > 1 && renderer.blockAccess.isAirBlock(x + 1, y - 1, z))
                {
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(blockXMin, (double)(y + 1), blockZMid, sideTexUMin, sideTexVMin);
            tessellator.addVertexWithUV(blockXMin, (double)(y + 0), blockZMid, sideTexUMin, sideTexVMax);
            tessellator.addVertexWithUV(blockXMax, (double)(y + 0), blockZMid, sideTexUMax, sideTexVMax);
            tessellator.addVertexWithUV(blockXMax, (double)(y + 1), blockZMid, sideTexUMax, sideTexVMin);
            tessellator.addVertexWithUV(blockXMax, (double)(y + 1), blockZMid, sideTexUMin, sideTexVMin);
            tessellator.addVertexWithUV(blockXMax, (double)(y + 0), blockZMid, sideTexUMin, sideTexVMax);
            tessellator.addVertexWithUV(blockXMin, (double)(y + 0), blockZMid, sideTexUMax, sideTexVMax);
            tessellator.addVertexWithUV(blockXMin, (double)(y + 1), blockZMid, sideTexUMax, sideTexVMin);

            if (shouldConnectAbove)
            {
                tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
            }
            else
            {
                if (y < worldHeight - 1 && renderer.blockAccess.isAirBlock(x - 1, y + 1, z))
                {
                    tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMin, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                }

                if (y < worldHeight - 1 && renderer.blockAccess.isAirBlock(x + 1, y + 1, z))
                {
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMax, (double)(y + 1) + offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                }
            }

            if (shouldConnectBelow)
            {
                tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
            }
            else
            {
                if (y > 1 && renderer.blockAccess.isAirBlock(x - 1, y - 1, z))
                {
                    tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMin, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                }

                if (y > 1 && renderer.blockAccess.isAirBlock(x + 1, y - 1, z))
                {
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidPlus1px, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMid, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMax, (double)y - offset, blockZMidMinus1px, topBottomTexUMidMinus1, topBottomTexVMin);
                }
            }
        }

        if ((!shouldConnectZMinus || !shouldConnectZPlus) && (shouldConnectXMinus || shouldConnectXPlus || shouldConnectZMinus || shouldConnectZPlus))
        {
            if (shouldConnectZMinus && !shouldConnectZPlus)
            {
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMin, sideTexUMin, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMin, sideTexUMin, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMid, sideTexUMid, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMid, sideTexUMid, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMid, sideTexUMin, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMid, sideTexUMin, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMin, sideTexUMid, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMin, sideTexUMid, sideTexVMin);

                if (!shouldConnectXPlus && !shouldConnectXMinus)
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1), blockZMid, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 0), blockZMid, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 0), blockZMid, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1), blockZMid, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1), blockZMid, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 0), blockZMid, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 0), blockZMid, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1), blockZMid, topBottomTexUMidPlus1, topBottomTexVMin);
                }

                if (shouldConnectAbove || y < worldHeight - 1 && renderer.blockAccess.isAirBlock(x, y + 1, z - 1))
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMin);
                }

                if (shouldConnectBelow || y > 1 && renderer.blockAccess.isAirBlock(x, y - 1, z - 1))
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMin);
                }
            }
            else if (!shouldConnectZMinus && shouldConnectZPlus)
            {
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMid, sideTexUMid, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMid, sideTexUMid, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMax, sideTexUMax, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMax, sideTexUMax, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMax, sideTexUMid, sideTexVMin);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMax, sideTexUMid, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMid, sideTexUMax, sideTexVMax);
                tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMid, sideTexUMax, sideTexVMin);

                if (!shouldConnectXPlus && !shouldConnectXMinus)
                {
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1), blockZMid, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 0), blockZMid, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 0), blockZMid, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1), blockZMid, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1), blockZMid, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 0), blockZMid, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 0), blockZMid, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1), blockZMid, topBottomTexUMidPlus1, topBottomTexVMin);
                }

                if (shouldConnectAbove || y < worldHeight - 1 && renderer.blockAccess.isAirBlock(x, y + 1, z + 1))
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMid);
                }

                if (shouldConnectBelow || y > 1 && renderer.blockAccess.isAirBlock(x, y - 1, z + 1))
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMid);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMax, sideTexUMin, sideTexVMin);
            tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMax, sideTexUMin, sideTexVMax);
            tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMin, sideTexUMax, sideTexVMax);
            tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMin, sideTexUMax, sideTexVMin);
            tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMin, sideTexUMin, sideTexVMin);
            tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMin, sideTexUMin, sideTexVMax);
            tessellator.addVertexWithUV(blockXMid, (double)(y + 0), blockZMax, sideTexUMax, sideTexVMax);
            tessellator.addVertexWithUV(blockXMid, (double)(y + 1), blockZMax, sideTexUMax, sideTexVMin);

            if (shouldConnectAbove)
            {
                tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMax);
            }
            else
            {
                if (y < worldHeight - 1 && renderer.blockAccess.isAirBlock(x, y + 1, z - 1))
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMin);
                }

                if (y < worldHeight - 1 && renderer.blockAccess.isAirBlock(x, y + 1, z + 1))
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)(y + 1) + halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMid);
                }
            }

            if (shouldConnectBelow)
            {
                tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMax);
                tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMin);
                tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMax);
            }
            else
            {
                if (y > 1 && renderer.blockAccess.isAirBlock(x, y - 1, z - 1))
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMin);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMin, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMin);
                }

                if (y > 1 && renderer.blockAccess.isAirBlock(x, y - 1, z + 1))
                {
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidMinus1, topBottomTexVMid);
                    tessellator.addVertexWithUV(blockXMidMinus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidMinus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMid, topBottomTexUMidPlus1, topBottomTexVMax);
                    tessellator.addVertexWithUV(blockXMidPlus1px, (double)y - halfOffset, blockZMax, topBottomTexUMidPlus1, topBottomTexVMid);
                }
            }
        }

        return true;
    }
}
