package camera_mode.mixins;

import camera_mode.helper.CameraMod;
import camera_mode.helper.ServerPlayerEntityMixinAccess;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

import static camera_mode.helper.CameraMod.CAMERA_LOGGER;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Inject(
            method = "onSpectatorTeleport",
            at = @At(
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V",
                    value = "INVOKE"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true
    )
    protected void disableCamTeleport(SpectatorTeleportC2SPacket packet, CallbackInfo ci, Iterator var2, ServerWorld serverWorld, Entity entity) {
        if (((ServerPlayerEntityMixinAccess)(((ServerPlayNetworkHandler) (Object) this).player)).camMode() && !CameraMod.canTeleport.getBool()) {
            ((ServerPlayNetworkHandler) (Object) this).player.sendMessage(new LiteralText("Teleportation is disabled in camera mode"), true);
            ci.cancel();
        }
        else if (CameraMod.consoleLogging.getBool())
            CAMERA_LOGGER.info("{} teleported to {} while in camera mode", ((ServerPlayNetworkHandler) (Object) this).player.getDisplayName().asString(), entity.getDisplayName().asString());
    }

}