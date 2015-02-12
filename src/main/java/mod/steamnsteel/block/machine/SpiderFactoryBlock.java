package mod.steamnsteel.block.machine;

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.tileentity.SpiderFactoryTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SpiderFactoryBlock extends SteamNSteelBlock implements ITileEntityProvider {

    public static final String NAME = "spiderFactory";

    public SpiderFactoryBlock() {
        super(Material.iron, true);
        setBlockName(NAME);
        setHardness(Float.MAX_VALUE);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new SpiderFactoryTE();
    }


    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            boolean isValid = false;
            for (int xCheck = -1; xCheck <= 1; xCheck += 2) {
                for (int zCheck = -1; zCheck <= 1; zCheck += 2) {
                    if (world.getBlock(x + xCheck, y, z + zCheck) == ModBlock.spiderFactory &&
                            world.getBlock(x, y + 1, z) == ModBlock.spiderFactory &&
                            world.getBlock(x + xCheck, y + 1, z + zCheck) == ModBlock.spiderFactory) {
                        isValid = true;
                    }
                }
            }
            //Structure invalid
            if (!isValid) {
                world.setBlock(x, y, z, Blocks.air);
                world.notifyBlockChange(x, y, z, this);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, x, entity, itemStack);

        final int orientation = BlockDirectional.getDirection(MathHelper.floor_double(entity.rotationYaw * 4.0f / 360.0f + 0.5));
        world.setBlockMetadataWithNotify(x, y, z, orientation, 0);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        super.onBlockClicked(world, x, y, z, player);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int p_149696_5_, int p_149696_6_)
    {
        super.onBlockEventReceived(world, x, y, z, p_149696_5_, p_149696_6_);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        return tileentity != null && tileentity.receiveClientEvent(p_149696_5_, p_149696_6_);
    }
}
