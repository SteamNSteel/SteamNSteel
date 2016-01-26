package mod.steamnsteel.api.plumbing;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IDelegatedTileEntityBlock
{
    TileEntity getDelegatedTileEntity(World worldObj, BlockPos blockCoord);
}
