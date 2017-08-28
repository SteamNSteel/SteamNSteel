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

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.tileentity.PipeValveTE;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PipeValveBlock extends SteamNSteelBlock implements ITileEntityProvider
{
    public PipeValveBlock()
    {
        super(Material.CIRCUITS, true);
        setDefaultState(
                this.blockState
                        .getBaseState()
                        .withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
        );
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new PipeValveTE();
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    /*@Override
    public void onNeighborBlockChange(World world, BlockPos pos Block newBlockType)
    {
        PipeTE entity = (PipeTE)world.getTileEntity(pos);
        entity.checkEnds();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos EntityPlayer player, int side, float u, float v, float w)
    {
        if (player != null) {
            ItemStack itemInUse = player.inventory.mainInventory[player.inventory.currentItem];
            if (itemInUse != null && itemInUse.getItem() == Items.bone)
            {
                if (!world.isRemote) {
                    PipeTE entity = (PipeTE) world.getTileEntity(pos);
                    entity.rotatePipe();
                }
                return true;
            }
            if (itemInUse != null && itemInUse.getItem() == Items.name_tag) {
                PipeTE entity = (PipeTE) world.getTileEntity(pos);
                Logger.info("%s - Entity Check - %s", world.isRemote ? "client" : "server", entity.toString());
            }
        }

        return false;
    }

    @Override
    public void onBlockPreDestroy(World world, BlockPos pos int metadata)
    {
        if (!world.isRemote) {
            PipeTE entity = (PipeTE) world.getTileEntity(pos);
            if (entity != null)
            {
                entity.detach();
            }
        }
    }*/

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof PipeValveTE)
        {
            PipeValveTE te = (PipeValveTE)tileEntity;

            EnumFacing direction = EnumFacing.EAST;
            int facing = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            if (facing == 0)
            {
                direction = EnumFacing.SOUTH;
            }
            else if (facing == 1)
            {
                direction = EnumFacing.WEST;
            }
            else if (facing == 2)
            {
                direction = EnumFacing.NORTH;
            }
            else if (facing == 3)
            {
                direction = EnumFacing.EAST;
            }

            worldIn.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, direction));

            te.setOrientation(direction);
            //te.setOrientation(direction);
            //te.checkEnds();
        }
    }
}