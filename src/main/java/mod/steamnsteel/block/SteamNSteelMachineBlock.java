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

package mod.steamnsteel.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class SteamNSteelMachineBlock extends SteamNSteelDirectionalBlock
{
    public static final Material MATERIAL = Material.piston;
    public static final SoundType SOUND = Block.soundTypePiston;
    public static final float HARDNESS = 0.5f;

    protected SteamNSteelMachineBlock()
    {
        super(MATERIAL);
        setStepSound(SOUND);
        setHardness(HARDNESS);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
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
    public int getMobilityFlag()
    {
        // total immobility and stop pistons
        return 2;
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventParameter)
    {
        super.onBlockEventReceived(world, x, y, z, eventId, eventParameter);
        final TileEntity te = world.getTileEntity(x, y, z);
        return te != null && te.receiveClientEvent(eventId, eventParameter);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        // no op
    }
}
