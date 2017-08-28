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

import com.foudroyantfactotum.tool.structure.block.StructureShapeBlock;
import mod.steamnsteel.TheMod;
import mod.steamnsteel.tileentity.structure.SteamNSteelStructureShapeTE;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static mod.steamnsteel.block.SteamNSteelStructureBlock.MIRROR;

public class SteamNSteelStructureShapeBlock extends StructureShapeBlock implements ITileEntityProvider
{
    public SteamNSteelStructureShapeBlock()
    {
        super();
        _DEBUG = false;
        /*setDefaultState(this.blockState
                .getBaseState()
                .withProperty(DIRECTION, EnumFacing.NORTH)
                .withProperty(MIRROR, false)
        );*/
    }

    /*@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, DIRECTION, MIRROR);
    }*/

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new SteamNSteelStructureShapeTE();
    }

    @Override
    public String getUnlocalizedName()
    {
        return "tile." + getRegistryName();
    }
}
