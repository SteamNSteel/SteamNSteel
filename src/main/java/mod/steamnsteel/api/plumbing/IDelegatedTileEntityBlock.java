package mod.steamnsteel.api.plumbing;

import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IDelegatedTileEntityBlock
{
    TileEntity getDelegatedTileEntity(World worldObj, WorldBlockCoord blockCoord);
}
