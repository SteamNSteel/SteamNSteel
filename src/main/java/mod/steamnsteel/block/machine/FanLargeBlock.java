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

import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.tileentity.LargeFanTE;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static net.minecraft.block.BlockDirectional.FACING;

public class FanLargeBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "fanLarge";

    public FanLargeBlock()
    {
        setUnlocalizedName(NAME);
        setDefaultState(
                this.blockState
                        .getBaseState()
                        .withProperty(BlockDirectional.FACING, EnumFacing.NORTH)
        );
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new LargeFanTE();
    }
}

