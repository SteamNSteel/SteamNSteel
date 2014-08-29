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
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CupolaBlock extends SteamNSteelDirectionalBlock implements ITileEntityProvider
{
    public static final String NAME = "cupola";

    public CupolaBlock()
    {
        super(Material.piston);
        setBlockName(NAME);
        setStepSound(Block.soundTypePiston);
        setHardness(0.5f);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, x, entity, itemStack);

        final int orientation = BlockDirectional.getDirection(MathHelper.floor_double(entity.rotationYaw * 4.0f / 360.0f + 0.5));
        world.setBlockMetadataWithNotify(x, y, z, orientation, 0);
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
        // Disable normal block rendering.
        return -1;
    }

    @Override
    public int getMobilityFlag()
    {
        // total immobility and stop pistons
        return 2;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof CupolaTE)
            if (((CupolaTE) te).isActive())
                return 15;

        return super.getLightValue(world, x, y, z);
    }
}
