package mod.steamnsteel.block.resource.structure;

import mod.steamnsteel.block.SteamNSteelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Random;

/**
 * <p>
 * Info about this class goes HERE!
 * </p>
 *
 * @author Bret 'Horfius' Dusseault
 * @version ${VERSION}
 */
public class ConcreteBlock extends SteamNSteelBlock {
    private static final PropertyInteger WETNESS = PropertyInteger.create("wetness", 0, 5);

    public static final String NAME = "blockConcrete";

    public ConcreteBlock() {
        super(Material.rock);
        setUnlocalizedName(NAME);
        setTickRandomly(true);
        setSoundType(SoundType.STONE);
        setDefaultState(blockState.getBaseState().withProperty(WETNESS, 0));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        worldIn.getBlockState(pos).getBlock().setResistance(meta * 3.0F);
        worldIn.getBlockState(pos).getBlock().setHardness(meta * .8F + .5F);
        worldIn.getBlockState(pos).getBlock().setHarvestLevel("pickaxe", meta >= 5 ? 2 : 0);

        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        float temp = worldIn.getBiomeGenForCoords(pos).getTemperature();
        int currentMeta = worldIn.getBlockState(pos).getBlock().getMetaFromState(state);

        if(currentMeta == 5) return;
        //Higher is worse
        int chanceToDry = 25 + rand.nextInt(10 - (int)(temp * 5)); //25 to 35

        if(worldIn.isRaining()){
            chanceToDry *= 1.25; //31 to 43
        }else{
            chanceToDry -= 2 + rand.nextInt(4); //20 to 33
        }

        if(worldIn.canBlockSeeSky(pos) && worldIn.isDaytime()){
            chanceToDry -= 3 + rand.nextInt(6);//23 to 40 or 12 to 30
        }else if(worldIn.canBlockSeeSky(pos)){
            chanceToDry -= 2 + rand.nextInt(5);//25 to 41 or 14 to 31
        }

        //Sets of data possible so far: [23,40], [12,30], [25,41], [14,31]
        int num2 = rand.nextInt(5);//0-4
        if(num2 < 4){//80% chance of proceeding
            //The more blocks of concrete around, the longer it takes to dry
            int num3 = (worldIn.getBlockState(pos.east()).getBlock() instanceof ConcreteBlock) ? rand.nextInt(2) + 2 : 1;//2 to 3 or 1
            int num4 = (worldIn.getBlockState(pos.west()).getBlock() instanceof ConcreteBlock) ? rand.nextInt(2) + 2 : 1;//2 to 3 or 1
            int num5 = (worldIn.getBlockState(pos.south()).getBlock() instanceof ConcreteBlock) ? rand.nextInt(2) + 2 : 1;//2 to 3 or 1
            int num6 = (worldIn.getBlockState(pos.north()).getBlock() instanceof ConcreteBlock) ? rand.nextInt(2) + 2 : 1;//2 to 3 or 1
            int num7 = (worldIn.getBlockState(pos.up()).getBlock() instanceof ConcreteBlock) ? rand.nextInt(2) + 2 : 1;//2 to 3 or 1
            int num8 = (worldIn.getBlockState(pos.down()).getBlock() instanceof ConcreteBlock) ? rand.nextInt(2) + 2 : 1;//2 to 3 or 1
            int xBlocks = num3 + num4;//2, 3 to 4, or 4 to 6
            int zBlocks = num5 + num6;//2, 3 to 4, or 4 to 6
            int yBlocks = num7 + num8;//2, 3 to 4, or 4 to 6

            //2 - (0 to 1) < 3 = 100%
            //(3 to 4) - (0 to 1) = 3;2;4;3 < 3 = 75%
            //(4 to 6) - (0 to 1) = 4;3;5;4;6;5 < 3 = 16.67%
            //Sum chance of 63.89%, although that is not accurate for any case, really

            //Horizontal not as bad for drying
            if(xBlocks - rand.nextInt(2) <= 3){
                //(51.11%) [18,32], [9,24], [20,32], [11,24]
                chanceToDry /= 1.25;
            }

            //Horizontal not as bad for drying
            if(zBlocks - rand.nextInt(2) <= 3){
                //With xBlocks (32.66%): [14,25], [7,19], [16,25], [8,19]
                //Without xBlocks (51.11%): [18,32], [9,24], [20,32], [11,24]
                chanceToDry /= 1.25;
            }

            //Vertical stacking is worse for stacking
            if(yBlocks - rand.nextInt(2) <= 3){
                //With xBlocks and zBlocks (20.86%): [9,16], [4,12], [10,16], [5,12]
                //With one of those (32.66%): [12,21], [6,16], [13,21], [7,16]
                //With neither (51.11%): [15,26], [8,20], [16,27], [9,20]
                chanceToDry /= 1.5;
            }

            //6, 7 to 8, 8 to 10, 8 to 9, 9 to 11, 9 to 12, 12 to 18, 11 to 16, 10 to 14
            if(yBlocks + zBlocks + xBlocks >= 11){//Probably about 50%, too lazy to do all calculations
                //With all passes (8.34%): [13,24], [6,18], [15,24], [7,18]
                //With both horizontal (13.06%): [21,37], [10,28], [24,37], [12,28]
                //With one horizontal and vertical (13.06%): [18,31], [9,24], [19,31], [10,24]
                //With one horizontal (20.44%): [27,48], [13,36], [30,48], [16,36]
                //With vertical (20.44%): [22,39], [12,30], [24,40], [13,30]
                //With none (40.00%): [34,60], [18,45], [37,61], [21,46]
                chanceToDry *= 1.5;
            }
        }

        //Possible sets of data:
        //(20.00%) [23,40], [12,30], [25,41], [14,31] = 16.67% pass rate
        //(8.34%)  [13,24], [6,18], [15,24], [7,18]   = 74.47% pass rate
        //(13.06%) [21,37], [10,28], [24,37], [12,28] = 23.88% pass rate
        //(13.06%) [18,31], [9,24], [19,31], [10,24]  = 34.48% pass rate
        //(20.44%) [27,48], [13,36], [30,48], [16,36] = 10.47% pass rate
        //(20.44%) [22,39], [12,30], [24,40], [13,30] = 18.06% pass rate
        //(40.00%) [34,60], [18,45], [37,61], [21,46] = 0.94% pass rate

        if(chanceToDry <= 18){ //9 to 11
            currentMeta++;
            worldIn.setBlockState(pos, getDefaultState().withProperty(WETNESS, currentMeta), 2);
            worldIn.getBlockState(pos).getBlock().setResistance(currentMeta * 3.0F);
            worldIn.getBlockState(pos).getBlock().setHardness(.8F * currentMeta + .5F);
            worldIn.getBlockState(pos).getBlock().setHarvestLevel("pickaxe", currentMeta >= 5 ? 2 : 0);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WETNESS);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(WETNESS, meta <= 5 ? meta : 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(WETNESS);
    }

    @Override
    public int damageDropped(IBlockState state) {
        System.out.println(state.getValue(WETNESS));
        return state.getValue(WETNESS) == 5 ? 5 : 0;
    }
}
