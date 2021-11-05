package camera_mode.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static camera_mode.helper.CameraConfig.loadConf;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(
            method = "loadWorld",
            at = @At("TAIL")
    )
    protected void configOnLoad(CallbackInfo ci) {
        loadConf((MinecraftServer) (Object) this);
    }

}
