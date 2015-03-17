package mod.steamnsteel.block.machine;

import cpw.mods.fml.common.Mod;
import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.tileentity.SpiderFactoryTE;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
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

    final WorldBlockCoord[] blocks = {
            WorldBlockCoord.of(0,0,0),
            WorldBlockCoord.of(-1,0,0),
            WorldBlockCoord.of(0,0,1),
            WorldBlockCoord.of(-1,0,1),
            WorldBlockCoord.of(0,1,0),
            WorldBlockCoord.of(-1,1,0),
            WorldBlockCoord.of(0,1,1),
            WorldBlockCoord.of(-1,1,1)
    };

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
    public TileEntity createNewTileEntity(World world, int metadata)
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
        SpiderFactoryTE tileEntity = (SpiderFactoryTE)world.getTileEntity(x, y, z);
        if (tileEntity == null) return;
        if (tileEntity.isSlave()) {
            tileEntity = (SpiderFactoryTE) tileEntity.getMaster().getTileEntity(world);
        }
        if (tileEntity == null) {
            world.setBlock(x, y, z, Blocks.air, 0, 3);
            return;
        };

        WorldBlockCoord parentPosition = tileEntity.getWorldBlockCoord();
        int orientation = parentPosition.getBlockMetadata(world);
        boolean isValid = true;
        for (final WorldBlockCoord blockCoord : blocks)
        {
            WorldBlockCoord newPos = rotateBlockCoord(parentPosition, orientation * 90, blockCoord);
            if (newPos.getBlock(world) != ModBlock.spiderFactory) {
                isValid = false;
                break;
            }
        }

        if (!isValid) {
            for (final WorldBlockCoord blockCoord : blocks)
            {
                WorldBlockCoord newPos = rotateBlockCoord(parentPosition, orientation * 90, blockCoord);
                newPos.setBlock(world, Blocks.air, 0, 3);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, x, entity, itemStack);
        double facing = -entity.rotationYaw;
        while (facing < 0) {
            facing += 360;
        }

        final int orientation = BlockDirectional.getDirection(MathHelper.floor_double(facing * 4.0f / 360.0f + 0.5));
        world.setBlockMetadataWithNotify(x, y, z, orientation, 0);

        WorldBlockCoord parentPosition = WorldBlockCoord.of(x, y, z);

        double direction = MathHelper.floor_double(facing * 4.0 / 360.0 + 0.5) * 90;
        for (final WorldBlockCoord blockCoord : blocks)
        {
            if (blockCoord.getX() == 0 && blockCoord.getY() == 0 && blockCoord.getZ() == 0) continue;

            WorldBlockCoord newPos = rotateBlockCoord(parentPosition, direction, blockCoord);

            newPos.setBlock(world, ModBlock.spiderFactory, orientation | 4, 0);

            SpiderFactoryTE te = (SpiderFactoryTE)newPos.getTileEntity(world);
            te.setParent(parentPosition);
        }
    }

    private WorldBlockCoord rotateBlockCoord(WorldBlockCoord parentPosition, double degrees, WorldBlockCoord blockCoord)
    {
        float radians = (float)Math.toRadians(degrees);

        double cos = MathHelper.cos(radians);
        double sin = MathHelper.sin(radians);
        return WorldBlockCoord.of(
                parentPosition.getX() + (int)Math.round(blockCoord.getX() * cos + blockCoord.getZ() * sin),
                parentPosition.getY() + blockCoord.getY(),
                parentPosition.getZ() + (int)Math.round(blockCoord.getZ() * cos - blockCoord.getX() * sin)
        );
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
