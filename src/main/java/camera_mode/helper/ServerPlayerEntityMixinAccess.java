package camera_mode.helper;

import net.minecraft.server.world.ServerWorld;

public interface ServerPlayerEntityMixinAccess {

    void storedData(boolean cam, ServerWorld word, double x, double y, double z, float yaw, float pitch);
    void storeCamMode(boolean cam);
    boolean camMode();
    ServerWorld storedWorld();
    double getStoredX();
    double getStoredY();
    double getStoredZ();
    float getStoredYaw();
    float getStoredPitch();

}
