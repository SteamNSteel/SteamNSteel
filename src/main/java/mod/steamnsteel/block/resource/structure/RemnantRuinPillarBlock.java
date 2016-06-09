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

import mod.steamnsteel.block.*;
import mod.steamnsteel.tileentity.RemnantRuinPillarTE;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RemnantRuinPillarBlock extends SteamNSteelDirectionalBlock
{
    public static PropertyEnum<PillarCaps> pillarCapsProperty = PropertyEnum.create("caps", PillarCaps.class);

    public RemnantRuinPillarBlock()
    {
        super(Material.ROCK);
        setDefaultState(blockState.getBaseState().withProperty(pillarCapsProperty, PillarCaps.BOTH));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING, pillarCapsProperty);
    }



    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        boolean hasPillarAbove = worldIn.getBlockState(pos.offset(EnumFacing.UP)).getBlock() == this;
        boolean hasPillarBelow = worldIn.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() == this;

        PillarCaps caps = PillarCaps.NONE;

        if (!hasPillarAbove && !hasPillarBelow) {
            caps = PillarCaps.BOTH;
        } else if (hasPillarAbove && !hasPillarBelow) {
            caps = PillarCaps.BOTTOM;
        } else if (!hasPillarAbove && hasPillarBelow) {
            caps = PillarCaps.TOP;
        }

        return super.getActualState(state, worldIn, pos).withProperty(pillarCapsProperty, caps);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState)
    {
        return new RemnantRuinPillarTE();
    }

    @Override
    public boolean hasTileEntity(IBlockState blockState)
    {
        return true;
    }

    public enum PillarCaps implements IStringSerializable {
        TOP,
        BOTTOM,
        BOTH,
        NONE;

        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
    }
}
