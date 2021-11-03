package camera_mode.mixins;

import camera_mode.helper.ServerPlayerEntityMixinAccess;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityMixinAccess {

    @Unique public boolean camMode;
    @Unique public ServerWorld storedWorld;
    @Unique public double storedX;
    @Unique public double storedY;
    @Unique public double storedZ;
    @Unique public float storedYaw;
    @Unique public float storedPitch;

    @Override
    public void storedData(boolean cam, ServerWorld world, double x, double y, double z, float yaw, float pitch) {
        camMode = cam;
        storedWorld = world;
        storedX = x;
        storedY = y;
        storedZ = z;
        storedYaw = yaw;
        storedPitch = pitch;
    }

    @Override
    public void storeCamMode(boolean cam) {
        camMode = cam;
    }

    @Override
    public boolean camMode() {
        return camMode;
    }

    @Override
    public ServerWorld storedWorld() {
        return storedWorld;
    }

    @Override
    public double getStoredX() {
        return storedX;
    }

    @Override
    public double getStoredY() {
        return storedY;
    }

    @Override
    public double getStoredZ() {
        return storedZ;
    }

    @Override
    public float getStoredYaw() {
        return storedYaw;
    }

    @Override
    public float getStoredPitch() {
        return storedPitch;
    }

    @Inject(
            method = "attack",
            at = @At(
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;setCameraEntity(Lnet/minecraft/entity/Entity;)V",
                    value = "INVOKE"
            ),
            cancellable = true
    )
    protected void disableCamEntitySpectating(Entity target, CallbackInfo ci) {
        if (camMode) {
            ((ServerPlayerEntity) (Object) this).sendMessage(new LiteralText("Entity spectating is disabled in camera mode"), true);
            ci.cancel();
        }
    }

    @Inject(
            method = "changeGameMode",
            at = @At("HEAD")
    )
    protected void camModeDisable(GameMode gameMode, CallbackInfoReturnable<Boolean> cir){
        if (camMode)
            ((ServerPlayerEntity) (Object) this).sendMessage(new LiteralText("Swapped gamemodes directly, disabling camera mode"), true);
        camMode = false;
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("HEAD")
    )
    protected void readStoredData(NbtCompound nbt, CallbackInfo ci) {
        camMode = nbt.getBoolean("CameraMode");
        storedWorld = Objects.requireNonNull(((ServerPlayerEntity) (Object) this).getServer()).getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(nbt.getString("StoredDimension"))));
        storedX = nbt.getDouble("StoredX");
        storedY = nbt.getDouble("StoredY");
        storedZ = nbt.getDouble("StoredZ");
        storedYaw = nbt.getFloat("StoredYaw");
        storedPitch = nbt.getFloat("StoredPitch");
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("HEAD")
    )
    protected void writeStoredData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("CameraMode", camMode);
        if (storedWorld != null)
            nbt.putString("StoredDimension", storedWorld.getRegistryKey().getValue().toString());
        nbt.putDouble("StoredX", storedX);
        nbt.putDouble("StoredY", storedY);
        nbt.putDouble("StoredZ", storedZ);
        nbt.putFloat("StoredYaw", storedYaw);
        nbt.putFloat("StoredPitch", storedPitch);
    }

}
