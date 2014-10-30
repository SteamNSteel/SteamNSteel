package mod.steamnsteel.client.renderer.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.entity.SteamSpiderEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

//This has to extend ModelBase so we can use RenderLivingEntity without attempting to roll our own.
@SideOnly(Side.CLIENT)
public class SteamSpiderModel extends ModelBase
{
    private static final ResourceLocation MODEL = SteamNSteelModel.getResourceLocation(SteamNSteelModel.getModelPath(SteamSpiderEntity.NAME));
    private final IModelCustom model;

    public SteamSpiderModel()
    {
        model = AdvancedModelLoader.loadModel(MODEL);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        double angle = Math.toDegrees(MathHelper.cos(limbSwing * 1.5F) * 0.3F * limbSwingAmount); //First param is speed, second is max angle
        double angle2 = Math.toDegrees(MathHelper.cos(limbSwing * 1.5F  + (float)Math.PI) * 0.3F * limbSwingAmount);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, -1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        //Render main parts apart from legs
        model.renderAllExcept("SSS_LegRF", "SSS_LegRB", "SSS_LegLF", "SSS_LegLB");
        GL11.glDisable(GL11.GL_BLEND);

        //The translate, rotate, translate back is so we rotate from where the legs joins the body instead of from the
        //center point. Basically we move to a rotation point but gotta move back before rendering
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.12F, 0.0F, 0.112F);
        GL11.glRotated(angle - 15.0D, 0.0D, 1.0D, 0.0D);
        GL11.glTranslatef(0.12F, 0.0F, -0.112F);
        model.renderPart("SSS_LegRF");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(-0.12F, 0.0F, -0.112F);
        GL11.glRotated(angle2 + 15.0D, 0.0D, 1.0D, 0.0D);
        GL11.glTranslatef(0.12F, 0.0F, 0.112F);
        model.renderPart("SSS_LegRB");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(0.12F, 0.0F, 0.112F);
        GL11.glRotated(angle2 + 15.0D, 0.0D, 1.0D, 0.0D);
        GL11.glTranslatef(-0.12F, 0.0F, -0.112F);
        model.renderPart("SSS_LegLF");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(0.12F, 0.0F, -0.112F);
        GL11.glRotated(angle - 15.0D, 0.0D, 1.0D, 0.0D);
        GL11.glTranslatef(-0.12F, 0.0F, 0.112F);
        model.renderPart("SSS_LegLB");
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }
}
