package mod.steamnsteel.factory;

import mod.steamnsteel.entity.SpiderFactoryEntity;
import mod.steamnsteel.library.ModBlock;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

/**
 * Created by Steven on 7/04/2015.
 */
public class SpiderFactory extends Factory {

    final static WorldBlockCoord[] blocks = {
            WorldBlockCoord.of(0, 0, 0),
            WorldBlockCoord.of(0, 0, 1),
            WorldBlockCoord.of(0, 1, 0),
            WorldBlockCoord.of(0, 1, 1)
    };

    public SpiderFactory(Class<? extends Entity> entity) {
        super(SpiderFactoryEntity.class, entity);
    }

    protected WorldBlockCoord[] getBlockLayout() {
        return blocks;
    }

    protected Block getFactoryBlock() {
        return ModBlock.spiderFactory;
    }
}
