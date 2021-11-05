package camera_mode.mixins;

import camera_mode.helper.ServerPlayerEntityMixinAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(
            method = "tickStatusEffects",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void disableCameraStatus(CallbackInfo ci) {
        if ((Object) this instanceof ServerPlayerEntity && ((LivingEntity) (Object) this).isSpectator() && ((ServerPlayerEntityMixinAccess) this).camMode())
            ci.cancel();
    }

}
