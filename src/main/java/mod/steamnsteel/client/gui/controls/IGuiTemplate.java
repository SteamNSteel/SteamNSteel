package mod.steamnsteel.client.gui.controls;

/**
 * Created by codew on 7/01/2016.
 */
public interface IGuiTemplate<TControl extends Control>
{
    TControl construct();

}
