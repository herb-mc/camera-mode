package camera_mode.helper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class CameraConfig {

    private static Path getFile(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("camera_mode.conf");
    }

    public static void writeSettingToConf(String key, String value, MinecraftServer server) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFile(server).toFile(), true))) {
            writer.write(key + ": " + value + "\n");
        } catch (IOException e) {
            CameraMod.CAMERA_LOGGER.error("Failed write value for '{}' to camera_mode.conf", key,  e);
        }
    }

    public static void overwriteSettingToConf(String key, String value, MinecraftServer server) throws IOException {
        ArrayList<String> file = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(getFile(server))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.replaceAll("[\\r\\n]", "").split(":\\s");
                if (fields[0].matches(key))
                    file.add(fields[0] + ": " + value);
                else
                    file.add(fields[0] + ": " + fields[1]);
            }
        } catch (NoSuchFileException e) {
            try {
                CameraMod.CAMERA_LOGGER.info("No existing config file, generating defaults for camera_mode.conf");
                Files.createFile(getFile(server));
                writeDefaults(server);
            } catch (IOException e1) {
                CameraMod.CAMERA_LOGGER.error("Unable to generate defaults for camera_mode.conf",  e);
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(getFile(server))) {
            for (String line : file) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            CameraMod.CAMERA_LOGGER.error("Failed write to camera_mode.conf",  e);
        }
    }

    private static void writeDefaults(MinecraftServer server) {
        String track = null;
        try (BufferedWriter writer = Files.newBufferedWriter(getFile(server))) {
            for (String key: CameraMod.configMaps.keySet()) {
                track = key;
                writer.write(key + ": " + CameraMod.configMaps.get(key).getAsString() + "\n");
            }
        } catch (IOException e) {
            CameraMod.CAMERA_LOGGER.error("Failed write value for '{}' to camera_mode.conf", track,  e);
        }
    }

    public static void loadConf(MinecraftServer server) {
        {
            CameraMod.configMaps = CameraMod.defaultConfigs;
            Path path = getFile(server);
            ArrayList<String> confOptions = defaultOpts();
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.replaceAll("[\\r\\n]", "").split(":\\s");
                    if (defaultContains(fields[0])) {
                        if (fields.length > 1) {
                            if (CameraMod.configMaps.get(fields[0]).getValue() instanceof Boolean && parseBool(fields[0], fields[1]) != null)
                                CameraMod.configMaps.get(fields[0]).setValue(parseBool(fields[0], fields[1]));
                            else if (CameraMod.configMaps.get(fields[0]).getValue() instanceof Integer)
                                try {
                                    Integer.parseInt(fields[1]);
                                } catch (NumberFormatException e) {
                                    CameraMod.CAMERA_LOGGER.error("Unable to parse value '{}' for '{}'", fields[1], fields[0], e);
                                }
                            confOptions.remove(fields[0]);
                        } else {
                            CameraMod.CAMERA_LOGGER.info("No value set for setting '{}', writing defaults", fields[0]);
                            writeSettingToConf(fields[0], CameraMod.defaultConfigs.get(fields[0]).getAsString(), server);
                        }
                    } else
                        CameraMod.CAMERA_LOGGER.info("'{}' not a configurable setting", fields[0]);
                }
                if (!confOptions.isEmpty()) {
                    CameraMod.CAMERA_LOGGER.info("Config file is missing settings, writing defaults");
                    for (String s : confOptions)
                        writeSettingToConf(s, CameraMod.defaultConfigs.get(s).getAsString(), server);
                }
                CameraMod.CAMERA_LOGGER.info("Successfully loaded configurations");
            } catch (NoSuchFileException e) {
                try {
                    CameraMod.CAMERA_LOGGER.info("No existing config file, generating defaults for camera_mode.conf");
                    Files.createFile(path);
                    writeDefaults(server);
                } catch (IOException e1) {
                    CameraMod.CAMERA_LOGGER.error("Unable to generate defaults for camera_mode.conf",  e);
                }
            }
            catch (IOException e) {
                CameraMod.CAMERA_LOGGER.error("Something went wrong while reading from camera_mode.conf",  e);
            }
        }
    }

    private static boolean defaultContains(String q) {
        for (Map.Entry<String,ConfigOpt> entry : CameraMod.defaultConfigs.entrySet())
            if (q.matches(entry.getKey())) return true;
        return false;
    }

    public static ArrayList<String> defaultOpts() {
        return new ArrayList<>(Arrays.asList(CameraMod.configSuggestions));
    }

    private static Boolean parseBool(String key, String value) {
        if (!value.matches("(true|false)")) {
            CameraMod.CAMERA_LOGGER.info("Unable to parse value '{}' for '{}'", value, key);
            return null;
        }
        return value.matches("true");
    }

}