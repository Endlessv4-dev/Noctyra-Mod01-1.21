package dev.endless.v4.command.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NoctyraTickSchedulerUtil {
    private static class ScheduledTask {
        int ticksRemaining;
        Runnable task;

        ScheduledTask(int delay, Runnable task) {
            this.ticksRemaining = delay;
            this.task = task;
        }
    }

    private static final List<ScheduledTask> tasks = new ArrayList<>();

    public static void schedule(MinecraftServer server, int delayTicks, Runnable task) {
        tasks.add(new ScheduledTask(delayTicks, () -> server.execute(task)));
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Iterator<ScheduledTask> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                ScheduledTask t = iterator.next();
                if (--t.ticksRemaining <= 0) {
                    t.task.run();
                    iterator.remove();
                }
            }
        });
    }
}
