package mod.steamnsteel.block.resource.structure;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.block.SteamNSteelBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
         
public class Concrete extends SteamNSteelBlock{

    //Uses the names blockConcrete_(0-4).png, so blockConcrete_0.png is the first stage (when it's freshly made)
    @SideOnly(Side.CLIENT)
    private IIcon[] textures = new IIcon[5];
    public static final String NAME = "blockConcrete";

    public Concrete(){
        super(Material.rock);
        this.setBlockName(NAME);
        this.setTickRandomly(true);
        this.setStepSound(SteamNSteelBlock.soundTypeStone);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        for(int i = 0; i < textures.length; i++  ){
            textures[i] = iconRegister.registerIcon(String.format("%s%s%s", getUnwrappedUnlocalizedName(getUnlocalizedName()), "_", i));
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return textures[meta];
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return textures[world.getBlockMetadata(x, y, z)];
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        world.getBlock(x, y, z).setResistance(metadata * 2.0F);
        world.getBlock(x, y, z).setHardness(metadata * 1.8F + 1.5F); //Changes the values the more dry it is
        return super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }

    //Will return the hardened concrete if it is done drying, otherwise the starting to dry stuff
    @Override
    public int damageDropped(int meta) {
        return meta == 4 ? 4 : 0;
    }

    //Lots of randomized info, temp causes  -5 at most usually. Having concrete around each other makes it dry more slowly. Rain also slows it down
    @SideOnly(Side.SERVER)
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        float temp = world.getBiomeGenForCoords(x, z).temperature;
        int currentMeta = world.getBlockMetadata(x, y, z);
        if(currentMeta == 4) return;
        int chanceToDry = 25 + random.nextInt(15 - (int)(temp * 5));

        if(world.isRaining()){
            chanceToDry *= 1.25;
        }else{
            chanceToDry -= 2 + random.nextInt(4);
        }

        if(world.canBlockSeeTheSky(x, y, z) && world.isDaytime()){
            chanceToDry -= 3 + random.nextInt(6);
        }else if(world.canBlockSeeTheSky(x, y, z)){
            chanceToDry -= 2 + random.nextInt(5);
        }

        int num2 = random.nextInt(5);
        if(num2 < 4){
            int num3 = (world.getBlock(x + 1, y, z) instanceof Concrete) ? random.nextInt(3) : 0;
            int num4 = (world.getBlock(x - 1, y, z) instanceof Concrete) ? random.nextInt(3) : 0;
            int num5 = (world.getBlock(x, y, z + 1) instanceof Concrete) ? random.nextInt(3) : 0;
            int num6 = (world.getBlock(x, y, z - 1) instanceof Concrete) ? random.nextInt(3) : 0;
            int num7 = (world.getBlock(x, y + 1, z) instanceof Concrete) ? random.nextInt(3) : 0;
            int num8 = (world.getBlock(x, y - 1, z) instanceof Concrete) ? random.nextInt(3) : 0;
            int xBlocks = num3 + num4;
            int zBlocks = num5 + num6;
            int yBlocks = num7 + num8;

            if(xBlocks - random.nextInt(3) < 2){
                chanceToDry /= 1.25;
            }
            if(zBlocks - random.nextInt(3) < 2){
                chanceToDry /= 1.25;
            }
            if(yBlocks - random.nextInt(3) < 1){
                chanceToDry /= 1.5;
            }

            if(yBlocks + zBlocks + xBlocks > 8){
                chanceToDry *= 2.5;
            }
        }


        if(chanceToDry <= 7 + random.nextInt(3)){
            currentMeta++;
            world.setBlockMetadataWithNotify(x, y, z, currentMeta, 2);
            world.getBlock(x, y, z).setResistance(currentMeta * 2.0F);
            world.getBlock(x, y, z).setHardness(1.8F * currentMeta + 1.5F);
        }
    }
    
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 4));
    }
}