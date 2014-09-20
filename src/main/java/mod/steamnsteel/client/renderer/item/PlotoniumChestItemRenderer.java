package mod.steamnsteel.client.renderer.item;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import cpw.mods.fml.client.FMLClientHandler;
import mod.steamnsteel.client.renderer.tileentity.PlotoniumChestTESR;
import mod.steamnsteel.utility.Vector;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class PlotoniumChestItemRenderer implements IItemRenderer
{
    private static final Optional<Vector<Float>> ENTITY_OFFSET = Optional.of(new Vector<Float>(0.0f, -1.0f, 0.0f));
    private static final Optional<Vector<Float>> EQUIPPED_OFFSET = Optional.of(new Vector<Float>(1.0f, 1.0f, 1.0f));
    private static final Optional<Vector<Float>> FIRST_PERSON_OFFSET = Optional.of(new Vector<Float>(1.0f, 1.0f, 1f));
    private static final Optional<Vector<Float>> INVENTORY_OFFSET = Optional.of(new Vector<Float>(0.0f, 0.09f, 0.0f));

    private static final Vector<Float> SCALE = new Vector<Float>(1f, 1.0F, 1.0F);

    private final ModelChest vanillaChest;

    public PlotoniumChestItemRenderer()
    {
       vanillaChest = new ModelChest();
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
        Optional<Vector<Float>> vector = Optional.absent();

        switch (type)
        {
            case ENTITY:
                vector = ENTITY_OFFSET;
                break;
            case EQUIPPED:
                vector = EQUIPPED_OFFSET;
                break;
            case EQUIPPED_FIRST_PERSON:
                vector = FIRST_PERSON_OFFSET;
                break;
            case INVENTORY:
                vector = INVENTORY_OFFSET;
                break;
            default:
        }

        if (vector.isPresent())
            renderPlotoniumChest(vector.get());
    }

    private void renderPlotoniumChest(Vector<Float> vector)
    {
        GL11.glPushMatrix();

        GL11.glScalef(SCALE.getX(), SCALE.getY(), SCALE.getZ());
        GL11.glTranslatef(vector.getX(), vector.getY(), vector.getZ());
        GL11.glRotatef(180f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(90f, 0.0f, -1.0f, 0.0f);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(PlotoniumChestTESR.TEXTURE);

        vanillaChest.renderAll();


        GL11.glPopMatrix();
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("model", vanillaChest)
                .toString();
    }
}
