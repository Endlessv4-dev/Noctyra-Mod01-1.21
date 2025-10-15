package dev.endless.v4.command.spawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SpawnStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File("config/noctyra_spawn.json");

    public String world;
    public double x, y, z;
    public float yaw, pitch;

    public static SpawnStorage load() {
        try {
            if (FILE.exists()) {
                try (FileReader reader = new FileReader(FILE)) {
                    return GSON.fromJson(reader, SpawnStorage.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save() {
        try {
            FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SpawnStorage fromPlayer(net.minecraft.server.network.ServerPlayerEntity player) {
        SpawnStorage s = new SpawnStorage();
        s.world = player.getWorld().getRegistryKey().getValue().toString();
        Vec3d pos = player.getPos();
        s.x = pos.x;
        s.y = pos.y;
        s.z = pos.z;
        s.yaw = player.getYaw();
        s.pitch = player.getPitch();
        return s;
    }

    public BlockPos getBlockPos() {
        return new BlockPos((int) x, (int) y, (int) z);
    }
}