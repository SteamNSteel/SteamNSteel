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
import mod.steamnsteel.tileentity.PipeTE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static net.minecraft.util.EnumFacing.*;

public class PipeBlock extends SteamNSteelBlock
{


    public static final PropertyEnum<PipeStates> PIPE_STATE = PropertyEnum.create("pipe", PipeStates.class);
    public static final PropertyBool END_A_CAP = PropertyBool.create("endacap");
    public static final PropertyBool END_B_CAP = PropertyBool.create("endbcap");

    public PipeBlock()
    {
        super(Material.CIRCUITS, true);

        setDefaultState(
                this.blockState
                        .getBaseState()
                        .withProperty(PIPE_STATE, PipeStates.ns)
                        .withProperty(END_A_CAP, true)
                        .withProperty(END_B_CAP, true)
        );
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, PIPE_STATE, END_A_CAP, END_B_CAP);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        final TileEntity ute = worldIn.getTileEntity(pos);

        if (ute instanceof PipeTE)
        {
            final PipeTE te = (PipeTE) ute;

            return state
                    .withProperty(PIPE_STATE, PipeStates.getMatchingState(te.getEndADirection(), te.getEndBConnected()))
                    .withProperty(END_A_CAP, !te.isEndAConnected())
                    .withProperty(END_B_CAP, !te.isEndBConnected());
        } else
        {
            return state;
        }
    }

    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new PipeTE();
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        PipeTE entity = (PipeTE) world.getTileEntity(pos);
        entity.checkEnds();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (playerIn != null)
        {
            ItemStack itemInUse = playerIn.inventory.mainInventory.get(playerIn.inventory.currentItem);
            if (itemInUse != null && itemInUse.getItem() == Items.BONE)
            {
                if (!worldIn.isRemote)
                {
                    PipeTE entity = (PipeTE) worldIn.getTileEntity(pos);
                    entity.rotatePipe();
                }
                return true;
            }
            /*if (itemInUse != null && itemInUse.getItem() == Items.name_tag) {
                BasePlumbingTE entity = (BasePlumbingTE) world.getTileEntity(pos);
                Logger.info("%s - Entity Check - %s", world.isRemote ? "client" : "server", entity.toString());
            }*/
        }

        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            PipeTE entity = (PipeTE) worldIn.getTileEntity(pos);
            if (entity != null)
            {
                entity.detach();
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof PipeTE)
        {
            PipeTE te = (PipeTE) tileEntity;

            EnumFacing direction = EAST;
            int facing = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            if (facing == 0)
            {
                direction = NORTH;
            } else if (facing == 1)
            {
                direction = EAST;
            } else if (facing == 2)
            {
                direction = SOUTH;
            } else if (facing == 3)
            {
                direction = WEST;
            }

            te.setOrientation(direction);
            te.checkEnds();
        }
    }

    public enum PipeStates implements IStringSerializable
    {
        nu(UP, NORTH), eu(UP, EAST), su(UP, SOUTH), uw(WEST, UP), //up->horizontal
        dn(NORTH, DOWN), de(EAST, DOWN), ds(SOUTH, DOWN), dw(WEST, DOWN), //down->horizontal
        en(NORTH, EAST), es(SOUTH, EAST), sw(WEST, SOUTH), nw(WEST, NORTH), //horizontal->horizontal
        du(DOWN, UP), ns(NORTH, SOUTH), ew(WEST, EAST);     //straight

        private EnumFacing enda;
        private EnumFacing endb;

        PipeStates(EnumFacing enda, EnumFacing endb)
        {
            this.enda = enda;
            this.endb = endb;
        }

        public static PipeStates getMatchingState(EnumFacing a, EnumFacing b)
        {
            final char ca = Character.toLowerCase(a.getName().charAt(0));
            final char cb = Character.toLowerCase(b.getName().charAt(0));

            return valueOf(ca < cb ? "" + ca + cb : "" + cb + ca);
        }

        public EnumFacing getEndA()
        {
            return enda;
        }

        public EnumFacing getEndB()
        {
            return endb;
        }

        public PipeStates rotateClockwise()
        {
            final EnumFacing tea = enda != UP || enda != DOWN ? enda.rotateY() : enda;
            final EnumFacing teb = endb != UP || endb != DOWN ? endb.rotateY() : endb;

            return getMatchingState(tea, teb);
        }

        public PipeStates rotateAnticlockwise()
        {
            final EnumFacing tea = enda != UP || enda != DOWN ? enda.rotateYCCW() : enda;
            final EnumFacing teb = endb != UP || endb != DOWN ? endb.rotateYCCW() : endb;

            return getMatchingState(tea, teb);
        }

        public String getName()
        {
            return name();
        }
    }
}