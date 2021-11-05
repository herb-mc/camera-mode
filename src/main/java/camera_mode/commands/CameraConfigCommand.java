package camera_mode.commands;

import camera_mode.helper.CameraConfig;
import camera_mode.helper.CameraMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.io.IOException;
import java.util.Arrays;

public class CameraConfigCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register((CommandManager.literal("camera-config")
            .requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(CameraMod.defaultPermissionLevel.getInt())))
            .then(CommandManager.literal("get")
            .executes((context) -> {
                for (String option : CameraMod.configSuggestions)
                    context.getSource().sendFeedback(new LiteralText(option + ": " + CameraMod.configMaps.get(option).getAsString()), false);
                return 1;
            })
            .then(CommandManager.argument("option", StringArgumentType.string())
                .suggests((context,builder) -> CommandSource.suggestMatching(CameraMod.configSuggestions,builder))
                .executes((context) -> {
                    context.getSource().sendFeedback(new LiteralText(context.getArgument("option", String.class) + ": " + CameraMod.configMaps.get(context.getArgument("option", String.class)).getAsString()), false);
                    return 1;
                })
            ))
            .then(CommandManager.literal("reload")
            .requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(4))
            .executes((context) -> {
                context.getSource().sendFeedback(new LiteralText("Reloading configurations"), true);
                CameraConfig.loadConf(context.getSource().getServer());
                return 1;
            }))
            .then(CommandManager.literal("set")
            .requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(4))
            .then(CommandManager.argument("option", StringArgumentType.string())
            .suggests((context,builder) -> CommandSource.suggestMatching(CameraMod.configSuggestions,builder))
                .then(CommandManager.argument("value", StringArgumentType.string())
                .suggests((context,builder) -> CommandSource.suggestMatching(CameraMod.typeMaps.get(context.getArgument("option", String.class)),builder))
                .executes((context) -> {
                    String temp = context.getArgument("value", String.class);
                    CameraMod.configMaps.get(context.getArgument("option", String.class)).setValue((Arrays.asList(CameraMod.bool).contains(temp)) ? Boolean.parseBoolean(temp) : Integer.parseInt(temp));
                    context.getSource().sendFeedback(new LiteralText(context.getArgument("option", String.class) + " set to " + CameraMod.configMaps.get(context.getArgument("option", String.class)).getAsString()), false);
                    CameraMod.CAMERA_LOGGER.info("{}: value of '{}' set to {}", context.getSource().getDisplayName().asString(), context.getArgument("option", String.class), context.getArgument("value", String.class));
                    try {
                        CameraConfig.overwriteSettingToConf(context.getArgument("option", String.class), temp, context.getSource().getServer());
                    } catch (IOException e) {
                        CameraMod.CAMERA_LOGGER.error("Failed write value for '{}' to camera_mode.conf", context.getArgument("option", String.class),  e);
                    }
                    return 1;
                }))
            ))
        );
    }
}