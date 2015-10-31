package mod.steamnsteel.client.model;

import mod.steamnsteel.client.model.opengex.ogex.OgexAnimation;
import net.minecraftforge.client.model.IModelPart;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;

public class OpenGEXState implements IModelState {
    private final OgexAnimation animation;
    private final int frame;
    private final IModelState state;

    public OpenGEXState(OgexAnimation animation, int frame) {
        this(animation, frame, null);
    }

    public OpenGEXState(OgexAnimation animation, int frame, IModelState state) {
        this.animation = animation;
        this.frame = frame;
        this.state = state;
    }

    @Override
    public TRSRTransformation apply(IModelPart part) {
        return null;
    }
}
