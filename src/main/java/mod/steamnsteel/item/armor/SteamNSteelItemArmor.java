package mod.steamnsteel.item.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.library.Reference;
import mod.steamnsteel.library.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class SteamNSteelItemArmor extends ItemArmor{

    public SteamNSteelItemArmor(ArmorMaterial material, int armorType, int renderIndex, String name){
        super(material, armorType, renderIndex);
        this.setCreativeTab(Reference.CREATIVE_TAB);
        this.setUnlocalizedName(name);
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("item.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        final String unlocalizedName = getUnlocalizedName();
        itemIcon = iconRegister.registerIcon(unlocalizedName.substring(unlocalizedName.indexOf('.') + 1));
    }

    @SuppressWarnings("WeakerAccess")
    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
    }


}
