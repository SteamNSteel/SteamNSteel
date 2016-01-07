package mod.steamnsteel.client.gui.components;

import com.google.common.collect.Lists;
import mod.steamnsteel.client.gui.GuiRenderer;
import mod.steamnsteel.utility.SteamNSteelException;
import org.lwjgl.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("TypeParameterNamingConvention")
public class ScrollPaneGuiComponent<TModel, TChildComponentTemplate extends GuiComponent & IGuiTemplate<TChildComponentTemplate> & IModelView<TModel>> extends GuiComponent
{
    private GuiComponent[] itemRenderers = new GuiComponent[0];
    private List<TModel> items = Lists.newArrayList();
    private TChildComponentTemplate template = null;
    private ScrollbarGuiComponent scrollbar = null;

    public ScrollPaneGuiComponent(GuiRenderer guiRenderer, Rectangle componentBounds)
    {
        super(guiRenderer, componentBounds);
    }

    public ScrollPaneGuiComponent(GuiRenderer guiRenderer, int width, int height) {
        this(guiRenderer, new Rectangle(0, 0, width, height));
    }

    public ScrollPaneGuiComponent<TModel, TChildComponentTemplate> setItemRendererTemplate(TChildComponentTemplate guiComponentTemplate) {
        template = guiComponentTemplate;
        return this;
    }

    public ScrollPaneGuiComponent<TModel, TChildComponentTemplate> setVisibleItemCount(int visibleItems) {
        if (template == null) {
            throw new SteamNSteelException("Can't set the visible item count, a template hasn't been defined yet");
        }
        final int actualItems = visibleItems + 1;
        itemRenderers = new GuiComponent[actualItems];
        for (int i = 0; i < actualItems; ++i) {
            itemRenderers[i] = template.construct();
            addChild(itemRenderers[i]);
        }
        return this;
    }

    public ScrollPaneGuiComponent<TModel, TChildComponentTemplate> setScrollbar(ScrollbarGuiComponent scrollbar)
    {
        this.scrollbar = scrollbar;
        return this;
    }

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public ScrollPaneGuiComponent<TModel, TChildComponentTemplate> setItems(List<TModel> items) {
        this.items = items == null ? new ArrayList<TModel>(0) : items;
        final int maximumValue = this.items.size() * template.getBounds().getHeight();
        scrollbar.setMaximumValue(maximumValue);
        return this;
    }

    @Override
    public void drawComponent()
    {
        if (itemRenderers.length == 0 || items.isEmpty()) {
            return;
        }

        final Rectangle templateBounds = template.getBounds();

        final Rectangle viewport = new Rectangle(
                0, 0,
                templateBounds.getWidth(), templateBounds.getHeight() * 5);

        guiRenderer.startViewport(this, viewport);

        int itemIndex = 0;
        for (int i = 0; i < itemRenderers.length; ++i)
        {
            final GuiComponent itemRenderer = itemRenderers[i];
            TModel model = null;
            if (itemIndex + i < items.size()) {
                model = items.get(itemIndex + i);
            }

            //This is unchecked, but the generic constraints ensure this cast is possible.
            //noinspection unchecked,CastToIncompatibleInterface
            ((IModelView<TModel>)itemRenderer).setModel(model);

            itemRenderer.setLocation(0, templateBounds.getHeight() * i);
        }

        super.drawComponent();

        guiRenderer.endViewport();
    }


}
