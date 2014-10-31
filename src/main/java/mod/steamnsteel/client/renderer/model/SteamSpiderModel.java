package mod.steamnsteel.client.renderer.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.steamnsteel.entity.SteamSpiderEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

//This has to extend ModelBase so we can use RenderLivingEntity without attempting to roll our own.
@SideOnly(Side.CLIENT)
public class SteamSpiderModel extends ModelBase
{
    private static final ResourceLocation MODEL = SteamNSteelModel.getResourceLocation(SteamNSteelModel.getModelPath(SteamSpiderEntity.NAME));
    private final WavefrontObject model;

    public SteamSpiderModel()
    {
        model = (WavefrontObject) AdvancedModelLoader.loadModel(MODEL);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        float angle = MathHelper.cos(limbSwing * 1.5F) * 0.2F * limbSwingAmount + 0.2F; //First param is speed, second is max angle
        float angle2 = MathHelper.cos(limbSwing * 1.5F + (float)Math.PI) * 0.2F * limbSwingAmount - 0.2F;
        GL11.glPushMatrix();

        Tessellator tessellator = Tessellator.instance;
        Tessellator.instance.startDrawing(GL11.GL_TRIANGLES);

        float bob = (float) Math.cos(entity.ticksExisted) / 200F;
        tessellator.addTranslation(0F, bob, 0F);
        tessellator.setColorRGBA(255, 255, 255, 255);
        model.tessellateAllExcept(tessellator, "SSS_LegRF", "SSS_LegRB", "SSS_LegLF", "SSS_LegLB", "SSS_Bulb", "SSS_Filiment");
        if (entity.getDataWatcher().getWatchableObjectByte(12) == 1)
        {
            tessellator.setColorRGBA(255, 75, 106, 255);
        }
        model.tessellateOnly(tessellator, "SSS_Bulb", "SSS_Filiment");
        tessellator.setColorRGBA(255, 255, 255, 255);
        tessellator.addTranslation(0F, -bob, 0F);

        for (GroupObject groupObject : model.groupObjects)
        {
            if ("SSS_LegRF".equalsIgnoreCase(groupObject.name)) {
                tessellateWithRotations(groupObject, tessellator, 0F, angle, 0F);
            }
            else if ("SSS_LegRB".equalsIgnoreCase(groupObject.name)) {
                tessellateWithRotations(groupObject, tessellator, 0F, angle2, 0F);
            }
            else if ("SSS_LegLF".equalsIgnoreCase(groupObject.name)) {
                tessellateWithRotations(groupObject, tessellator, 0F, angle2, 0F);
            }
            else if ("SSS_LegLB".equalsIgnoreCase(groupObject.name)) {
                tessellateWithRotations(groupObject, tessellator, 0F, angle, 0F);
            }
        }

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        tessellator.setTranslation(0, 0, 0);
        Tessellator.instance.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    //Requires radians
    private void tessellateWithRotations(GroupObject group, Tessellator tessellator, float rotX, float rotY, float rotZ) {
        if (group.faces.size() > 0)
        {
            for (Face face : group.faces)
            {
                //Code copied from Face
                if (face.faceNormal == null)
                {
                    face.faceNormal = face.calculateFaceNormal();
                }

                tessellator.setNormal(face.faceNormal.x, face.faceNormal.y, face.faceNormal.z);

                float averageU = 0F;
                float averageV = 0F;

                if ((face.textureCoordinates != null) && (face.textureCoordinates.length > 0))
                {
                    for (int i = 0; i < face.textureCoordinates.length; ++i)
                    {
                        averageU += face.textureCoordinates[i].u;
                        averageV += face.textureCoordinates[i].v;
                    }

                    averageU = averageU / face.textureCoordinates.length;
                    averageV = averageV / face.textureCoordinates.length;
                }

                float offsetU, offsetV;

                for (int i = 0; i < face.vertices.length; ++i)
                {
                    //TODO optimization. Create a vec pool?
                    Vec3 vec3 = Vec3.createVectorHelper(face.vertices[i].x, face.vertices[i].y, face.vertices[i].z);
                    vec3.rotateAroundX(rotX);
                    vec3.rotateAroundY(rotY);
                    vec3.rotateAroundZ(rotZ);

                    if ((face.textureCoordinates != null) && (face.textureCoordinates.length > 0))
                    {
                        offsetU = 0.0005F;
                        offsetV = 0.0005F;

                        if (face.textureCoordinates[i].u > averageU)
                        {
                            offsetU = -offsetU;
                        }
                        if (face.textureCoordinates[i].v > averageV)
                        {
                            offsetV = -offsetV;
                        }

                        tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, face.textureCoordinates[i].u + offsetU, face.textureCoordinates[i].v + offsetV);
                    }
                    else
                    {
                        tessellator.addVertex(vec3.xCoord, vec3.yCoord, vec3.zCoord);
                    }
                }
            }
        }
    }
}
