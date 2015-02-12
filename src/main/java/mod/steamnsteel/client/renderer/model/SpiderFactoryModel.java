package mod.steamnsteel.client.renderer.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.entity.SpiderFactoryEntity;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SpiderFactoryModel extends SteamNSteelModel {

    private static final ResourceLocation MODEL = getResourceLocation(getModelPath(SpiderFactoryEntity.NAME));
    private final WavefrontObject model;

    public SpiderFactoryModel()
    {
        model = (WavefrontObject) AdvancedModelLoader.loadModel(MODEL);
    }

    public void render()
    {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
    }
}
