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
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RemnantRuinChestBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "remnantRuinChest";

    public RemnantRuinChestBlock() {
        setBlockName(NAME);
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        final RemnantRuinChestTE te = (RemnantRuinChestTE) world.getTileEntity(x, y, z);

        if (te != null)
        {
            dropInventory(world, WorldBlockCoord.of(x, y, z), te);
            world.func_147453_f(x, y, z, block); // notify neighbors
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
    {
        final TileEntity te = world.getTileEntity(x, y, z);

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

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(getUnwrappedUnlocalizedName(getUnlocalizedName()));
    }
}
