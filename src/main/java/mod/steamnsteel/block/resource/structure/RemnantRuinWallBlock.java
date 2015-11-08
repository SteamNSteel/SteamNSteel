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
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class RemnantRuinWallBlock extends SteamNSteelBlock
{
    public static final String NAME = "remnantRuinWall";
    //ProceduralConnectedTexture textureManager;

    public RemnantRuinWallBlock()
    {
        super(Material.rock);
        setUnlocalizedName(NAME);
    }

    @Override
    protected BlockState createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] {
                ModProperties.PROPERTY_BLOCK_POS,
                ModProperties.PROPERTY_BLOCK_ACCESS
        });
    }

    /*
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        //blockIcon = iconRegister.registerIcon(getUnwrappedUnlocalizedName(getUnlocalizedName()));

        textureManager = new RemnantRuinWallTexture();
        textureManager.registerIcons(iconRegister);
        blockIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":" + "remnantRuinWall/Wall_Default");
    }
    */

    /*@Override
    public boolean onBlockActivated(World world, BlockPos pos EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            String description = textureManager.describeTextureAt(world, WorldBlockCoord.of(pos), side);
            player.addChatComponentMessage(new ChatComponentText(description));
        }
        return super.onBlockActivated(world, pos, player, side, p_149727_7_, p_149727_8_, p_149727_9_);
    }*/

    /*long[] durations = new long[10];
    int index = 0;
    int sidesCalculated = 0;
    long currentMillis;

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, BlockPos pos, int side)
    {
        long startTime = System.currentTimeMillis();
        if (startTime - currentMillis > 1000) {
            //Logger.info("%d sides calculated in 1 seconds", sidesCalculated);
            currentMillis = startTime;
            sidesCalculated = 0;
        }
        final IIcon iconForSide = textureManager.getIconForSide(blockAccess, pos, side);
        long endTime = System.currentTimeMillis();
        sidesCalculated++;
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        durations[index++] = duration;
        if (index == durations.length) {
            index = 0;

            long sum = 0;
            for (int i = 0; i < durations.length; i++) {
                sum += durations[i];
            }
            //Logger.info("ProceduralWall took %f milliseconds", (sum / (float)durations.length));
            durations = new long[100];
        }


        return iconForSide;
    }*/

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
