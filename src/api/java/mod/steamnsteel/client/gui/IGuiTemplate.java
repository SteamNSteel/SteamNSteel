package mod.steamnsteel.client.gui;

import mod.steamnsteel.client.gui.Control;

/**
 * Created by codew on 7/01/2016.
 */
public interface IGuiTemplate<TControl extends Control>
{
    TControl construct();

}
