package camera_mode.mixins;

import camera_mode.helper.ServerPlayerEntityMixinAccess;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Inject(
            method = "onSpectatorTeleport",
            at = @At(
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V",
                    value = "INVOKE"
            ),
            cancellable = true
    )
    protected void disableCamTeleport(SpectatorTeleportC2SPacket packet, CallbackInfo ci) {
        if (((ServerPlayerEntityMixinAccess)(((ServerPlayNetworkHandler) (Object) this).player)).camMode()) {
            ((ServerPlayNetworkHandler) (Object) this).player.sendMessage(new LiteralText("Teleportation is disabled in camera mode"), true);
            ci.cancel();
        }
    }

}