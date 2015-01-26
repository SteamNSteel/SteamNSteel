package mod.steamnsteel.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ObsoleteBlock extends Block
{
    public ObsoleteBlock(String unlocalizedName)
    {
        super(Material.iron);
        setBlockName(unlocalizedName);

    }
}
