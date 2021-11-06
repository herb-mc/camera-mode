package camera_mode.mixins;

import camera_mode.helper.CameraMod;
import camera_mode.helper.ServerPlayerEntityMixinAccess;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = ThreadedAnvilChunkStorage.class, priority = 1027)
public class ThreadedAnvilChunkStorageMixin {

    @ModifyVariable(
            method = "updatePosition",
            at = @At(
                    target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;doesNotGenerateChunks(Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
                    value = "INVOKE_ASSIGN"
            ),
            ordinal = 1
    )
    protected boolean cameraGeneratesChunks(boolean bl, ServerPlayerEntity player) {
        return ((ServerPlayerEntityMixinAccess) player).camMode() ? player.isSpectator() && !CameraMod.cameraGeneratesChunks.getBool() : bl;
    }

}
