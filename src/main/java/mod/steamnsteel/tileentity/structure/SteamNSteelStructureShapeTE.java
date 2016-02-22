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
package mod.steamnsteel.tileentity.structure;

import com.foudroyantfactotum.tool.structure.StructureRegistry;
import com.foudroyantfactotum.tool.structure.block.StructureBlock;
import com.foudroyantfactotum.tool.structure.block.StructureShapeBlock;
import com.foudroyantfactotum.tool.structure.coordinates.BlockPosUtil;
import com.foudroyantfactotum.tool.structure.tileentity.StructureShapeTE;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import static com.foudroyantfactotum.tool.structure.block.StructureBlock.getMirror;
import static com.foudroyantfactotum.tool.structure.coordinates.TransformLAG.localToGlobal;
import static mod.steamnsteel.tileentity.structure.SteamNSteelStructureTE.*;
import static net.minecraft.block.BlockDirectional.FACING;

public final class SteamNSteelStructureShapeTE extends StructureShapeTE
{
    private BlockPos local = BlockPos.ORIGIN;
    private int definitionHash = -1;

    private Optional<BlockPos> masterLocation = Optional.absent();
    private Optional<SteamNSteelStructureTE> originTE = Optional.absent();
    private boolean hasNotAttemptedAcquisitionOfOriginTE = true;

    private SteamNSteelStructureTE getOriginTE()
    {
        return originTE.get();
    }

    private boolean hasOriginTE()
    {
        if (originTE.isPresent())
        {
            if (!originTE.get().isInvalid())
            {
                return true;
            }
        }

        //get te and test
        final TileEntity te = getWorld().getTileEntity(getMasterBlockLocation());

        if (hasNotAttemptedAcquisitionOfOriginTE && te instanceof SteamNSteelStructureTE)
        {
            if (!te.isInvalid())
            {
                originTE = Optional.of((SteamNSteelStructureTE) te);
                return true;
            }
        }

        hasNotAttemptedAcquisitionOfOriginTE = false;
        return false;
    }

    //================================================================
    //                 S T R U C T U R E   C O N F I G
    //================================================================

    @Override
    public int getRegHash()
    {
        return definitionHash;
    }

    @Override
    public StructureBlock getMasterBlockInstance()
    {
        return StructureRegistry.getStructureBlock(definitionHash);
    }

    @Override
    public BlockPos getMasterBlockLocation()
    {
        if (!masterLocation.isPresent())
        {
            final IBlockState state = getWorld().getBlockState(pos);
            final StructureBlock sb = StructureRegistry.getStructureBlock(definitionHash);

            if (sb == null)
            {
                return pos;
            }

            masterLocation = Optional.of(localToGlobal(
                    -local.getX(), -local.getY(), -local.getZ(),
                    pos.getX(), pos.getY(), pos.getZ(),
                    state.getValue(FACING), getMirror(state),
                    sb.getPattern().getBlockBounds()));
        }

        return masterLocation.get();
    }

    @Override
    public IBlockState getTransmutedBlock()
    {
        final StructureBlock sb = StructureRegistry.getStructureBlock(definitionHash);

        if (sb != null)
        {
            final IBlockState state = worldObj.getBlockState(pos);

            if (state != null && state.getBlock() instanceof StructureShapeBlock)
            {
                final IBlockState block = sb.getPattern().getBlock(local).getBlockState();

                return block == null ?
                        Blocks.air.getDefaultState() :
                        localToGlobal(
                                block,
                                state.getValue(FACING),
                                getMirror(state)
                        );
            }
        }

        return Blocks.air.getDefaultState();
    }

    @Override
    public void configureBlock(BlockPos local, int definitionHash)
    {
        this.local = local;
        this.definitionHash = definitionHash;
    }

    @Override
    public BlockPos getLocal()
    {
        return local;
    }


