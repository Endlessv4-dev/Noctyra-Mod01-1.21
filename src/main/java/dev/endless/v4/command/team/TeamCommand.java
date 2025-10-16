package dev.endless.v4.command.team;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.endless.v4.Noctyra;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TeamCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCommand(dispatcher);
            Noctyra.LOGGER.info("Registering Team command for Noctyra.");
        });
    }

    private static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.getRoot().getChildren().removeIf(node -> node.getName().equals("team"));
        dispatcher.register(
                CommandManager.literal("team")
                        .executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                            StringBuilder help = new StringBuilder();
                            for (int i  = 0; i < Noctyra.TEAM_COMMANDS.size(); i++) {
                                help.append("§7/team ").append(Noctyra.TEAM_COMMANDS.get(i)).append("\n");
                            }
                            player.sendMessage(Text.literal("§fTeam Commands:"));
                            player.sendMessage(Text.literal(help.toString()));
                            return 1;
                        })
                        .then(CommandManager.literal("create")
                                .then(CommandManager.argument("name", StringArgumentType.word())
                                        .executes(TeamCommand::createTeam)))

                        .then(CommandManager.literal("join")
                                .then(CommandManager.argument("name", StringArgumentType.word())
                                        .executes(TeamCommand::joinTeam)))

                        .then(CommandManager.literal("leave")
                                .executes(TeamCommand::leaveTeam))
        );
    }

    private static int createTeam(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        String name = StringArgumentType.getString(ctx, "name");
        TeamManager.getInstance().createTeam(name, player, player.getServer());
        return 1;
    }

    private static int joinTeam(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        String name = StringArgumentType.getString(ctx, "name");
        TeamManager.getInstance().joinTeam(name, player);
        return 1;
    }

    private static int leaveTeam(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        TeamManager.getInstance().leaveTeam(player);
        return 1;
    }
}
