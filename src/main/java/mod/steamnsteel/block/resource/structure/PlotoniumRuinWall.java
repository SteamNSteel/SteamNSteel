/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.block.resource.structure;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.*;

public class PlotoniumRuinWall extends SteamNSteelBlock
{
    public static final String NAME = "ruinWallPlotonium";
    public static final int ROTATION_INDEX_LEFT = 0;
    public static final int ROTATION_INDEX_RIGHT = 1;
    public static final int ROTATION_INDEX_BACK = 2;
    public static final int ROTATION_INDEX_ABOVE = 5;
    public static final int ROTATION_INDEX_BELOW = 4;

    private HashMap<Integer, IIcon> icons2 = new HashMap<Integer, IIcon>();
    private HashMap<Integer, IRuinWallFeature> features = new HashMap<Integer, IRuinWallFeature>();
    ForgeDirection[][] ROTATION_MATRIX = {
            {ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN},
            {ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN},
            {ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN}
    };

    final int DEFAULT = 0;
    final int TOP = 1 << 0;
    final int BOTTOM = 1 << 1;
    final int LEFT = 1 << 2;
    final int RIGHT = 1 << 3;

    final int FEATURE_EDGE_TOP = 1<<4;
    final int FEATURE_EDGE_BOTTOM = 1<<5;
    final int FEATURE_EDGE_LEFT = 1<<6;
    final int FEATURE_EDGE_RIGHT = 1<<7;

    final int FEATURE_CROWN = 1<<8;
    final int FEATURE_BASE = 1<<9;
    final int FEATURE_PLATE = 1<<10;
    final int FEATURE_PIPES = 1<<11;

    final int FEATURE_MASK = FEATURE_PLATE | FEATURE_PIPES;

    final int NO_FEATURE = -1;

    final int MISSING_TEXTURE = Integer.MAX_VALUE;

