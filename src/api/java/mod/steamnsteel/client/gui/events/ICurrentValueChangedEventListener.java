package mod.steamnsteel.client.gui.events;

public interface ICurrentValueChangedEventListener
{
    void invoke(ScrollbarControl scrollbarControl, int previousValue, int newValue);
}
