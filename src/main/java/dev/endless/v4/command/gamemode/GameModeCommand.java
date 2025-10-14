package dev.endless.v4.command.gamemode;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.endless.v4.Noctyra;
import dev.endless.v4.command.util.NoctyraTextUtil;
import dev.endless.v4.init.CommandInit;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class GameModeCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((disptacher, registryAccess, environment) -> {
            if (environment.dedicated || environment.integrated) {
                registerCommand(disptacher);
                Noctyra.LOGGER.info("Registering Gamemode Command for noctyra");
            }
        });
    }
    private static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gamemode")
                .requires(source -> source.hasPermissionLevel(2)) // requires OP
                .then(CommandManager.argument("mode", StringArgumentType.word())
                        .executes(ctx -> changeGameMode(ctx, null))
                        .then(CommandManager.argument("player", StringArgumentType.word())
                                .executes(ctx -> changeGameMode(ctx, StringArgumentType.getString(ctx, "player")))))
        );
    }
    private static int changeGameMode(CommandContext<ServerCommandSource> context, String targetName) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity sender = source.getPlayer();
        String modeName = StringArgumentType.getString(context, "mode").toLowerCase();
        GameMode mode = switch (modeName) {
            case "creative", "c", "1" -> GameMode.CREATIVE;
            case "survival", "s", "0" -> GameMode.SURVIVAL;
            case "adventure", "a", "2" -> GameMode.ADVENTURE;
            case "spectator", "sp", "3" -> GameMode.SPECTATOR;
            default -> {
                sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(Text.translatable(CommandInit.GAMEMODE_USAGE).setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
                yield null;
            }
        };

        if (mode == null) return 0;

        ServerPlayerEntity target;
        if (targetName == null) {
            target = sender;
        } else {
            target = source.getServer().getPlayerManager().getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.PLAYER_NOT_FOUND()));

                return 0;
            }
        }
        target.changeGameMode(mode);

        MutableText displayName = switch (mode) {
            case CREATIVE -> Text.translatable(CommandInit.MODE_CREATIVE);
            case SURVIVAL -> Text.translatable(CommandInit.MODE_SURVIVAL);
            case ADVENTURE -> Text.translatable(CommandInit.MODE_ADVENTURE);
            case SPECTATOR -> Text.translatable(CommandInit.MODE_SPECTATOR);
        };

        if (target == sender) {
            MutableText modeN = displayName.copy()
                    .setStyle(Style.EMPTY.withColor(0x976cbd).withBold(false));

            MutableText message = Text.translatable(CommandInit.GAMEMODE_SELF, modeN)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

            sender.sendMessage(NoctyraTextUtil.PREFIX().copy().append(message));
        } else {
            MutableText modeN = displayName.copy()
                    .setStyle(Style.EMPTY.withColor(0x976cbd).withBold(false));

            MutableText playerT = target.getName().copy()
                    .setStyle(Style.EMPTY.withColor(0xceb6f5).withBold(false));

            MutableText message = Text.translatable(CommandInit.GAMEMODE_OTHER, playerT, modeN)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

            MutableText messageS = Text.translatable(CommandInit.GAMEMODE_SELF, modeN)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

            sender.sendMessage(NoctyraTextUtil.PREFIX().copy().append(message));
            target.sendMessage(NoctyraTextUtil.PREFIX().copy().append(messageS));
        }
        Noctyra.LOGGER.info(sender.getName().getString() + " changed the gamemode for " + target.getName().getString() + " to " + modeName);
        return 1;
    }
}