    public void registerBlockIcons(IIconRegister iconRegister)
    {
        icons2.clear();
        cachedNoiseGens.clear();
        cachedFeatures.clear();

        icons2.put(DEFAULT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
        icons2.put(LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
        icons2.put(RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
        icons2.put(LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));

        icons2.put(FEATURE_PIPES | FEATURE_EDGE_TOP , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        icons2.put(FEATURE_PIPES | FEATURE_EDGE_BOTTOM , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));
        icons2.put(LEFT | FEATURE_PIPES | FEATURE_EDGE_TOP , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        icons2.put(LEFT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));
        icons2.put(RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        icons2.put(RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));
        icons2.put(LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_TOP , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        icons2.put(LEFT | RIGHT | FEATURE_PIPES | FEATURE_EDGE_BOTTOM , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));


        icons2.put(FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));

        icons2.put(FEATURE_PLATE | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVL"));
        icons2.put(FEATURE_PLATE | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVR"));
        icons2.put(FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHU"));
        icons2.put(FEATURE_PLATE | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHD"));

        icons2.put(FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRDC"));
        icons2.put(FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRUC"));
        icons2.put(FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLDC"));
        icons2.put(FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLUC"));

        icons2.put(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLDC"));
        icons2.put(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLUC"));
        icons2.put(FEATURE_PLATE | LEFT | FEATURE_EDGE_LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVL"));

        icons2.put(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRDC"));
        icons2.put(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRUC"));
        icons2.put(FEATURE_PLATE | RIGHT | FEATURE_EDGE_RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVR"));

        icons2.put(FEATURE_BASE | BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeL"));
        icons2.put(FEATURE_BASE | BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeR"));
        icons2.put(FEATURE_BASE | BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_AllEdge"));
        icons2.put(FEATURE_BASE | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_Center"));

        icons2.put(FEATURE_CROWN | TOP | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL"));
        icons2.put(FEATURE_CROWN | TOP | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR"));
        icons2.put(FEATURE_CROWN | TOP | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_AllEdge"));
        icons2.put(FEATURE_CROWN | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_Center"));

        icons2.put(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_Center"));
        icons2.put(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_DDPanelCL"));
        icons2.put(FEATURE_CROWN | TOP | FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_DDPanelCR"));
        icons2.put(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR"));
        icons2.put(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE | FEATURE_EDGE_TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL"));
        icons2.put(FEATURE_CROWN | TOP | RIGHT | FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR"));
        icons2.put(FEATURE_CROWN | TOP | LEFT | FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL"));


        icons2.put(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_Center"));
        icons2.put(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_LEFT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_DDPanelCL"));
        icons2.put(FEATURE_BASE | BOTTOM | FEATURE_PLATE | FEATURE_EDGE_RIGHT | FEATURE_EDGE_BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_DDPanelCR"));

        /*icons2.put(FEATURE_PLATE | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVL"));
        icons2.put(FEATURE_PLATE | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHU"));
        icons2.put(FEATURE_PLATE | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVR"));
        icons2.put(FEATURE_PLATE | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHD"));
        icons2.put(FEATURE_PLATE | RIGHT | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRDC"));
        icons2.put(FEATURE_PLATE | RIGHT | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRUC"));
        icons2.put(FEATURE_PLATE | LEFT | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLDC"));
        icons2.put(FEATURE_PLATE | LEFT | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLUC"));



        icons2.put(FEATURE_PIPES | BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));
        icons2.put(FEATURE_PIPES | BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeB"));
        icons2.put(FEATURE_PIPES | TOP | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));
        icons2.put(FEATURE_PIPES | TOP | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD1_PipeA"));

        //Unusual Truss circumstances. Not sure if I'm handling this right.
        icons2.put(FEATURE_CROWN | TOP | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_Center"));
        icons2.put(FEATURE_CROWN | TOP | BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL"));
        icons2.put(FEATURE_CROWN | TOP | BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR"));
        icons2.put(FEATURE_CROWN | TOP | BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_AllEdge"));
        icons2.put(BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_AllEdge"));
        icons2.put(BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
        icons2.put(BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
        icons2.put(BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));

        //Single pipes
        icons2.put(FEATURE_PIPES | TOP | BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));*/

        icons2.put(MISSING_TEXTURE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-Missing"));

        //Remove these later once the default is the default block
        icons2.put(TOP | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            StringBuilder debugInfo = new StringBuilder();
            int blockProperties = getTexturePropertiesForSide(world, x, y, z, side, debugInfo);
            String description = describeTextureProperties(blockProperties);
            player.addChatComponentMessage(new ChatComponentText(description));
            player.addChatComponentMessage(new ChatComponentText(debugInfo.toString()));
        }
        return super.onBlockActivated(world, x, y, z, player, side, p_149727_7_, p_149727_8_, p_149727_9_);
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int blockProperties = getTexturePropertiesForSide(blockAccess, x, y, z, side, null);

        if (!icons2.containsKey(blockProperties))
        {
            String blockPropertiesDescription = describeTextureProperties(blockProperties);

            //Logger.warning("Unknown Ruin Wall bitmap: %d (%s) - %s @ (%d, %d, %d) - %d", blockProperties, Integer.toBinaryString(blockProperties), blockPropertiesDescription, x, y, z, side);

            blockProperties = MISSING_TEXTURE;
        }
        return icons2.get(blockProperties);
    }



    private int getTexturePropertiesForSide(IBlockAccess blockAccess, int x, int y, int z, int side, StringBuilder debugger)
    {
        boolean debugging = debugger != null;
        if (debugger == null) {
            debugger = new StringBuilder();
        }

        try
        {
            int blockProperties = 0;

            final WorldBlockCoord worldBlockCoord = WorldBlockCoord.of(x, y, z);
            ForgeDirection orientation = ForgeDirection.getOrientation(side);
            if (orientation == ForgeDirection.UP || orientation == ForgeDirection.DOWN)
            {
                return DEFAULT;
            }
            ForgeDirection[] rotationMatrix = ROTATION_MATRIX[side];
            ForgeDirection left = rotationMatrix[ROTATION_INDEX_LEFT];
            ForgeDirection right = rotationMatrix[ROTATION_INDEX_RIGHT];
            ForgeDirection back = rotationMatrix[ROTATION_INDEX_BACK];
            ForgeDirection above = rotationMatrix[ROTATION_INDEX_ABOVE];
            ForgeDirection below = rotationMatrix[ROTATION_INDEX_BELOW];

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

            int featureId = getValidFeature(blockAccess, worldBlockCoord, orientation, debugging);
            debugger.append(String.format("Feature:%d,", featureId));
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

    private interface IRuinWallFeature {
        boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId, boolean debugging);

        Collection<Feature> getFeatureAreasFor(ChunkCoord chunkCoord);
    }

    private class PipesRuinWallFeature implements IRuinWallFeature {

        @Override
        public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId, boolean debugging)
        {
            ForgeDirection[] rotationMatrix = ROTATION_MATRIX[orientation.ordinal()];
            ForgeDirection back = rotationMatrix[ROTATION_INDEX_BACK];

            if (!checkRuinWallAndNotObscured(blockAccess, worldBlockCoord, back)) {
                return false;
            }

            ForgeDirection below = rotationMatrix[ROTATION_INDEX_BELOW];
            ForgeDirection above = rotationMatrix[ROTATION_INDEX_ABOVE];

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
                int xPos = random.nextInt(16);
                int yPos = random.nextInt(15);
                int zPos = random.nextInt(16);

                features.add(new Feature(FEATURE_PIPES, WorldBlockCoord.of(xPos, yPos, zPos), 1, 2, 1));
            }
            return features;
        }
    }

    private class PlateRuinWallFeature implements IRuinWallFeature {
        public boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId, boolean debugging)
        {
            ForgeDirection[] rotationMatrix = ROTATION_MATRIX[orientation.ordinal()];
            ForgeDirection back = rotationMatrix[ROTATION_INDEX_BACK];

            if (!checkRuinWallAndNotObscured(blockAccess, worldBlockCoord, back)) {
                return false;
            }

            ForgeDirection left = rotationMatrix[ROTATION_INDEX_LEFT];
            ForgeDirection right = rotationMatrix[ROTATION_INDEX_RIGHT];
            ForgeDirection below = rotationMatrix[ROTATION_INDEX_BELOW];
            ForgeDirection above = rotationMatrix[ROTATION_INDEX_ABOVE];

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
                int xPos = random.nextInt(16);
                int yPos = random.nextInt(16);
                int zPos = random.nextInt(16);

                int width = random.nextInt(5) + 1;
                int height = random.nextInt(5) + 1;
                int depth = random.nextInt(5) + 1;

                features.add(new Feature(FEATURE_PLATE, WorldBlockCoord.of(xPos, yPos, zPos), width, height, depth));
            }

            return features;
        }
    }

    private int getValidFeature(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, boolean debugging) {
        int desiredFeatureId = getFeatureAt(worldBlockCoord);
        if (desiredFeatureId == NO_FEATURE) {
            return NO_FEATURE;
        }
        IRuinWallFeature wallFeature = features.get(desiredFeatureId);
        if (wallFeature.isFeatureValid(blockAccess, worldBlockCoord, orientation, desiredFeatureId, debugging)) {
            return desiredFeatureId;
        }
        return NO_FEATURE;
    }



    private int getSubProperties(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, ForgeDirection orientation, int featureId)
    {
        ForgeDirection[] rotationMatrix = ROTATION_MATRIX[orientation.ordinal()];
        ForgeDirection left = rotationMatrix[ROTATION_INDEX_LEFT];
        ForgeDirection right = rotationMatrix[ROTATION_INDEX_RIGHT];
        ForgeDirection below = rotationMatrix[ROTATION_INDEX_BELOW];
        ForgeDirection above = rotationMatrix[ROTATION_INDEX_ABOVE];

        int subProperties = featureId;
        int leftBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(left), orientation, false);
        int rightBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(right), orientation, false);
        int aboveBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(above), orientation, false);
        int belowBlockFeature = getValidFeature(blockAccess, worldBlockCoord.offset(below), orientation, false);

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


    private int getFeatureAt(WorldBlockCoord worldBlockCoord)
    {
        final ChunkCoord chunkCoord = ChunkCoord.of(worldBlockCoord);
        int[] noiseData = cachedNoiseGens.get(chunkCoord);
        if (noiseData == null)
        {
            noiseData = new int[16 * 256 * 16];
            cachedNoiseGens.put(chunkCoord, noiseData);
        }
        ChunkBlockCoord localCoord = ChunkBlockCoord.of(worldBlockCoord);
        final int index = localCoord.getY() | localCoord.getZ() << 8 | localCoord.getX() << 12;
        int featureId = noiseData[index];
        if (featureId == 0)
        {
            int x = localCoord.getX();
            int y = localCoord.getY() & 15;
            int z = localCoord.getZ();
            //-1 denotes processed, but needs special handling elsewhere then.
            featureId = -1;
            for (Feature feature : getChunkFeatures(chunkCoord))
            {
                if (x >= feature.blockCoord.getX() && x < feature.blockCoord.getX() + feature.width)
                {
                    if (z >= feature.blockCoord.getZ() && z < feature.blockCoord.getZ() + feature.depth)
                    {
                        if (y >= feature.blockCoord.getY() && y < feature.blockCoord.getY() + feature.height)
                        {
                            featureId = feature.featureId;
                            break;
                        }
                    }
                }
            }
            noiseData[index] = featureId;
        }
        return featureId;
    }

    HashMap<ChunkCoord, int[]> cachedNoiseGens = new HashMap<ChunkCoord, int[]>();
    HashMap<ChunkCoord, List<Feature>> cachedFeatures = new HashMap<ChunkCoord, List<Feature>>();

    private List<Feature> getChunkFeatures(ChunkCoord chunkCoord)
    {
        List<Feature> features = cachedFeatures.get(chunkCoord);
        if (features != null)
        {
            return features;
        }
        features = new ArrayList<Feature>();

        for (IRuinWallFeature wallFeature : this.features.values()) {
            features.addAll(wallFeature.getFeatureAreasFor(chunkCoord));
        }

        cachedFeatures.put(chunkCoord, features);

        return features;
    }


    private String describeTextureProperties(int blockProperties)
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


    public PlotoniumRuinWall()
    {
        super(Material.rock);
        setBlockName(NAME);

        features.put(FEATURE_PLATE, new PlateRuinWallFeature());
        features.put(FEATURE_PIPES, new PipesRuinWallFeature());
    }

    private class Feature
    {
        private final int featureId;
        private final WorldBlockCoord blockCoord;
        private final int width;
        private final int height;
        private final int depth;

        public Feature(int featureId, WorldBlockCoord blockCoord, int width, int height, int depth)
        {

            this.featureId = featureId;
            this.blockCoord = blockCoord;
            this.width = width;
            this.height = height;
            this.depth = depth;
        }
    }
}
