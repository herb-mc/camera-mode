package camera_mode.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class CameraMod {

    public static final Logger CAMERA_LOGGER = LogManager.getLogger();

    public static HashMap<String,ConfigOpt> configMaps = new HashMap<>();
    public static HashMap<String,ConfigOpt> defaultConfigs = new HashMap<>();
    public static HashMap<String,String[]> typeMaps = new HashMap<>();
    public static ConfigOpt canTeleport = new ConfigOpt(false);
    public static ConfigOpt canSpectate = new ConfigOpt(false);
    public static ConfigOpt consoleLogging = new ConfigOpt(false);
    public static ConfigOpt defaultPermissionLevel = new ConfigOpt(0);
    public static String[] configSuggestions = {"canSpectate","canTeleport","consoleLogging","defaultPermissionLevel"};
    public static String[] bool = {"true","false"};
    public static String[] permLevels = {"0","1","2","3","4"};

    static {
        CameraMod.configMaps.put("canSpectate", CameraMod.canSpectate);
        CameraMod.configMaps.put("canTeleport", CameraMod.canTeleport);
        CameraMod.configMaps.put("consoleLogging", CameraMod.consoleLogging);
        CameraMod.configMaps.put("defaultPermissionLevel", CameraMod.defaultPermissionLevel);
        CameraMod.defaultConfigs.put("canSpectate", CameraMod.canSpectate);
        CameraMod.defaultConfigs.put("canTeleport", CameraMod.canTeleport);
        CameraMod.defaultConfigs.put("consoleLogging", CameraMod.consoleLogging);
        CameraMod.defaultConfigs.put("defaultPermissionLevel", CameraMod.defaultPermissionLevel);
        CameraMod.typeMaps.put("canSpectate", bool);
        CameraMod.typeMaps.put("canTeleport", bool);
        CameraMod.typeMaps.put("consoleLogging", bool);
        CameraMod.typeMaps.put("defaultPermissionLevel", permLevels);
    }



}
