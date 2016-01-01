package mod.steamnsteel.client.model.opengex;

import com.google.common.base.Optional;
import mod.steamnsteel.client.model.opengex.ogex.OgexAnimation;
import net.minecraftforge.client.model.IModelPart;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;

public class OpenGEXState implements IModelState {
    private final OgexAnimation animation;
    private final float time;
    private final IModelState state;

    public OpenGEXState(OgexAnimation animation, float time) {
        this(animation, time, null);
    }

    public OpenGEXState(OgexAnimation animation, float time, IModelState state) {
        this.animation = animation;
        this.time = time;
        this.state = state;
    }

    @Override
    public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {

        return Optional.absent();
    }

    public OgexAnimation getAnimation()
    {
        return animation;
    }

    public float getTime()
    {
        return time;
    }

    public IModelState getState()
    {
        return state;
    }
}
