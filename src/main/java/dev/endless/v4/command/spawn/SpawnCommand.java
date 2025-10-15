package dev.endless.v4.command.spawn;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.endless.v4.Noctyra;
import dev.endless.v4.command.util.NoctyraSuggestionsUtil;
import dev.endless.v4.command.util.NoctyraTextUtil;
import dev.endless.v4.command.util.NoctyraTickSchedulerUtil;
import dev.endless.v4.init.CommandInit;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnCommand {
    private static SpawnStorage spawnData;

    // Track pending teleports: player UUID -> starting position
    private static final Map<UUID, Vec3d> pendingTeleports = new HashMap<>();

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment.dedicated || environment.integrated) {
                registerCommand(dispatcher);
                Noctyra.LOGGER.info("Registering Spawn Command for Noctyra");
            }
        });
        spawnData = SpawnStorage.load();
    }

    private static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("spawn")
                .executes(ctx -> spawnCommand(ctx, null))
                .then(CommandManager.argument("player", StringArgumentType.word())
                        .suggests(NoctyraSuggestionsUtil::suggestPlayer)
                        .executes(ctx -> spawnCommand(ctx, StringArgumentType.getString(ctx, "player"))))
        );
    }

    private static int spawnCommand(CommandContext<ServerCommandSource> context, String targetName) {
        ServerPlayerEntity sender = context.getSource().getPlayer();
        if (spawnData == null) {
            sender.sendMessage(Text.translatable(CommandInit.SPAWN_NOT_SET_YET).setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));
            return 0;
        }

        ServerPlayerEntity target;
        if (targetName == null) {
            target = sender;
        } else {
            target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.PLAYER_NOT_FOUND()));
                return 0;
            }
        }

        startTeleportCountdown(context.getSource().getServer(), target);
        return 1;
    }

    private static void startTeleportCountdown(MinecraftServer server, ServerPlayerEntity player) {
        UUID id = player.getUuid();
        Vec3d startPos = player.getPos();
        pendingTeleports.put(id, startPos);

        for (int i = 0; i < 5; i++) {
            int secondsLeft = 5 - i;
            int delayTicks = i * 20; // 20 ticks = 1 second
            NoctyraTickSchedulerUtil.schedule(server, delayTicks, () -> {
                // Check if player moved
                Vec3d original = pendingTeleports.get(id);
                if (original == null) return; // already cancelled or teleported
                if (!player.getPos().isInRange(original, 0.1)) {
                    //player.sendMessage(Text.literal("§cTeleport cancelled because you moved!"));
                    player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(Text.translatable(CommandInit.TELEPORT_CANCELLED).setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
                    pendingTeleports.remove(id);
                    return;
                }

                if (secondsLeft > 1) {
                    //player.sendMessage(Text.literal("§7Teleporting in " + secondsLeft + "..."));
                    player.sendMessage(Text.translatable(CommandInit.TELEPORTING_TIMED, secondsLeft).setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));
                } else {
                    // Last second: actually teleport
                    doTeleport(player);
                    pendingTeleports.remove(id);
                }
            });
        }
    }

    private static void doTeleport(ServerPlayerEntity player) {
        RegistryKey<World> key = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(spawnData.world));
        var server = player.getServer();
        var world = server.getWorld(key);

        if (world == null) {
            player.sendMessage(Text.translatable(CommandInit.SPAWN_NOT_SET_YET).setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));
            return;
        }

        player.teleport(world, spawnData.x, spawnData.y, spawnData.z, spawnData.yaw, spawnData.pitch);
        player.sendMessage(Text.translatable(CommandInit.TELEPORTED).setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));
    }
}
