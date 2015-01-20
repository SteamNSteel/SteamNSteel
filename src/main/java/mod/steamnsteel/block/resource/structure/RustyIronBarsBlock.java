package mod.steamnsteel.block.resource.structure;

import mod.steamnsteel.block.SteamNSteelPaneBlock;
import net.minecraft.block.material.Material;

public class RustyIronBarsBlock extends SteamNSteelPaneBlock
{
    public static final String NAME = "ironBarsRust";

    public RustyIronBarsBlock()
    {
        super(NAME, Material.iron);

        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setBlockName(NAME);
    }
}
