package mod.steamnsteel.block.aesthetic;

import mod.steamnsteel.Reference.BlockProperties;
import mod.steamnsteel.block.SteamNSteelDirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RemnantRuinPillarBlock extends SteamNSteelDirectionalBlock
{


    public RemnantRuinPillarBlock()
    {
        super(Material.ROCK);
        setDefaultState(blockState.getBaseState()
                .withProperty(BlockProperties.HORIZONTAL_FACING, EnumFacing.NORTH)
                .withProperty(BlockProperties.CONNECT_TOP, true)
                .withProperty(BlockProperties.CONNECT_BOTTOM, true)
        );
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,
                BlockProperties.HORIZONTAL_FACING,
                BlockProperties.CONNECT_TOP,
                BlockProperties.CONNECT_BOTTOM);
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        final EnumFacing thisFacing = normalizePillarFacing(state.getValue(BlockProperties.HORIZONTAL_FACING));

        final IBlockState upBlockState = worldIn.getBlockState(pos.offset(EnumFacing.UP));
        boolean useTopCap = true;
        if (upBlockState.getBlock() == this)
        {
            final EnumFacing upFacing = normalizePillarFacing(upBlockState.getValue(BlockProperties.HORIZONTAL_FACING));
            useTopCap = thisFacing != upFacing;
        }
        final IBlockState downBlockState = worldIn.getBlockState(pos.offset(EnumFacing.DOWN));
        boolean useBottomCap = true;
        if (downBlockState.getBlock() == this)
        {
            final EnumFacing downFacing = normalizePillarFacing(downBlockState.getValue(BlockProperties.HORIZONTAL_FACING));
            useBottomCap = thisFacing != downFacing;
        }

        return super.getActualState(state, worldIn, pos)
                .withProperty(BlockProperties.CONNECT_TOP, useTopCap)
                .withProperty(BlockProperties.CONNECT_BOTTOM, useBottomCap);
    }

    private EnumFacing normalizePillarFacing(EnumFacing value)
    {
        return value == EnumFacing.SOUTH || value == EnumFacing.EAST
                ? value.getOpposite()
                : value;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
