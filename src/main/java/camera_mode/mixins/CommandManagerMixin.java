package camera_mode.mixins;

import camera_mode.commands.CameraCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static camera_mode.commands.CameraCommand.CAMERA_LOGGER;


@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {

    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void onRegister(CommandManager.RegistrationEnvironment arg, CallbackInfo ci) {
        CameraCommand.register(this.dispatcher);
        CAMERA_LOGGER.info("Camera command successfully loaded");
    }

}