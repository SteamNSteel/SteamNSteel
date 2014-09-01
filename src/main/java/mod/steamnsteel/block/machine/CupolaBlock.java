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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelMachineBlock;
import mod.steamnsteel.gui.ModGuis;
import mod.steamnsteel.library.ModBlocks;
import mod.steamnsteel.tileentity.CupolaTE;
import mod.steamnsteel.tileentity.FillerTE;
import mod.steamnsteel.utility.Orientation;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.Random;

public class CupolaBlock extends SteamNSteelMachineBlock implements ITileEntityProvider
{
    public static final String NAME = "cupola";

    public CupolaBlock()
    {
        setBlockName(NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new CupolaTE();
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof CupolaTE)
            if (((CupolaTE) te).isActive())
                return 15;

        return super.getLightValue(world, x, y, z);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
    {
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof CupolaTE)
        {
            player.openGui(TheMod.instance, ModGuis.CUPOLA.getID(), world, x, y, z);
        }
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (world.isAirBlock(x, y + 1, z))
        {
            final int metadata = world.getBlockMetadata(x, y, z);
            world.setBlockToAir(x, y, z);
            if (!world.isRemote)
            {
                dropBlockAsItem(world, x, y, z, metadata, 0);
            }
        }
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int metadata)
    {
        super.onPostBlockPlaced(world, x, y, z, metadata);

        final int fillerY = y + 1;
        world.setBlock(x, fillerY, z, ModBlocks.CUPOLA_FILLER, 0, 2);
        final TileEntity te = world.getTileEntity(x, fillerY, z);
        if (te instanceof FillerTE)
        {
            final FillerTE filler = (FillerTE) te;
            filler.setMasterX(x);
            filler.setMasterY(y);
            filler.setMasterZ(z);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rng)
    {
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof CupolaTE)
        {
            final CupolaTE cupola = (CupolaTE) te;
            if (cupola.isActive())
            {
                final float effectX = x + 0.5f;
                final float effectY = y + 0.5f + rng.nextFloat() * 5.0f / 16.0f;
                final float effectZ = z + 0.5f;
                final float edgeOffset = 0.52f;
                final float widthOffset = rng.nextFloat() * 0.6f - 0.3f;

                final int metadata = world.getBlockMetadata(x, y, z);
                final Orientation orientation = Orientation.getdecodedOrientation(metadata);

                switch (orientation)
                {
                    case SOUTH:
                        world.spawnParticle("smoke", effectX + widthOffset, effectY, effectZ - edgeOffset, 0.0d, 0.0d, 0.0d);
                        world.spawnParticle("flame", effectX + widthOffset, effectY, effectZ - edgeOffset, 0.0d, 0.0d, 0.0d);
                        break;

                    case WEST:
                        world.spawnParticle("smoke", effectX + edgeOffset, effectY, effectZ + widthOffset, 0.0d, 0.0d, 0.0d);
                        world.spawnParticle("flame", effectX + edgeOffset, effectY, effectZ + widthOffset, 0.0d, 0.0d, 0.0d);
                        break;

                    case NORTH:
                        world.spawnParticle("smoke", effectX + widthOffset, effectY, effectZ + edgeOffset, 0.0d, 0.0d, 0.0d);
                        world.spawnParticle("flame", effectX + widthOffset, effectY, effectZ + edgeOffset, 0.0d, 0.0d, 0.0d);
                        break;

                    case EAST:
                        world.spawnParticle("smoke", effectX - edgeOffset, effectY, effectZ + widthOffset, 0.0d, 0.0d, 0.0d);
                        world.spawnParticle("flame", effectX - edgeOffset, effectY, effectZ + widthOffset, 0.0d, 0.0d, 0.0d);
                }

                final float centerOffset1 = rng.nextFloat() * 0.6f - 0.3f;
                final float centerOffset2 = rng.nextFloat() * 0.6f - 0.3f;

                world.spawnParticle("smoke", x + 0.5d + centerOffset1, y + 2.0d, z + 0.5d + centerOffset2, 0.0d, 0.1d, 0.0d);
            }
        }
    }
}
