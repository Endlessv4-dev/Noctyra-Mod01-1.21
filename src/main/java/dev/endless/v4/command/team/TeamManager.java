package dev.endless.v4.command.team;

import dev.endless.v4.Noctyra;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class TeamManager {
    private static TeamManager instance;
    public static TeamManager getInstance() {
        if (instance == null) instance = new TeamManager();
        return instance;
    }

    public final List<String> teams = new ArrayList<>();
    public final Map<UUID, String> playerTeam = new HashMap<>();
    public final Map<UUID, String> teamOwner = new HashMap<>();

    public boolean hasTeam(ServerPlayerEntity player) {
        return Noctyra.playerInTeam.containsKey(player.getUuid());
    }

    public String getTeam(ServerPlayerEntity player) {
        return Noctyra.playerInTeam.get(player.getUuid());
    }

    public boolean isOwner(ServerPlayerEntity player) {
        return Noctyra.teamOwner.containsKey(player.getUuid());
    }

    public void updateDisplayName(ServerPlayerEntity player) {
        String team = getTeam(player);
        if (team == null) {
            player.setCustomName(Text.literal(player.getName().getString()));
            player.setCustomNameVisible(false);
            return;
        }

        Formatting teamColor = getTeamColor(team);
        MutableText name = Text.literal("[")
                .formatted(Formatting.DARK_GRAY)
                .append(Text.literal(team).formatted(teamColor))
                .append(Text.literal("] ").formatted(Formatting.DARK_GRAY))
                .append(Text.literal(player.getName().getString()).formatted(Formatting.WHITE));

        player.setCustomName(name);
        player.setCustomNameVisible(true);
    }


    public void createTeam(String team, ServerPlayerEntity creator, MinecraftServer server) {
        if (hasTeam(creator)) {
            creator.sendMessage(Text.literal("§c§lINCORRECT | §7You are already in a team."));
            return;
        }

        if (Noctyra.teams.stream().anyMatch(t -> t.equalsIgnoreCase(team))) {
            creator.sendMessage(Text.literal("§c§lINCORRECT | §7This name is already used (§e"+ team +"§7). Please choose a different one."));
            return;
        }

        Noctyra.teams.add(team);
        Noctyra.playerInTeam.put(creator.getUuid(), team);
        Noctyra.teamOwner.put(creator.getUuid(), team);
        Noctyra.teamDataManager.saveTeams();


        //creator.setCustomName(Text.literal(getTeamColor(creator) + team + " §8| §r" + creator.getNameForScoreboard()));
        updateDisplayName(creator);
        creator.sendMessage(Text.literal("§7You have successfully created §e" + team + " §7team."));
    }

    public void leaveTeam(ServerPlayerEntity player) {
        if (!hasTeam(player)) {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7You are not in a team."));
            return;
        }

        if (Noctyra.teamOwner.containsKey(player.getUuid())) {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7You cannot leave the team because you are the owner."));
            return;
        }

        String teamName = getTeam(player);
        Noctyra.playerInTeam.remove(player.getUuid());
        Noctyra.TEAM_RANK_CONSUL.computeIfPresent(player.getUuid(), (uuid, value) -> null);
        Noctyra.TEAM_RANK_SCOUT.computeIfPresent(player.getUuid(), (uuid, value) -> null);
        Noctyra.TEAM_PROMOTER_SCOUT.computeIfPresent(player.getUuid(), (uuid, value) -> null);
        Noctyra.teamDataManager.saveTeams();

        player.sendMessage(Text.literal("§7You have left the §e" + teamName + " §7team."));
        player.setCustomName(Text.literal(player.getName().getString()));
        player.setCustomNameVisible(false);
    }

    public void joinTeam(String team, ServerPlayerEntity player) {
        if (hasTeam(player)) {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7You are already in a team."));
            return;
        }

        Optional<String> existing = Noctyra.teams.stream()
                .filter(t -> t.equalsIgnoreCase(team))
                .findFirst();

        if (existing.isEmpty()) {
            player.sendMessage(Text.literal("§c§lINCORRECT | §7This team doesn't exist."));
            return;
        }

        Noctyra.playerInTeam.put(player.getUuid(), existing.get());
        Noctyra.teamDataManager.saveTeams();

        player.sendMessage(Text.literal("§7You have joined the §e" + existing.get() + " §7team."));
    }

    public void addKill(ServerPlayerEntity killer, ServerPlayerEntity victim) {
        String killerTeam = Noctyra.playerInTeam.get(killer.getUuid());
        String victimTeam = Noctyra.playerInTeam.get(victim.getUuid());

        // Skip if killer not in a team
        if (killerTeam == null) return;

        // Skip if victim is on the same team
        if (killerTeam.equals(victimTeam)) return;

        // Add +1 kill
        int kills = Noctyra.teamKills.getOrDefault(killerTeam, 0) + 1;
        Noctyra.teamKills.put(killerTeam, kills);

        // Save immediately
        Noctyra.teamDataManager.saveTeams();

        // Optional feedback to killer
        killer.sendMessage(Text.literal("§7Your team now has §e" + kills + " §7kills."));
    }

    public int getKills(String team) {
        if (team == null || team.isEmpty()) return 0;
        return Noctyra.teamKills.getOrDefault(team, 0);
    }

    public void addAlly(String team, String ally) {
        Noctyra.teamAllies.computeIfAbsent(team, k -> new ArrayList<>()).add(ally);
    }

    public void removeAlly(String team, String ally) {
        List<String> allies = Noctyra.teamAllies.get(team);
        if (allies != null) {
            allies.remove(ally);
        }
    }

    public List<String> getAllies(String team) {
        return Noctyra.teamAllies.get(team);
    }

    public ServerPlayerEntity getOwner(String team, MinecraftServer server) {
        for (Map.Entry<UUID, String> entry : Noctyra.teamOwner.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(team)) {
                UUID ownerUUID = entry.getKey();
                return server.getPlayerManager().getPlayer(ownerUUID); // returns null if offline
            }
        }
        return null; // no team or no owner found
    }

    public Formatting getTeamColor(String teamName) {
        // TODO: bővíteni
        return Formatting.AQUA; // You can later make this dynamic (kills, rank, etc.)
    }
}