    //================================================================
    //                     I T E M   I N P U T
    //================================================================

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return hasOriginTE() ?
                getOriginTE().getSlotsForStructureFace(side, local) :
                new int[0];
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side)
    {
        return hasOriginTE() && getOriginTE()
                .canStructureInsertItem(slotIndex, itemStack, side, local);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side)
    {
        return hasOriginTE() && getOriginTE()
                .canStructureExtractItem(slotIndex, itemStack, side, local);
    }

    @Override
    public int getSizeInventory()
    {
        if (hasOriginTE())
        {
            return getOriginTE().getSizeInventory();
        }

        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        if (hasOriginTE())
        {
            return getOriginTE().getStackInSlot(slotIndex);
        }

        return null;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrAmount)
    {
        if (hasOriginTE())
        {
            return getOriginTE().decrStackSize(slotIndex, decrAmount);
        }

        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int slotIndex)
    {
        if (hasOriginTE())
        {
            return getOriginTE().removeStackFromSlot(slotIndex);
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        if (hasOriginTE())
        {
            getOriginTE().setInventorySlotContents(slotIndex, itemStack);
        }
    }

    @Override
    public IChatComponent getDisplayName()
    {
        if (hasOriginTE())
        {
            return getOriginTE().getDisplayName();
        }

        return new ChatComponentText("");
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public boolean hasCustomName()
    {
        return hasOriginTE() && getOriginTE().hasCustomName();
    }

    @Override
    public int getInventoryStackLimit()
    {
        if (hasOriginTE())
        {
            return getOriginTE().getInventoryStackLimit();
        }

        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return hasOriginTE() && getOriginTE().isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        if (hasOriginTE())
        {
            getOriginTE().openInventory(player);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (hasOriginTE())
        {
            getOriginTE().closeInventory(player);
        }
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return hasOriginTE() && getOriginTE().isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {

    }

    //================================================================
    //                  F L U I D   H A N D L E R
    //================================================================

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        if (hasOriginTE())
        {
            return getOriginTE().structureFill(from, resource, doFill, local);
        }

        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        if (hasOriginTE())
        {
            return getOriginTE().structureDrain(from, resource, doDrain, local);
        }

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        if (hasOriginTE())
        {
            return getOriginTE().structureDrain(from, maxDrain, doDrain, local);
        }

        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return hasOriginTE() && getOriginTE().canStructureFill(from, fluid, local);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return hasOriginTE() && getOriginTE().canStructureDrain(from, fluid, local);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        if (hasOriginTE())
        {
            return getOriginTE().getStructureTankInfo(from, local);
        }

        return emptyFluidTankInfo;
    }

    //================================================================
    //                 P I P E   C O N E C T I O N
    //================================================================
/*
    @Override
    public boolean isSideConnected(EnumFacing opposite)
    {
        return hasOriginTE() && getOriginTE().isStructureSideConnected(opposite, local);
    }

    @Override
    public boolean tryConnect(EnumFacing opposite)
    {
        return hasOriginTE() && getOriginTE().tryStructureConnect(opposite, local);
    }

    @Override
    public boolean canConnect(EnumFacing opposite)
    {
        return hasOriginTE() && getOriginTE().canStructureConnect(opposite, local);
    }

    @Override
    public void recalculateVisuals()
    {
        //noop
    }

    @Override
    public void disconnect(EnumFacing opposite)
    {
        if (hasOriginTE())
        {
            getOriginTE().disconnectStructure(opposite, local);
        }
    }
*/
    //================================================================
    //                            N B T
    //================================================================

    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);

        return new S35PacketUpdateTileEntity(pos, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        final int blockInfo = nbt.getInteger(BLOCK_INFO);

        local = BlockPosUtil.fromInt(blockInfo);
        definitionHash = nbt.getInteger(BLOCK_PATTERN_NAME);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setInteger(BLOCK_INFO, BlockPosUtil.toInt(local));
        nbt.setInteger(BLOCK_PATTERN_NAME, definitionHash);
    }

    //================================================================
    //                          C l a s s
    //================================================================

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("local", local)
                .add("definitionHash", definitionHash)
                .add("masterLocation", masterLocation)
                .add("originTE", originTE)
                .add("hasNotAttemptedAcquisitionOfOriginTE", hasNotAttemptedAcquisitionOfOriginTE)
                .toString();
    }
}
