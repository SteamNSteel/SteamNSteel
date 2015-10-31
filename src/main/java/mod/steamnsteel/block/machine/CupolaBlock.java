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

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.gui.ModGuis;
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static net.minecraft.block.BlockDirectional.FACING;

public class CupolaBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "cupola";

    public static final PropertyBool IS_SLAVE = PropertyBool.create("is_slave");
    public static final PropertyBool IS_ACTIVE = PropertyBool.create("is_active");

    private static final int flagSlave = 1 << 2;

    public CupolaBlock()
    {
        setUnlocalizedName(NAME);
        setDefaultState(
                this.blockState
                        .getBaseState()
                        .withProperty(BlockDirectional.FACING, EnumFacing.NORTH)
                        .withProperty(IS_SLAVE, false)
                        .withProperty(IS_ACTIVE, false)
        );
    }

    private static void renderSmokeOnTop(World world, BlockPos pos, Random rng)
    {
        for (int i = 0; i < 3; i++)
        {
            final float centerOffset1 = rng.nextFloat() * 0.6f - 0.3f;
            final float centerOffset2 = rng.nextFloat() * 0.6f - 0.3f;

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                    pos.getX() + 0.5d + centerOffset1,
                    pos.getY() + 2.0d,
                    pos.getZ() + 0.5d + centerOffset2,
                    0.0d,
                    0.1d,
                    0.0d);
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, IS_SLAVE, IS_ACTIVE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isSlave = (meta & 4) == 4;
        return super.getStateFromMeta(meta)
                .withProperty(IS_SLAVE, isSlave);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = super.getMetaFromState(state);
        meta |= (Boolean)state.getValue(IS_SLAVE) ? 4 : 0;
        return meta;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        final TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof CupolaTE) {
            final CupolaTE cupolaTE = (CupolaTE) te;
            state = state.withProperty(IS_ACTIVE, cupolaTE.isActive());
        }
        return state;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new CupolaTE();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rng)
    {
        final TileEntity te = world.getTileEntity(pos);
        if (te instanceof CupolaTE)
        {
            final CupolaTE cupola = (CupolaTE) te;
            if (cupola.isActive())
            {
                final float effectX = pos.getX() + 0.5f;
                final float effectY = pos.getY() + 0.5f + rng.nextFloat() * 5.0f / 16.0f;
                final float effectZ = pos.getZ() + 0.5f;
                final float edgeOffset = 0.52f;
                final float widthOffset = rng.nextFloat() * 0.6f - 0.3f;

                final IBlockState metadata = world.getBlockState(pos);

                final EnumFacing orientation = (EnumFacing)metadata.getValue(BlockDirectional.FACING);

                switch (orientation)
                {
                    case SOUTH:
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, effectX + widthOffset, effectY, effectZ - edgeOffset, 0.0d, 0.0d, 0.0d);
                        world.spawnParticle(EnumParticleTypes.FLAME, effectX + widthOffset, effectY, effectZ - edgeOffset, 0.0d, 0.0d, 0.0d);
                        break;

                    case WEST:
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, effectX + edgeOffset, effectY, effectZ + widthOffset, 0.0d, 0.0d, 0.0d);
                        world.spawnParticle(EnumParticleTypes.FLAME, effectX + edgeOffset, effectY, effectZ + widthOffset, 0.0d, 0.0d, 0.0d);
                        break;

                    case NORTH:
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, effectX + widthOffset, effectY, effectZ + edgeOffset, 0.0d, 0.0d, 0.0d);
                        world.spawnParticle(EnumParticleTypes.FLAME, effectX + widthOffset, effectY, effectZ + edgeOffset, 0.0d, 0.0d, 0.0d);
                        break;

                    case EAST:
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, effectX - edgeOffset, effectY, effectZ + widthOffset, 0.0d, 0.0d, 0.0d);
                        world.spawnParticle(EnumParticleTypes.FLAME, effectX - edgeOffset, effectY, effectZ + widthOffset, 0.0d, 0.0d, 0.0d);
                }

                renderSmokeOnTop(world, pos, rng);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        final TileEntity te = world.getTileEntity(pos);
        if (((CupolaTE) te).isSlave())
        {
            if (world.isAirBlock(pos.down()))
            {
                world.setBlockToAir(pos);
                world.removeTileEntity(pos);
            }
            return;
        }

        if (world.isAirBlock(pos.up()))
        {
            world.setBlockToAir(pos);
            if (!world.isRemote)
            {
                //QUESTION: This used to pass "8" as it's metadata? What for?
                dropBlockAsItem(world, pos, state, 0);
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        final CupolaTE te = (CupolaTE) world.getTileEntity(pos);

        if (te != null && !te.isSlave())
        {
            dropInventory(world, pos, te);
            world.notifyNeighborsOfStateChange(pos, state.getBlock()); // notify neighbors
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        //QUESTION: Verify if dupe glitch still exists
        //if (metadata != 8) // if we get 8, we will spawn 2 items...so skip one
            return super.getItemDropped(state, rand, fortune);
        //return Item.getItemById(0);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up());
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        BlockPos actualBlockPos = pos;
        final TileEntity te = world.getTileEntity(actualBlockPos);
        if (((CupolaTE) te).isSlave()) actualBlockPos.down();

        player.openGui(TheMod.instance, ModGuis.CUPOLA.getID(), world, actualBlockPos.getX(), actualBlockPos.getY(), actualBlockPos.getZ());
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess block, BlockPos pos)
    {
        final IBlockState meta = block.getBlockState(pos);

        if (!((Boolean)meta.getValue(IS_SLAVE)))
        {
            maxY = 2;   //is Master
            minY = 0;
        } else
        {
            maxY = 1;   //is Slave
            minY = -1;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        final BlockPos fillerY = pos.up();
        final IBlockState iBlockState = worldIn.getBlockState(pos)
                .withProperty(IS_SLAVE, true);
        worldIn.setBlockState(fillerY, iBlockState, 2);
        final TileEntity te = worldIn.getTileEntity(fillerY);
        ((CupolaTE) te).setSlave();
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
        TileEntity te = world.getTileEntity(pos);

        if (((CupolaTE) te).isSlave())
        {
            te = world.getTileEntity(pos.down());
        }

        if (te != null && ((CupolaTE) te).isActive()) return 15;

        return super.getLightValue(world, pos);
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (player.capabilities.isCreativeMode)
        {
            final TileEntity te = world.getTileEntity(pos);
            if (((CupolaTE) te).isSlave())
            {
                world.setBlockToAir(pos.down());
                return false;
            }
        }

        return super.removedByPlayer(world, pos, player, willHarvest);
    }
}

