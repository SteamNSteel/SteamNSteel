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

import com.foudroyantfactotum.tool.structure.IStructure.structure.IStructureFluidHandler;
import com.foudroyantfactotum.tool.structure.IStructure.structure.IStructureSidedInventory;
import com.foudroyantfactotum.tool.structure.coordinates.BlockPosUtil;
import com.foudroyantfactotum.tool.structure.registry.StructureDefinition;
import mod.steamnsteel.block.machine.structure.SSBallMillStructure;
import mod.steamnsteel.inventory.Inventory;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import static com.foudroyantfactotum.tool.structure.coordinates.TransformLAG.flagEnumFacing;
import static com.foudroyantfactotum.tool.structure.coordinates.TransformLAG.localToGlobalDirection;
import static com.foudroyantfactotum.tool.structure.coordinates.TransformLAG.transformFromDefinitionToMaster;

public class BallMillTE extends SteamNSteelStructureTE implements IStructureSidedInventory, IStructureFluidHandler
{
    private static final BlockPos LOCATION_STEAM_INPUT = BlockPosUtil.of(0,0,1);
    private static final int DIRECTIONS_STEAM_INPUT = flagEnumFacing(EnumFacing.SOUTH);

    private static final BlockPos LOCATION_WATER_INPUT = BlockPosUtil.of(4,1,1);
    private static final int DIRECTIONS_WATER_INPUT = flagEnumFacing(EnumFacing.EAST);

    private static final BlockPos LOCATION_MATERIAL_INPUT = BlockPosUtil.of(0,0,0);
    private static final int DIRECTIONS_MATERIAL_INPUT = flagEnumFacing(EnumFacing.NORTH);

    private static final BlockPos LOCATION_MATERIAL_OUTPUT = BlockPosUtil.of(4,0,0);
    private static final int DIRECTIONS_MATERIAL_OUTPUT = flagEnumFacing(EnumFacing.NORTH);

    //Global Directions
    private int globalDirectionsSteamInput;
    private int globalDirectionsWaterInput;
    private int globalDirectionsMaterialInput;
    private int globalDirectionsMaterialOutput;

    private BlockPos globalLocationSteamInput;
    private BlockPos globalLocationWaterInput;
    private BlockPos globalLocationMaterialInput;
    private BlockPos globalLocationMaterialOutput;

    private final Inventory inventory = new Inventory(1);
    private static final int INPUT = 0;
    private static final int[] slotsDefault = {};
    private static final int[] slotsMaterialInput = {INPUT};

    public BallMillTE()
    {
        //noop
    }

    public BallMillTE(StructureDefinition sd, EnumFacing orientation, boolean mirror)
    {
        super(sd, orientation, mirror);
    }

    //================================================================
    //                     I T E M   I N P U T
    //================================================================
    @Override
    public int getSizeInventory()
    {
        return inventory.getSize();
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return inventory.getStack(slotIndex);
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrAmount)
    {
        return inventory.decrStackSize(slotIndex, decrAmount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slotIndex)
    {
        return inventory.getStackOnClosing(slotIndex);
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        inventory.setSlot(slotIndex, itemStack);
    }

    public String getInventoryName()
    {
        return SteamNSteelTE.containerName(SSBallMillStructure.NAME);
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public IChatComponent getDisplayName()
    {
        return null;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return inventory.getStackSizeMax();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
        return slotIndex == INPUT;
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

    @Override
    public boolean canStructureInsertItem(int slot, ItemStack item, EnumFacing side, BlockPos local)
    {
        return isSide(globalDirectionsMaterialInput, side) &&
                local.equals(globalLocationMaterialInput) &&
                isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canStructureExtractItem(int slot, ItemStack item, EnumFacing side, BlockPos local)
    {
        return slot == INPUT && isSide(globalDirectionsMaterialOutput, side);
    }

    @Override
    public int[] getSlotsForStructureFace(EnumFacing side, BlockPos local)
    {
        return globalLocationMaterialInput.equals(local) || globalLocationMaterialOutput.equals(local) ?
                slotsMaterialInput :
                slotsDefault;
    }

    //================================================================
    //                  F L U I D   H A N D L E R
    //================================================================

    @Override
    public boolean canStructureFill(EnumFacing from, Fluid fluid, BlockPos local)
    {
        return false;
    }

    @Override
    public boolean canStructureDrain(EnumFacing from, Fluid fluid, BlockPos local)
    {
        return false;
    }

    @Override
    public int structureFill(EnumFacing from, FluidStack resource, boolean doFill, BlockPos local)
    {
        return 0;
    }

    @Override
    public FluidStack structureDrain(EnumFacing from, FluidStack resource, boolean doDrain, BlockPos local)
    {
        return null;
    }

    @Override
    public FluidStack structureDrain(EnumFacing from, int maxDrain, boolean doDrain, BlockPos local)
    {
        return null;
    }

    @Override
    public FluidTankInfo[] getStructureTankInfo(EnumFacing from, BlockPos local)
    {
        return emptyFluidTankInfo;
    }

    //================================================================
    //                 P I P E   C O N E C T I O N
    //================================================================
    /*
    @Override
    public boolean isStructureSideConnected(EnumFacing opposite, BlockPos local)
    {
        return false;
    }

    @Override
    public boolean tryStructureConnect(EnumFacing opposite, BlockPos local)
    {
        return false;
    }

    @Override
    public boolean canStructureConnect(EnumFacing opposite, BlockPos local)
    {
        return isSide(globalDirectionsSteamInput, opposite) && globalLocationSteamInput.equals(local) ||
                isSide(globalDirectionsWaterInput, opposite) && globalLocationWaterInput.equals(local);
    }

    @Override
    public void disconnectStructure(EnumFacing opposite, BlockPos local)
    {

    }
*/
    //================================================================
    //                            N B T
    //================================================================

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        inventory.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        inventory.writeToNBT(nbt);
    }

    @Override
    protected void transformDirectionsOnLoad(StructureDefinition sd)
    {
        globalDirectionsSteamInput     = localToGlobalDirection(DIRECTIONS_STEAM_INPUT,     orientation, mirror);
        globalDirectionsWaterInput     = localToGlobalDirection(DIRECTIONS_WATER_INPUT,     orientation, mirror);
        globalDirectionsMaterialInput  = localToGlobalDirection(DIRECTIONS_MATERIAL_INPUT,  orientation, mirror);
        globalDirectionsMaterialOutput = localToGlobalDirection(DIRECTIONS_MATERIAL_OUTPUT, orientation, mirror);

        globalLocationSteamInput     = transformFromDefinitionToMaster(sd, LOCATION_STEAM_INPUT);
        globalLocationWaterInput     = transformFromDefinitionToMaster(sd, LOCATION_WATER_INPUT);
        globalLocationMaterialInput  = transformFromDefinitionToMaster(sd, LOCATION_MATERIAL_INPUT);
        globalLocationMaterialOutput = transformFromDefinitionToMaster(sd, LOCATION_MATERIAL_OUTPUT);
    }
}
