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

import mod.steamnsteel.Reference;
import mod.steamnsteel.Reference.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class SteamNSteelDirectionalBlock extends Block
{
    protected SteamNSteelDirectionalBlock(Material material) {
        super(material);
        setDefaultState(getDefaultState().withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockProperties.HORIZONTAL_FACING);
    }

    @Override
    @Deprecated
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(BlockProperties.HORIZONTAL_FACING, placer.getHorizontalFacing());
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        EnumFacing value = state.getValue(BlockProperties.HORIZONTAL_FACING);
        if (value == EnumFacing.UP || value == EnumFacing.DOWN) {
            value = EnumFacing.NORTH;
        }

        return value.getHorizontalIndex();
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        return super.getStateFromMeta(meta)
                .withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.getHorizontal(meta & 3));
    }
}
