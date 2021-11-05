package camera_mode.commands;

import camera_mode.helper.CameraMod;
import camera_mode.helper.ServerPlayerEntityMixinAccess;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;

public class CameraCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register((CommandManager.literal("cam")
            .requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(CameraMod.defaultPermissionLevel.getInt()) && serverCommandSource.getEntity() instanceof ServerPlayerEntity))
            .executes((context) -> {
                ServerCommandSource source = context.getSource();
                ServerPlayerEntity player = source.getPlayer();
                if (player.interactionManager.getGameMode() == GameMode.SURVIVAL)
                    swapCamera(source);
                else if (player.interactionManager.getGameMode() == GameMode.SPECTATOR && ((ServerPlayerEntityMixinAccess) player).camMode())
                    swapSurvival(source);
                else if (player.interactionManager.getGameMode() != GameMode.SURVIVAL && player.interactionManager.getGameMode() != GameMode.SPECTATOR) {
                    player.sendMessage(new LiteralText("You must be in survival mode to swap to camera mode"), true);
                    return 0;
                }
                else {
                    player.sendMessage(new LiteralText("You may only swap back to survival mode from spectator mode if you entered camera mode beforehand"), true);
                    return 0;
                }
                return 1;
            })
        );
    }

    public static void swapCamera(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if (player.getVehicle() != null) {
            player.sendMessage(new LiteralText("Exit vehicles before entering camera mode"), true);
        } else {
            player.sendMessage(new LiteralText("Entering camera mode"), true);
            if (CameraMod.consoleLogging.getBool())
                CameraMod.CAMERA_LOGGER.info("{} entered camera mode", player.getDisplayName().asString());
            ((ServerPlayerEntityMixinAccess) player).storedData(false, player.getServerWorld(), player.getX(),
                    player.getY(), player.getZ(), player.getYaw(), player.getPitch());
            player.changeGameMode(GameMode.SPECTATOR);
            ((ServerPlayerEntityMixinAccess) player).storeCamMode(true);
        }
    }

    public static void swapSurvival(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        player.sendMessage(new LiteralText("Exiting camera mode"), true);
        player.teleport(((ServerPlayerEntityMixinAccess) player).storedWorld(), ((ServerPlayerEntityMixinAccess) player).getStoredX(), ((ServerPlayerEntityMixinAccess) player).getStoredY(), ((ServerPlayerEntityMixinAccess) player).getStoredZ(), ((ServerPlayerEntityMixinAccess) player).getStoredYaw(), ((ServerPlayerEntityMixinAccess) player).getStoredPitch());
        ((ServerPlayerEntityMixinAccess) player).storeCamMode(false);
        player.changeGameMode(GameMode.SURVIVAL);
    }

}