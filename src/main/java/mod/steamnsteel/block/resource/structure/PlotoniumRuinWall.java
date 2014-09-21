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
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.Random;

public class PlotoniumRuinWall extends SteamNSteelBlock {
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

	final int FEATURE_PLATE = 16;
	final int FEATURE_TRUSS = 32;
	final int NO_FEATURE = 0;

	final int MISSING_TEXTURE = Integer.MAX_VALUE;

	public void registerBlockIcons(IIconRegister iconRegister) {
		icons2.clear();

		icons2.put(DEFAULT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));
		icons2.put(LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));
		icons2.put(RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));
		icons2.put(LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));

		icons2.put(FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));

		icons2.put(FEATURE_PLATE | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-PlateL"));
		icons2.put(FEATURE_PLATE | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-PlateT"));
		icons2.put(FEATURE_PLATE | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-PlateR"));
		icons2.put(FEATURE_PLATE | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-PlateB"));
		icons2.put(FEATURE_PLATE | RIGHT | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-PlateBR"));
		icons2.put(FEATURE_PLATE | RIGHT | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-PlateTR"));
		icons2.put(FEATURE_PLATE | LEFT | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-PlateBL"));
		icons2.put(FEATURE_PLATE | LEFT | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-PlateTL"));

		icons2.put(FEATURE_TRUSS | TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussT"));
		icons2.put(FEATURE_TRUSS | TOP | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussTL"));
		icons2.put(FEATURE_TRUSS | TOP | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussTR"));
		icons2.put(FEATURE_TRUSS | TOP | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussTC"));
		icons2.put(FEATURE_TRUSS | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussB"));
		icons2.put(FEATURE_TRUSS | BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussBL"));
		icons2.put(FEATURE_TRUSS | BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussBR"));
		icons2.put(FEATURE_TRUSS | BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussBC"));

		icons2.put(FEATURE_TRUSS | TOP | FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussT"));
		icons2.put(FEATURE_TRUSS | TOP | FEATURE_PLATE | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussTPlateL"));
		icons2.put(FEATURE_TRUSS | TOP | FEATURE_PLATE | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussTPlateR"));
		icons2.put(FEATURE_TRUSS | BOTTOM | FEATURE_PLATE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussB"));
		icons2.put(FEATURE_TRUSS | BOTTOM | FEATURE_PLATE | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussBPlateL"));
		icons2.put(FEATURE_TRUSS | BOTTOM | FEATURE_PLATE | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussBPlateR"));

		//Unusual Truss circumstances. Not sure if I'm handling this right.
		icons2.put(FEATURE_TRUSS | TOP | BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussT"));
		icons2.put(FEATURE_TRUSS | TOP | BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussTL"));
		icons2.put(FEATURE_TRUSS | TOP | BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussTR"));
		icons2.put(FEATURE_TRUSS | TOP | BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussTC"));
		icons2.put(BOTTOM | LEFT | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TrussBC"));
		icons2.put(BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));
		icons2.put(BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));
		icons2.put(BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));

		icons2.put(MISSING_TEXTURE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-Missing"));
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (world.isRemote) {
			int blockProperties = getTexturePropertiesForSide(world, x, y, z, side);
			String description = describeTextureProperties(blockProperties);
			player.addChatComponentMessage(new ChatComponentText(description));
		}
		return super.onBlockActivated(world, x, y, z, player, side, p_149727_7_, p_149727_8_, p_149727_9_);
	}

	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		//Logger.info("%d, %d, %d, %d", );

		int blockProperties = getTexturePropertiesForSide(blockAccess, x, y, z, side);

		if (!icons2.containsKey(blockProperties)) {
			String blockPropertiesDescription = describeTextureProperties(blockProperties);

			Logger.warning("Unknown Ruin Wall bitmap: %d (%s) - %s @ (%d, %d, %d) - %d", blockProperties, Integer.toBinaryString(blockProperties), blockPropertiesDescription, x, y, z, side);

			blockProperties = MISSING_TEXTURE;
		}
		return icons2.get(blockProperties);
	}

	private String describeTextureProperties(int blockProperties) {
		StringBuilder sb = new StringBuilder();
		if ((blockProperties & TOP) == TOP) {
			sb.append("T,");
		}
		if ((blockProperties & BOTTOM) == BOTTOM) {
			sb.append("B,");
		}
		if ((blockProperties & LEFT) == LEFT) {
			sb.append("L,");
		}
		if ((blockProperties & RIGHT) == RIGHT) {
			sb.append("R,");
		}

		if ((blockProperties & FEATURE_PLATE) == FEATURE_PLATE) {
			sb.append("Plate,");
		}
		if ((blockProperties & FEATURE_TRUSS) == FEATURE_TRUSS) {
			sb.append("Truss,");
		}

		return sb.toString();
	}

	private int getTexturePropertiesForSide(IBlockAccess blockAccess, int x, int y, int z, int side) {
		try {
			final WorldBlockCoord worldBlockCoord = WorldBlockCoord.of(x, y, z);
			ForgeDirection orientation = ForgeDirection.getOrientation(side);
			if (orientation == ForgeDirection.UP || orientation == ForgeDirection.DOWN) {
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
			if (!blockAbove.getMaterial().isOpaque() || blockBackAndUp.getMaterial().isOpaque()) {
				blockProperties |= TOP;
				blockProperties |= FEATURE_TRUSS;
			}
			if (blockBackAndBottom.getMaterial().isOpaque()) {
				blockProperties |= BOTTOM;
			} else if (blockBelow != ModBlock.ruinWallPlotonium && blockBelow.getMaterial().isOpaque()) {
				blockProperties = BOTTOM;
				blockProperties |= FEATURE_TRUSS;
			}
			if (blockLeft != ModBlock.ruinWallPlotonium || blockBackAndLeft.getMaterial().isOpaque()) {
				blockProperties |= LEFT;
			}

			if (blockRight != ModBlock.ruinWallPlotonium || blockBackAndRight.getMaterial().isOpaque()) {
				blockProperties |= RIGHT;
			}


			int featureId = getSideFeature(worldBlockCoord);
			if (featureId != NO_FEATURE) {
				int savedBlockProperties = blockProperties;
				blockProperties |= featureId;
				if (getSideFeature(worldBlockCoord.offset(left)) != featureId) {
					blockProperties |= LEFT;
				}
				if (getSideFeature(worldBlockCoord.offset(right)) != featureId) {
					blockProperties |= RIGHT;
				}
				if (getSideFeature(worldBlockCoord.offset(above)) != featureId) {
					blockProperties |= TOP;
				}
				if (getSideFeature(worldBlockCoord.offset(below)) != featureId) {
					blockProperties |= BOTTOM;
				}

				//Plates don't support single width/height blocks. Revert to non-plate feature.
				if ((blockProperties & (TOP | BOTTOM)) == (TOP | BOTTOM)) {
					blockProperties = savedBlockProperties;
				}

				if ((blockProperties & (LEFT | RIGHT)) == (LEFT | RIGHT)) {
					blockProperties = savedBlockProperties;
				}
			}

			return blockProperties;
		} catch (Exception e) {
			e.printStackTrace();
			return MISSING_TEXTURE;
		}
	}

	HashMap<ChunkCoord, double[]> cachedNoiseGens = new HashMap<ChunkCoord, double[]>();
	NoiseGeneratorOctaves noiseGen = new NoiseGeneratorOctaves(new Random(1L), 5);

	//x, y, z in world coordinates
	private double[] getNoiseGen(ChunkCoord chunkCoord) {
		double[] noiseData = cachedNoiseGens.get(chunkCoord);
		if (noiseData == null) {
			/**
			 * pars:(par2,3,4=noiseOffset ; so that adjacent noise segments connect) (pars5,6,7=x,y,zArraySize),(pars8,10,12 =
			 * x,y,z noiseScale)
			 */
			noiseData = new double[16 * 256 * 16];
			final WorldBlockCoord origin = WorldBlockCoord.forOriginOf(chunkCoord);
			noiseGen.generateNoiseOctaves(noiseData, origin.getX(), 0, origin.getZ(), 16, 256, 16, 3, 3, 3);
			cachedNoiseGens.put(chunkCoord, noiseData);
		}
		return noiseData;
	}

	private int getSideFeature(WorldBlockCoord worldBlockCoord) {
		double[] noiseData = getNoiseGen(ChunkCoord.of(worldBlockCoord));
		ChunkBlockCoord localCoord = ChunkBlockCoord.of(worldBlockCoord);

		final int i = localCoord.getY() + (localCoord.getZ()*256) + (localCoord.getX() * 256 * 16);
		double featureNoise = noiseData[i];

		if (featureNoise > -17.8877 && featureNoise < -10.6177) {
			return FEATURE_PLATE;
		} else if (featureNoise >= -2.9167 && featureNoise < 0.403) {
			return FEATURE_PLATE;
		}
		return 0;
	}

	public PlotoniumRuinWall() {
		super(Material.rock);
		setBlockName(NAME);
	}
}
