package dev.endless.v4.init;

import dev.endless.v4.Noctyra;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;

public class CommandInit {
    /* GENERAL */
    public static final String CODE_INCORRECT = register("code_incorrect");
    public static final String PLAYER_NOT_FOUND = register("player_not_found");

    /* GAMEMODE */
    public static final String GAMEMODE_USAGE = register("gamemode.usage");
    public static final String MODE_CREATIVE = register("mode_creative");
    public static final String MODE_SURVIVAL = register("mode_survival");
    public static final String MODE_ADVENTURE = register("mode_adventure");
    public static final String MODE_SPECTATOR = register("mode_spectator");
    public static final String GAMEMODE_SELF = register("gamemode.self");
    public static final String GAMEMODE_OTHER = register("gamemode.other");

    /* SPAWN */
    public static final String TELEPORTING_TIMED = register("spawn.teleport.timed");
    public static final String TELEPORT_CANCELLED = register("spawn.teleport.cancelled");
    public static final String TELEPORT_SUCCESSFUL = register("spawn.teleport.successful");
    public static final String TELEPORT_OTHERS = register("spawn.teleport.others");
    public static final String TELEPORT_SELF = register("spawn.teleport.self");
    public static final String TELEPORTED = register("spawn.teleported");
    public static final String SETSPAWN = register("spawn.set");
    public static final String SPAWN_NOT_SET_YET = register("spawn.notset");

    public static String register(String name) {
        return "command." + Noctyra.id(name);
    }

    public static void load() {}
}
