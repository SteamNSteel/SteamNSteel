package mod.steamnsteel.utility;

/**
 * Created by steblo on 11/05/2015.
 */
public class SteamNSteelException extends RuntimeException {
    public SteamNSteelException() {
    }

    public SteamNSteelException(String s) {
        super(s);
    }

    public SteamNSteelException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SteamNSteelException(Throwable throwable) {
        super(throwable);
    }

    public SteamNSteelException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
