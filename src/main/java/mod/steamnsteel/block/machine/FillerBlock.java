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
import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.tileentity.FillerTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FillerBlock extends SteamNSteelBlock implements ITileEntityProvider
{
    public FillerBlock(String name)
    {
        super(SteamNSteelMachineBlock.MATERIAL, false);
        setStepSound(SteamNSteelMachineBlock.SOUND);
        setHardness(SteamNSteelMachineBlock.HARDNESS);
        setBlockName(name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new FillerTE();
    }

    @Override
    public int getMobilityFlag()
    {
        // total immobility and stop pistons
        return 2;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        // Disable normal block rendering.
        return -1;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float change, int fortune)
    {
        // no op
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventParameter)
    {
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof FillerTE)
        {
            final FillerTE filler = (FillerTE) te;
            final int masterX = filler.getMasterX();
            final int masterY = filler.getMasterY();
            final int masterZ = filler.getMasterZ();

            final TileEntity masterTE = world.getTileEntity(masterX, masterY, masterZ);
            return masterTE != null && masterTE.receiveClientEvent(eventId, eventParameter);
        }
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof FillerTE)
        {
            final FillerTE filler = (FillerTE) te;
            if (world.isAirBlock(filler.getMasterX(), filler.getMasterY(), filler.getMasterZ()))
            {
                world.setBlockToAir(x, y, z);
                world.removeTileEntity(x, y, z);
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        // no op
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        if (player.capabilities.isCreativeMode)
        {
            final TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof FillerTE)
            {
                final FillerTE filler = (FillerTE) te;
                final int masterX = filler.getMasterX();
                final int masterY = filler.getMasterY();
                final int masterZ = filler.getMasterZ();
                world.setBlockToAir(masterX, masterY, masterZ);
                return false;
            }
        }

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
}