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
import mod.steamnsteel.tileentity.BasePlumbingTE;
import mod.steamnsteel.tileentity.PipeTE;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeBlock extends SteamNSteelBlock implements ITileEntityProvider
{
    public static final String NAME = "pipe";
    private static int RenderId;

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
        return RenderId;
    }

    public static void setRenderType(int renderId) { RenderId = renderId; }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block newBlockType)
    {
        PipeTE entity = (PipeTE)world.getTileEntity(x, y, z);
        entity.checkEnds();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float u, float v, float w)
    {
        if (player != null) {
            ItemStack itemInUse = player.inventory.mainInventory[player.inventory.currentItem];
            if (itemInUse != null && itemInUse.getItem() == Items.bone)
            {
                if (!world.isRemote) {
                    PipeTE entity = (PipeTE) world.getTileEntity(x, y, z);
                    entity.rotatePipe();
                }
                return true;
            }
            /*if (itemInUse != null && itemInUse.getItem() == Items.name_tag) {
                BasePlumbingTE entity = (BasePlumbingTE) world.getTileEntity(x, y, z);
                Logger.info("%s - Entity Check - %s", world.isRemote ? "client" : "server", entity.toString());
            }*/
        }

        return false;
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int metadata)
    {
        if (!world.isRemote) {
            PipeTE entity = (PipeTE) world.getTileEntity(x, y, z);
            if (entity != null)
            {
                entity.detach();
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof PipeTE)
        {
            PipeTE te = (PipeTE)tileEntity;

            ForgeDirection direction = ForgeDirection.EAST;
            int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            if (facing == 0)
            {
                direction = ForgeDirection.NORTH;
            }
            else if (facing == 1)
            {
                direction = ForgeDirection.EAST;
            }
            else if (facing == 2)
            {
                direction = ForgeDirection.SOUTH;
            }
            else if (facing == 3)
            {
                direction = ForgeDirection.WEST;
            }

            te.setOrientation(direction);
            te.checkEnds();
        }
    }
}