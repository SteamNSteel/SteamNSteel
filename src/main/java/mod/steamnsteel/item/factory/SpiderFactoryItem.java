package mod.steamnsteel.item.factory;

import mod.steamnsteel.entity.SpiderFactoryEntity;
import mod.steamnsteel.item.SteamNSteelItem;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.library.ModItem;
import mod.steamnsteel.tileentity.SpiderFactoryTE;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Steven on 5/04/2015.
 */
public class SpiderFactoryItem extends SteamNSteelItem {

    public SpiderFactoryItem() {
        this.setUnlocalizedName(ModItem.Names.SPIDER_FACTORY);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return EnumChatFormatting.RED + super.getItemStackDisplayName(itemStack) + EnumChatFormatting.RESET;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float u, float v, float w) {
        ForgeDirection orientation = ForgeDirection.getOrientation(side);
        x += orientation.offsetX;
        y += orientation.offsetY;
        z += orientation.offsetZ;

        double facing = -player.rotationYaw;
        while (facing < 0) {
            facing += 360;
        }

        final int direction = BlockDirectional.getDirection(MathHelper.floor_double(facing * 4.0f / 360.0f + 0.5));
        createSpiderFactory(world, x, y, z, direction);
        return true;
    }

    final static WorldBlockCoord[] blocks = {
            WorldBlockCoord.of(0,0,0),
            WorldBlockCoord.of(0,0,1),
            WorldBlockCoord.of(0,1,0),
            WorldBlockCoord.of(0,1,1)
    };

    public static boolean createSpiderFactory(World world, int x, int y, int z, int direction) {

        WorldBlockCoord parentPosition = WorldBlockCoord.of(x, y, z);

        for (final WorldBlockCoord blockCoord : blocks)
        {
            WorldBlockCoord newPos = rotateBlockCoord(parentPosition, direction, blockCoord);

            Block block = newPos.getBlock(world);
            if (!block.isReplaceable(world, newPos.getX(), newPos.getY(), newPos.getZ())) {
                return false;
            }
        }

        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (final WorldBlockCoord blockCoord : blocks)
        {
            boolean isMaster = blockCoord.getX() == 0 && blockCoord.getY() == 0 && blockCoord.getZ() == 0;

            minX = Math.min(minX, blockCoord.getX());
            maxX = Math.max(maxX, blockCoord.getX());
            minZ = Math.min(minZ, blockCoord.getZ());
            maxZ = Math.max(maxZ, blockCoord.getZ());

            WorldBlockCoord newPos = rotateBlockCoord(parentPosition, direction * 90, blockCoord);



            newPos.setBlock(world, ModBlock.spiderFactory, direction | (isMaster ? 0 : 4), 3);

            SpiderFactoryTE te = (SpiderFactoryTE)newPos.getTileEntity(world);
            if (!isMaster) {
                te.setParent(parentPosition);
            }
        }

        if (!world.isRemote) {

            SpiderFactoryEntity entity = (SpiderFactoryEntity) EntityList.createEntityByName(
                    (String) EntityList.classToStringMapping.get(SpiderFactoryEntity.class),
                    world
            );

            double centreXPos = ((maxX + 1 - minX) / 2.0d) + minX;
            double centreZPos = ((maxZ + 1 - minZ) / 2.0d) + minZ;

            double cos = MathHelper.cos(direction * 90);
            double sin = MathHelper.sin(direction * 90);

            double entityXPos = centreXPos * cos + centreZPos * sin;
            double entityZPos = centreZPos * cos - centreXPos * sin;

            Logger.info("CenterPos - (%f, %f)", centreXPos, centreZPos);
            Logger.info("EntityPos - (%f, %f)", entityXPos, entityZPos);
            Logger.info("%f, %f", parentPosition.getX() + entityXPos, parentPosition.getZ() + entityZPos);
            entity.setPositionAndRotation(parentPosition.getX() + entityXPos, y, parentPosition.getZ() + entityZPos, direction * 90, 0);
            entity.setMasterBlockLocation(parentPosition);
            world.spawnEntityInWorld(entity);
        }
        return true;
    }

    private static WorldBlockCoord rotateBlockCoord(WorldBlockCoord parentPosition, double degrees, WorldBlockCoord blockCoord)
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

    public static void validateFactory(World world, int x, int y, int z) {
        SpiderFactoryTE tileEntity = (SpiderFactoryTE)world.getTileEntity(x, y, z);
        if (tileEntity == null) return;
        if (tileEntity.isSlave()) {
            tileEntity = (SpiderFactoryTE) tileEntity.getMaster().getTileEntity(world);
        }
        if (tileEntity == null) {
            world.setBlock(x, y, z, Blocks.air, 0, 3);
            return;
        }

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
}
