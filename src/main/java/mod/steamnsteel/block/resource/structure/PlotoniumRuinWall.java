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
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.ChunkBlockCoord;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlotoniumRuinWall extends SteamNSteelBlock
{
    public static final String NAME = "ruinWallPlotonium";

    private HashMap<Integer, IIcon> icons2 = new HashMap<Integer, IIcon>();

    int[][] ROTATION_MATRIX = {
            {0, 0, 0, 0, 0, 0, 6},
            {0, 0, 0, 0, 0, 0, 6},
            {5, 4, 2, 3, 0, 1, 6},
            {4, 5, 3, 2, 0, 1, 6},
            {2, 3, 4, 5, 0, 1, 6},
            {3, 2, 5, 4, 0, 1, 6}
    };

    final int DEFAULT = 0;
    final int TOP = 1;
    final int BOTTOM = 2;
    final int LEFT = 4;
    final int RIGHT = 8;

    final int FEATURE_CROWN = 16;
    final int FEATURE_BASE = 32;
    final int FEATURE_PLATE = 64;
    final int FEATURE_PIPES = 128;

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

        icons2.put(FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));

        icons2.put(FEATURE_PLATE | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVL"));
        icons2.put(FEATURE_PLATE | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHU"));
        icons2.put(FEATURE_PLATE | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeVR"));
        icons2.put(FEATURE_PLATE | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeHD"));
        icons2.put(FEATURE_PLATE | RIGHT | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRDC"));
        icons2.put(FEATURE_PLATE | RIGHT | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeRUC"));
        icons2.put(FEATURE_PLATE | LEFT | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLDC"));
        icons2.put(FEATURE_PLATE | LEFT | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_DD2_PEdgeLUC"));

        icons2.put(FEATURE_CROWN | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_Center"));
        icons2.put(FEATURE_CROWN | TOP | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeL"));
        icons2.put(FEATURE_CROWN | TOP | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_EdgeR"));
        icons2.put(FEATURE_CROWN | TOP | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_AllEdge"));
        icons2.put(FEATURE_BASE | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_Center"));
        icons2.put(FEATURE_BASE | BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeL"));
        icons2.put(FEATURE_BASE | BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_EdgeR"));
        icons2.put(FEATURE_BASE | BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_AllEdge"));

        icons2.put(FEATURE_CROWN | TOP | FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_Center"));
        icons2.put(FEATURE_CROWN | TOP | FEATURE_PLATE | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_DDPanelCL"));
        icons2.put(FEATURE_CROWN | TOP | FEATURE_PLATE | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_DDPanelCR"));
        icons2.put(FEATURE_CROWN | TOP | FEATURE_PLATE | LEFT | RIGHT , iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_CrownM_AllEdge"));
        icons2.put(FEATURE_BASE | BOTTOM | FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_Center"));
        icons2.put(FEATURE_BASE | BOTTOM | FEATURE_PLATE | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_DDPanelCL"));
        icons2.put(FEATURE_BASE | BOTTOM | FEATURE_PLATE | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_BaseM_DDPanelCR"));

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
        icons2.put(FEATURE_PIPES | TOP | BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "Wall_Generic"));

        icons2.put(MISSING_TEXTURE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-Missing"));
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            int blockProperties = getTexturePropertiesForSide(world, x, y, z, side);
            String description = describeTextureProperties(blockProperties);
            player.addChatComponentMessage(new ChatComponentText(description));
        }
        return super.onBlockActivated(world, x, y, z, player, side, p_149727_7_, p_149727_8_, p_149727_9_);
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        //Logger.info("%d, %d, %d, %d", );

        int blockProperties = getTexturePropertiesForSide(blockAccess, x, y, z, side);

        if (!icons2.containsKey(blockProperties))
        {
            String blockPropertiesDescription = describeTextureProperties(blockProperties);

            Logger.warning("Unknown Ruin Wall bitmap: %d (%s) - %s @ (%d, %d, %d) - %d", blockProperties, Integer.toBinaryString(blockProperties), blockPropertiesDescription, x, y, z, side);

            blockProperties = MISSING_TEXTURE;
        }
        return icons2.get(blockProperties);
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

        return sb.toString();
    }

    private int getTexturePropertiesForSide(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        try
        {
            final WorldBlockCoord worldBlockCoord = WorldBlockCoord.of(x, y, z);
            ForgeDirection orientation = ForgeDirection.getOrientation(side);
            if (orientation == ForgeDirection.UP || orientation == ForgeDirection.DOWN)
            {
                return DEFAULT;
            }

            int[] rotationMatrix = ROTATION_MATRIX[side];

            ForgeDirection left = ForgeDirection.getOrientation(rotationMatrix[0]);
            ForgeDirection right = ForgeDirection.getOrientation(rotationMatrix[1]);
            ForgeDirection back = ForgeDirection.getOrientation(rotationMatrix[2]);
            ForgeDirection above = ForgeDirection.getOrientation(rotationMatrix[5]);
            ForgeDirection below = ForgeDirection.getOrientation(rotationMatrix[4]);

            Block blockLeft = blockAccess.getBlock(x + left.offsetX, y + left.offsetY, z + left.offsetZ);
            Block blockRight = blockAccess.getBlock(x + right.offsetX, y + right.offsetY, z + right.offsetZ);
            Block blockAbove = blockAccess.getBlock(x + above.offsetX, y + above.offsetY, z + above.offsetZ);
            Block blockBelow = blockAccess.getBlock(x + below.offsetX, y + below.offsetY, z + below.offsetZ);
            Block blockBackAndUp = blockAccess.getBlock(x + above.offsetX + back.offsetX, y + above.offsetY + back.offsetY, z + above.offsetZ + back.offsetZ);
            Block blockBackAndLeft = blockAccess.getBlock(x + left.offsetX + back.offsetX, y + left.offsetY + back.offsetY, z + left.offsetZ + back.offsetZ);
            Block blockBackAndRight = blockAccess.getBlock(x + right.offsetX + back.offsetX, y + right.offsetY + back.offsetY, z + right.offsetZ + back.offsetZ);
            Block blockBackAndBottom = blockAccess.getBlock(x + below.offsetX + back.offsetX, y + below.offsetY + back.offsetY, z + below.offsetZ + back.offsetZ);

            int blockProperties = 0;
            if (!blockAbove.getMaterial().isOpaque() || blockBackAndUp.getMaterial().isOpaque())
            {
                blockProperties |= TOP;
                blockProperties |= FEATURE_CROWN;
            }
            if (blockBackAndBottom.getMaterial().isOpaque())
            {
                blockProperties |= BOTTOM;
            } else if (blockBelow != ModBlock.ruinWallPlotonium && blockBelow.getMaterial().isOpaque())
            {
                blockProperties = BOTTOM;
                blockProperties |= FEATURE_BASE;
            }
            if (blockLeft != ModBlock.ruinWallPlotonium || blockBackAndLeft.getMaterial().isOpaque())
            {
                blockProperties |= LEFT;
            }

            if (blockRight != ModBlock.ruinWallPlotonium || blockBackAndRight.getMaterial().isOpaque())
            {
                blockProperties |= RIGHT;
            }

            int featureId = getSideFeature(worldBlockCoord);
            if (featureId != NO_FEATURE)
            {// && isFeatureValid(worldBlockCoord, featureId, side)) {
                int savedBlockProperties = blockProperties;
                blockProperties |= featureId;
                final WorldBlockCoord leftBlockCoord = worldBlockCoord.offset(left);
                if (getSideFeature(leftBlockCoord) != featureId || !isFeatureValid(blockAccess, leftBlockCoord, featureId, side))
                {
                    blockProperties |= LEFT;
                }
                final WorldBlockCoord rightBlockCoord = worldBlockCoord.offset(right);
                if (getSideFeature(rightBlockCoord) != featureId || !isFeatureValid(blockAccess, rightBlockCoord, featureId, side))
                {
                    blockProperties |= RIGHT;
                }
                final WorldBlockCoord aboveBlockCoord = worldBlockCoord.offset(above);
                if (getSideFeature(aboveBlockCoord) != featureId || !isFeatureValid(blockAccess, aboveBlockCoord, featureId, side))
                {
                    blockProperties |= TOP;
                }
                final WorldBlockCoord belowBlockCoord = worldBlockCoord.offset(below);
                final int sideFeature = getSideFeature(belowBlockCoord);
                final boolean belowBlockFeature = sideFeature != featureId;
                final boolean belowFeatureIsValid = isFeatureValid(blockAccess, belowBlockCoord, featureId, side);
                if (belowBlockFeature || !belowFeatureIsValid || blockBelow != ModBlock.ruinWallPlotonium)
                {
                    blockProperties |= BOTTOM;
                }

                if (featureId == FEATURE_PLATE)
                {
                    //Plates don't support single width/height blocks. Revert to non-plate feature.
                    if ((blockProperties & (TOP | BOTTOM)) == (TOP | BOTTOM))
                    {
                        blockProperties = savedBlockProperties;
                    }
                    if ((blockProperties & (LEFT | RIGHT)) == (LEFT | RIGHT))
                    {
                        blockProperties = savedBlockProperties;
                    }
                }

                if ((blockProperties & FEATURE_CROWN) == FEATURE_CROWN) {
                    Block blockLeftAndAbove = worldBlockCoord.offset(left).offset(above).getBlock(blockAccess);
                    Block blockRightAndAbove = worldBlockCoord.offset(right).offset(above).getBlock(blockAccess);

                    if (blockLeftAndAbove instanceof PlotoniumRuinWall) {
                        blockProperties |= LEFT;
                    }

                    if (blockRightAndAbove instanceof PlotoniumRuinWall) {
                        blockProperties |= RIGHT;
                    }

                }
            }

            return blockProperties;
        } catch (Exception e)
        {
            e.printStackTrace();
            return MISSING_TEXTURE;
        }
    }


    private int getSideFeature(WorldBlockCoord worldBlockCoord)
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
        Random random = new Random(chunkCoord.hashCode());

        //Generate Pipe features
        for (int i = 0; i < 64; ++i)
        {
            int xPos = random.nextInt(16);
            int yPos = random.nextInt(15);
            int zPos = random.nextInt(16);

            features.add(new Feature(FEATURE_PIPES, WorldBlockCoord.of(xPos, yPos, zPos), 1, 2, 1));
        }

        //Generate plate features
        for (int i = 0; i < 64; ++i)
        {
            int xPos = random.nextInt(16);
            int yPos = random.nextInt(16);
            int zPos = random.nextInt(16);

            int width = random.nextInt(5) + 1;
            int height = random.nextInt(5) + 1;
            int depth = random.nextInt(5) + 1;

            features.add(new Feature(FEATURE_PLATE, WorldBlockCoord.of(xPos, yPos, zPos), width, height, depth));
        }
        cachedFeatures.put(chunkCoord, features);

        return features;
    }

    private boolean isFeatureValid(IBlockAccess blockAccess, WorldBlockCoord worldBlockCoord, int featureId, int side)
    {
        boolean leftSet = false;
        boolean rightSet = false;
        boolean aboveSet = false;
        boolean belowSet = false;

        int[] rotationMatrix = ROTATION_MATRIX[side];

        ForgeDirection left = ForgeDirection.getOrientation(rotationMatrix[0]);
        ForgeDirection right = ForgeDirection.getOrientation(rotationMatrix[1]);
        ForgeDirection above = ForgeDirection.getOrientation(rotationMatrix[5]);
        ForgeDirection below = ForgeDirection.getOrientation(rotationMatrix[4]);
        ForgeDirection back = ForgeDirection.getOrientation(rotationMatrix[2]);

        Block blockBackAndUp = worldBlockCoord.offset(back).offset(above).getBlock(blockAccess);
        Block blockBackAndLeft = worldBlockCoord.offset(back).offset(left).getBlock(blockAccess);
        Block blockBackAndRight = worldBlockCoord.offset(back).offset(right).getBlock(blockAccess);
        Block blockBackAndBottom = worldBlockCoord.offset(back).offset(below).getBlock(blockAccess);

        final WorldBlockCoord leftBlockCoord = worldBlockCoord.offset(left);
        int neighbours = 0;
        if (getSideFeature(leftBlockCoord) != featureId)// || blockBackAndLeft.getMaterial().isOpaque())
        {
            leftSet = true;
            neighbours++;
        }
        final WorldBlockCoord rightBlockCoord = worldBlockCoord.offset(right);
        if (getSideFeature(rightBlockCoord) != featureId)// || blockBackAndRight.getMaterial().isOpaque())
        {
            rightSet = true;
            neighbours++;
        }
        final WorldBlockCoord aboveBlockCoord = worldBlockCoord.offset(above);
        if (getSideFeature(aboveBlockCoord) != featureId)// || blockBackAndUp.getMaterial().isOpaque())
        {
            aboveSet = true;
            neighbours++;
        }
        final WorldBlockCoord belowBlockCoord = worldBlockCoord.offset(below);
        if (belowBlockCoord.getY() > 0 && (getSideFeature(belowBlockCoord) != featureId) || !(belowBlockCoord.getBlock(blockAccess) instanceof PlotoniumRuinWall))// || blockBackAndBottom.getMaterial().isOpaque()))
        {
            belowSet = true;
            neighbours++;
        }

        if ((aboveSet && belowSet) || (leftSet && rightSet)) return false;
        //if (neighbours == 1) return false;

        return true;
    }

    public PlotoniumRuinWall()
    {
        super(Material.rock);
        setBlockName(NAME);
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
