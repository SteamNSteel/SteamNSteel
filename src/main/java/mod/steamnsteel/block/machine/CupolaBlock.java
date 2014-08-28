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

import mod.steamnsteel.block.SteamNSteelDirectionalBlock;
import mod.steamnsteel.tileentity.CupolaTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import java.util.Random;

public class CupolaBlock extends SteamNSteelDirectionalBlock implements ITileEntityProvider
{
    public static final String NAME = "cupola";

    private static final int SLAVE_MASK = 0x8;

    public CupolaBlock()
    {
        super(Material.piston);
        setBlockName(NAME);
        setStepSound(Block.soundTypePiston);
        setHardness(0.5f);
    }

    public static boolean isSlave(int metadata)
    {
        return (metadata & SLAVE_MASK) == SLAVE_MASK;
    }

    public static int codeSlaveMetadata(int metadata) {return metadata | SLAVE_MASK;}

    @Override
    public boolean onBlockActivated(World world, int x, int targetY, int z, EntityPlayer player, int side, float hitVectorX, float hitVectorY, float hitVectorZ)
    {
        int y = targetY;
        if (world.isRemote) return true;

        final int metadata = world.getBlockMetadata(x, y, z);
        if (isSlave(metadata)) y--;

        // TODO: Right Click behavior

        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new CupolaTE();
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
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float dropChance, int fortune)
    {
        if (!isSlave(metadata))
            super.dropBlockAsItemWithChance(world, x, y, z, metadata, dropChance, fortune);
    }

    @Override
    public int getMobilityFlag()
    {
        // total immobility and stop pistons
        return 2;
    }

    @Override
    public Item getItemDropped(int metadata, Random rng, int fortune)
    {
        return isSlave(metadata) ? Item.getItemById(0) : super.getItemDropped(metadata, rng, fortune);
    }

    @SuppressWarnings("ObjectEquality")
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        final int metadata = world.getBlockMetadata(x, y, z);

        if (isSlave(metadata))
        {
            if (world.getBlock(x, y - 1, z) != this)
                world.setBlockToAir(x, y, z);
        }
        else if (world.getBlock(x, y + 1, z) != this)
        {
            world.setBlockToAir(x, y, z);
            if (!world.isRemote)
                dropBlockAsItem(world, x, y, z, metadata, 0);
        }
    }

    @SuppressWarnings("ObjectEquality")
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode && isSlave(metadata))
        {
            final int targetY = y - 1;
            if (world.getBlock(x, targetY, z) == this)
                world.setBlockToAir(x, targetY, z);
        }
    }
}
