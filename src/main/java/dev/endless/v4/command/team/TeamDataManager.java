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
        JsonObject root = new JsonObject();
        for (String team : Noctyra.teams) {
            JsonObject teamObj = new JsonObject();
            teamObj.addProperty("ownerUUID", getOwnerUUID(team));
            teamObj.add("players", new Gson().toJsonTree(getPlayers(team)));
            root.add(team, teamObj);
        }

        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.writeString(FILE_PATH, new GsonBuilder().setPrettyPrinting().create().toJson(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTeams() {
        if (!Files.exists(FILE_PATH)) return;

        try {
            JsonObject root = JsonParser.parseString(Files.readString(FILE_PATH)).getAsJsonObject();
            for (String team : root.keySet()) {
                JsonObject teamObj = root.get(team).getAsJsonObject();
                Noctyra.teams.add(team);

                String ownerUUID = teamObj.get("ownerUUID").getAsString();
                Noctyra.teamOwner.put(UUID.fromString(ownerUUID), team);

                for (JsonElement el : teamObj.getAsJsonArray("players")) {
                    UUID uuid = UUID.fromString(el.getAsString());
                    Noctyra.playerInTeam.put(uuid, team);
                }
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

    private String getOwnerUUID(String team) {
        for (Map.Entry<UUID, String> entry : Noctyra.teamOwner.entrySet()) {
            if (entry.getValue().equals(team)) return entry.getKey().toString();
        }
        return "";
    }
}
