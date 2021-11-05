# Herb's Camera Mode

A survival-friendly spectator mode with configurable restrictions, based off of the /cam and /cs Carpet scripts.

## Configuration
A configuration file (path: `.minecraft/saves/[save]/camera_mode.conf`) will be generated in your save directory if it does not exist yet on world loading. This can be edited directly or in-game via command.\
\
Settings:\
`canSpectate` - Whether camera mode players will be able to spectate entities. Default: `false`\
`canTeleport` - Whether camera mode players will be able to teleport to other players. Default: `false`\
`consoleLogging` - Whether camera mode should output certain player actions to the console. Default: `false`\
`defaultPermissionLevel` - Default permission level for `/cam` and `/camera-config get`. Default: `0`


## Commands
`/cam` - Swap between camera mode and survival mode. \
`/camera-config get [setting name]` - Displays values of a config setting, or all settings if no argument is specified. \
`/camera-config set [setting name]` - Changes the value of a setting. Requires op-level perms. \
`/camera-config reload` - Reloads settings if the config file is edited directly. Requires op-level perms.