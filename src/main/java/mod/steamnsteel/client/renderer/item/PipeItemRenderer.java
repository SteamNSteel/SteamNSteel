package mod.steamnsteel.client.renderer.item;

import com.google.common.base.Objects;
import cpw.mods.fml.client.FMLClientHandler;
import mod.steamnsteel.client.renderer.model.PipeModel;
import mod.steamnsteel.client.renderer.tileentity.PipeTESR;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class PipeItemRenderer implements IItemRenderer
{
    private static final ImmutableTriple<Float, Float, Float> ENTITY_OFFSET = ImmutableTriple.of(0.0f, -1.0f, 0.0f);
    private static final ImmutableTriple<Float, Float, Float> EQUIPPED_OFFSET = ImmutableTriple.of(1.0f, 0.0f, 1.5f);
    private static final ImmutableTriple<Float, Float, Float> FIRST_PERSON_OFFSET = ImmutableTriple.of(-0.0f, 0.0f, 0.0f);
    private static final ImmutableTriple<Float, Float, Float> INVENTORY_OFFSET = ImmutableTriple.of(-0.0f, -0.5f, 0.0f);

    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(1f, 1f, 1f);

    private final PipeModel model;

    public PipeItemRenderer()
    {
        model = new PipeModel();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
            case ENTITY:
                renderPipe(ENTITY_OFFSET);
                break;
            case EQUIPPED:
                renderPipe(EQUIPPED_OFFSET);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderPipe(FIRST_PERSON_OFFSET);
                break;
            case INVENTORY:
                renderPipe(INVENTORY_OFFSET);
                break;
            default:
        }
    }

    private void renderPipe(ImmutableTriple<Float, Float, Float> offset)
    {
        GL11.glPushMatrix();
        //GL11.glScalef(SCALE.left, SCALE.middle, SCALE.right);
        GL11.glTranslatef(offset.left, offset.middle, offset.right);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(PipeTESR.TEXTURE);
        GL11.glPushMatrix();
        model.renderPipeStraight();
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        //GL11.glTranslatef(0f, -0.5f, 0f);
        GL11.glRotatef(-180, 0.0f, 0.0f, 1.0f);
        model.renderPipeOpening();
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glRotatef(-0, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(0f,  (13f * (1f / 16f)), 0f);
        model.renderPipeOpening();
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("model", model)
                .toString();
    }
}
