package mod.steamnsteel.client.gui;

import mod.steamnsteel.TheMod;
import mod.steamnsteel.mcgui.client.gui.McGUI;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class SteamNSteelGui extends McGUI {
    private static final String LOCATION = "textures/gui/";
    private static final String FILE_EXTENSION = ".png";

    public SteamNSteelGui(Container container) {
        super(container);
    }

    protected ResourceLocation getResourceLocation(String path)
    {
        return getResourceLocation(TheMod.MOD_ID.toLowerCase(), LOCATION + path + FILE_EXTENSION);
    }

    @Override
    protected String getInventoryName() {
        return null;
    }

    protected ResourceLocation getResourceLocation(String modID, String path)
    {
        return new ResourceLocation(modID, path);
    }
}
