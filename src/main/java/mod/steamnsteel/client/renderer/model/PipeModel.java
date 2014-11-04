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

    public void renderPipeCornerNE() {
        model.renderPart("PipeCrnrNE");
    }
    public void renderPipeCornerNW() {
        model.renderPart("PipeCrnrNW");
    }
    public void renderPipeCornerSE() {
        model.renderPart("PipeCrnrSE");
    }
    public void renderPipeCornerSW() {
        model.renderPart("PipeCrnrSW");
    }

    public void renderPipeStraight() { model.renderPart("Pipe0C"); }

    /**
     * Used for where a pipe connects to a machine or junction
     */
    public void renderPipeOpening() { model.renderPart("PipeOpening"); }

    /**
     * Used where a pipe ends.
     */
    public void renderPipeCap() {
        model.renderPart("PipeCap");
    }

    public void renderJunctionBox() {
        model.renderPart("Box002");
    }
}
