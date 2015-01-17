package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.block.machine.PipeRedstoneValveBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class PipeRedstoneValveModel extends SteamNSteelModel
{
    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(PipeRedstoneValveBlock.NAME));
    private final IModelCustom model;

    public PipeRedstoneValveModel()
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
    public void renderRedstoneValve() {
        model.renderPart("VPRedstone");
    }
    public void renderOpeningA() {
        model.renderPart("VPOpening1");
    }
    public void renderOpeningB() {
        model.renderPart("VPOpening2");
    }
}
