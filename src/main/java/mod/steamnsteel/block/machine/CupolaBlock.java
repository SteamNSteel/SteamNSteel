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

import com.google.common.base.Optional;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.gui.ModGuis;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.tileentity.CupolaTE;
import mod.steamnsteel.utility.Orientation;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class CupolaBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "cupola";

    private static final int flagSlave = 1 << 2;
    private Optional<IIcon> iconMaster = Optional.absent();
    private Optional<IIcon> iconSlave = Optional.absent();

    public CupolaBlock()
    {
        setUnlocalizedName(NAME);
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
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new CupolaTE();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        final String objName = getUnwrappedUnlocalizedName(getUnlocalizedName());

        if (!iconMaster.isPresent()) iconMaster = Optional.of(iconRegister.registerIcon(objName + "/bSide"));
        if (!iconSlave.isPresent()) iconSlave = Optional.of(iconRegister.registerIcon(objName + "/tSide"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return (meta & flagSlave) == 0 ? iconMaster.get() : iconSlave.get();
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
                final Orientation orientation = Orientation.getdecodedOrientation(metadata);

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
    public Item getItemDropped(int metadata, Random rng, int fortune)
    {
        if (metadata != 8) // if we get 8, we will spawn 2 items...so skip one
            return super.getItemDropped(metadata, rng, fortune);
        return Item.getItemById(0);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, x, y + 1, z);
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
        final int meta = block.getBlockMetadata(pos);

        if ((meta & flagSlave) == 0)
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
    public void onPostBlockPlaced(World world, BlockPos pos, IBlockState blockState)
    {
        super.onPostBlockPlaced(world, pos, blockState);

        final int fillerY = y + 1;
        world.setBlock(x, fillerY, z, ModBlock.cupola, flagSlave, 2);
        final TileEntity te = world.getTileEntity(x, fillerY, z);
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

