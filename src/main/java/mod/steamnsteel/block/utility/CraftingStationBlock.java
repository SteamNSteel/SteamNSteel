package mod.steamnsteel.block.utility;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.gui.ModGuis;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by codew on 4/01/2016.
 */
public class CraftingStationBlock extends SteamNSteelBlock {

    public static final String NAME = "craftingStation";

    public CraftingStationBlock() {

        super(Material.wood, true);
        setUnlocalizedName(NAME);
        //FIXME: Axe?
        setHarvestLevel("pickaxe", 1); // stone pick
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
        Logger.info("clickzing");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(TheMod.instance, ModGuis.PROJECT_TABLE.getID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
