package mod.steamnsteel.block.machine;

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.tileentity.PipeJunctionTE;
import mod.steamnsteel.tileentity.PipeTE;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class PipeJunctionBlock extends SteamNSteelBlock implements ITileEntityProvider
{
    public static final String NAME = "pipeJunction";
    private static int RenderId;

    public PipeJunctionBlock()
    {
        super(Material.circuits, true);
        setUnlocalizedName(NAME);
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new PipeJunctionTE();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity instanceof IUpdatePlayerListBox) {
            ((IUpdatePlayerListBox) entity).update();
        }
    }
}
