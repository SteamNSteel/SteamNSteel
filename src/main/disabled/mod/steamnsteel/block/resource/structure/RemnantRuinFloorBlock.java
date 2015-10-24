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
import mod.steamnsteel.texturing.api.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.wall.RemnantRuinFloorSideTexture;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class RemnantRuinFloorBlock extends SteamNSteelBlock
{
    public static final String NAME = "remnantRuinFloor";
    public static IIcon[] floorIcons;
    ProceduralConnectedTexture textureManager;

    public RemnantRuinFloorBlock()
    {
        super(Material.rock);
        setUnlocalizedName(NAME);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, BlockPos pos, int side)
    {
        if (side == EnumFacing.UP.ordinal() || side == EnumFacing.DOWN.ordinal()) {
            int xPos = pos.getX() % 3;
            if (xPos < 0) xPos += 3;
            int zPos = pos.getZ() % 9;
            if (zPos < 0) zPos += 9;

            final int index = zPos * 3 + xPos;
            return floorIcons[index];
        } else {
            final IIcon iconForSide = textureManager.getIconForSide(world, pos, side);
            return iconForSide;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            String description = textureManager.describeTextureAt(worldIn, pos, side);
            playerIn.addChatComponentMessage(new ChatComponentText(description));
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        floorIcons = new IIcon[3*9];
        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 3; ++x) {
                String filename = String.format("%s:steelfloor/steelfloor_%dx%d", TheMod.MOD_ID, x + 1, y + 1);
                floorIcons[y * 3 + x] = iconRegister.registerIcon(filename);
            }
        }

        textureManager = new RemnantRuinFloorSideTexture();
        textureManager.registerIcons(iconRegister);

        blockIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":" + "remnantRuinFloorSide/RemnantRuinFloorSideSingle");
    }
}
