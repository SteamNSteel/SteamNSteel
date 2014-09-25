package mod.steamnsteel.texturing;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.resource.structure.PlotoniumRuinWall;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class RuinWallTexture extends ProceduralConnectedTexture
{
    final int FEATURE_CROWN = 1<<8;
    final int FEATURE_BASE = 1<<9;
    final int FEATURE_PLATE = 1<<10;
    final int FEATURE_PIPES = 1<<11;

    final int FEATURE_MASK = FEATURE_PLATE | FEATURE_PIPES;


    @Override
    protected HashMap<Integer, IRuinWallFeature> getFeatures()
    {
        HashMap<Integer, IRuinWallFeature> features = new HashMap<Integer, IRuinWallFeature>();
        features.put(FEATURE_PLATE, new PlateRuinWallFeature());
        features.put(FEATURE_PIPES, new PipesRuinWallFeature());
        return features;
    }

    @Override
    public void registerIconsInternal(IIconRegister iconRegister)
    {

        addIcon(DEFAULT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
        addIcon(LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
        addIcon(RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
        addIcon(LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));

        addIcon(FEATURE_PIPES | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        addIcon(FEATURE_PIPES | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));
        addIcon(LEFT | FEATURE_PIPES | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        addIcon(LEFT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));
        addIcon(RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        addIcon(RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));
        addIcon(LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        addIcon(LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));

        addIcon(FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));

        addIcon(FEATURE_PLATE | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVL"));
        addIcon(FEATURE_PLATE | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVR"));
        addIcon(FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHU"));
        addIcon(FEATURE_PLATE | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHD"));

        addIcon(FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRDC"));
        addIcon(FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRUC"));
        addIcon(FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLDC"));
        addIcon(FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLUC"));

        addIcon(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLDC"));
        addIcon(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLUC"));
        addIcon(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVL"));

        addIcon(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRDC"));
        addIcon(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRUC"));
        addIcon(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVR"));

        addIcon(FEATURE_CROWN | TOP | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL"));
        addIcon(FEATURE_CROWN | TOP | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR"));
        addIcon(FEATURE_CROWN | TOP | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_AllEdge"));
        addIcon(FEATURE_CROWN | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_Center"));
        addIcon(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_Center"));
        addIcon(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_DDPanelCL"));
        addIcon(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_DDPanelCR"));
        addIcon(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR"));
        addIcon(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL"));
        addIcon(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR"));
        addIcon(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL"));
        ////////
        addIcon(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR_DDPanelCR_S"));
        addIcon(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR_DDPanelCL_S"));
        addIcon(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL_DDPanelCL_S"));
        addIcon(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL_DDPanelCR_S"));
        addIcon(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_AllEdge_DDPanelCR_S"));
        addIcon(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_AllEdge_DDPanelCL_S"));

        addIcon(FEATURE_CROWN | TOP | FEATURE_PIPES | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_Center_DD1_PipeA_S"));
        addIcon(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_AllEdge_DD1_PipeA_S"));
        addIcon(FEATURE_CROWN | TOP | LEFT | FEATURE_PIPES | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeL_DD1_PipeA_S"));
        addIcon(FEATURE_CROWN | TOP | RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeR_DD1_PipeA_S"));

        addIcon(FEATURE_BASE | BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeL"));
        addIcon(FEATURE_BASE | BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeR"));
        addIcon(FEATURE_BASE | BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_AllEdge"));
        addIcon(FEATURE_BASE | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_Center"));
        addIcon(FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeR"));
        addIcon(FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeL"));
        addIcon(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_Center"));
        addIcon(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_DDPanelCL"));
        addIcon(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_DDPanelCR"));

        ///Check?
        addIcon(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHD"));
        addIcon(FEATURE_CROWN | TOP | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHU"));
        ///////////////
        addIcon(FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeL_DDPanelCL_S"));
        addIcon(FEATURE_BASE | BOTTOM | LEFT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeL_DDPanelCR_S"));
        addIcon(FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeR_DDPanelCR_S"));
        addIcon(FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeR_DDPanelCL_S"));
        addIcon(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_AllEdge_DDPanelCR_S"));
        addIcon(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PLATE | FEATURE_EDGE_BOTTOM | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_AllEdge_DDPanelCL_S"));

        addIcon(FEATURE_BASE | BOTTOM | FEATURE_PIPES | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_Center_DD1_PipeB_S"));
        addIcon(FEATURE_BASE | BOTTOM | RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeR_DD1_PipeB_S"));
        addIcon(FEATURE_BASE | BOTTOM | LEFT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeL_DD1_PipeB_S"));
        addIcon(FEATURE_BASE | BOTTOM | LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_AllEdge_DD1_PipeB_S"));

        //Remove these later once the default is the default block
        addIcon(TOP | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));

    }

    protected int getTexturePropertiesForSide(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int side)
    {
        try
        {
            int blockProperties = 0;
            ForgeDirection orientation = ForgeDirection.getOrientation(side);
            if (orientation == ForgeDirection.UP || orientation == ForgeDirection.DOWN)
            {
                return DEFAULT;
            }
            /*ForgeDirection[] rotationMatrix = ForgeDirectionRotation.forSide(side);
            ForgeDirection left = rotationMatrix[ROTATION_INDEX_LEFT];*/
            ForgeDirection left = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.LEFT, orientation);
            ForgeDirection right = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.RIGHT, orientation);
            ForgeDirection back = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BACK, orientation);
            ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);
            ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);

            boolean leftIsRuinWallAndNotObscured = checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left), back);
            boolean rightIsRuinWallAndNotObscured = checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right), back);
            boolean aboveIsRuinWallAndNotObscured = checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above), back);
            boolean belowIsRuinWallAndNotObscured = checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below), back);

            if (!aboveIsRuinWallAndNotObscured) {
                blockProperties |= TOP;
            }
            if (!belowIsRuinWallAndNotObscured) {
                blockProperties |= BOTTOM;
            }
            if (!leftIsRuinWallAndNotObscured) {
                blockProperties |= LEFT;
            }
            if (!rightIsRuinWallAndNotObscured) {
                blockProperties |= RIGHT;
            }

            int featureId = getValidFeature(blockAccess, worldBlockCoord, orientation);
            if (featureId != NO_FEATURE)
            {
                int subProperties = getSubProperties(blockAccess, worldBlockCoord, orientation, featureId);

                if ((subProperties & FEATURE_MASK) != 0)
                {
                    blockProperties |= subProperties;
                }
            }

            if ((blockProperties & BOTTOM) == BOTTOM) {
                blockProperties |= FEATURE_BASE;
                blockProperties &= (-1 ^ TOP); //Force the TOP bit off Not sure if I should do this...
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left).offset(below), back)) {
                    blockProperties |= LEFT;
                }
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right).offset(below), back)) {
                    blockProperties |= RIGHT;
                }
            } else if ((blockProperties & TOP) == TOP ) {
                blockProperties |= FEATURE_CROWN;
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left).offset(above), back)) {
                    blockProperties |= LEFT;
                }
                if (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right).offset(above), back)) {
                    blockProperties |= RIGHT;
                }
            }

            return blockProperties;
        } catch (Exception e)
        {
            e.printStackTrace();
            return MISSING_TEXTURE;
        }
    }

    private int getSubProperties(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId)
    {
        ForgeDirection left = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.LEFT, orientation);
        ForgeDirection right = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.RIGHT, orientation);
        ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);
        ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);

        int subProperties = featureId;
        int leftBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(left), orientation);
        int rightBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(right), orientation);
        int aboveBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(above), orientation);
        int belowBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(below), orientation);

        if (leftBlockFeature != featureId) {
            subProperties |= FEATURE_EDGE_LEFT;
        }
        if (rightBlockFeature != featureId) {
            subProperties |= FEATURE_EDGE_RIGHT;
        }
        if (aboveBlockFeature != featureId) {
            subProperties |= FEATURE_EDGE_TOP;
        }
        if (belowBlockFeature != featureId) {
            subProperties |= FEATURE_EDGE_BOTTOM;
        }

        final int FEATURE_EDGE_TOP_AND_BOTTOM = FEATURE_EDGE_TOP | FEATURE_EDGE_BOTTOM;
        final int FEATURE_EDGE_LEFT_AND_RIGHT = FEATURE_EDGE_LEFT | FEATURE_EDGE_RIGHT;

        switch (featureId) {
            case FEATURE_PIPES:
                //Pipes are only a single block wide and must ignore LEFT | RIGHT edges
                subProperties &= featureId | FEATURE_EDGE_TOP_AND_BOTTOM;
                break;
            case FEATURE_PLATE:
                //Plates cannot be a single block wide.
                if ((subProperties & FEATURE_EDGE_TOP_AND_BOTTOM) == FEATURE_EDGE_TOP_AND_BOTTOM) {
                    subProperties = 0;
                } else if ((subProperties & FEATURE_EDGE_LEFT_AND_RIGHT) == FEATURE_EDGE_LEFT_AND_RIGHT) {
                    subProperties = 0;
                }
                break;
        }
        return subProperties;
    }

    private boolean checkRuinWallAndNotObscured(IBlockAccess blockAccess, WorldBlockCoord startingBlock, ForgeDirection back)
    {
        if (!(startingBlock.getBlock(blockAccess) instanceof PlotoniumRuinWall)) {
            return false;
        }
        if (startingBlock.offset(back).getBlock(blockAccess).getMaterial().isOpaque()) {
            return false;
        }
        return true;
    }


    protected String describeTextureProperties(int blockProperties)
    {
        StringBuilder sb = new StringBuilder();
        if ((blockProperties & FEATURE_PLATE) == FEATURE_PLATE)
        {
            sb.append("Plate,");
        }
        if ((blockProperties & FEATURE_CROWN) == FEATURE_CROWN)
        {
            sb.append("Crown,");
        }
        if ((blockProperties & FEATURE_BASE) == FEATURE_BASE)
        {
            sb.append("Base,");
        }
        if ((blockProperties & FEATURE_PIPES) == FEATURE_PIPES)
        {
            sb.append("Pipes,");
        }

        if ((blockProperties & TOP) == TOP)
        {
            sb.append("T,");
        }
        if ((blockProperties & BOTTOM) == BOTTOM)
        {
            sb.append("B,");
        }
        if ((blockProperties & LEFT) == LEFT)
        {
            sb.append("L,");
        }
        if ((blockProperties & RIGHT) == RIGHT)
        {
            sb.append("R,");
        }

        if ((blockProperties & FEATURE_EDGE_TOP) == FEATURE_EDGE_TOP)
        {
            sb.append("FE_T,");
        }
        if ((blockProperties & FEATURE_EDGE_BOTTOM) == FEATURE_EDGE_BOTTOM)
        {
            sb.append("FE_B,");
        }
        if ((blockProperties & FEATURE_EDGE_LEFT) == FEATURE_EDGE_LEFT)
        {
            sb.append("FE_L,");
        }
        if ((blockProperties & FEATURE_EDGE_RIGHT) == FEATURE_EDGE_RIGHT)
        {
            sb.append("FE_R,");
        }

        return sb.toString();
    }

    private class PipesRuinWallFeature implements IRuinWallFeature {

        @Override
        public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId)
        {
            ForgeDirection back = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BACK, orientation);

            if (!checkRuinWallAndNotObscured(blockAccess, worldBlockCoord, back)) {
                return false;
            }

            ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);
            ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);

            boolean aboveValid = (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above), back) && getFeatureAt(worldBlockCoord.offset(above)) == featureId);
            if (aboveValid) {
                return true;
            }
            boolean belowValid = (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below), back) && getFeatureAt(worldBlockCoord.offset(below)) == featureId);

            if (belowValid) {
                return true;
            }

            return false;
        }

        @Override
        public Collection<Feature> getFeatureAreasFor(ChunkCoord chunkCoord)
        {
            Random random = new Random(Objects.hash(chunkCoord, FEATURE_PIPES));

            final int featureCount = 64;

            List<Feature> features = new ArrayList<Feature>(featureCount);
            //Generate Pipe features
            for (int i = 0; i < featureCount; ++i)
            {
                int xPos = random.nextInt(18) - 1;
                int yPos = random.nextInt(15);
                int zPos = random.nextInt(18) - 1;

                features.add(new Feature(FEATURE_PIPES, WorldBlockCoord.of(xPos, yPos, zPos), 1, 2, 1));
            }
            return features;
        }
    }

    private class PlateRuinWallFeature implements IRuinWallFeature {
        public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId)
        {
            ForgeDirection back = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BACK, orientation);

            if (!checkRuinWallAndNotObscured(blockAccess, worldBlockCoord, back)) {
                return false;
            }

            ForgeDirection left = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.LEFT, orientation);
            ForgeDirection right = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.RIGHT, orientation);
            ForgeDirection below = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.BELOW, orientation);
            ForgeDirection above = BlockSideRotation.forOrientation(BlockSideRotation.TextureDirection.ABOVE, orientation);

            //check Left
            boolean leftValid = (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(left), back) && getFeatureAt(worldBlockCoord.offset(left)) == featureId);
            boolean rightValid = (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(right), back) && getFeatureAt(worldBlockCoord.offset(right)) == featureId);
            if (!leftValid && !rightValid) {
                return false;
            }
            boolean aboveValid = (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above), back) && getFeatureAt(worldBlockCoord.offset(above)) == featureId);
            boolean belowValid = (checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below), back) && getFeatureAt(worldBlockCoord.offset(below)) == featureId);

            if (!aboveValid && !belowValid) {
                return false;
            }

            //check for a cluster of 4 - Automatically valid
            //check above and left
            if (aboveValid && leftValid && checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above).offset(left), back) && getFeatureAt(worldBlockCoord.offset(above).offset(left)) == featureId) {
                return true;
            }
            //check above and right
            if (aboveValid && rightValid && checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(above).offset(right), back) && getFeatureAt(worldBlockCoord.offset(above).offset(right)) == featureId) {
                return true;
            }
            //check below and left
            if (belowValid && leftValid && checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below).offset(left), back) && getFeatureAt(worldBlockCoord.offset(below).offset(left)) == featureId) {
                return true;
            }
            //check below and right
            if (belowValid && rightValid && checkRuinWallAndNotObscured(blockAccess, worldBlockCoord.offset(below).offset(right), back) && getFeatureAt(worldBlockCoord.offset(below).offset(right)) == featureId) {
                return true;
            }

            //We have encountered an S or Z shape;
            return false;
        }

        @Override
        public Collection<Feature> getFeatureAreasFor(ChunkCoord chunkCoord)
        {
            final int featureCount = 64;

            Random random = new Random(Objects.hash(chunkCoord, FEATURE_PIPES));

            List<Feature> features = new ArrayList<Feature>(featureCount);

            //Generate plate features
            for (int i = 0; i < featureCount; ++i)
            {
                int xPos = random.nextInt(18) - 1;
                int yPos = random.nextInt(16);
                int zPos = random.nextInt(18) - 1;

                int width = random.nextInt(7) + 1;
                int height = random.nextInt(7) + 1;
                int depth = random.nextInt(7) + 1;

                features.add(new Feature(FEATURE_PLATE, WorldBlockCoord.of(xPos, yPos, zPos), width, height, depth));
            }

            return features;
        }
    }


}
