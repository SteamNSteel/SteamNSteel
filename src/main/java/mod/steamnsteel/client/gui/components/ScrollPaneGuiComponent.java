package mod.steamnsteel.client.gui.components;

import com.google.common.collect.Lists;
import mod.steamnsteel.utility.SteamNSteelException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import java.util.List;

@SuppressWarnings("TypeParameterNamingConvention")
public class ScrollPaneGuiComponent<TModel, TChildComponentTemplate extends GuiComponent & IGuiTemplate<TChildComponentTemplate> & IModelView<TModel>> extends GuiComponent
{
    private final Minecraft client;
    private GuiComponent[] itemRenderers = new GuiComponent[0];
    private List<TModel> items = Lists.newArrayList();
    private TChildComponentTemplate template = null;
    private ScrollbarGuiComponent scrollbar;

    public ScrollPaneGuiComponent(Rectangle componentBounds, Minecraft client)
    {
        super(componentBounds);
        this.client = client;
    }

    public ScrollPaneGuiComponent(int width, int height, Minecraft client) {
        this(new Rectangle(0, 0, width, height), client);
    }

    public ScrollPaneGuiComponent setItemRendererTemplate(TChildComponentTemplate guiComponentTemplate) {
        template = guiComponentTemplate;
        return this;
    }

    public ScrollPaneGuiComponent setVisibleItemCount(int visibleItems) {
        if (template == null) {
            throw new SteamNSteelException("Can't set the visible item count, a template hasn't been defined yet");
        }
        final int actualItems = visibleItems + 1;
        itemRenderers = new GuiComponent[actualItems];
        for (int i = 0; i < actualItems; ++i) {
            itemRenderers[i] = template.construct();
        }
        return this;
    }

    public ScrollPaneGuiComponent setScrollbar(ScrollbarGuiComponent scrollbar)
    {
        this.scrollbar = scrollbar;
        return this;
    }

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public ScrollPaneGuiComponent setItems(List<TModel> items) {
        if (items == null) {
            items = Lists.newArrayList();
        }
        this.items = items;
        final int maximumValue = items.size() * template.getBounds().getHeight();
        scrollbar.setMaximumValue(maximumValue);
        return this;
    }

    @Override
    public void drawComponent()
    {
        if (itemRenderers.length == 0 || items.isEmpty()) {
            return;
        }

        final Rectangle bounds = getBounds();
        final Rectangle templateBounds = template.getBounds();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        final ScaledResolution res = new ScaledResolution(client);
        final double scaleW = client.displayWidth / res.getScaledWidth_double();
        final double scaleH = client.displayHeight / res.getScaledHeight_double();

        //noinspection NumericCastThatLosesPrecision
        GL11.glScissor(
                (int)(bounds.getX() * scaleW),
                client.displayHeight - ((int)(bounds.getY() * scaleH) + (int)(templateBounds.getHeight() * 5 * scaleH)),
                (int)(templateBounds.getWidth() * scaleW),
                (int)(templateBounds.getHeight() * 5 * scaleH));

        int itemIndex = 0;
        for (int i = 0; i < itemRenderers.length; ++i)
        {
            final GuiComponent itemRenderer = itemRenderers[i];
            TModel model = null;
            if (itemIndex + i < items.size()) {
                model = items.get(itemIndex + i);
            }
            //This is unchecked, but the generic constraints ensure this is true.
            //noinspection unchecked,CastToIncompatibleInterface
            ((IModelView<TModel>)itemRenderer).setModel(model);

            final int x = bounds.getX();
            final int y = bounds.getY() + templateBounds.getHeight() * i;

            itemRenderer.setLocation(x, y);
            itemRenderer.drawComponent();
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }


}
