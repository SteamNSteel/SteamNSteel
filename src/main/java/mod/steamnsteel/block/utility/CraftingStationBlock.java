package mod.steamnsteel.block.utility;

import mod.steamnsteel.block.SteamNSteelBlock;
import mod.steamnsteel.utility.log.Logger;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
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
}
