package mod.steamnsteel.api.voxbox;


import com.google.common.base.Optional;

/**
 * API Handler for the VoxBox. Use {@link com.google.common.base.Optional#get()} to get the API instance.
 */
public enum VoxBoxHandler {
    INSTANCE;

    public static Optional<IVoxBoxDictionary> voxBoxLibrary = Optional.absent();
}
