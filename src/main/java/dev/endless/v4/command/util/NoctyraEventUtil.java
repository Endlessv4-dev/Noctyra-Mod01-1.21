package dev.endless.v4.command.util;

import dev.endless.v4.command.team.TeamManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class NoctyraEventUtil {
    public static void registerTeamMessage() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            TeamManager tm = TeamManager.getInstance();

            if (tm.hasTeam(sender)) {
                String team = tm.getTeam(sender);
                Formatting color = tm.getTeamColor(team);

                MutableText customMsg = Text.literal("[")
                        .formatted(Formatting.DARK_GRAY)
                        .append(Text.literal(team).formatted(color))
                        .append(Text.literal("] ").formatted(Formatting.DARK_GRAY))
                        .append(Text.literal(sender.getName().getString() + ": ").formatted(Formatting.GRAY))
                        .append(message.getContent().copy());

                // Send to all players manually
                for (ServerPlayerEntity player : sender.getServer().getPlayerManager().getPlayerList()) {
                    player.sendMessage(customMsg);
                }

                // Returning false stops the original broadcast
                return false;
            }

            return true; // non-team players -> normal chat
        });
    }

    public static void teamKillEvent() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(((world, killer, victim) -> {
            if (!(killer instanceof ServerPlayerEntity playerKiller)) return;
            if (!(victim instanceof ServerPlayerEntity playerVictim)) return;

            TeamManager.getInstance().addKill(playerKiller, playerVictim);

            for (ServerPlayerEntity players : playerKiller.getServer().getPlayerManager().getPlayerList()) {
                players.sendMessage(Text.literal("§e" + playerKiller.getName().getString() + " §7has killed §e" + playerVictim.getName().getString()));
            }
        }));
    }
}
