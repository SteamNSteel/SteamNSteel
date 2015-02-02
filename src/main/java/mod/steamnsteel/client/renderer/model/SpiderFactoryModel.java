package mod.steamnsteel.client.renderer.model;

import mod.steamnsteel.entity.SpiderFactoryEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class SpiderFactoryModel extends ModelBase {

    private static final ResourceLocation MODEL = SteamNSteelModel.getResourceLocation(SteamNSteelModel.getModelPath(SpiderFactoryEntity.NAME));
    private final WavefrontObject model;

    public SpiderFactoryModel()
    {
        model = (WavefrontObject) AdvancedModelLoader.loadModel(MODEL);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setTranslation(0, 1.5, 0);
        Tessellator.instance.startDrawing(GL11.GL_TRIANGLES);
        model.tessellateAll(tessellator);
        tessellator.setTranslation(0, 0, 0);
        tessellator.draw();
    }
}
