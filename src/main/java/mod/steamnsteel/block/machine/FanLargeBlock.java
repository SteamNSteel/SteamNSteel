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
import mod.steamnsteel.client.model.opengex.OpenGEXAnimationFrameProperty;
import mod.steamnsteel.tileentity.LargeFanTE;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import static net.minecraft.block.BlockDirectional.FACING;

public class FanLargeBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{

    public static final PropertyBool RENDER_DYNAMIC = PropertyBool.create("render-dynamic");

    public static final String NAME = "fanLarge";

    public static int counter;

    public FanLargeBlock()
    {
        setUnlocalizedName(NAME);
        setDefaultState(
                blockState
                        .getBaseState()
                        .withProperty(FACING, EnumFacing.NORTH)
                        .withProperty(RENDER_DYNAMIC, false)
        );
    }

    @Override
    protected BlockState createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[]{FACING, RENDER_DYNAMIC}, new IUnlistedProperty[]{OpenGEXAnimationFrameProperty.instance});
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            System.out.println("click " + counter);
            if (player.isSneaking()) counter--;
            else counter++;
            //if(counter >= model.getNode().getKeys().size()) counter = 0;
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return super.getStateFromMeta(meta);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return super.getActualState(state, worldIn, pos);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new LargeFanTE();
    }
}

