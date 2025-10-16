package dev.endless.v4.command.gamemode;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.endless.v4.Noctyra;
import dev.endless.v4.command.util.NoctyraSuggestionsUtil;
import dev.endless.v4.command.util.NoctyraTextUtil;
import dev.endless.v4.init.CommandInit;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GameModeCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((disptacher, registryAccess, environment) -> {
            if (environment.dedicated || environment.integrated) {
                registerCommand(disptacher);
                Noctyra.LOGGER.info("Registering Gamemode command for Noctyra");
            }
        });
    }
    private static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Remove vanilla gamemode command if it exists
        dispatcher.getRoot().getChildren().removeIf(node -> node.getName().equals("gamemode"));

        dispatcher.getRoot().addChild(buildGamemodeNode("gamemode"));
        dispatcher.getRoot().addChild(buildGamemodeNode("gm"));
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

    private static LiteralCommandNode<ServerCommandSource> buildGamemodeNode(String name) {
        return CommandManager.literal(name)
                .requires(source -> source.hasPermissionLevel(2))
                .executes(ctx -> {
                    var sender = ctx.getSource().getPlayer();
                    sender.sendMessage(
                            NoctyraTextUtil.INCORRECT().copy()
                                    .append(Text.translatable(CommandInit.GAMEMODE_USAGE)
                                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)))
                    );
                    return 0;
                })
                .then(CommandManager.argument("mode", StringArgumentType.word())
                        .suggests(NoctyraSuggestionsUtil::suggestModes)
                        .executes(ctx -> changeGameMode(ctx, null))
                        .then(CommandManager.argument("player", StringArgumentType.word())
                                .suggests(NoctyraSuggestionsUtil::suggestPlayer)
                                .executes(ctx -> changeGameMode(ctx, StringArgumentType.getString(ctx, "player"))))
                )
                .build();
    }
}
