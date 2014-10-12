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
import mod.steamnsteel.tileentity.CupolaTE;
import mod.steamnsteel.tileentity.PipeTE;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PipeBlock extends SteamNSteelBlock implements ITileEntityProvider
{
    public static final String NAME = "pipe";
    public static int RenderId;

    public PipeBlock()
    {
        super(Material.circuits, true);
        setBlockName(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new PipeTE();
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        //return super.getRenderType();
        return RenderId;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block newBlockType)
    {
        SteamNSteelTE entity = (SteamNSteelTE)world.getTileEntity(x, y, z);
        entity.updateEntity();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float u, float v, float w)
    {
        try {
            if (player != null) {
                ItemStack itemInUse = player.inventory.mainInventory[player.inventory.currentItem];
                if (itemInUse != null && itemInUse.getItem() == Items.bone)
                {
                    PipeTE entity = (PipeTE) world.getTileEntity(x, y, z);
                    entity.rotatePipe();
                    return true;
                }
            }

            return false;
        } catch (Exception e) {

        }
        return false;
    }
}