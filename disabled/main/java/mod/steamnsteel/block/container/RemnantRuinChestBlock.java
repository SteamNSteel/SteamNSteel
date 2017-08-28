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

package mod.steamnsteel.block.container;

import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.tileentity.RemnantRuinChestTE;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RemnantRuinChestBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    private final AxisAlignedBB defaultBoundingBox = new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

    public RemnantRuinChestBlock() {
        setDefaultState(getDefaultState());
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return defaultBoundingBox;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState)
    {
        final RemnantRuinChestTE te = (RemnantRuinChestTE) world.getTileEntity(pos);

        if (te != null)
        {
            dropInventory(world, pos, te);

            world.notifyNeighborsOfStateChange(pos, blockState.getBlock());
        }

        super.breakBlock(world, pos, blockState);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        final TileEntity te = world.getTileEntity(pos);

        if (!player.isSneaking())
            if (!world.isRemote && te != null && te instanceof RemnantRuinChestTE)
                player.displayGUIChest((IInventory) te);

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new RemnantRuinChestTE();
    }
}
