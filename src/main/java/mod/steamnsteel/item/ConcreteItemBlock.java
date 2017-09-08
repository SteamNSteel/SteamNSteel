package mod.steamnsteel.item;

import mod.steamnsteel.Reference;
import mod.steamnsteel.library.BlockLibrary;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConcreteItemBlock extends ItemBlock
{
	public ConcreteItemBlock(Block block)
	{
		super(block);
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		int placedBlocks = 0;
		final Block blockBeneath = world.getBlockState(pos.down()).getBlock();
		if (blockBeneath == BlockLibrary.concrete || blockBeneath == BlockLibrary.concreteWet && !player.isSneaking()) {
			pos = pos.down();
		} else
		{
			if (!world.setBlockState(pos, newState, 11)) return false;
			placedBlocks++;
		}

		final BlockPos[] donut = {
				pos.offset(EnumFacing.NORTH),
				pos.offset(EnumFacing.NORTH).offset(EnumFacing.EAST),
				pos.offset(EnumFacing.EAST),
				pos.offset(EnumFacing.EAST).offset(EnumFacing.SOUTH),
				pos.offset(EnumFacing.SOUTH),
				pos.offset(EnumFacing.SOUTH).offset(EnumFacing.WEST),
				pos.offset(EnumFacing.WEST),
				pos.offset(EnumFacing.WEST).offset(EnumFacing.NORTH)
		};

		final IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.block)
		{
			this.block.onBlockPlacedBy(world, pos, state, player, stack);

			if (player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
		}

		for (final BlockPos blockPos : donut)
		{
			if (world.mayPlace(this.block, blockPos, false, EnumFacing.UP, player))
			{
				final BlockPos down = blockPos.down();
				final IBlockState blockState = world.getBlockState(down);
				boolean isSideBelowSolid = blockState.isSideSolid(world, down, EnumFacing.UP);

				if (isSideBelowSolid)
				{
					if (world.setBlockState(blockPos, this.block.getDefaultState(), Reference.SetBlockState.SendChange)) {
						placedBlocks++;
					}
				}
			}
		}

		return placedBlocks > 0;
	}
}
