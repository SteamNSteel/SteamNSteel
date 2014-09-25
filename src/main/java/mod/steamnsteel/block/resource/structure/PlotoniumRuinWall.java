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

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.texturing.ProceduralConnectedTexture;
import mod.steamnsteel.texturing.RuinWallTexture;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PlotoniumRuinWall extends SteamNSteelBlock
{
    public static final String NAME = "ruinWallPlotonium";
    ProceduralConnectedTexture textureManager;

    public void registerBlockIcons(IIconRegister iconRegister)
    {
        textureManager = new RuinWallTexture();
        textureManager.registerIcons(iconRegister);
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
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return textureManager.getIconForSide(blockAccess, WorldBlockCoord.of(x, y, z), side);

    }

    public PlotoniumRuinWall()
    {
        super(Material.rock);
        setBlockName(NAME);
    }
}
