package mod.steamnsteel.block.resource.structure;

import mod.steamnsteel.block.SteamNSteelPaneBlock;
import net.minecraft.block.material.Material;

public class MossyIronBarsBlock extends SteamNSteelPaneBlock
{
    public static final String NAME = "ironBarsMoss";

    public MossyIronBarsBlock()
    {
        super(NAME, Material.iron);

        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setBlockName(NAME);
    }
}
