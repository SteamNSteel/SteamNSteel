package mod.steamnsteel.factory;

import mod.steamnsteel.utility.position.WorldBlockCoord;

/**
 * Created by Steven on 7/04/2015.
 */
public interface IFactoryEntity {
    void setPositionAndRotation(double x, double y, double z, float yaw, float roll);

    void setMasterBlockLocation(WorldBlockCoord parentPosition);
}
