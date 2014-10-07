package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.block.machine.PipeBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class PipeModel extends SteamNSteelModel
{
    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(PipeBlock.NAME));
    private final IModelCustom model;

    public PipeModel()
    {
        model = AdvancedModelLoader.loadModel(MODEL);
    }

    public void render()
    {
        model.renderAll();
    }
}
