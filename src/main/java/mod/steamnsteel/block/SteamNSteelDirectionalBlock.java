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

package mod.steamnsteel.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static net.minecraft.block.BlockDirectional.*;

public abstract class SteamNSteelDirectionalBlock extends SteamNSteelBlock
{
    protected SteamNSteelDirectionalBlock(Material material)
    {
        super(material);
        setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        final int orientation = (MathHelper.floor_double(placer.rotationYaw * 4.0f / 360.0f + 0.5)) & 3;
        final EnumFacing horizontal = EnumFacing.getHorizontal(orientation);
        final IBlockState newState = worldIn.getBlockState(pos)
                .withProperty(FACING, horizontal);

        worldIn.setBlockState(pos, newState, 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        meta |= ((EnumFacing)state.getValue(FACING)).ordinal() - 2;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta)
                .withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
    }
}
