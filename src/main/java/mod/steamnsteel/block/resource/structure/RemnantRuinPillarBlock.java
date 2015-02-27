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

package mod.steamnsteel.block.resource.structure;

import mod.steamnsteel.block.*;
import mod.steamnsteel.tileentity.RemnantRuinPillarTE;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class RemnantRuinPillarBlock extends SteamNSteelBlock
{
    public static final String NAME = "remnantRuinPillar";
    private static int renderId;

    public RemnantRuinPillarBlock()
    {
        super(Material.rock);
        setBlockName(NAME);
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


    public static void setRenderType(int renderType)
    {
        renderId = renderType;
    }

    @Override
    public int getRenderType()
    {
        return renderId;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new RemnantRuinPillarTE();
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        final int orientation = BlockDirectional.getDirection(MathHelper.floor_double(entity.rotationYaw * 4.0f / 360.0f + 0.5));
        world.setBlockMetadataWithNotify(x, y, z, orientation & 1, 0);
    }


}
