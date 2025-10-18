package dev.endless.v4.command.team;

import com.google.gson.*;
import dev.endless.v4.Noctyra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamDataManager {
    private static final Path FILE_PATH = Paths.get("config/teams.json");

    public void saveTeams() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject root = new JsonObject();

        for (String team : Noctyra.teams) {
            JsonObject teamObj = new JsonObject();
            teamObj.addProperty("ownerUUID", getOwnerUUID(team));
            teamObj.add("players", gson.toJsonTree(getPlayers(team)));
            teamObj.add("allies", new Gson().toJsonTree(getAllies(team)));
            teamObj.add("scouts", gson.toJsonTree(getScouts(team)));
            teamObj.add("scoutmasters", gson.toJsonTree(getScoutMasters(team)));
            teamObj.add("consul", gson.toJsonTree(getConsul(team)));

            int kills = Noctyra.teamKills.getOrDefault(team, 0);
            teamObj.addProperty("kills", kills);

            root.add(team, teamObj);
        }

        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.writeString(FILE_PATH, gson.toJson(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTeams() {
        if (!Files.exists(FILE_PATH)) return;

        try {
            JsonObject root = JsonParser.parseString(Files.readString(FILE_PATH)).getAsJsonObject();

            for (String team : root.keySet()) {
                JsonObject teamObj = root.getAsJsonObject(team);
                Noctyra.teams.add(team);

                if (teamObj.has("ownerUUID")) {
                    String ownerUUID = teamObj.get("ownerUUID").getAsString();
                    Noctyra.teamOwner.put(UUID.fromString(ownerUUID), team);
                }

                if (teamObj.has("players")) {
                    for (JsonElement el : teamObj.getAsJsonArray("players")) {
                        UUID uuid = UUID.fromString(el.getAsString());
                        Noctyra.playerInTeam.put(uuid, team);
                    }
                }

                if (teamObj.has("allies")) {
                    for (JsonElement allyEl : teamObj.getAsJsonArray("allies")) {
                        String allyName = allyEl.getAsString();
                        Noctyra.teamAllies.computeIfAbsent(team, k -> new ArrayList<>()).add(allyName);
                    }
                }

                if (teamObj.has("scouts")) {
                    for (JsonElement el : teamObj.getAsJsonArray("scouts")) {
                        UUID uuid = UUID.fromString(el.getAsString());
                        Noctyra.TEAM_RANK_SCOUT.put(uuid, team);
                    }
                }

                if (teamObj.has("scoutmasters")) {
                    for (JsonElement el : teamObj.getAsJsonArray("scoutmasters")) {
                        UUID uuid = UUID.fromString(el.getAsString());
                        Noctyra.TEAM_PROMOTER_SCOUT.put(uuid, team);
                    }
                }

                if (teamObj.has("consul")) {
                    for (JsonElement el : teamObj.getAsJsonArray("consul")) {
                        UUID uuid = UUID.fromString(el.getAsString());
                        Noctyra.TEAM_RANK_CONSUL.put(uuid, team);
                    }
                }

                int kills = teamObj.has("kills") ? teamObj.get("kills").getAsInt() : 0;
                Noctyra.teamKills.put(team, kills);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getPlayers(String team) {
        List<String> players = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : Noctyra.playerInTeam.entrySet()) {
            if (entry.getValue().equals(team)) players.add(entry.getKey().toString());
        }
        return players;
    }

    private List<String> getAllies(String team) {
        return Noctyra.teamAllies.getOrDefault(team, new ArrayList<>());
    }

    private List<String> getScouts(String team) {
        List<String> players = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : Noctyra.TEAM_RANK_SCOUT.entrySet()) {
            if (entry.getValue().equals(team)) players.add(entry.getKey().toString());
        }
        return players;
    }

    private List<String> getScoutMasters(String team) {
        List<String> players = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : Noctyra.TEAM_PROMOTER_SCOUT.entrySet()) {
            if (entry.getValue().equals(team)) players.add(entry.getKey().toString());
        }
        return players;
    }

    private List<String> getConsul(String team) {
        List<String> players = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : Noctyra.TEAM_RANK_CONSUL.entrySet()) {
            if (entry.getValue().equals(team)) players.add(entry.getKey().toString());
        }
        return players;
    }


    private String getOwnerUUID(String team) {
        for (Map.Entry<UUID, String> entry : Noctyra.teamOwner.entrySet()) {
            if (entry.getValue().equals(team)) return entry.getKey().toString();
        }
        return "";
    }
}
