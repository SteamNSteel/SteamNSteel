package mod.steamnsteel.client.gui.events;

import mod.steamnsteel.client.gui.Control;

public interface ICurrentValueChangedEventListener
{
    void invoke(Control scrollbarControl, int previousValue, int newValue);
}
