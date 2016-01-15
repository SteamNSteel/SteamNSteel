package mod.steamnsteel.client.gui.controls;

import java.util.HashSet;

/**
 * Created by codew on 12/01/2016.
 */
class MouseCapture {
    private static HashSet<Control> capturingControls = new HashSet<>();
    public static void register(Control control) {
        capturingControls.add(control);
    }

    public static void unregister(Control control) {
        capturingControls.remove(control);
    }

    public static Iterable<Control> getCapturedControls() {
        return capturingControls;
    }

    public static boolean isCapturing(Control control) {
        return capturingControls.contains(control);
    }
}
