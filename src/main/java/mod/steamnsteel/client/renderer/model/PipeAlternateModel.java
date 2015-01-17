package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.block.machine.PipeBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class PipeAlternateModel extends SteamNSteelModel
{
    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(PipeBlock.NAME + "-alt"));
    private final IModelCustom model;

    public PipeAlternateModel()
    {
        model = AdvancedModelLoader.loadModel(MODEL);
    }

    public void renderAll()
    {
        model.renderAll();
    }

    public void renderPipeCornerNW() {
        model.renderPart("PipeCrnrNWAlt");
    }

}
