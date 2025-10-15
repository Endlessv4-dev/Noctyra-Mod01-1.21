package dev.endless.v4.command.util;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;

public class NoctyraSuggestionsUtil {

    public static CompletableFuture<Suggestions> suggestPlayer(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        for (ServerPlayerEntity player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
            String name = player.getGameProfile().getName();
            if (name.toLowerCase().startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestModes(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        String[] primary = {"survival", "creative", "adventure", "spectator"};
        String[] aliases = {"s", "c", "a", "sp", "0", "1", "2", "3"};
        String remaining = builder.getRemainingLowerCase();

        if (remaining.isEmpty()) {
            for (String m : primary) builder.suggest(m);
        } else {
            for (String m : primary) if (m.startsWith(remaining)) builder.suggest(m);
            for (String a : aliases) if (a.startsWith(remaining)) builder.suggest(a);
        }
        return builder.buildFuture();
    }
}
