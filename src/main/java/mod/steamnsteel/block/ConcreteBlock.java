package mod.steamnsteel.block;

import mod.steamnsteel.Reference.BlockProperties;
import mod.steamnsteel.api.ISlowConcreteDrying;
import mod.steamnsteel.library.BlockLibrary;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author Bret 'Horfius' Dusseault
 * @version ${VERSION}
 */
public class ConcreteBlock extends Block implements ISlowConcreteDrying
{
    public ConcreteBlock() {
        super(Material.ROCK);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
        setResistance(5 * 3.0f);
        setHardness(5 * 0.8f + 0.5f);
    }
}
