package mod.steamnsteel.factory;

import mod.steamnsteel.tileentity.SpiderFactoryTE;
import mod.steamnsteel.utility.log.Logger;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by Steven on 7/04/2015.
 */
public abstract class Factory {
    private final Class<? extends Entity> factoryEntity;
    private final Class<? extends Entity> spawnedEntity;

    protected WorldBlockCoord rotateBlockCoord(WorldBlockCoord parentPosition, double degrees, WorldBlockCoord blockCoord)
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

    protected Factory(Class<? extends Entity> factoryEntity, Class<? extends Entity> spawnedEntity) {
        this.factoryEntity = factoryEntity;
        this.spawnedEntity = spawnedEntity;
    }


    public boolean createFactory(World world, int x, int y, int z, int direction) {
        WorldBlockCoord parentPosition = WorldBlockCoord.of(x, y, z);
        WorldBlockCoord[] blocks = getBlockLayout();

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

        Block factoryBlock = getFactoryBlock();

        for (final WorldBlockCoord blockCoord : blocks)
        {
            boolean isMaster = blockCoord.getX() == 0 && blockCoord.getY() == 0 && blockCoord.getZ() == 0;
            WorldBlockCoord newPos = rotateBlockCoord(parentPosition, direction * 90, blockCoord);

            minX = Math.min(minX, newPos.getX());
            maxX = Math.max(maxX, newPos.getX());
            minZ = Math.min(minZ, newPos.getZ());
            maxZ = Math.max(maxZ, newPos.getZ());


            newPos.setBlock(world, factoryBlock, direction | (isMaster ? 0 : 4), 2);

            IFactoryTE te = (IFactoryTE)newPos.getTileEntity(world);
            if (!isMaster) {
                te.setParent(parentPosition);
            }
        }

        if (!world.isRemote) {
            IFactoryEntity entity = (IFactoryEntity) EntityList.createEntityByName(
                    (String) EntityList.classToStringMapping.get(factoryEntity),
                    world
            );

            double centreXPos = ((maxX + 1 - minX) / 2.0d) + minX;
            double centreZPos = ((maxZ + 1 - minZ) / 2.0d) + minZ;
            entity.setPositionAndRotation(centreXPos, y, centreZPos, direction * 90, 0);
            entity.setMasterBlockLocation(parentPosition);
            world.spawnEntityInWorld((Entity)entity);
        }

        return true;
    }

    protected abstract Block getFactoryBlock();

    protected abstract WorldBlockCoord[] getBlockLayout();

    public void validateFactory(World world, int x, int y, int z) {
        SpiderFactoryTE tileEntity = (SpiderFactoryTE)world.getTileEntity(x, y, z);
        if (tileEntity == null) return;
        if (tileEntity.isSlave()) {
            tileEntity = (SpiderFactoryTE) tileEntity.getMaster().getTileEntity(world);
        }
        if (tileEntity == null) {
            world.setBlock(x, y, z, Blocks.air, 0, 3);
            return;
        }

        WorldBlockCoord[] blocks = getBlockLayout();

        WorldBlockCoord parentPosition = tileEntity.getWorldBlockCoord();
        int orientation = parentPosition.getBlockMetadata(world);
        boolean isValid = true;
        for (final WorldBlockCoord blockCoord : blocks)
        {
            WorldBlockCoord newPos = rotateBlockCoord(parentPosition, orientation * 90, blockCoord);
            if (newPos.getBlock(world) != getFactoryBlock()) {
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


    private static HashMap<Class<? extends Entity>, Factory> registeredFactories = new HashMap<>();

    public static void registerFactory(Class<? extends Factory> factoryClass, Class<? extends Entity> entityClass) {
        try {
            Constructor<? extends Factory> constructor = factoryClass.getConstructor(Class.class);
            Factory factory = constructor.newInstance(entityClass);
            registeredFactories.put(entityClass, factory);
        } catch (Exception e) {
            Logger.severe("Error registering factory: %s", e);
        }
    }

    public static Factory[] getAllFactories() {
        return registeredFactories.values().toArray(new Factory[registeredFactories.values().size()]);
    }

    public static Factory getFactoryForEntity(Class<? extends Entity> entityClass) {
        return registeredFactories.get(entityClass);
    }

    public Class<? extends Entity> getSpawnedEntity() {
        return spawnedEntity;
    }


}
