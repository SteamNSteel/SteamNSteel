package mod.steamnsteel.block.resource.structure;

import mod.steamnsteel.block.SteamNSteelPaneBlock;
import net.minecraft.block.material.Material;

public class MossyRustyIronBarsBlock extends SteamNSteelPaneBlock
{
    public static final String NAME = "ironBarsMossRust";

    public MossyRustyIronBarsBlock()
    {
        super(NAME, Material.iron);

        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setBlockName(NAME);
    }
}
