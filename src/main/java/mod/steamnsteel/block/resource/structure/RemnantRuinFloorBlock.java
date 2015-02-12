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
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RemnantRuinFloorBlock extends SteamNSteelBlock
{
    public static final String NAME = "remnantRuinFloor";
    public static IIcon[] floorIcons;
    ProceduralConnectedTexture textureManager;

    public RemnantRuinFloorBlock()
    {
        super(Material.rock);
        setBlockName(NAME);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal()) {
            int xPos = x % 3;
            if (xPos < 0) xPos += 3;
            int zPos = z % 9;
            if (zPos < 0) zPos += 9;

            final int index = zPos * 3 + xPos;
            return floorIcons[index];
        } else {
            final IIcon iconForSide = textureManager.getIconForSide(world, WorldBlockCoord.of(x, y, z), side);
            return iconForSide;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            String description = textureManager.describeTextureAt(world, WorldBlockCoord.of(x, y, z), side);
            player.addChatComponentMessage(new ChatComponentText(description));
        }
        return super.onBlockActivated(world, x, y, z, player, side, p_149727_7_, p_149727_8_, p_149727_9_);
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
