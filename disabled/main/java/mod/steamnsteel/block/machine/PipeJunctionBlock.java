package mod.steamnsteel.block.machine;

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.tileentity.PipeJunctionTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PipeJunctionBlock extends SteamNSteelBlock implements ITileEntityProvider
{
    public PipeJunctionBlock()
    {
        super(Material.CIRCUITS, true);
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new PipeJunctionTE();
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        TileEntity entity = world.getTileEntity(pos);
        if (entity instanceof ITickable) {
            ((ITickable) entity).update();
        }
    }
}
