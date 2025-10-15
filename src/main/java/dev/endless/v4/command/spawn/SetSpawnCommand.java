package dev.endless.v4.command.spawn;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.endless.v4.Noctyra;
import dev.endless.v4.init.CommandInit;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class SetSpawnCommand {
    private static SpawnStorage spawnData;

    public static void register() {
        CommandRegistrationCallback.EVENT.register((disptacher, registryAccess, environment) -> {
            if (environment.dedicated || environment.integrated) {
                registerCommand(disptacher);
                Noctyra.LOGGER.info("Registering SetSpawn Command for Noctyra");
            }
        });
        // Load saved spawn on startup
        spawnData = SpawnStorage.load();
    }

    private static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("setspawn")
                .requires(src -> src.hasPermissionLevel(2)) // OP only
                .executes(SetSpawnCommand::setSpawnCommand));
    }

    private static int setSpawnCommand(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        spawnData = SpawnStorage.fromPlayer(player);
        spawnData.save();
        player.sendMessage(Text.literal("§aSpawn set at your current location."));
        player.sendMessage(Text.translatable(CommandInit.SETSPAWN).setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));
        return 1;
    }
}
