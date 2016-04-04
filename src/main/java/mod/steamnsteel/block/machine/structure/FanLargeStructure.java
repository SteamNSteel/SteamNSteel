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
import mod.steamnsteel.client.model.opengex.OpenGEXAnimationFrameProperty;
import mod.steamnsteel.tileentity.structure.LargeFanTE;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static com.foudroyantfactotum.tool.structure.block.StructureShapeBlock.DIRECTION;

public class FanLargeStructure extends SteamNSteelStructureBlock
{
    public static final PropertyBool RENDER_DYNAMIC = PropertyBool.create("render_dynamic");

    public static int counter;

    public FanLargeStructure()
    {
        super(true, true);
        setDefaultState(
                blockState
                        .getBaseState()
                        .withProperty(DIRECTION, EnumFacing.NORTH)
                        .withProperty(MIRROR, false)
                        .withProperty(RENDER_DYNAMIC, false)
        );
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[]{DIRECTION, MIRROR, RENDER_DYNAMIC}, new IUnlistedProperty[]{OpenGEXAnimationFrameProperty.instance});
    }

    @Override
    public boolean onStructureBlockActivated(World world, BlockPos pos, EntityPlayer player, BlockPos callPos, EnumFacing side, BlockPos local, float sx, float sy, float sz)
    {
        if (world.isRemote)
        {
            Logger.info("click " + counter);

            if (player.isSneaking())
                --counter;
            else
                ++counter;

            //if(counter >= model.getNode().getKeys().size()) counter = 0;
            world.markBlockRangeForRenderUpdate(pos, pos);
        }

        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return super.getStateFromMeta(meta);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean canMirror()
    {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new LargeFanTE(getPattern(), state.getValue(DIRECTION), state.getValue(MIRROR));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void spawnBreakParticle(World world, StructureTE te, BlockPos local, float sx, float sy, float sz)
    {
        /*final int x = coord.getX();
        final int y = coord.getY();
        final int z = coord.getZ();

        final Block block = coord.getStructureDefinition().getBlock(coord.getLX(), coord.getLY(), coord.getLZ());

        if (block != null)
        {
            for (int i = 0; i < 5; ++i)
            {
                world.spawnParticle("explode", x + rndRC(), y + 1, z + rndRC(), sx, sy, sz);
                world.spawnParticle("explode", x, y + 0.5, z, sx, sy, sz);
                world.spawnParticle("explode", x + rndRC(), y, z + rndRC(), sx, sy, sz);
            }
        }*/
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn)
    {
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);

        /*final List<AxisAlignedBB> collisionList = new ArrayList<>(1);

        localToGlobalCollisionBoxes(
            pos.getX(), pos.getY(), pos.getZ(),
            mask, collisionList, new float[][]{{0.1f,0.1f,0.3f, 2.9f,2.9f,0.7f}},
            state.getValue(FACING), getMirror(state), getPattern().getBlockBounds());

        if (!collisionList.isEmpty())
        {
            collidingEntity.attackEntityFrom(DamageSource.generic, 5.0f);
        }*/
    }

    @Override
    public StructureDefinitionBuilder getStructureBuild()
    {
        final StructureDefinitionBuilder builder = new StructureDefinitionBuilder();

        builder.assignConstructionDef(ImmutableMap.of(
                'c', "minecraft:cobblestone",
                's', "minecraft:sand"
        ));
        builder.assignConstructionBlocks(
                new String[]{
                        "ccc"
                },
                new String[]{
                        "csc"
                },
                new String[]{
                        "ccc"
                }
        );

        builder.assignToolFormPosition(BlockPosUtil.of(1,1,0));

        builder.setConfiguration(BlockPosUtil.of(0,0,0),
                new String[]{
                        "---"
                },
                new String[]{
                        "-M-"
                },
                new String[]{
                        "---"
                }
        );

        builder.setCollisionBoxes(
                new float[]{0.0f,0.0f,0.0f, 3.0f,0.19f,1.0f}, //bottom centre
                new float[]{0.0f,2.81f,0.0f, 3.0f,3.0f,1.0f}, //top centre
                new float[]{0.0f,0.0f,0.0f, 0.19f,3.0f,1.0f}, //left centre
                new float[]{2.81f,0.0f,0.0f, 3.0f,3.0f,1.0f}, //right centre
                new float[]{0.0f,0.0f,0.0f, 0.5f,0.5f,1.0f},  //lower left corner
                new float[]{2.5f,0.0f,0.0f, 3.0f,0.5f,1.0f},  //lower right corner
                new float[]{0.0f,2.5f,0.0f, 0.5f,3.0f,1.0f},  //top left corner
                new float[]{2.5f,2.5f,0.0f, 3.0f,3.0f,1.0f},  //top right corner
                new float[]{0.0f,0.0f,0.21f, 3.0f,3.0f,0.29f} //back centre
        );

        return builder;
    }
}

