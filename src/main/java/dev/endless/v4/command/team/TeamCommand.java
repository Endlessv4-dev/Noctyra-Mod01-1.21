package dev.endless.v4.command.team;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.kinds.IdF;
import dev.endless.v4.Noctyra;
import dev.endless.v4.command.util.NoctyraSuggestionsUtil;
import dev.endless.v4.command.util.NoctyraTextUtil;
import dev.endless.v4.init.CommandInit;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.*;

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
                                .then(CommandManager.argument("team", StringArgumentType.word())
                                        .executes(TeamCommand::createTeam)))

                        .then(CommandManager.literal("accept")
                                .then(CommandManager.argument("team", StringArgumentType.word())
                                        .executes(TeamCommand::acceptTeam)))

                        .then(CommandManager.literal("deny")
                                .then(CommandManager.argument("team", StringArgumentType.word())
                                        .executes(TeamCommand::denyTeam)))

                        .then(CommandManager.literal("leave")
                                .executes(TeamCommand::leaveTeam))

                        .then(CommandManager.literal("setowner")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                        .suggests(NoctyraSuggestionsUtil::suggestPlayer)
                                        .executes(ctx -> setOwner(ctx, "player"))))

                        .then(CommandManager.literal("disband")
                                .executes(TeamCommand::disbandTeam))

                        .then(CommandManager.literal("invite")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                    .suggests(NoctyraSuggestionsUtil::suggestPlayer)
                                    .executes(ctx -> inviteMember(ctx, StringArgumentType.getString(ctx, "player")))))

                        .then(CommandManager.literal("promote")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                        .suggests(NoctyraSuggestionsUtil::suggestPlayer)
                                        .then(CommandManager.argument("rank", StringArgumentType.word())
                                                .executes(ctx -> promote(ctx, StringArgumentType.getString(ctx, "player"), "rank")))))

                        .then(CommandManager.literal("info")
                                .then(CommandManager.argument("team/player", StringArgumentType.word())
                                        .executes(TeamCommand::info)))

                        .then(CommandManager.literal("toplist")
                                .executes(TeamCommand::teamToplist)
                                .then(CommandManager.argument("page", StringArgumentType.word())
                                        .executes(TeamCommand::teamToplist)))

                        .then(CommandManager.literal("chat")
                                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                        .executes(TeamCommand::teamChat)))

                        .then(CommandManager.literal("allychat")
                                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                        .executes(TeamCommand::allyChat)))

                        .then(CommandManager.literal("ranks")
                                .executes(TeamCommand::teamRanks))

                        .then(CommandManager.literal("rankinfo")
                                .then(CommandManager.argument("rank", StringArgumentType.word())
                                        .executes(TeamCommand::teamRankInfo)))

                        .then(CommandManager.literal("ally")
                                .then(CommandManager.argument("team", StringArgumentType.word())
                                        .executes(TeamCommand::ally)))

                        .then(CommandManager.literal("allyaccept")
                                .then(CommandManager.argument("team", StringArgumentType.word())
                                        .executes(TeamCommand::allyAccept)))

                        .then(CommandManager.literal("allydeny")
                                .then(CommandManager.argument("team", StringArgumentType.word())
                                        .executes(TeamCommand::allyDeny)))

                        .then(CommandManager.literal("neutral")
                                .then(CommandManager.argument("team", StringArgumentType.word())
                                        .executes(TeamCommand::neutral)))
        );
    }

    private static int allyChat(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity sender = ctx.getSource().getPlayer();
        String message = StringArgumentType.getString(ctx, "message");

        if (!TeamManager.getInstance().hasTeam(sender)) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        if (TeamManager.getInstance().getAllies(TeamManager.getInstance().getTeam(sender)).isEmpty()) {
            MutableText text = Text.translatable(CommandInit.NO_ALLIANCES)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        for (ServerPlayerEntity player : sender.getServer().getPlayerManager().getPlayerList()) {
            String playerTeam = TeamManager.getInstance().getTeam(player);
            if (TeamManager.getInstance().getAllies(TeamManager.getInstance().getTeam(sender)).contains(playerTeam)) {
                MutableText allychat = Text.translatable(CommandInit.ALLY_CHAT)
                                .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(true));

                String msg =  " §f" + sender.getName().getString() + " §8❯ §7" + message;

                player.sendMessage(allychat.copy().append(msg));
                sender.sendMessage(allychat.copy().append(msg));
                //player.sendMessage(Text.literal("§e§l[ALLYCHAT] §f" + sender.getName().getString() + " §8❯ §7" + message));
                //sender.sendMessage(Text.literal("§e§l[ALLYCHAT] §f" + sender.getName().getString() + " §8❯ §7" + message));
            }
        }

        return 1;
    }

    private static int neutral(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        MinecraftServer server = ctx.getSource().getServer();
        String targetTeam = StringArgumentType.getString(ctx, "team");

        if (!Noctyra.teams.contains(targetTeam)) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.TEAM_DOESNT_EXIST()));
            return 0;
        }

        if (!TeamManager.getInstance().getAllies(TeamManager.getInstance().getTeam(player)).contains(targetTeam)) {
            MutableText text = Text.translatable(CommandInit.TEAM_NOT_ALLY)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        TeamManager.getInstance().removeAlly(TeamManager.getInstance().getTeam(player), targetTeam);
        TeamManager.getInstance().removeAlly(targetTeam, TeamManager.getInstance().getTeam(player));
        Noctyra.teamDataManager.saveTeams();

        MutableText targetTeamName = Text.literal(targetTeam)
                .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

        MutableText text1 = Text.translatable(CommandInit.TEAM_NOW_NEUTRAL, targetTeamName)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        MutableText playerTeamName = Text.literal(TeamManager.getInstance().getTeam(player))
                .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

        MutableText text2 = Text.translatable(CommandInit.TEAM_NOW_NEUTRAL, playerTeamName)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        //broadcastToTeam(TeamManager.getInstance().getTeam(player), Text.literal("§f§lNEUTRAL | §7Your team is now neutral with §e" + targetTeam + "§7!"), server);
        broadcastToTeam(TeamManager.getInstance().getTeam(player), text1.copy(), server);
        //broadcastToTeam(targetTeam, Text.literal("§f§lNEUTRAL | §7Your team is now neutral with §e" + TeamManager.getInstance().getTeam(player) + "§7!"), server);
        broadcastToTeam(TeamManager.getInstance().getTeam(player), text2.copy(), server);

        return 1;
    }

    private static int allyDeny(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        MinecraftServer server = ctx.getSource().getServer();
        String requesterTeam = StringArgumentType.getString(ctx, "team");

        String playerTeam = Noctyra.playerInTeam.get(player.getUuid());
        if (playerTeam == null) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        boolean isOwner = TeamManager.getInstance().isOwner(player);
        boolean isConsul = Noctyra.TEAM_RANK_CONSUL.containsKey(player.getUuid())
                && Noctyra.TEAM_RANK_CONSUL.get(player.getUuid()).equalsIgnoreCase(playerTeam);

        if (!isOwner && !isConsul) {
            //player.sendMessage(Text.literal("§c§lINCORRECT | §7You don't have permission to deny alliances."));
            MutableText text = Text.translatable(CommandInit.NO_PERMISSION_TO_DENY_ALLIANCE)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        boolean hasRequest = Noctyra.pendingAllyRequests.containsValue(requesterTeam);
        if (!hasRequest) {
            MutableText reqTeam = Text.literal(requesterTeam)
                            .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

            MutableText text = Text.translatable(CommandInit.NO_ALLIANCE_REQUEST_FOUND, reqTeam)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        if (!Noctyra.teams.contains(requesterTeam)) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.TEAM_DOESNT_EXIST()));
            return 0;
        }


        Noctyra.pendingAllyRequests.entrySet().removeIf(entry -> entry.getValue().equalsIgnoreCase(requesterTeam));

        MutableText reqTeam = Text.literal(requesterTeam)
                        .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

        MutableText text = Text.translatable(CommandInit.ALLIANCE_DENIED, reqTeam)
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        MutableText allyPr = Text.translatable(CommandInit.ALLIANCE)
                        .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(true));

        MutableText sepator = Text.literal(" | ")
                .setStyle(Style.EMPTY.withColor(0x555555).withBold(false));

        broadcastToTeam(playerTeam, allyPr.copy().append(sepator).append(text), server);

        MutableText plrTeam = Text.literal(playerTeam)
                .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

        MutableText text2 = Text.translatable(CommandInit.ALLIANCE_DENIED_SELF, plrTeam)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
        player.sendMessage(text2);

        return 1;
    }

    private static int allyAccept(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        MinecraftServer server = ctx.getSource().getServer();
        String requesterTeam = StringArgumentType.getString(ctx, "team");

        String playerTeam = Noctyra.playerInTeam.get(player.getUuid());
        if (playerTeam == null) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        boolean isOwner = TeamManager.getInstance().isOwner(player);
        boolean isConsul = Noctyra.TEAM_RANK_CONSUL.containsKey(player.getUuid())
                && Noctyra.TEAM_RANK_CONSUL.get(player.getUuid()).equalsIgnoreCase(playerTeam);

        if (!isOwner && !isConsul) {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7You don't have permission to accept alliances."));
            MutableText text = Text.translatable(CommandInit.NO_PERMISSION_TO_ACCEPT_ALLIANCE)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        boolean hasRequest = Noctyra.pendingAllyRequests.containsValue(requesterTeam);
        if (!hasRequest) {
            MutableText reqTeam = Text.literal(requesterTeam)
                    .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

            MutableText text = Text.translatable(CommandInit.NO_ALLIANCE_REQUEST_FOUND, reqTeam)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        if (!Noctyra.teams.contains(requesterTeam)) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.TEAM_DOESNT_EXIST()));
            return 0;
        }

        TeamManager.getInstance().addAlly(playerTeam, requesterTeam);
        TeamManager.getInstance().addAlly(requesterTeam, playerTeam);
        Noctyra.teamDataManager.saveTeams();

        Noctyra.pendingAllyRequests.entrySet().removeIf(entry -> entry.getValue().equalsIgnoreCase(requesterTeam));

        MutableText reqTeam = Text.literal(requesterTeam)
                .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

        MutableText text = Text.translatable(CommandInit.ALLIANCE_ACCEPTED, reqTeam)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        MutableText plrTeam = Text.literal(playerTeam)
                .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

        MutableText text2 = Text.translatable(CommandInit.ALLIANCE_ACCEPTED, plrTeam)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        MutableText allyPr = Text.translatable(CommandInit.ALLIANCE)
                .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(true));

        MutableText sepator = Text.literal(" | ")
                .setStyle(Style.EMPTY.withColor(0x555555).withBold(false));

        //broadcastToTeam(playerTeam, Text.literal("§a§lALLIANCE | §7Your team is now allied with §e" + requesterTeam + "§7!"), server);
        broadcastToTeam(playerTeam, allyPr.copy().append(sepator).append(text), server);
        broadcastToTeam(requesterTeam, allyPr.copy().append(sepator).append(text2), server);

        return 1;
    }

    private static int ally(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity sender = ctx.getSource().getPlayer();
        String targetTeam = StringArgumentType.getString(ctx, "team");
        MinecraftServer server = ctx.getSource().getServer();

        String senderTeam = Noctyra.playerInTeam.get(sender.getUuid());
        if (senderTeam == null) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        boolean isOwner = TeamManager.getInstance().isOwner(sender);
        boolean isConsul = Noctyra.TEAM_RANK_CONSUL.containsKey(sender.getUuid())
                && Noctyra.TEAM_RANK_CONSUL.get(sender.getUuid()).equalsIgnoreCase(senderTeam);

        if (!isOwner && !isConsul) {
            MutableText text = Text.translatable(CommandInit.NO_PERMISSION_TO_MAKE_ALLIANCE)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        if (senderTeam.equalsIgnoreCase(targetTeam)) {
            MutableText text = Text.translatable(CommandInit.CANNOT_ALLY_OWN)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        if (!Noctyra.teams.contains(targetTeam)) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.TEAM_DOESNT_EXIST()));
            return 0;
        }

        // Collect all eligible recipients (owner + consuls)
        List<ServerPlayerEntity> recipients = new ArrayList<>();

        // Add the owner if online
        ServerPlayerEntity targetOwner = TeamManager.getInstance().getOwner(targetTeam, server);
        if (targetOwner != null) recipients.add(targetOwner);

        // Add all online consuls from that team
        for (Map.Entry<UUID, String> entry : Noctyra.TEAM_RANK_CONSUL.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(targetTeam)) {
                ServerPlayerEntity consul = server.getPlayerManager().getPlayer(entry.getKey());
                if (consul != null) recipients.add(consul);
            }
        }

        if (recipients.isEmpty()) {
            MutableText text = Text.translatable(CommandInit.NO_PLAYER_PERMISSION_ONLINE_ALLIANCE)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(text));
            return 0;
        }

        for (ServerPlayerEntity target : recipients) {
            Noctyra.pendingAllyRequests.put(target.getUuid(), senderTeam);

            MutableText team = Text.literal(senderTeam)
                            .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

            MutableText text = Text.translatable(CommandInit.ALLIANCE_REQUEST_MESSAGE, team)
                            .setStyle(Style.EMPTY.withColor(0xAAAAA).withBold(false));

            MutableText pref = Text.translatable(CommandInit.ALLIANCE_REQUEST_PREFIX)
                            .setStyle(Style.EMPTY.withColor(0xFFAA00).withBold(true));

            //target.sendMessage(Text.literal("§6[ALLIANCE REQUEST] §e" + senderTeam + " §7wants to form an alliance."));
            target.sendMessage(pref.copy().append(" ").append(text));

            MutableText replyAcc = Text.literal("/team allyaccept " + senderTeam)
                    .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

            MutableText replyDen = Text.literal("/team allydeny " + senderTeam)
                    .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

            MutableText reply = Text.translatable(CommandInit.ALLIANCE_REQUEST_MESSAGE_ND, replyAcc, replyDen)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

            //target.sendMessage(Text.literal("§7Type §e/team allyaccept " + senderTeam + " §7to accept, or §e/team allydeny " + senderTeam + " §7to deny."));
            target.sendMessage(reply.copy());
        }

        MutableText trgtTeam = Text.literal(targetTeam)
                        .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

        MutableText text = Text.translatable(CommandInit.ALLIANCE_REQUEST_SENT, trgtTeam)
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        sender.sendMessage(text.copy());
        return 1;
    }

    private static int teamRankInfo(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity sender = ctx.getSource().getPlayer();
        String rank = StringArgumentType.getString(ctx, "rank");

        if (rank.equalsIgnoreCase("scout")) {

            sender.sendMessage(Text.literal("§e§lSCOUT: ").copy()
                    .append(Text.translatable(CommandInit.RANK_SCOUT_DESC)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
        }
        else if (rank.equalsIgnoreCase("scoutmaster")) {

            MutableText scout = Text.literal("§eSCOUT")
                            .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));

            MutableText text = Text.translatable(CommandInit.RANK_SCOUTMASTER_DESC, scout)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

            sender.sendMessage(Text.literal("§e§lSCOUTMASTER: ").copy().append(text));
        }
        else if (rank.equalsIgnoreCase("consul")) {
            sender.sendMessage(Text.literal("§e§lCONSUL: ").copy()
                    .append(Text.translatable(CommandInit.RANK_CONSUL_DESC)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
        }
        else {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(Text.translatable(CommandInit.RANK_DOESNT_EXIST).setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
            return 0;
        }

        return 1;
    }

    private static int teamRanks(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity sender = ctx.getSource().getPlayer();

        sender.sendMessage(Text.translatable(CommandInit.RANK_TITLE).setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(true)));
        sender.sendMessage(Text.literal("§fScout§7, §fScoutMaster§7, §fConsul"));

        return 1;
    }

    private static int teamChat(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity sender = ctx.getSource().getPlayer();
        String message = StringArgumentType.getString(ctx, "message");

        if (!TeamManager.getInstance().hasTeam(sender)) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        for (ServerPlayerEntity player : sender.getServer().getPlayerManager().getPlayerList()) {
            String playerTeam = TeamManager.getInstance().getTeam(player);
            if (TeamManager.getInstance().getTeam(sender).equals(playerTeam)) {
                MutableText teamchat = Text.translatable(CommandInit.TEAM_CHAT)
                        .setStyle(Style.EMPTY.withColor(0xFFFFFF).withBold(true));

                String msg =  " §c" + sender.getName().getString() + " §8❯ §7" + message;

                player.sendMessage(teamchat.copy().append(msg));
                //player.sendMessage(Text.literal("§f§l[TEAMCHAT] §c" + sender.getName().getString() + " §8❯ §7" + message));
            }
        }

        return 1;
    }

    private static int teamToplist(CommandContext<ServerCommandSource> ctx) {

        ServerPlayerEntity player = ctx.getSource().getPlayer();

        int page = 1;
        try {
            if (ctx.getInput().split(" ").length > 2) {
                page = Integer.parseInt(ctx.getInput().split(" ")[2]);
            }
        } catch (NumberFormatException ignored) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy()
                    .append(Text.translatable(CommandInit.RANK_TOPLIST_INVALID_PAGE)
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
            return 0;
        }

        final int ENTRIES_PER_PAGE = 6;

        Map<String, Integer> teamKills = new HashMap<>();
        for (String team : Noctyra.teams) {
            int kills = Noctyra.teamKills.getOrDefault(team, 0);
            teamKills.put(team, kills);
        }

        List<Map.Entry<String, Integer>> sortedTeams = new ArrayList<>(teamKills.entrySet());
        sortedTeams.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int totalPages = (int) Math.ceil((double) sortedTeams.size() / ENTRIES_PER_PAGE);
        if (page > totalPages) {
            //player.sendMessage(Text.literal("§cPage " + page + " does not exist! Max page: " + totalPages));
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(Text.translatable(CommandInit.PAGE_DOESNT_EXIST, page, totalPages)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
            return 0;
        }

        int startIndex = (page - 1) * ENTRIES_PER_PAGE;
        int endIndex = Math.min(startIndex + ENTRIES_PER_PAGE, sortedTeams.size());

        //player.sendMessage(Text.literal("§e§lTEAM TOPLIST §8(Page " + page + "/" + totalPages + ")"));

        MutableText pgs = Text.literal(" " + page + "/" + totalPages + ")")
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        MutableText pgsTranslate = Text.translatable(CommandInit.TEAM_TOPLIST_PAGE)
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        MutableText sep = Text.literal(" (")
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false));

        MutableText pref = Text.translatable(CommandInit.TEAM_TOPLIST_TITLE)
                        .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(true));

        player.sendMessage(pref.copy().append(sep).append(pgsTranslate).append(pgs));

        for (int i = startIndex; i < endIndex; i++) {
            Map.Entry<String, Integer> entry = sortedTeams.get(i);
            int rank = i + 1;
            player.sendMessage(Text.literal("§e" + rank + ". §6" + entry.getKey() + " §8- §7" + entry.getValue()));
        }

        if (page < totalPages) {
            //player.sendMessage(Text.literal("§7Type §e/team toplist " + (page + 1) + " §7to see the next page."));
            MutableText command = Text.literal("/team toplist").setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false));
            player.sendMessage(Text.translatable(CommandInit.TEAM_TOPLIST_NEXT_PAGE, command, (page + 1))
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));
        }

        return 1;
    }

    private static int info(CommandContext<ServerCommandSource> ctx) {
        
        // TODO: Bejefezni az infókat és fordításokat csinálni
        
        ServerPlayerEntity sender = ctx.getSource().getPlayer();
        String teamOrPlayer = StringArgumentType.getString(ctx, "team/player");

        if (ctx.getSource().getServer().getPlayerManager().getPlayer(teamOrPlayer) instanceof ServerPlayerEntity player) {
            String team = "Not in a team";
            String rank = "No rank";
            if (TeamManager.getInstance().hasTeam(player)) {
                team = TeamManager.getInstance().getTeam(player);
                if (Noctyra.TEAM_PROMOTER_SCOUT.containsKey(player.getUuid())) {
                    rank = "SCOUTMASTER";
                } else if (Noctyra.TEAM_RANK_SCOUT.containsKey(player.getUuid())) {
                    rank = "SCOUT";
                }
            }
            sender.sendMessage(Text.literal("§7Info of the player §e" + player.getName().getString()));
            sender.sendMessage(Text.literal("§eTeam: §7" + team));
            sender.sendMessage(Text.literal("§eRank: §7" + rank));
        } else {
            if (Noctyra.teams.contains(teamOrPlayer)) {
                int playerCount = 0;
                for (String team : Noctyra.teams) {
                    if (team.equalsIgnoreCase(teamOrPlayer)) {
                        for (String value : Noctyra.playerInTeam.values()) {
                            if (value.equalsIgnoreCase(teamOrPlayer)) playerCount++;
                        }
                    }
                }

                sender.sendMessage(Text.literal("§7Info of the team §e" + teamOrPlayer));
                sender.sendMessage(Text.literal("§eOwner: §7" + TeamManager.getInstance().getOwner(teamOrPlayer, ctx.getSource().getServer()).getName().getString()));
                sender.sendMessage(Text.literal("§ePlayers: §7" + playerCount));
            } else {
                sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.TEAM_DOESNT_EXIST()));
            }
        }

        return 1;
    }

    private static int inviteMember(CommandContext<ServerCommandSource> ctx, String targetName) {
        ServerPlayerEntity sender = ctx.getSource().getPlayer();

        if (!TeamManager.getInstance().hasTeam(sender)) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        if (!TeamManager.getInstance().isOwner(sender)
                && !Noctyra.TEAM_RANK_SCOUT.containsKey(sender.getUuid())
                && !Noctyra.TEAM_PROMOTER_SCOUT.containsKey(sender.getUuid())) {
            //sender.sendMessage(Text.literal("§c§lINCORRECT | §7You don't have the required permission to invite players to the team."));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(Text.translatable(CommandInit.NO_PERMISSION_TO_INVITE)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
            return 0;
        }

        ServerPlayerEntity target;
        if (targetName == null) {
            target = sender;
        } else {
            target = ctx.getSource().getServer().getPlayerManager().getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.PLAYER_NOT_FOUND()));

                return 0;
            }
        }

        if (target.getName() == sender.getName()) {
            //sender.sendMessage(Text.literal("§c§lINCORRECT | §7You cannot invite yourself."));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(CommandInit.CANT_INVITE_SELF)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));
            return 0;
        }

        if (TeamManager.getInstance().hasTeam(target)) {
            //sender.sendMessage(Text.literal("§c§lINCORRECT | §7This player is already in a team."));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(CommandInit.PLAYER_ALREADY_IN_TEAM)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));
            return 0;
        }

        Noctyra.invitedPlayer.put(target.getUuid(), TeamManager.getInstance().getTeam(sender));
        sender.sendMessage(Text.literal("§7You have invited §b" + target.getName().getString() + " §7to the team."));
        sender.sendMessage(Text.translatable(CommandInit.INVITE_PLAYER_SUCCESS, Text.literal(target.getName().getString())
                        .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false)))
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));

        //target.sendMessage(Text.literal("§7You have been invited to the §e" + TeamManager.getInstance().getTeam(sender) + " §7team."));
        sender.sendMessage(Text.translatable(CommandInit.INVITE_MESSAGE, Text.literal(TeamManager.getInstance().getTeam(sender))
                        .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false)))
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));

        //target.sendMessage(Text.literal("§7Type §e/team accept " + TeamManager.getInstance().getTeam(sender) + " §7to join or §c/team deny " + TeamManager.getInstance().getTeam(sender) + " §7to dismiss the invitation."));

        sender.sendMessage(Text.translatable(CommandInit.INVITE_MESSAGE_ND, Text.literal("/team accept " + TeamManager.getInstance().getTeam(sender))
                        .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false)), Text.literal("/team deny " + TeamManager.getInstance().getTeam(sender))
                        .setStyle(Style.EMPTY.withColor(0xFFFF55).withBold(false)))
                .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false)));


        return 1;
    }

    private static int promote(CommandContext<ServerCommandSource> ctx, String player, String rankName) {
        ServerPlayerEntity sender = ctx.getSource().getPlayer();
        ServerPlayerEntity target = ctx.getSource().getServer().getPlayerManager().getPlayer(player);
        String rank = StringArgumentType.getString(ctx, rankName).toLowerCase();

        if (!TeamManager.getInstance().hasTeam(sender)) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        if (target == null) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.PLAYER_NOT_FOUND()));
            return 0;
        }

        if (!TeamManager.getInstance().hasTeam(target)) {
            //sender.sendMessage(Text.literal("§c§lINCORRECT | §7This player is not in a team."));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(Text.translatable(CommandInit.OTHER_NOT_IN_TEAM)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
            return 0;
        }

        if (!Objects.equals(TeamManager.getInstance().getTeam(sender), TeamManager.getInstance().getTeam(target))) {
            //sender.sendMessage(Text.literal("§c§lINCORRECT | §7This player is not in your team."));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(Text.translatable(CommandInit.PLAYER_NOT_IN_YOUR_TEAM)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
            return 0;
        }

        if (target.getName() == sender.getName()) {
            //sender.sendMessage(Text.literal("§c§lINCORRECT | §7You cannot promote yourself."));
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(Text.translatable(CommandInit.CANNOT_PROMOTE_SELF)
                    .setStyle(Style.EMPTY.withColor(0xAAAAAA).withBold(false))));
            return 0;
        }

        // TODO: Fordítások befejezése.

        switch (rank) {
            case "scout" -> {
                if (!TeamManager.getInstance().isOwner(sender) && !Noctyra.TEAM_PROMOTER_SCOUT.containsKey(sender.getUuid())) {
                    sender.sendMessage(Text.literal("§c§lINCORRECT | §7You don't have permission to promote a player to §eSCOUT§7."));
                    return 0;
                }

                Noctyra.TEAM_RANK_SCOUT.put(target.getUuid(), TeamManager.getInstance().getTeam(sender));
                Noctyra.teamDataManager.saveTeams();
                sender.sendMessage(Text.literal("§7You have successfully promoted §b" + target.getName().getString() + " §7to §eSCOUT§7."));
                target.sendMessage(Text.literal("§7You have been promoted to §eSCOUT§7."));
            }
            case "scoutmaster" -> {
                if (!TeamManager.getInstance().isOwner(sender)) {
                    sender.sendMessage(Text.literal("§c§lINCORRECT | §7You don't have permission to promote a player to §eSCOUTMASTER§7."));
                    return 0;
                }

                Noctyra.TEAM_PROMOTER_SCOUT.put(target.getUuid(), TeamManager.getInstance().getTeam(sender));
                Noctyra.teamDataManager.saveTeams();
                sender.sendMessage(Text.literal("§7You have successfully promoted §b" + target.getName().getString() + " §7to §eSCOUTMASTER§7."));
                target.sendMessage(Text.literal("§7You have been promoted to §eSCOUTMASTER§7."));
            }
            case "consul" -> {
                if (!TeamManager.getInstance().isOwner(sender)) {
                    sender.sendMessage(Text.literal("§c§lINCORRECT | §7You don't have permission to promote a player to §eCONSUL§7."));
                    return 0;
                }

                Noctyra.TEAM_RANK_CONSUL.put(target.getUuid(), TeamManager.getInstance().getTeam(sender));
                Noctyra.teamDataManager.saveTeams();
                sender.sendMessage(Text.literal("§7You have successfully promoted §b" + target.getName().getString() + " §7to §eCONSUL§7."));
                target.sendMessage(Text.literal("§7You have been promoted to §eCONSUL§7."));
            }
            default -> {
                sender.sendMessage(Text.literal("§c§lINCORRECT | §7This rank doesn't exist (§e" + rank + "§7)."));
                return 0;
            }
        }

        return 1;
    }

    private static int createTeam(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        String name = StringArgumentType.getString(ctx, "team");
        TeamManager.getInstance().createTeam(name, player, player.getServer());
        return 1;
    }

    private static int acceptTeam(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        String teamName = StringArgumentType.getString(ctx, "team");
        String name = teamName.toLowerCase();

        if (TeamManager.getInstance().hasTeam(player))  {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7You are already in a team."));
            return 0;
        }

        boolean exists = Noctyra.teams.stream()
                .anyMatch(t -> t.equalsIgnoreCase(teamName));

        if (!exists) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.TEAM_DOESNT_EXIST()));
            return 0;
        }

        if (!Noctyra.invitedPlayer.containsKey(player.getUuid()) && !Noctyra.invitedPlayer.containsValue(name)) {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7You haven't been invited to this team (§e" + teamName + "§7)."));
            return 0;
        }



        TeamManager.getInstance().joinTeam(name, player);
        Noctyra.invitedPlayer.remove(player.getUuid());

        for (ServerPlayerEntity players : ctx.getSource().getServer().getPlayerManager().getPlayerList()) {
            String team = TeamManager.getInstance().getTeam(players);
            if (team != null && team.equalsIgnoreCase(name)) {
                players.sendMessage(Text.literal("§e" + player.getName().getString() + " §7has joined the team."));
            }
        }

        player.sendMessage(Text.literal("§7You have joined the §e" + teamName + " §7team."));
        return 1;
    }

    private static int denyTeam(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        String teamName = StringArgumentType.getString(ctx, "team");
        String name = teamName.toLowerCase();

        boolean exists = Noctyra.teams.stream()
                .anyMatch(t -> t.equalsIgnoreCase(teamName));

        if (!exists) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.TEAM_DOESNT_EXIST()));
            return 0;
        }

        if (!Noctyra.invitedPlayer.containsKey(player.getUuid()) && !Noctyra.invitedPlayer.containsValue(name)) {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7You haven't been invited to this team (§e" + teamName + "§7)."));
            return 0;
        }

        Noctyra.invitedPlayer.remove(player.getUuid());
        player.sendMessage(Text.literal("§7You have dismissed the invitation from §e" + teamName + " §7team."));

        return 1;
    }

    private static int leaveTeam(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        TeamManager.getInstance().leaveTeam(player);
        return 1;
    }

    private static int setOwner(CommandContext<ServerCommandSource> ctx, String playerName) {
        ServerPlayerEntity sender = ctx.getSource().getPlayer();
        ServerPlayerEntity target = ctx.getSource().getServer().getPlayerManager().getPlayer(playerName);

        if (!TeamManager.getInstance().hasTeam(sender)) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        if (!TeamManager.getInstance().isOwner(sender)) {
            sender.sendMessage(Text.literal("§c§lINCORRECT | §7You are not the team owner."));
            return 0;
        }

        if (target == null) {
            sender.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.PLAYER_NOT_FOUND()));
            return 0;
        }

        if (!TeamManager.getInstance().hasTeam(target)) {
            sender.sendMessage(Text.literal("§c§lINCORRECT | §7This player is not in a team (§e" + target.getName().getString() + "§7)."));
            return 0;
        }

        if (!Objects.equals(TeamManager.getInstance().getTeam(target), TeamManager.getInstance().getTeam(sender))) {
            sender.sendMessage(Text.literal("§c§lINCORRECT | §7This player is not in your team."));
            return 0;
        }

        sender.sendMessage(Text.literal("§7You have successfully set the team owner to be §e" + target.getName().getString() + "§7."));

        Noctyra.teamOwner.put(target.getUuid(), TeamManager.getInstance().getTeam(sender));
        Noctyra.teamOwner.remove(sender.getUuid());
        Noctyra.teamDataManager.saveTeams();
        return 1;
    }

    private static int disbandTeam(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();

        if (!TeamManager.getInstance().hasTeam(player)) {
            player.sendMessage(NoctyraTextUtil.INCORRECT().copy().append(NoctyraTextUtil.NOT_IN_TEAM()));
            return 0;
        }

        if (!TeamManager.getInstance().isOwner(player)) {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7You are not the team owner."));
            return 0;
        }

        String teamName = TeamManager.getInstance().getTeam(player);

        for (ServerPlayerEntity players : ctx.getSource().getServer().getPlayerManager().getPlayerList()) {
            if (Objects.equals(TeamManager.getInstance().getTeam(players), teamName)) {
                Noctyra.playerInTeam.remove(players.getUuid());
                players.setCustomName(Text.literal(player.getName().getString()));
                players.setCustomNameVisible(false);
            }
        }

        Noctyra.teamOwner.values().removeIf(v -> v.equals(teamName));
        Noctyra.playerInTeam.values().removeIf(v -> v.equals(teamName));
        Noctyra.teams.remove(teamName);

        player.sendMessage(Text.literal("§7You have successfully disbanded your team (§e" + teamName + "§7)."));

        Noctyra.teamDataManager.saveTeams();

        player.setCustomName(Text.literal(player.getName().getString()));
        player.setCustomNameVisible(false);

        return 1;
    }

    private static void broadcastToTeam(String team, Text message, MinecraftServer server) {
        for (Map.Entry<UUID, String> entry : Noctyra.playerInTeam.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(team)) {
                ServerPlayerEntity member = server.getPlayerManager().getPlayer(entry.getKey());
                if (member != null) {
                    member.sendMessage(message);
                }
            }
        }
    }
}
