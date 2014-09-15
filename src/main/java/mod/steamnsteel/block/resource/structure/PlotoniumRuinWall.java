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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

public class PlotoniumRuinWall extends SteamNSteelBlock
{
    public static final String NAME = "ruinWallPlotonium";

	private HashMap<Integer, IIcon> icons2 = new HashMap<Integer, IIcon>();

	final int DEFAULT = 0;
	final int TOP = 1;
	final int BOTTOM = 2;
	final int LEFT = 4;
	final int RIGHT = 8;
	final int SINGLE = 12;

	int[][] ROTATION_MATRIX = {
			{0,0,0,0,6},
			{0,0,0,0,6},
			{5,4,0,1,6},
			{4,5,0,1,6},
			{2,3,0,1,6},
			{3,2,0,1,6}
	};

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		icons2.put(TOP, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-Top"));
		icons2.put(TOP | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TopL"));
		icons2.put(TOP | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TopR"));
		icons2.put(TOP | SINGLE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-TopSingle"));

		icons2.put(DEFAULT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall"));

		icons2.put(BOTTOM, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-Bottom"));
		icons2.put(BOTTOM | LEFT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-BottomL"));
		icons2.put(BOTTOM | RIGHT, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-BottomR"));
		icons2.put(BOTTOM | SINGLE, iconRegister.registerIcon(TheMod.MOD_ID + ":" + "blockPlotoniumWall-BottomSingle"));
	}

	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		Logger.info("%d, %d, %d, %d", x, y, z, side);

		ForgeDirection orientation = ForgeDirection.getOrientation(side);
		if (orientation == ForgeDirection.UP || orientation == ForgeDirection.DOWN) {
			return icons2.get(DEFAULT);
		}

		int[] rotationMatrix = ROTATION_MATRIX[side];

		ForgeDirection above = ForgeDirection.getOrientation(rotationMatrix[3]);
		ForgeDirection below = ForgeDirection.getOrientation(rotationMatrix[2]);
		ForgeDirection left = ForgeDirection.getOrientation(rotationMatrix[0]);
		ForgeDirection right = ForgeDirection.getOrientation(rotationMatrix[1]);

		Block blockRight = blockAccess.getBlock(x + left.offsetX, y + left.offsetY, z + left.offsetZ);
		Block blockLeft = blockAccess.getBlock(x + right.offsetX, y + right.offsetY, z + right.offsetZ);
		Block blockAbove = blockAccess.getBlock(x + above.offsetX, y + above.offsetY, z + above.offsetZ);
		Block blockBelow = blockAccess.getBlock(x + below.offsetX, y + below.offsetY, z + below.offsetZ);

		int blockProperties = 0;

		//if (blockAbove != ModBlock.ruinWallPlotonium) {
		if (!blockAbove.getMaterial().isOpaque()) {
			blockProperties |= 1;
		}
		if (blockBelow != ModBlock.ruinWallPlotonium && blockBelow.getMaterial().isOpaque()) {// && !blockAbove.getMaterial().isOpaque()) {
			blockProperties = 2;
		}

		if (blockLeft != ModBlock.ruinWallPlotonium) {
			blockProperties |= 8;
		}
		if (blockRight != ModBlock.ruinWallPlotonium) {
			blockProperties |= 4;
		}

		if (!icons2.containsKey(blockProperties))
		{
			blockProperties = 0;
		}

		return icons2.get(blockProperties);
	}

	public PlotoniumRuinWall()
    {
        super(Material.rock);
        setBlockName(NAME);


    }
}
