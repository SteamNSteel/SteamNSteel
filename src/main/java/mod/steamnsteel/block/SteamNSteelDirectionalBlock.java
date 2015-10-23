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

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

abstract class SteamNSteelDirectionalBlock extends SteamNSteelBlock
{
    SteamNSteelDirectionalBlock(Material material)
    {
        super(material);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, EntityLivingBase entity, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, blockPos, entity, itemStack);

        final int orientation = BlockDirectional.getDirection(MathHelper.floor_double(entity.rotationYaw * 4.0f / 360.0f + 0.5));
        world.setBlockMetadataWithNotify(blockPos, orientation, 0);
    }
}
