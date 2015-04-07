package mod.steamnsteel.item.factory;

import mod.steamnsteel.entity.SteamSpiderEntity;
import mod.steamnsteel.item.SteamNSteelItem;
import mod.steamnsteel.factory.Factory;
import mod.steamnsteel.library.ModItem;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
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
        Factory factory = Factory.getFactoryForEntity(SteamSpiderEntity.class);
        factory.createFactory(world, x, y, z, direction);
        return true;
    }
}
