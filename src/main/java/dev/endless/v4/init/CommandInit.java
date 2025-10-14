package dev.endless.v4.init;

import dev.endless.v4.Noctyra;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;

public class CommandInit {
    public static final String CODE_INCORRECT = register("code_incorrect");
    public static final String PLAYER_NOT_FOUND = register("player_not_found");

    public static final String GAMEMODE_USAGE = register("gamemode.usage");
    public static final String MODE_CREATIVE = register("mode_creative");
    public static final String MODE_SURVIVAL = register("mode_survival");
    public static final String MODE_ADVENTURE = register("mode_adventure");
    public static final String MODE_SPECTATOR = register("mode_spectator");
    public static final String GAMEMODE_SELF = register("gamemode.self");
    public static final String GAMEMODE_OTHER = register("gamemode.other");

    public static String register(String name) {
        return "command." + Noctyra.id(name);
    }

    public static void load() {}
}
