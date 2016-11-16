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

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.library.ModProperties;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class RemnantRuinWallBlock extends SteamNSteelBlock
{
    public RemnantRuinWallBlock()
    {
        super(Material.ROCK);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] {
                ModProperties.PROPERTY_BLOCK_POS,
                ModProperties.PROPERTY_BLOCK_ACCESS
        });
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        /*if (worldIn.isRemote) {
            String description = PCTModelLoader.describeTextureAt(worldIn, pos, side);
            playerIn.addChatComponentMessage(new ChatComponentText(description));
        }*/
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state instanceof IExtendedBlockState) {
            BlockPos blockPos = pos;
            if (blockPos instanceof MutableBlockPos) {
                blockPos = new BlockPos(pos);
            }

            return ((IExtendedBlockState) state)
                    .withProperty(ModProperties.PROPERTY_BLOCK_POS, blockPos)
                    .withProperty(ModProperties.PROPERTY_BLOCK_ACCESS, world);
        }

        //Shouldn't ever happen, but just in case.
        return state;
    }
}
