package mod.steamnsteel.client.renderer.item;

import com.google.common.base.Objects;
import cpw.mods.fml.client.FMLClientHandler;
import mod.steamnsteel.client.renderer.model.PipeRedstoneValveModel;
import mod.steamnsteel.client.renderer.model.PipeValveModel;
import mod.steamnsteel.client.renderer.tileentity.PipeRedstoneValveTESR;
import mod.steamnsteel.client.renderer.tileentity.PipeTESR;
import mod.steamnsteel.client.renderer.tileentity.PipeValveTESR;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;

public class PipeRedstoneValveItemRenderer implements IItemRenderer
{
    private static final ImmutableTriple<Float, Float, Float> ENTITY_OFFSET = ImmutableTriple.of(0.0f, -1.0f, 0.0f);
    private static final ImmutableTriple<Float, Float, Float> EQUIPPED_OFFSET = ImmutableTriple.of(1.0f, 0.0f, 1.5f);
    private static final ImmutableTriple<Float, Float, Float> FIRST_PERSON_OFFSET = ImmutableTriple.of(-0.0f, 0.0f, 0.0f);
    private static final ImmutableTriple<Float, Float, Float> INVENTORY_OFFSET = ImmutableTriple.of(-0.0f, -1.0f, 0.0f);

    private static final ImmutableTriple<Float, Float, Float> SCALE = ImmutableTriple.of(0.666667f, 0.666667f, 0.666667f);

    private final PipeRedstoneValveModel model;

    public PipeRedstoneValveItemRenderer()
    {
        model = new PipeRedstoneValveModel();
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
                renderJunction(ENTITY_OFFSET);
                break;
            case EQUIPPED:
                renderJunction(EQUIPPED_OFFSET);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderJunction(FIRST_PERSON_OFFSET);
                break;
            case INVENTORY:
                renderJunction(INVENTORY_OFFSET);
                break;
            default:
        }
    }

    private void renderJunction(ImmutableTriple<Float, Float, Float> offset)
    {
        GL11.glPushMatrix();
        GL11.glScalef(SCALE.left, SCALE.middle, SCALE.right);
        GL11.glTranslatef(offset.left, offset.middle, offset.right);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(PipeTESR.TEXTURE);
        model.renderPipe();
        model.renderOpeningA();
        model.renderOpeningB();

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(PipeValveTESR.TEXTURE);
        model.renderBody();

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(PipeRedstoneValveTESR.TEXTURE_ON);
        model.renderRedstoneValve();

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
