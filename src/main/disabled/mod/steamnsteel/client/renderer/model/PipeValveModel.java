package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.block.machine.PipeValveBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class PipeValveModel extends SteamNSteelModel
{
    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(PipeValveBlock.NAME));
    private final IModelCustom model;

    public PipeValveModel()
    {
        model = AdvancedModelLoader.loadModel(MODEL);
    }

    public void renderAll()
    {
        model.renderAll();
    }

    public void renderPipe() {
        model.renderPart("VPPipe");
    }
    public void renderBody() {
        model.renderPart("VPBody");
    }
    public void renderValve() {
        model.renderPart("VPValve");
    }
    public void renderOpeningA() {
        model.renderPart("VPOpening1");
    }
    public void renderOpeningB() {
        model.renderPart("VPOpening2");
    }
}
