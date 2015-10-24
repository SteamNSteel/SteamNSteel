package mod.steamnsteel.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.steamnsteel.TheMod;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class SteamNSteelPaneBlock extends BlockPane
{
    protected SteamNSteelPaneBlock(String textureName, String textureTopName, Material material, boolean dropsWhenBroken, boolean addToCreativeTab)
    {
        super(textureName, textureTopName, material, dropsWhenBroken);
        if (addToCreativeTab) setCreativeTab(TheMod.CREATIVE_TAB);
    }

    public SteamNSteelPaneBlock(String textureName, Material material) {
        this(TheMod.RESOURCE_PREFIX + textureName, TheMod.RESOURCE_PREFIX + textureName, material, true, true);
    }

    @SuppressWarnings("WeakerAccess")
    protected static String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
    }

    @Override
    public String getUnlocalizedName()
    {
        //noinspection StringConcatenationMissingWhitespace
        return "tile." + TheMod.RESOURCE_PREFIX + getUnwrappedUnlocalizedName(super.getUnlocalizedName());
    }
}
