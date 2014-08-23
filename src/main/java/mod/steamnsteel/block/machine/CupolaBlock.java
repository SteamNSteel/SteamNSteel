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

package mod.steamnsteel.block.machine;

import mod.steamnsteel.block.SteamNSteelDirectionalBlock;
import mod.steamnsteel.library.Blocks;
import mod.steamnsteel.library.RenderIds;
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CupolaBlock extends SteamNSteelDirectionalBlock implements ITileEntityProvider
{
    private static final int SLAVE_MASK = 0x8;

    public CupolaBlock()
    {
        super(Material.rock);
        setBlockName(Blocks.Names.CUPOLA);
    }

    public static boolean isSlave(int metadata)
    {
        return (metadata & SLAVE_MASK) == SLAVE_MASK;
    }

    public static int codeSlaveMetadata(int metadata) {return metadata | SLAVE_MASK;}

    @Override
    public boolean onBlockActivated(World world, int x, int targetY, int z, EntityPlayer player, int side, float hitVectorX, float hitVectorY, float hitVectorZ)
    {
        int y = targetY;
        if (world.isRemote) return true;

        final int metadata = world.getBlockMetadata(x, y, z);
        if (isSlave(metadata)) y--;

        // TODO: Right Click behavior

        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new CupolaTE();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return RenderIds.cupola;
    }
}
