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
package mod.steamnsteel.block.machine.structure;

import com.foudroyantfactotum.tool.structure.coordinates.BlockPosUtil;
import com.foudroyantfactotum.tool.structure.tileentity.StructureTE;
import com.foudroyantfactotum.tool.structure.utility.StructureDefinitionBuilder;
import com.google.common.collect.ImmutableMap;
import mod.steamnsteel.block.SteamNSteelStructureBlock;
import mod.steamnsteel.tileentity.structure.BoilerTE;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static net.minecraft.block.BlockDirectional.FACING;

public class SSBoilerStructure extends SteamNSteelStructureBlock
{
    public static final String NAME = "ssBoiler";

    public SSBoilerStructure()
    {
        super(true, true);
        setUnlocalizedName(NAME);
        setDefaultState(
                this.blockState
                        .getBaseState()
                        .withProperty(FACING, EnumFacing.NORTH)
                        .withProperty(MIRROR, false)
        );
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING, MIRROR);
    }

    @Override
    public void spawnBreakParticle(World world, StructureTE te, BlockPos local, float sx, float sy, float sz)
    {

    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new BoilerTE(getPattern(), state.getValue(FACING), state.getValue(MIRROR));
    }

    @Override
    public boolean onStructureBlockActivated(World world, BlockPos pos, EntityPlayer player, BlockPos callPos, EnumFacing side, BlockPos local, float sx, float sy, float sz)
    {
        Logger.info("Active: " + world.getTileEntity(pos));
        return super.onStructureBlockActivated(world, pos, player, callPos, side, local, sx, sy, sz);
    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignConstructionDef(ImmutableMap.of(
                'p', "steamnsteel:blockPlotonium",
                's', "steamnsteel:blockSteel",
                'g', "minecraft:glass_pane",
                'f', "minecraft:fire",
                'w', "minecraft:planks"
        ));

        builder.assignConstructionBlocks(
                new String[]{
                        "ppp",
                        "sss",
                        "ppp"
                },
                new String[]{
                        "ppp",
                        "sws",
                        "pgp"
                },
                new String[]{
                        "ppp",
                        "sfs",
                        "pgp"
                },
                new String[]{
                        "ppp",
                        "sss",
                        "ppp"
                }
        );

        builder.assignToolFormPosition(BlockPosUtil.of(1,2,2));

        builder.setConfiguration(BlockPosUtil.of(0,0,0),
                new String[]{
                        "M--",
                        "---",
                        "---"
                },
                new String[]{
                        "---",
                        "---",
                        "---"
                },
                new String[]{
                        "---",
                        "---",
                        "---"
                },
                new String[]{
                        "---",
                        "---",
                        "---"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.7f,3.5f,0.7f, 2.3f,4.0f,2.3f},
                new float[]{0.3f,3.0f,0.3f, 2.7f,3.5f,2.7f},
                new float[]{0.0f,1.0f,0.0f, 3.0f,3.0f,3.0f},
                new float[]{0.3f,0.5f,0.3f, 2.7f,1.0f,2.7f},
                new float[]{0.7f,0.0f,0.7f, 2.3f,0.5f,2.3f}
        );

        return builder;
    }
}
