package mod.steamnsteel.block.utility;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.block.SteamNSteelDirectionalBlock;
import mod.steamnsteel.gui.ModGuis;
import mod.steamnsteel.library.Reference;
import mod.steamnsteel.library.Reference.BlockNames;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by codew on 4/01/2016.
 */
public class ProjectTableBlock extends SteamNSteelDirectionalBlock
{

    public ProjectTableBlock() {

        super(Material.WOOD, true);
        setUnlocalizedName(BlockNames.PROJECT_TABLE);
        setHarvestLevel("axe", 1);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(TheMod.instance, ModGuis.PROJECT_TABLE.getID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
