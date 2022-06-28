package camera_mode.mixins;

import camera_mode.commands.CameraCommand;
import camera_mode.commands.CameraConfigCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static camera_mode.helper.CameraMod.CAMERA_LOGGER;


@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {

    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void onRegister(CommandManager.RegistrationEnvironment environment, CommandRegistryAccess commandRegistryAccess, CallbackInfo ci) {
        CameraCommand.register(this.dispatcher);
        CameraConfigCommand.register(this.dispatcher);
        CAMERA_LOGGER.info("Camera commands successfully loaded");
    }

}