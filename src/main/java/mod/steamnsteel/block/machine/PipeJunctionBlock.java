package mod.steamnsteel.block.machine;

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.tileentity.PipeJunctionTE;
import mod.steamnsteel.tileentity.PipeTE;
import mod.steamnsteel.tileentity.SteamNSteelTE;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PipeJunctionBlock extends SteamNSteelBlock implements ITileEntityProvider
{
    public static final String NAME = "pipeJunction";
    private static int RenderId;

    public PipeJunctionBlock()
    {
        super(Material.circuits, true);
        setBlockName(NAME);
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new PipeJunctionTE();
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return RenderId;
    }

    public static void setRenderType(int renderId) {
        RenderId = renderId;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block newBlockType)
    {
        SteamNSteelTE entity = (SteamNSteelTE)world.getTileEntity(x, y, z);
        entity.updateEntity();
    }
}
