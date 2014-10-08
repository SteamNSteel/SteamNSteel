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

    public void renderAll()
    {
        model.renderAll();
    }

    public void renderPipe2xC() {
        model.renderPart("Pipe2xC");
    }

    public void renderPipe1xC() {
        model.renderPart("Pipe1xC");
    }

    public void renderPipe0C() {
        model.renderPart("Pipe0C");
    }

    public void renderPipe1xCrnr() {
        model.renderPart("Pipe1xCrnr");
    }

    public void renderPipeCap() {
        model.renderPart("PipeCap");
    }

    public void renderJunctionBox() {
        model.renderPart("Box002");
    }
}
